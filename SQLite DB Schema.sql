CREATE SCHEMA db_shuffle;

USE db_shuffle;

CREATE TABLE IF NOT EXISTS sh_playlist(
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    status INTEGER DEFAULT 1,
    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sh_playlist_songs (
	_playlistId INTEGER,
    song TEXT,
    status INTEGER DEFAULT 1,
    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(_playlistId,song),
    FOREIGN KEY (_playlistId) REFERENCES sh_playlist(_id)
);

CREATE TABLE IF NOT EXISTS sh_score (
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
    artist TEXT,
    playlistId INTEGER,
    score INTEGER NOT NULL,
    status INTEGER DEFAULT 1,
    dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(playlistId) REFERENCES sh_playlist(_id)
);