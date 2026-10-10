create table innorp005t_role_permissions_mapping
(
    innorp002_role_id       bigint not null
        constraint role_permissions_role_id_fkey
            references innorp002t_roles
            on delete cascade,
    innorp004_permission_id bigint not null
        constraint role_permissions_permission_id_fkey
            references innorp004t_permissions
            on delete cascade,
    constraint role_permissions_pkey
        primary key (innorp002_role_id, innorp004_permission_id)
);

alter table innorp005t_role_permissions_mapping
    owner to postgres;

create index idx_role_permissions_permission_id
    on innorp005t_role_permissions_mapping (innorp004_permission_id);

create index idx_role_permissions_role_id
    on innorp005t_role_permissions_mapping (innorp002_role_id);

INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (1, 1);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (1, 2);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (1, 3);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (1, 4);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (1, 5);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (2, 1);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (2, 2);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (2, 3);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (3, 2);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (3, 3);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (3, 4);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (4, 2);
INSERT INTO innorp005t_role_permissions_mapping (innorp002_role_id, innorp004_permission_id) VALUES (4, 1);
