CREATE TABLE runs (
  id bigint NOT NULL AUTO_INCREMENT,
  run_name varchar(128) NOT NULL,
  viability_only boolean NOT NULL,  
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_by bigint,
  PRIMARY KEY (id)
);

CREATE TABLE users (
  id bigint AUTO_INCREMENT NOT NULL,
  user_name varchar(128) NOT NULL,
  first_name varchar(128) NOT NULL,
  last_name varchar(128) NOT NULL,
  password_hash varchar(512) NOT NULL,
  active boolean NOT NULL,
  admin boolean NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX users_name ON users(user_name);

CREATE TABLE run_security (
  run_id bigint,
  user_id bigint,
  PRIMARY KEY (run_id, user_id)
);

ALTER TABLE run_security ADD FOREIGN KEY (run_id) REFERENCES runs(id);

ALTER TABLE run_security ADD FOREIGN KEY (user_id) REFERENCES users(id);

CREATE VIEW user_security AS
	SELECT u.id as user_id, u.user_name, r.id as run_id FROM users u
		CROSS JOIN runs r
		LEFT JOIN run_security rs ON rs.run_id = r.id AND rs.user_id = u.id
	WHERE rs.run_id IS NOT NULL OR u.admin = 1;

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
  gene_id varchar(128)  NOT NULL,
  gene_symbol varchar(128)  NOT NULL,
  time_marker float  NOT NULL,
  data float  NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE raw_data_controls ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

CREATE TABLE raw_data (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  gene_id varchar(128)  NOT NULL,
  gene_symbol varchar(128)  NOT NULL,
  time_marker float  NOT NULL,
  data float NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE raw_data ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

ALTER TABLE raw_data ADD CONSTRAINT raw_data_uc_1 UNIQUE (plate_id,gene_symbol,time_marker);

CREATE TABLE cell_viability_controls (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  gene_id varchar(128)  NOT NULL,
  gene_symbol varchar(128)  NOT NULL,
  data float  NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE cell_viability_controls ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

CREATE TABLE cell_viability (
  id bigint AUTO_INCREMENT NOT NULL,
  plate_id bigint NOT NULL,
  gene_id varchar(128)  NOT NULL,
  gene_symbol varchar(128)  NOT NULL,
  data float NOT NULL,
  create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE cell_viability ADD FOREIGN KEY (plate_id) REFERENCES plates(id);

ALTER TABLE cell_viability ADD CONSTRAINT cell_viability_uc_1 UNIQUE (plate_id,gene_symbol);

CREATE TABLE hts_version_info (
  property_name varchar(128),
  property_value varchar(512)
);

CREATE TABLE ncbi_taxonomy (
  id varchar(128),
  name varchar(512),
  PRIMARY KEY (id)
);

CREATE TABLE ncbi_homologue (
  taxonomy_id varchar(128),
  gene_id varchar(128),
  gene_symbol varchar(128),
  PRIMARY KEY (taxonomy_id,gene_id)
);