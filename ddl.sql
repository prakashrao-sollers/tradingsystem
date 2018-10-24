/* stocks table */
drop table if exists `stocks`;
CREATE TABLE `stocks` (
 `ticker` varchar(10) NOT NULL,
 `full_name` tinytext NOT NULL,
 `bid` double NOT NULL,
 `ask` double NOT NULL,
 `last` double NOT NULL,
 PRIMARY KEY (`ticker`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* bank table */
drop table if exists `bank`;
CREATE TABLE `bank` (
 `U_Id` varchar(10) NOT NULL,
 `B_Owner` tinytext NOT NULL,
 `B_Name` tinytext NOT NULL,
 `B_AccNo` tinytext NOT NULL,
 `B_RNo` tinytext NOT NULL,
 PRIMARY KEY (`U_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* creditcard table */
drop table if exists `creditcard`;
CREATE TABLE `creditcard` (
 `U_Id` varchar(10) NOT NULL,
 `C_Owner` tinytext NOT NULL,
 `C_Type` tinytext NOT NULL,
 `C_CCNo` tinytext NOT NULL,
 `C_ExpM` tinyint(2) NOT NULL,
 `C_ExpY` smallint(4) NOT NULL,
 `C_CVVNo` tinytext NOT NULL,
 PRIMARY KEY (`U_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `accounts`;
CREATE TABLE `accounts` (
 `id` int(11) NOT NULL COMMENT 'login.id gets copied to this id',
 `first_name` varchar(20) NOT NULL,
 `last_name` varchar(20) NOT NULL,
 `dob` date NOT NULL,
 `ssn` varchar(12) NOT NULL,
 `email` varchar(40) NOT NULL,
 `address_id` int(11) DEFAULT NULL,
 `mailing_address_id` int(11) DEFAULT NULL,
 `is_verified` tinyint(1) DEFAULT NULL,
 UNIQUE KEY `id` (`id`),
 KEY `mailing address id foreign key` (`mailing_address_id`),
 KEY `address id foreign key` (`address_id`),
 CONSTRAINT `address id foreign key` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
 CONSTRAINT `mailing address id foreign key` FOREIGN KEY (`mailing_address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `positions`;
CREATE TABLE `positions` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `account_id` int(11) NOT NULL,
 `side` int(11) NOT NULL COMMENT '-1 for short, 1 for long',
 `size` int(11) NOT NULL,
 `price` double NOT NULL,
 `symbol` varchar(10) NOT NULL,
 `is_open` tinyint(1) NOT NULL,
 `creation_date` date NOT NULL,
 PRIMARY KEY (`id`),
 KEY `account_id foreign key` (`account_id`),
 CONSTRAINT `account_id foreign key` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

drop table if exists `address`;
CREATE TABLE `address` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `street1` varchar(30) NOT NULL,
 `street2` varchar(30) DEFAULT NULL,
 `city` varchar(30) NOT NULL,
 `state` varchar(20) NOT NULL,
 `zip` varchar(10) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;