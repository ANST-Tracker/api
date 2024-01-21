create table public.user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);