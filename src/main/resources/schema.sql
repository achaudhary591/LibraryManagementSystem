-- Create sequences if they don't exist
DO $$
BEGIN
    -- Create member_id_seq if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'member_id_seq') THEN
        CREATE SEQUENCE member_id_seq START 1;
        ALTER TABLE member ALTER COLUMN id SET DEFAULT nextval('member_id_seq');
        PERFORM setval('member_id_seq', COALESCE((SELECT MAX(id) FROM member), 0) + 1, false);
    END IF;

    -- Create users_id_seq if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'users_id_seq') THEN
        CREATE SEQUENCE users_id_seq START 1;
        ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq');
        PERFORM setval('users_id_seq', COALESCE((SELECT MAX(id) FROM users), 0) + 1, false);
    END IF;

    -- Create book_id_seq if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'book_id_seq') THEN
        CREATE SEQUENCE book_id_seq START 1;
        ALTER TABLE book ALTER COLUMN id SET DEFAULT nextval('book_id_seq');
        PERFORM setval('book_id_seq', COALESCE((SELECT MAX(id) FROM book), 0) + 1, false);
    END IF;

    -- Create category_id_seq if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'category_id_seq') THEN
        CREATE SEQUENCE category_id_seq START 1;
        ALTER TABLE category ALTER COLUMN id SET DEFAULT nextval('category_id_seq');
        PERFORM setval('category_id_seq', COALESCE((SELECT MAX(id) FROM category), 0) + 1, false);
    END IF;

    -- Create issue_id_seq if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'issue_id_seq') THEN
        CREATE SEQUENCE issue_id_seq START 1;
        ALTER TABLE issue ALTER COLUMN id SET DEFAULT nextval('issue_id_seq');
        PERFORM setval('issue_id_seq', COALESCE((SELECT MAX(id) FROM issue), 0) + 1, false);
    END IF;

    -- Create issued_book_id_seq if it doesn't exist
    IF NOT EXISTS (SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename = 'issued_book_id_seq') THEN
        CREATE SEQUENCE issued_book_id_seq START 1;
        ALTER TABLE issued_book ALTER COLUMN id SET DEFAULT nextval('issued_book_id_seq');
        PERFORM setval('issued_book_id_seq', COALESCE((SELECT MAX(id) FROM issued_book), 0) + 1, false);
    END IF;
END $$;
