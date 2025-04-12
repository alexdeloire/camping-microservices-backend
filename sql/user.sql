-- docker exec -it user_db psql -U user_user -d user_db
DROP TABLE IF EXISTS "user";

CREATE TABLE IF NOT EXISTS "user" (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(30),
    username VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    telephone VARCHAR(30),
    requested_deletion BOOLEAN DEFAULT FALSE, -- Whether the user has requested deletion
    is_admin BOOLEAN DEFAULT FALSE         -- Whether the user has admin privileges
);

INSERT INTO "user" (name, username, email, password, telephone, requested_deletion, is_admin)
VALUES 
    ('Alice Smith', 'ASmith', 'alice.smith@example.com', 'ASmith', '1234567890', FALSE, FALSE),
    ('Bob Johnson', 'BJohn', 'bob.johnson@example.com', 'BJohn', '0987654321', FALSE, TRUE),
    ('Charlie Brown', 'CBrown', 'charlie.brown@example.com', 'CBrown', '1122334455', TRUE, FALSE),
    ('Diana White', 'DWhite', 'diana.white@example.com', 'DWhite', '2233445566', FALSE, FALSE);
