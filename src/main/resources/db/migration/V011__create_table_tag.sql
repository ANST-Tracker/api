create table public.tag
(
    id         uuid primary key,
    name       varchar not null,
    project_id uuid    not null
);