DROP TABLE IF EXISTS "genres" CASCADE;
DROP TABLE IF EXISTS "films" CASCADE;
DROP TABLE IF EXISTS "film_genres" CASCADE;
DROP TABLE IF EXISTS "ratings" CASCADE;
DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS "user_films_liked" CASCADE;
DROP TABLE IF EXISTS "user_friends" CASCADE;
DROP TABLE IF EXISTS "directors" CASCADE;
DROP TABLE IF EXISTS "film_directors" CASCADE;
DROP TABLE IF EXISTS "reviews" CASCADE;
DROP TABLE IF EXISTS "user_reviews_scored" CASCADE;
DROP TABLE IF EXISTS "users_feed" CASCADE;

CREATE TABLE IF NOT EXISTS "genres"
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "ratings"
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "films"
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    release_date date,
    duration INTEGER,
    rating_id INTEGER NOT NULL REFERENCES "ratings" (id)
);

CREATE TABLE IF NOT EXISTS "users"
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email TEXT NOT NULL,
    login TEXT NOT NULL,
    name TEXT,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS "film_genres"
(
    film_id INTEGER NOT NULL REFERENCES "films" (id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES "genres" (id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "user_films_liked"
(
    user_id INTEGER NOT NULL REFERENCES "users" (id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    film_id INTEGER NOT NULL REFERENCES "films" (id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE "user_films_liked"
    ADD CONSTRAINT "uq_user_films"
        UNIQUE ("user_id", "film_id");

CREATE TABLE IF NOT EXISTS "user_friends"
(
    user_id INTEGER NOT NULL REFERENCES "users" (id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    friend_id INTEGER NOT NULL REFERENCES "users" (id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    is_accept BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS "directors"
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS "reviews"
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content TEXT NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL REFERENCES "users" (id),
    film_id INTEGER NOT NULL REFERENCES "films" (id)
);

CREATE TABLE IF NOT EXISTS "user_reviews_scored"
(
    user_id INTEGER NOT NULL REFERENCES "users" (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    review_id INTEGER NOT NULL REFERENCES "reviews" (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    score INTEGER NOT NULL
);

ALTER TABLE "user_reviews_scored"
ADD CONSTRAINT "uq_user_reviews"
    UNIQUE ("review_id", "user_id");



CREATE TABLE IF NOT EXISTS "film_directors"
(
    film_id INTEGER NOT NULL REFERENCES "films" (id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    director_id INTEGER NOT NULL REFERENCES "directors" (id)
    ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS "users_feed"
(
	event_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	user_id INTEGER NOT NULL REFERENCES "users" (id)
	ON DELETE CASCADE ON UPDATE CASCADE,
	operation ENUM('remove', 'add', 'update') NOT NULL,
	event_type ENUM('like', 'review', 'friend') NOT NULL,
	timestamp TIMESTAMP NOT NULL,
	entity_id INTEGER NOT NULL
);