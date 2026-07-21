create table recovery_transactions
(
    id         varchar(255)                not null
        primary key,
    attempts   integer                     not null,
    email      varchar(255)                not null,
    expires_at timestamp(6) with time zone not null,
    otp_hash   varchar(255)                not null,
    used       boolean                     not null
);

alter table recovery_transactions
    owner to postgres;

INSERT INTO app.recovery_transactions (id, attempts, email, expires_at, otp_hash, used) VALUES ('2abc2dc9-b741-4ac6-8e27-d9ca4de69cf7', 0, 'mde@wevioo.com', '2026-07-05 20:45:04.963452 +00:00', '$2a$10$YWOX7v25g6fE5g53iqRIt.sZrhLDn7XNaOP/7CqSgkSATQpwRUlei', true);
INSERT INTO app.recovery_transactions (id, attempts, email, expires_at, otp_hash, used) VALUES ('823800a6-9e09-42eb-8c27-54b457cfd4fe', 0, 'claude-test@example.com', '2026-07-05 21:02:07.636585 +00:00', '$2a$10$KJ69.zHtXdUyFeaRPGDtV.JXl3MAUD413gc2CShfb5E1HXXcG.1Sa', false);
