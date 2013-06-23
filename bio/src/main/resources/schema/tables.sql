CREATE TABLE runs (
  id bigint NOT NULL AUTO_INCREMENT,
  run_name varchar(128) NOT NULL DEFAULT '',
  create_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

ALTER TABLE runs ADD CONSTRAINT runs_uc_1 UNIQUE (run_name);

CREATE TABLE plates (
  id bigint NOT NULL AUTO_INCREMENT,
  run_id bigint NOT NULL,
  plate_name varchar(128) NOT NULL DEFAULT '',
  create_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

ALTER TABLE plates ADD CONSTRAINT plates_uc_1 UNIQUE (run_id,plate_name);

CREATE TABLE controls (
  id bigint NOT NULL AUTO_INCREMENT,
  plate_id bigint NOT NULL,
  positive_control float DEFAULT NULL,
  negative_control float DEFAULT NULL,
  time_marker int DEFAULT NULL,
  create_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

ALTER TABLE controls ADD CONSTRAINT controls_uc_1 UNIQUE (plate_id,time_marker);

CREATE TABLE raw_data (
  id bigint NOT NULL AUTO_INCREMENT,
  plate_id bigint NOT NULL,
  identifier varchar(128) DEFAULT NULL,
  time_marker int DEFAULT NULL,
  data float DEFAULT NULL,
  create_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

ALTER TABLE raw_data ADD CONSTRAINT raw_data_uc_1 UNIQUE (plate_id,identifier,time_marker);