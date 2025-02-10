create table public.story_task
(
    id           uuid primary key,
    status       varchar not null,
    tester_id    uuid,
    sprint_id    uuid,
    epic_task_id uuid
-- add not null to sprint_id and epic_task_id in task AT-106
);