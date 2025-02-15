create table public.story_task
(
    id           uuid primary key,
    status       varchar not null,
    tester_id    uuid,
    sprint_id    uuid not null,
    epic_task_id uuid not null
);