create table role_permissions
(
    role_id       bigint not null
        constraint fklodb7xh4a2xjv39gc3lsop95n
            references role,
    permission_id bigint not null
        constraint fkh0v7u4w7mttcu81o8wegayr8e
            references permission,
    constraint role_permissions_pkey1
        primary key (role_id, permission_id)
);

alter table role_permissions
    owner to postgres;

