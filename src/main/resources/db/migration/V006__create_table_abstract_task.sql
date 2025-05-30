create table public.abstract_task
(
    id              uuid primary key,
    simple_id       varchar not null,
    name            varchar not null,
    description     varchar,
    type            varchar not null,
    status          varchar not null,
    priority        varchar not null,
    story_points    bigint,
    assignee_id     uuid,
    reviewer_id     uuid,
    creator_id      uuid    not null,
    project_id      uuid    not null,
    due_date        varchar,
    order_number    numeric(13, 10),
    time_estimation jsonb
);
