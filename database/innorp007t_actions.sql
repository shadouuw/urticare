create table innorp007t_actions
(
    innorp007_id          bigint       not null
        constraint actions_pkey
            primary key,
    innorp007_name        varchar(255) not null
        constraint actions_name_key
            unique,
    innorp007_description varchar(255),
    innorp007_created_at  timestamp default CURRENT_TIMESTAMP
);

alter table innorp007t_actions
    owner to postgres;

INSERT INTO innorp007t_actions (innorp007_id, innorp007_name, innorp007_description, innorp007_created_at) VALUES (1, 'READ', 'Read access', '2025-10-07 15:54:52.994578');
INSERT INTO innorp007t_actions (innorp007_id, innorp007_name, innorp007_description, innorp007_created_at) VALUES (2, 'CREATE', 'Create new records', '2025-10-07 15:54:52.994578');
INSERT INTO innorp007t_actions (innorp007_id, innorp007_name, innorp007_description, innorp007_created_at) VALUES (3, 'UPDATE', 'Update existing records', '2025-10-07 15:54:52.994578');
INSERT INTO innorp007t_actions (innorp007_id, innorp007_name, innorp007_description, innorp007_created_at) VALUES (4, 'DELETE', 'Delete records', '2025-10-07 15:54:52.994578');
INSERT INTO innorp007t_actions (innorp007_id, innorp007_name, innorp007_description, innorp007_created_at) VALUES (5, 'APPROVE', 'Approve requests', '2025-10-07 15:54:52.994578');
INSERT INTO innorp007t_actions (innorp007_id, innorp007_name, innorp007_description, innorp007_created_at) VALUES (6, 'EXPORT', 'Export data', '2025-10-07 15:54:52.994578');
