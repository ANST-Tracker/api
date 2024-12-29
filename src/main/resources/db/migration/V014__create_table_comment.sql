create table public.comment
(
    id         uuid primary key,
    task_id    uuid        not null,
    content    varchar     not null,
    author_id  uuid        not null,

    created_at timestamptz not null,
    updated_at timestamptz
);