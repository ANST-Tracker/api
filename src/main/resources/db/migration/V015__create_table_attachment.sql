create table public.attachment
(
    id          uuid primary key,
    task_id     uuid        not null,
    file_name   varchar     not null,
    file_id     varchar     not null,
    uploader_id uuid        not null,
    comment_id  uuid,

    created_at  timestamptz not null,
    updated_at  timestamptz
);