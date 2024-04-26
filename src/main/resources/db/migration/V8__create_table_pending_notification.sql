create table pending_notification
(
    id        bigint primary key generated always as identity,
    remind    timestamptz,
    amount    bigint  not null,
    time_type varchar not null,
    task_id   bigint,
    foreign key (task_id) references task (id)
);