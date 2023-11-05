create table public.user_role
(
    user_id bigint not null
        constraint fk_user_role_user_id references public.users,
    role_id bigint not null
        constraint fk_user_role_role_id references public.role,
    primary key (user_id, role_id)
);

alter table public.user_role
    owner to postgres;