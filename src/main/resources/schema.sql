CREATE TABLE track
(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  description VARCHAR,
  length_unit VARCHAR NOT NULL,
  length_value DOUBLE PRECISION NOT NULL
);

CREATE TABLE car
(
  id SERIAL PRIMARY KEY,
  track_id integer NOT NULL,
  code VARCHAR NOT NULL,
  transmission VARCHAR NOT NULL,
  ai BOOLEAN NOT NULL,
  max_speed_unit VARCHAR NOT NULL,
  max_speed_value DOUBLE PRECISION NOT NULL,
  FOREIGN KEY (track_id) REFERENCES track (id)
);

INSERT INTO track (name, description, length_unit, length_value)
VALUES ('Millbrook', 'Millbrook city course race track', 'km', 7.4);

INSERT INTO car (track_id, code, transmission, ai, max_speed_unit, max_speed_value)
VALUES ((SELECT id FROM track WHERE name = 'Millbrook'), 'rdb1', 'automatic', true, 'mps',
        110.12121212);
INSERT INTO car (track_id, code, transmission, ai, max_speed_unit, max_speed_value)
VALUES ((SELECT id FROM track WHERE name = 'Millbrook'), 'rdb3', 'automatic', false, 'mps',
        120.967);