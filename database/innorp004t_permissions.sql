create table innorp004t_permissions
(
    innorp004_id            bigint       not null
        constraint permissions_pkey
            primary key,
    innorp004_name          varchar(100) not null
        constraint permissions_name_key
            unique,
    innorp004_description   varchar(255),
    innorp004_resource_type varchar(50)  not null,
    innorp004_created_at    timestamp default CURRENT_TIMESTAMP
);

alter table innorp004t_permissions
    owner to postgres;

INSERT INTO innorp004t_permissions (innorp004_id, innorp004_name, innorp004_description, innorp004_resource_type, innorp004_created_at) VALUES (1, 'USER_MANAGEMENT', 'Manage users', 'USER', '2025-10-07 15:54:53.130458');
INSERT INTO innorp004t_permissions (innorp004_id, innorp004_name, innorp004_description, innorp004_resource_type, innorp004_created_at) VALUES (2, 'ORDER_MANAGEMENT', 'Manage orders', 'ORDER', '2025-10-07 15:54:53.130458');
INSERT INTO innorp004t_permissions (innorp004_id, innorp004_name, innorp004_description, innorp004_resource_type, innorp004_created_at) VALUES (3, 'PRODUCT_MANAGEMENT', 'Manage products', 'PRODUCT', '2025-10-07 15:54:53.130458');
INSERT INTO innorp004t_permissions (innorp004_id, innorp004_name, innorp004_description, innorp004_resource_type, innorp004_created_at) VALUES (4, 'REPORT_VIEW', 'View reports', 'REPORT', '2025-10-07 15:54:53.130458');
INSERT INTO innorp004t_permissions (innorp004_id, innorp004_name, innorp004_description, innorp004_resource_type, innorp004_created_at) VALUES (5, 'SETTINGS_MANAGEMENT', 'Manage system settings', 'SETTINGS', '2025-10-07 15:54:53.130458');
