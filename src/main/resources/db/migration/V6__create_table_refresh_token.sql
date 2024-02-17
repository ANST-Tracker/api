create table public.refresh_token
(
    id        bigint primary key generated always as identity,
    token     varchar unique not null,
    device_id bigint         not null,
    user_id   bigint         not null,

    created   timestamptz    not null
);