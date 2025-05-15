create table public.users_projects
(
    id              uuid primary key,
    user_id         uuid        not null,
    project_id      uuid        not null,
    permission_code varchar,
    created_at      timestamptz not null,
    updated_at      timestamptz
);