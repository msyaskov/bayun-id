CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    email VARCHAR(320) UNIQUE NOT NULL,
    username VARCHAR(63) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    picture_url VARCHAR(255) NOT NULL,
    authorities VARCHAR(255) NOT NULL,
    password VARCHAR(63),
    last_update_date TIMESTAMP WITH TIME ZONE NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    using_2fa boolean NOT NULL,
    expired boolean NOT NULL,
    locked boolean NOT NULL,
    credential_expired boolean NOT NULL,
    enabled boolean NOT NULL
);

CREATE UNIQUE INDEX account_email_idx ON accounts(email);
CREATE UNIQUE INDEX account_username_idx ON accounts(username);