create table public.defect_task
(
    id            uuid primary key,
    tester_id     uuid,
    sprint_id uuid not null,
    story_task_id uuid
);