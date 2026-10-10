create table submission_affected_areas
(
    submission_id bigint not null
        constraint fk8ns6j20x9pr0yk7qw4julrh9q
            references submissions,
    area          varchar(255)
);

alter table submission_affected_areas
    owner to postgres;

INSERT INTO submission_affected_areas (submission_id, area) VALUES (1, 'chest');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (2, 'neck');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (2, 'face');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (3, 'neck');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (4, 'neck');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (5, 'face');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (6, 'neck');
INSERT INTO submission_affected_areas (submission_id, area) VALUES (6, 'chest');
