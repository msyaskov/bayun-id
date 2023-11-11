CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    password_hash VARCHAR(63),
    locked boolean NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE account_authorities (
    account_id UUID PRIMARY KEY,
    authority VARCHAR(60) NOT NULL,
    UNIQUE (account_id, authority)
);