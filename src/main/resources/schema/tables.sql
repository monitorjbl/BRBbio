CREATE TABLE runs (
  id bigint NOT NULL AUTO_INCREMENT,
  run_name varchar(128) NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE runs ADD CONSTRAINT runs_uc_1 UNIQUE (run_name);

CREATE TABLE plates (
  id bigint AUTO_INCREMENT NOT NULL,
  run_id bigint NOT NULL,
  plate_name varchar(128) NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE plates ADD FOREIGN KEY (run_id) REFERENCES runs(id);

ALTER TABLE plates ADD CONSTRAINT plates_uc_1 UNIQUE (run_id,plate_name);

CREATE TABLE raw_data_controls (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  identifier varchar(64)  NOT NULL,
  time_marker int  NOT NULL,
  data float  NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE raw_data_controls ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

CREATE TABLE cell_viability_controls (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  identifier varchar(64)  NOT NULL,
  data float  NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE cell_viability_controls ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

CREATE TABLE cell_viability (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  identifier varchar(128)  NOT NULL,
  data float NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE cell_viability ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

ALTER TABLE cell_viability ADD CONSTRAINT cell_viability_uc_1 UNIQUE (plate_id,identifier);

CREATE TABLE raw_data (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  identifier varchar(128)  NOT NULL,
  time_marker int  NOT NULL,
  data float NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE raw_data ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

ALTER TABLE raw_data ADD CONSTRAINT raw_data_uc_1 UNIQUE (plate_id,identifier,time_marker);