create table public.users
(
    id         bigint primary key,
    email      varchar unique not null,
    first_name varchar,
    last_name  varchar,
    password   varchar        not null,
    username   varchar        not null,

    created    timestamptz    not null,
    updated    timestamptz
);

alter table public.users
    owner to postgres;