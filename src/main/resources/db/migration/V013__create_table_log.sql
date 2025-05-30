create table public.logs
(
    id              uuid        primary key,
    task_id         uuid        not null,
    comment         varchar,
    user_id         uuid        not null,
    time_estimation jsonb,
    "date"           date        not null,

    created_at      timestamptz not null,
    updated_at      timestamptz
);