create table innorp008t_token
(
    innorp008_id            integer not null
        constraint token_entity_pkey
            primary key,
    innorp008_is_expired    boolean not null,
    innorp008_refresh_token varchar(4000)
        constraint uk1haio7jlpi0p64oh1xiyn6p5f
            unique,
    innorp008_is_revoked    boolean not null,
    innorp008_access_token  varchar(4000)
        constraint uk2gtx0vpbs5ljauorddy3w4wqy
            unique,
    innorp008_token_type    varchar(255)
        constraint token_entity_token_type_check
            check ((innorp008_token_type)::text = 'BEARER'::text),
    innorp001_account_id    bigint
        constraint fkpkyq5upqxsixwv0luar313xo4
            references innorp001t_account
);

alter table innorp008t_token
    owner to postgres;

