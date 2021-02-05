CREATE TABLE artist_type(
	id INTEGER,
	name CHAR(50),
	PRIMARY KEY (id));


CREATE TABLE gender(
	id INTEGER,
	name CHAR(50),
	PRIMARY KEY (id));

CREATE TABLE country(
	id INTEGER,
	name CHAR(50),
	PRIMARY KEY (id));

CREATE TABLE artist(
	id INTEGER, 
	name CHAR(500), 
	gender INTEGER, 
	sday INTEGER, 
	smonth INTEGER, 
	syear INTEGER, 
	eday INTEGER, 
	emonth INTEGER, 
	eyear INTEGER, 
	type INTEGER, 
	area INTEGER,
	PRIMARY KEY (id),
	FOREIGN KEY (gender) REFERENCES gender(id),
	FOREIGN KEY (type) REFERENCES artist_type(id),
	FOREIGN KEY (area) REFERENCES country(id)
);

CREATE TABLE release_status(
	id INTEGER,
	name CHAR(50),
	PRIMARY KEY (id));

CREATE TABLE release(
	id INTEGER,
	title CHAR(2000),
	status INTEGER,
	barcode CHAR(26),
	packaging CHAR(22),
	PRIMARY KEY (id),
	FOREIGN KEY (status) REFERENCES release_status(id));


CREATE TABLE release_country(
	release INTEGER,
	country INTEGER,
	day INTEGER,
	month INTEGER,
	year INTEGER,
	PRIMARY KEY (release, country),
	FOREIGN KEY (release) REFERENCES release(id),
	FOREIGN KEY (country) REFERENCES country(id)
);


CREATE TABLE release_has_artist(
	release INTEGER,
	artist INTEGER,
	contribution INTEGER,
	PRIMARY KEY (release, artist),
	FOREIGN KEY (release) REFERENCES release(id),
	FOREIGN KEY (artist) REFERENCES artist(id));


CREATE TABLE track(
	id INTEGER UNIQUE,
	name CHAR(2000),
	no INTEGER,
	length INTEGER,
	release INTEGER,
	PRIMARY KEY (id, release),
	FOREIGN KEY (release) REFERENCES release(id));


CREATE TABLE track_has_artist(
	artist INTEGER,
	track INTEGER,
	contribution INTEGER,
	PRIMARY KEY (artist, track),
	FOREIGN KEY (track) REFERENCES track(id),
	FOREIGN KEY (artist) REFERENCES artist(id));




