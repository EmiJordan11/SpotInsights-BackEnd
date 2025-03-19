CREATE TABLE artist_genre (
    id_artist INT NOT NULL,
    id_genre INT NOT NULL,
    PRIMARY KEY (id_artist, id_genre),
    FOREIGN KEY (id_artist) REFERENCES artist(id),
    FOREIGN KEY (id_genre) REFERENCES genre(id)
);