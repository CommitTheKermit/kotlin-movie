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

CREATE TABLE IF NOT EXISTS seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number VARCHAR(10) NOT NULL,
    grade VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    showing_id BIGINT NOT NULL,
    FOREIGN KEY (showing_id) REFERENCES showing(id)
);

CREATE TABLE IF NOT EXISTS reservation_seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id),
    FOREIGN KEY (seat_id) REFERENCES seat(id),
    UNIQUE (reservation_id, seat_id)
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


INSERT INTO seat (id, seat_number, grade)
SELECT * FROM (
VALUES
    (1,  'A1', 'B'),
    (2,  'A2', 'B'),
    (3,  'A3', 'B'),
    (4,  'A4', 'B'),
    (5,  'B1', 'B'),
    (6,  'B2', 'B'),
    (7,  'B3', 'B'),
    (8,  'B4', 'B'),
    (9,  'C1', 'S'),
    (10, 'C2', 'S'),
    (11, 'C3', 'S'),
    (12, 'C4', 'S'),
    (13, 'D1', 'S'),
    (14, 'D2', 'S'),
    (15, 'D3', 'S'),
    (16, 'D4', 'S'),
    (17, 'E1', 'A'),
    (18, 'E2', 'A'),
    (19, 'E3', 'A'),
    (20, 'E4', 'A')
) AS s(id, seat_number, grade)
WHERE NOT EXISTS (SELECT 1 FROM seat);

ALTER TABLE seat ALTER COLUMN id RESTART WITH 21;

ALTER TABLE movie ALTER COLUMN id RESTART WITH 4;
ALTER TABLE screen ALTER COLUMN id RESTART WITH 4;