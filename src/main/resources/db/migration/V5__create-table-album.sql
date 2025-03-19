CREATE TABLE album (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(15) NOT NULL,
    id_spotify VARCHAR(50) UNIQUE NOT NULL,
    artist_id INT NOT NULL,
    FOREIGN KEY(artist_id) REFERENCES artist(id)
);