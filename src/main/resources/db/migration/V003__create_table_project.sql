create table public.project
(
    id           uuid primary key,
    name         varchar not null,
    description  varchar,
    head_id      uuid    not null,
    next_task_id bigint  not null,
    key          varchar not null
);