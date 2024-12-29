create table public.subtask
(
    id            uuid primary key,
    status        varchar not null,
    story_task_id uuid
);