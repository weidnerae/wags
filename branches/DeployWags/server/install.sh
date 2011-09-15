#
# Install script for WE.
#
# @author Robert Bost <bostrt at appstate dot edu> 

#
# Functions used for installation
#

function validateDbType(){
    if [ $dbType -gt "3" ]; then
        echo
        echo "Error: Invalid database type chosen!"
        exit
    elif [ $dbType -le "0" ]; then
        echo
        echo "Error: Invalid database type chosen!"
        exit
    fi
}
function dbType(){
    case $dbType in
        "1" )
            echo "MySQL"
            ;;
        "2" )
            echo "PostgreSQL"
            ;;
        "3" )
            echo "SQLite"
            ;;
        * )
            echo "Unknown"
            ;;
    esac
}
# TODO: Support things other than localhost
function dbDSN(){
    case $dbType in
        "1" )
            echo "mysql:dbname=$dbName;host=localhost"
            ;;
        "2" )
            echo "pgsql:dbname=$dbName;host=localhost"
            ;;
        "3" )
            echo "sqlite:$dbName"
            ;;
        * )
            echo "Unknown"
            ;;
    esac
}

function installSqlite(){
    sqlite3 $dbName < install.sqlite.sql
# The chmod's below are specific to sqlite
    chmod o+w $dbName
    chmod o+w $(pwd)
}

# Setup for a Mysql database. 
function installMysql(){
    mysql --user=$dbUser --password=$dbPass $dbName < install.mysql.sql
}

function installPsql(){
    echo 'TODO'
}

function chooseInstall(){
    case $dbType in
        "1" )
            installMysql
            ;;
        "2" )
            installPsql
            ;;
        "3" )
            installSqlite
            ;;
        * )
            echo "Error!!!!"
            ;;
    esac
}

function createConf(){
# Create configuration file.
    conf="we_conf.php"
    touch $conf
# Echo out config file. TODO: May need to do this differently
# when config file starts to get bigger.
    echo "<?php" > $conf
    echo "    define('WE_ROOT', '"$(pwd -P)"');" >> $conf
    echo "    define('WE_DB_DSN',  '"$(dbDSN)"');" >> $conf
    echo "    define('WE_DB_USER', '"$dbUser"');" >> $conf
    echo "    define('WE_DB_PASS', '"$dbPass"');" >> $conf
    echo "?>" >> $conf
}

# "Main"

# Ask user what database they are using.
read -n 1 -p "What database are you using?

    Choose matching number:
    [1] MySQL
    [2] PostgreSQL
    [3] SQLite

>> " dbType

validateDbType
echo

# Get database name from user
read -p "What is the name of the database?

    Type: $(dbType)
    
>> " dbName

# If database type is SQLite then no user/pass.
if [ $dbType = "3" ];then
    installSqlite
    createConf
    exit
fi

# Get username for database
read -p "What is the username for the database?

    Type: $(dbType)
    Name: $dbName

>> " dbUser

# Get password for database
read -s -p "What is the password for the database?

    Type: $(dbType)
    Name: $dbName
    User: $dbUser

>> " dbPass

chooseInstall
createConf
