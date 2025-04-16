-- Create sequences if they don't exist
CREATE SEQUENCE IF NOT EXISTS member_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS book_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS category_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS issue_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS issued_book_id_seq START WITH 1;

-- Insert default admin and librarian users if they don't exist
INSERT INTO users (username, password, active, role, display_name, created_date)
SELECT 'admin', '$2a$10$9v8ZJU0.gBgRzAx8cAEyl.0WrAwZ4p1U76ElbaruldaMp6Xnf8HZe', true, 'ADMIN', 'Mr. Admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password, active, role, display_name, created_date)
SELECT 'librarian', '$2a$10$.csiIBqbzjBU8FERHq/en.7trtkr64RVlzLlSCLhscox26jga9bmu', true, 'LIBRARIAN', 'Mr. Librarian', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'librarian');
