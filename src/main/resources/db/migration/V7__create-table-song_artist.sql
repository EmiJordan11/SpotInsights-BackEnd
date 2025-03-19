CREATE TABLE song_artist (
    id_song INT NOT NULL,
    id_artist INT NOT NULL,
    PRIMARY KEY (id_song, id_artist),
    FOREIGN KEY (id_song) REFERENCES song(id),
    FOREIGN KEY (id_artist) REFERENCES artist(id)
);