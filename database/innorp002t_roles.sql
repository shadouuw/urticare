create table innorp002t_roles
(
    innorp002_id          bigint       not null
        constraint roles_pkey
            primary key,
    innorp002_name        varchar(100) not null
        constraint roles_name_key
            unique,
    innorp002_description varchar(255),
    innorp002_created_at  timestamp default CURRENT_TIMESTAMP
);

alter table innorp002t_roles
    owner to postgres;

INSERT INTO app.innorp002t_roles (innorp002_id, innorp002_name, innorp002_description, innorp002_created_at) VALUES (4, 'USER', 'Regular user access', '2025-10-07 15:54:53.054692');
INSERT INTO app.innorp002t_roles (innorp002_id, innorp002_name, innorp002_description, innorp002_created_at) VALUES (3, 'MANAGER', 'Management access', '2025-10-07 15:54:53.054692');
INSERT INTO app.innorp002t_roles (innorp002_id, innorp002_name, innorp002_description, innorp002_created_at) VALUES (1, 'SUPER_ADMIN', 'Full system access', '2025-10-07 15:54:53.054692');
INSERT INTO app.innorp002t_roles (innorp002_id, innorp002_name, innorp002_description, innorp002_created_at) VALUES (2, 'ADMIN', 'Administrative access', '2025-10-07 15:54:53.054692');
