CREATE SCHEMA db_shuffle;

USE db_shuffle;

CREATE TABLE sh_playlist(
	_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL,
    status INT DEFAULT 1,
    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP
) engine = innoDB;

CREATE TABLE sh_playlist_songs (
	_playlistId INT,
    song VARCHAR(45),
    status INT DEFAULT 1,
    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(_playlistId,song),
    CONSTRAINT playlistfk_1
		FOREIGN KEY (_playlistId)
        REFERENCES sh_playlist(_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) engine = innoDB;

CREATE TABLE sh_score (
	_id INT PRIMARY KEY,
    artist VARCHAR(45),
    playlistId INT,
    score INT NOT NULL,
    status INT DEFAULT 1,
    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT scorefk_1
		FOREIGN KEY(playlistId)
		REFERENCES sh_playlist(_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE    
) engine = innoDB;