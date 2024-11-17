  CREATE TABLE author (
      id SERIAL PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      birth_date DATE NOT NULL
  );

  CREATE TABLE book (
      id SERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      price INT NOT NULL CHECK (price >= 0),
      status VARCHAR(20) NOT NULL CHECK (status IN ('UNPUBLISHED', 'PUBLISHED'))
  );

  CREATE TABLE book_author (
      book_id INT NOT NULL REFERENCES book (id),
      author_id INT NOT NULL REFERENCES author (id),
      PRIMARY KEY (book_id, author_id)
  );