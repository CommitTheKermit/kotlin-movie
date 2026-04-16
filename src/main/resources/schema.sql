DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    running_minutes INT NOT NULL
);

CREATE TABLE IF NOT EXISTS screen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cinema_id BIGINT
);

CREATE TABLE IF NOT EXISTS showing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    screen_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    FOREIGN KEY (screen_id) REFERENCES screen(id),
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

INSERT INTO movie (id, title, running_minutes)
SELECT * FROM (
  VALUES
      (1, '해리 포터', 130),
      (2, '인터스텔라', 100),
      (3, '기생충', 126)
) AS  mov(id, title, running_minutes)
WHERE NOT EXISTS (SELECT 1 FROM movie);



INSERT INTO screen (id, cinema_id)
SELECT * FROM (
  VALUES
      (1, NULL),
      (2, NULL),
      (3, NULL)
) AS  scr(id, cinema_id)
WHERE NOT EXISTS (SELECT 1 FROM screen);

ALTER TABLE movie ALTER COLUMN id RESTART WITH 4;
ALTER TABLE screen ALTER COLUMN id RESTART WITH 4;