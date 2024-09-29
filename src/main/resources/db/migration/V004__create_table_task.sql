create table public.task
(
    id          bigint primary key generated always as identity,
    description text,
    status      text        not null,
    data        varchar     not null,
    deadline    timestamptz,
    project_id  bigint      not null,

    created     timestamptz not null,
    updated     timestamptz
);