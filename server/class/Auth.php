<?php

require_once('User.php');
require_once('Util.php');

class Auth
{
    /**
     * Set the session ID if the user has one stored.
     * Initialize the session.
     */
    public static function begin()
    {
        if(isset($_COOKIE['PHPSESSID'])){ 
            session_id($_COOKIE['PHPSESSID']); 
        } 
        session_start();
    }

    /**
     * End session.
     */
    public static function end()
    {
        session_commit();
    }

    /**
     * Check if user is logged in according
     * to: is their session id stored? is there a user object stored?
     */
    public static function isLoggedIn()
    {
        return isset($_COOKIE['PHPSESSID']) && isset($_SESSION['user']);
    }

    /**
     * Check database for username. If that username exists
     * then check the password against it.
     * If that passes then store the User object in SESSION.
     * 
     * @return boolean - TRUE is returned if username is valid and password matches.
     *                   FALSE is returned otherwise.
     */
    public static function login($username, $password)
    {
        require_once('User.php');
        if(!User::isUsername($username)){
            self::logout();
            return false;
        }
        if(!User::passwordMatches($username, $password)){
            self::logout();
            return false;
        }

        // If we get here then user has correct username and password.
        //return JSON::warn($username);
        $user = User::getUserByUsername($username);

        if(is_null($user)){
            return false;
        }

        $user->setLastLogin(time());
        $_SESSION['user'] = $user;
        if($user->getUsername() == 'Root'){
            $user->setSection(19); // Root logs in to test section
        }
        $user->save();

        return true;
    }

    /**
     * Unset User object in SESSION.
     * Unset everything else.
     * Destroy session and cookie.
     */
    public static function logout()
    {
        unset($_SESSION['user']);
        session_unset();
        session_destroy();
        setcookie('PHPSESSID', '', time()-3600, "/");
        return true;
    }

    /**
     * If there is a User object stored in SESSION
     * then return it. Otherwise return NULL.
     */
    public static function getCurrentUser()
    {
        if(isset($_SESSION['user'])){
            return $_SESSION['user'];
        }else{
            return NULL;
        }
    }
}
?>
