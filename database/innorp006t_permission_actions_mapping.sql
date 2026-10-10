create table innorp006t_permission_actions_mapping
(
    innorp004_permission_id bigint not null
        constraint permission_actions_permission_id_fkey
            references innorp004t_permissions
            on delete cascade,
    innorp007_action_id     bigint not null
        constraint permission_actions_action_id_fkey
            references innorp007t_actions
            on delete cascade,
    constraint permission_actions_pkey
        primary key (innorp004_permission_id, innorp007_action_id)
);

alter table innorp006t_permission_actions_mapping
    owner to postgres;

create index idx_permission_actions_action_id
    on innorp006t_permission_actions_mapping (innorp007_action_id);

create index idx_permission_actions_permission_id
    on innorp006t_permission_actions_mapping (innorp004_permission_id);

INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (1, 1);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (1, 2);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (1, 3);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (1, 4);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (2, 1);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (2, 2);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (2, 3);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (2, 4);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (2, 5);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (3, 1);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (3, 2);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (3, 3);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (3, 4);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (4, 1);
INSERT INTO innorp006t_permission_actions_mapping (innorp004_permission_id, innorp007_action_id) VALUES (4, 6);
