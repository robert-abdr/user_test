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

    CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_full_name ON users (full_name);
    CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_phone_number ON users (phone_number);