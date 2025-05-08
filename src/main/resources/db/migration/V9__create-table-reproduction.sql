CREATE TABLE reproduction (
    id SERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    song_id INT NOT NULL,
    FOREIGN KEY (user_id)REFERENCES "user"(id),
    FOREIGN KEY (song_id) REFERENCES song(id)
);