create table public.logs
(
    id              varchar primary key,
    task_id         uuid        not null,
    comment         varchar,
    user_id         uuid        not null,
    time_estimation jsonb,

    created_at      timestamptz not null,
    updated_at      timestamptz
);