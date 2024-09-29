create table public.tag
(
    id      bigint primary key generated always as identity,
    name    text   not null,
    color   text,
    user_id bigint not null
);