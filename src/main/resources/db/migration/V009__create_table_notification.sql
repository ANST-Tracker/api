create table notification
(
    id             bigint primary key generated always as identity,
    execution_date timestamptz,
    task_name      varchar not null,
    project_name   varchar,
    task_id        bigint  not null
);