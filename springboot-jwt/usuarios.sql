CREATE TABLE `db_springboot`.`users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NOT NULL,
    `password` VARCHAR(60) NOT NULL,
    `enabled` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE
  );


  CREATE TABLE `db_springboot`.`authorities` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `authority` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id_authority_unique` (`user_id` ASC, `authority` ASC) VISIBLE,
  CONSTRAINT `fk_autothorities_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `db_springboot`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );


INSERT INTO users (username, password, enabled) values ('jonathanoleaz', '$2a$10$6DvG1fR.k6J1xs3RBzBr0eznU3j3LUiBdMNSwTN6WCRr9J7TYvcvu', 1);
INSERT INTO users (username, password, enabled) values ('admin', '$2a$10$Yh72fKlQ/MVSuqdnjwaOGeEFy95WWeeBRHo4/N5sDxF.chU9f9HWu', 1);

INSERT INTO authorities (user_id, authority) values (1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) values (2, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) values (2, 'ROLE_ADMIN');
