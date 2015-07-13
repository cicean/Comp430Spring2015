CREATE TABLE PEAK (
	NAME VARCHAR(30),
	ELEV INT,
	DIFF INT,
	MAP VARCHAR(30),
	REGION VARCHAR(30)
	PRIMARY KEY (NAME));

CREATE TABLE PARTICIPATED (
	TRIP_ID INT,
	NAME VARCHAR(30)
	PRIMARY KEY (TRIP_ID,NAME));

CREATE TABLE CLIMBER (
	NAME VARCHAR(30),
	SEX VARCHAR(1)
	PRIMARY KEY (NAME));

CREATE TABLE CLIMBED (
	TRIP_ID INT,
	PEAK VARCHAR(30),
	WHEN_CLIMBED DATETIME
	PRIMARY KEY (TRIP_ID,PEAK,WHEN_CLIMBED));

