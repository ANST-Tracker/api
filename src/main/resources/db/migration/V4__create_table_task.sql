create table public.task
(
    id          bigint primary key generated always as identity,
    description text,
    status      text        not null,
    data        varchar     not null,
    deadline    timestamptz,
    user_id     bigint      not null
        constraint fk_task_user_id references public.users,

    created     timestamptz not null,
    updated     timestamptz
);