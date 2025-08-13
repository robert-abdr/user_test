CREATE TABLE IF NOT EXISTS roles (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role VARCHAR NOT NULL UNIQUE
);

CREATE TABLE  IF NOT EXISTS users (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(64) NOT NULL,
    phone_number VARCHAR(17) NOT NULL,
    avatar_url VARCHAR NOT NULL,
    role_id UUID NOT NULL,

    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
        REFERENCES roles (uuid)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
    );

    CREATE INDEX IF NOT EXISTS idx_unique_full_name ON users (full_name);
    CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_phone_number ON users (phone_number);

    INSERT INTO roles (role)
    VALUES ('ADMINISTRATOR'), ('PREMIUM_MEMBER'), ('MEMBER'), ('TESTER'), ('SMM_MANAGER')
    ON CONFLICT (role) DO NOTHING;

    INSERT INTO users (full_name, phone_number, avatar_url, role_id)
    VALUES
        ('Иван Иванов', '+79123456789', 'https://example.com/avatar1.jpg', (SELECT uuid FROM roles WHERE role = 'ADMINISTRATOR')),
        ('Петр Петров', '+79234567890', 'https://example.com/avatar2.jpg', (SELECT uuid FROM roles WHERE role = 'MEMBER'))
    ON CONFLICT (phone_number) DO NOTHING;
