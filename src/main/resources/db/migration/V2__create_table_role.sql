create table public.role
(
    id   bigint primary key,
    name varchar not null
);

alter table public.role
    owner to postgres;

INSERT INTO role
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'PREMIUM');