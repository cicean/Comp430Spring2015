drop table DATA
drop table DATAPOINTS

CREATE TABLE DATAPOINTS (
	POINT_ID INT,
	ISTRAINING INT,
	CLASSLABEL INT
	PRIMARY KEY (POINT_ID));

CREATE TABLE DATA (
	POINT_ID INT,
	DIM INT,
	VALUE INT
	PRIMARY KEY (POINT_ID,DIM));