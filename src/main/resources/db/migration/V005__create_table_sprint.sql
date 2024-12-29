create table public.sprint
(
    id          uuid primary key,
    name        varchar     not null,
    start_date  varchar     not null,
    end_date    varchar,
    is_active   boolean     not null,
    description varchar,
    project_id  uuid        not null,

    created_at  timestamptz not null,
    updated_at  timestamptz
);