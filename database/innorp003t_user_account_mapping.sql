create table innorp003t_user_account_mapping
(
    innorp001_account_id bigint not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references innorp001t_account,
    innorp002_role_id    bigint not null
        constraint user_roles_role_id_fkey
            references innorp002t_roles
            on delete cascade,
    constraint user_roles_pkey
        primary key (innorp001_account_id, innorp002_role_id)
);

alter table innorp003t_user_account_mapping
    owner to postgres;

create index idx_user_roles_role_id
    on innorp003t_user_account_mapping (innorp002_role_id);

create index idx_user_roles_user_id
    on innorp003t_user_account_mapping (innorp001_account_id);

INSERT INTO innorp003t_user_account_mapping (innorp001_account_id, innorp002_role_id) VALUES (1, 1);
