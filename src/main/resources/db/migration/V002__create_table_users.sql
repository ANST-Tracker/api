create table public.users
(
    id                 uuid primary key,
    email              varchar unique not null,
    position           varchar        not null,
    department_name    varchar        not null,
    registration_date  varchar        not null,
    time_zone          bigint         not null,
    profile_picture_id varchar,
    telegram_id        varchar unique not null,
    first_name         varchar        not null,
    last_name          varchar        not null,
    password           varchar        not null,
    username           varchar unique not null,

    created_at         timestamptz    not null,
    updated_at         timestamptz
);