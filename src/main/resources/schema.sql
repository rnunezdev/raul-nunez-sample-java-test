CREATE TABLE USER (
  ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
  NAME VARCHAR(100) NOT NULL,
  LAST_NAME VARCHAR(100) NOT NULL,
  EMAIL VARCHAR(100) DEFAULT NULL
);

CREATE TABLE TRANSACTION (
 ID VARCHAR(100) PRIMARY KEY NOT NULL ,
 USER_ID INT NOT NULL,
 AMOUNT DECIMAL(20,2) NOT NULL,
 DESCRIPTION VARCHAR(250) DEFAULT NULL,
 DATE TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
 foreign key (USER_ID) references USER(ID)
);