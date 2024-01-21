create table public.device
(
    id           bigint primary key generated always as identity,
    device_token uuid,
    user_id      bigint      not null,

    created      timestamptz not null,
    last_login   timestamptz not null
);