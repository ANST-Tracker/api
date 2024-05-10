create table pending_notification
(
    id             bigint primary key generated always as identity,
    execution_date timestamptz not null,
    amount         bigint  not null,
    time_type      varchar not null,
    task_id        bigint not null
);