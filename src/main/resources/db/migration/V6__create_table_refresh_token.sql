create table public.refresh_token
(
    id        bigint primary key,
    token     varchar unique not null,
    device_id bigint         not null
        constraint fk_refresh_token_device_id references public.device,
    user_id   bigint         not null
        constraint fk_refresh_token_user_id references public.users,

    created     timestamptz not null,
    updated     timestamptz
);

alter table public.refresh_token
    owner to postgres;