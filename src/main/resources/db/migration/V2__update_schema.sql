-- 書籍テーブルの修正
ALTER TABLE book
    ALTER COLUMN price TYPE DECIMAL(10, 2) USING price::DECIMAL(10, 2),
    ALTER COLUMN status TYPE VARCHAR(50),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- 著者テーブルの修正
ALTER TABLE author
    ALTER COLUMN name TYPE VARCHAR(255),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD CONSTRAINT birth_date_check CHECK (birth_date < CURRENT_DATE);

-- 中間テーブル（book_author）の修正
ALTER TABLE book_author DROP CONSTRAINT IF EXISTS book_author_pkey;
ALTER TABLE book_author
    ALTER COLUMN book_id TYPE BIGINT,
    ALTER COLUMN author_id TYPE BIGINT,
    ADD CONSTRAINT book_author_pkey PRIMARY KEY (book_id, author_id),
    ADD CONSTRAINT book_author_book_id_fk FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE,
    ADD CONSTRAINT book_author_author_id_fk FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE CASCADE;

-- トリガー関数を作成
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 書籍テーブル用トリガー
CREATE TRIGGER set_timestamp_book
BEFORE UPDATE ON book
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- 著者テーブル用トリガー
CREATE TRIGGER set_timestamp_author
BEFORE UPDATE ON author
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- 外部キーを削除
ALTER TABLE book_author DROP CONSTRAINT IF EXISTS book_author_book_id_fk;
ALTER TABLE book_author DROP CONSTRAINT IF EXISTS book_author_book_id_fkey;
ALTER TABLE book_author DROP CONSTRAINT IF EXISTS book_author_author_id_fk;
ALTER TABLE book_author DROP CONSTRAINT IF EXISTS book_author_author_id_fkey;

-- 外部キーを明確な名前で再作成
ALTER TABLE book_author ADD CONSTRAINT fk_book_author_book_id FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE;
ALTER TABLE book_author ADD CONSTRAINT fk_book_author_author_id FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE CASCADE;