create table public.users
(
    id         bigint primary key generated always as identity,
    telegram_id      varchar unique not null,
    first_name varchar,
    last_name  varchar,
    password   varchar        not null,
    username   varchar        not null,

    created    timestamptz    not null,
    updated    timestamptz
);