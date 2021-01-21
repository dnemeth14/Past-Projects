DROP DATABASE IF EXISTS stocks;
CREATE DATABASE stocks;
USE stocks;
CREATE TABLE Users(
	userID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    hashPass VARCHAR(50) NOT NULL
);
CREATE TABLE Buys(
	buyID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(5) NOT NULL,
	shares INT(11) NOT NULL,
    buyDate DATE NOT NULL,
    price DOUBLE(15, 5) NOT NULL
);
CREATE TABLE Sells(
	sellID INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(5) NOT NULL,
	shares INT(11) NOT NULL,
    sellDate DATE NOT NULL,
    price DOUBLE(15, 5) NOT NULL
);
CREATE TABLE UserToBuys(
	buyID INT(11) PRIMARY KEY NOT NULL,
	userID INT(11) NOT NULL,
	FOREIGN KEY fk1(userID) REFERENCES Users(userID),
	FOREIGN KEY fk2(buyID) REFERENCES Buys(buyID)
);
CREATE TABLE UserToSells(
	sellID INT(11) PRIMARY KEY NOT NULL,
	userID INT(11) NOT NULL,
	FOREIGN KEY fk1(userID) REFERENCES Users(userID),
	FOREIGN KEY fk2(sellID) REFERENCES Sells(sellID)
);
INSERT INTO Users(username, hashPass)
VALUES ("test", "test");
INSERT INTO Users(username, hashPass)
VALUES ("username", "awrpssdo");
INSERT INTO Users(username, hashPass)
VALUES ("trojan", "etts");
INSERT INTO Buys(symbol, shares, buyDate, price)
VALUES ("AAPL", 50, '2020-09-02', 110.95);
INSERT INTO UserToBuys(buyID, userID)
VALUES (1, 1);
INSERT INTO Buys(symbol, shares, buyDate, price)
VALUES ("GOOGL", 100, '2018-09-02', 1200);
INSERT INTO UserToBuys(buyID, userID)
VALUES (2, 1);
INSERT INTO Buys(symbol, shares, buyDate, price)
VALUES ("GE", 35, '2013-09-02', 76.54);
INSERT INTO UserToBuys(buyID, userID)
VALUES (3,1);
INSERT INTO Sells(symbol, shares, sellDate, price)
VALUES ("AAPL", 30, '2020-09-20', 115.67);
INSERT INTO UserToSells(sellID, userID)
VALUES (1, 1);
INSERT INTO Sells(symbol, shares, sellDate, price)
VALUES ("GOOGL", 100, '2019-09-20', 1476);
INSERT INTO UserToSells(sellID, userID)
VALUES (2, 1);
INSERT INTO Sells(symbol, shares, sellDate, price)
VALUES ("GE", 34, '2018-09-20', 53.69);
INSERT INTO UserToSells(sellID, userID)
VALUES (3,1);
INSERT INTO Buys(symbol, shares, buyDate, price)
VALUES ("AMZN", 10, '2020-09-02', 110.95);
INSERT INTO UserToBuys(buyID, userID)
VALUES (4, 1);
INSERT INTO Sells(symbol, shares, sellDate, price)
VALUES ("AMZN", 10, '2020-09-20', 110.95);
INSERT INTO UserToSells(sellID, userID)
VALUES (4,1);

INSERT INTO Buys(symbol, shares, buyDate, price) VALUES ("AAPL", 50, '2020-09-02', 110.95);
INSERT INTO UserToBuys(buyID, userID) VALUES (5, 3);
INSERT INTO Buys(symbol, shares, buyDate, price) VALUES ("GOOGL", 100, '2018-09-02', 1200);
INSERT INTO UserToBuys(buyID, userID) VALUES (6, 3);
