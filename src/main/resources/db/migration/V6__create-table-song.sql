CREATE TABLE song (
    id SERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    popularity INT NOT NULL,
    duration_ms INT NOT NULL,
    id_spotify VARCHAR(50) UNIQUE NOT NULL,
    album_id INT,
    FOREIGN KEY (album_id) REFERENCES album(id)
);