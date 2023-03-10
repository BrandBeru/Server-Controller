CREATE TABLE `Projects` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL AUTO_INCREMENT,
	`password` varchar(255) NOT NULL,
	`updated` BOOLEAN(255) NOT NULL,
	`complete` INT(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Details` (
	`id` INT NOT NULL,
	`create` DATE NOT NULL,
	`last_modified` DATE NOT NULL,
	`description` varchar(255) NOT NULL DEFAULT 'name'
);

ALTER TABLE `Details` ADD CONSTRAINT `Details_fk0` FOREIGN KEY (`id`) REFERENCES `Projects`(`id`);



