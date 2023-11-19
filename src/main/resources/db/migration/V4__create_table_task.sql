create table public.task
(
    id          bigint primary key,
    description text,
    status      text        not null,
    data        varchar     not null,
    deadline    timestamptz,
    user_id     bigint      not null
        constraint fk_task_user_id references public.users,

    created     timestamptz not null,
    updated     timestamptz
);

alter table public.task
    owner to postgres;