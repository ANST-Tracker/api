create table public.device
(
    id           uuid primary key,
    device_token uuid,
    user_id      uuid        not null,
    token        varchar,
    user_agent   varchar,
    ip           varchar,

    created      timestamptz not null,
    last_login   timestamptz not null
);