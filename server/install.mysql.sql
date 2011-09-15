CREATE TABLE user (
       id INTEGER NOT NULL,
       username VARCHAR(255) NOT NULL,
       firstName VARCHAR(255) NULL,
       lastName VARCHAR(255) NULL,
       email VARCHAR(255) NOT NULL,
       password VARCHAR(255) NOT NULL,
       added INT NOT NULL,
       updated INT NOT NULL,
       lastLogin INT NOT NULL,
       admin INT NOT NULL,
       section INT NOT NULL REFERENCES section(id),
       PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE file (
       id INTEGER NOT NULL,
       ownerId INT NOT NULL REFERENCES user(id),
       exceriseId INT NOT NULL REFERENCES exercise(id),
       name VARCHAR(255) NOT NULL,
       contents TEXT,
       section INT NOT NULL REFERENCES section(id),
       updated INT NOT NULL,
       added INT NOT NULL,
       PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE exercise (
       id INT NOT NULL,
       title VARCHAR(255) NOT NULL,
       description TEXT,
       skeleton TEXT,
       solution TEXT,  
       testClass TEXT,
       multiUser INT NOT NULL,
       visible INT NOT NULL, 
       section INT NOT NULL,
       added INT NOT NULL,
       updated INT NOT NULL,
       PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE submission (
       id INT NOT NULL,
       exerciseId NOT NULL REFERENCES exercise(id),
       fileId NOT NULL REFERENCES file(id),
       userId NOT NULL REFERENCES user(id),
       partner TEXT,
       success INT NOT NULL,
       added INT NOT NULL,
       updated INT NOT NULL,
       PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE section {
       id INTEGER NOT NULL,
       name VARCHAR(255) NOT NULL,
       administrator REFERENCES user(id),
       PRIMARY KEY(id)
}
-- Insert some admins
-- password 123456
INSERT INTO user VALUES (1, 'admin', 'Admin', 'Istrator', 'bostrt@gmail.com', 'e10adc3949ba59abbe56e057f20f883e', 0, 0, 0, 1, 0);
INSERT INTO section VALUES (0, 'Class 1', 1);
