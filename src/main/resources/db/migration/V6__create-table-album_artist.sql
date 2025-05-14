CREATE TABLE album_artist (
    id_album INT NOT NULL,
    id_artist INT NOT NULL,
    PRIMARY KEY (id_album, id_artist),
    FOREIGN KEY (id_album) REFERENCES album(id),
    FOREIGN KEY (id_artist) REFERENCES artist(id)
);