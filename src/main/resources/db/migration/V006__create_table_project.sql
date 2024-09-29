create table public.project
(
    id      bigint primary key generated always as identity,
    name    varchar     not null,
    type    varchar     not null,
    user_id bigint      not null,

    created timestamptz not null,
    updated timestamptz
);