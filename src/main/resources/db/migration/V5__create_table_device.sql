create table public.device
(
    id           bigint primary key,
    device_token uuid,
    user_id      bigint      not null
        constraint fk_device_user_id references public.users,

    created      timestamptz not null,
    last_login   timestamptz not null
);

alter table public.device
    owner to postgres;