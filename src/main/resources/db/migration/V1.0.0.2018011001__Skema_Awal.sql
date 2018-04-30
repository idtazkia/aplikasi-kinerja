create table staff_role (
  id VARCHAR (255)NOT NULL ,
  role_name VARCHAR (255)NOT NULL ,
  description VARCHAR (255),
  id_role_superior VARCHAR(36),
  status VARCHAR(36),
  PRIMARY KEY (id)

);

CREATE TABLE staff (
  id VARCHAR(36)not null,
  employee_name VARCHAR(255) NOT NULL ,
  employee_number VARCHAR(255),
  job_level VARCHAR (255),
  job_title VARCHAR (40),
  department VARCHAR (40),
  area VARCHAR (40),
  id_user VARCHAR (255),

  PRIMARY KEY (id)
);

create table staff_role_staff (
  id_staff VARCHAR (255)NOT NULL ,
  id_staff_role VARCHAR (255)NOT NULL ,
  PRIMARY KEY (id_staff, id_staff_role),
  FOREIGN KEY (id_staff) REFERENCES staff(id),
  FOREIGN KEY (id_staff_role) REFERENCES staff_role(id)
);

CREATE TABLE category (
  id VARCHAR(36)not null,
  name VARCHAR(36) NOT NULL ,
  PRIMARY KEY (id)
);

CREATE TABLE kpi (
  id VARCHAR(36)not null,
  id_category VARCHAR(36) NOT NULL ,
  key_result VARCHAR(255) NOT NULL ,
  weight NUMERIC (4,2) ,
  base_line NUMERIC (4,2),
  target NUMERIC (4,2),
  status VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (id_category) REFERENCES category(id)
);

create table staff_role_kpi (
  id_kpi VARCHAR (255)NOT NULL ,
  id_staff_role VARCHAR (255)NOT NULL ,
  PRIMARY KEY (id_kpi, id_staff_role),
  FOREIGN KEY (id_kpi) REFERENCES kpi(id),
  FOREIGN KEY (id_staff_role) REFERENCES staff_role(id)
);

CREATE TABLE indicators (
  id VARCHAR(36)not null,
  id_kpi VARCHAR(36) NOT NULL ,
  score NUMERIC(1) NOT NULL ,
  content VARCHAR(255) NOT NULL ,
  PRIMARY KEY (id),
  FOREIGN KEY (id_kpi) REFERENCES kpi(id)
);

create table periode (
  id VARCHAR (36)NOT NULL ,
  periode_name VARCHAR (36)NOT NULL,
  description VARCHAR (255) NOT NULL,
  start_date DATE NOT NULL ,
  end_date DATE NOT NULL ,
  active VARCHAR (36) NOT NULL,
  status VARCHAR(36) NOT NULL,
  PRIMARY KEY (id)

);

CREATE TABLE score (
  id VARCHAR(36)not null,
  id_staff VARCHAR(36) NOT NULL ,
  id_kpi VARCHAR(36)  ,
  id_periode VARCHAR(36)  ,
  score NUMERIC (1) ,
  remark VARCHAR (255) ,
  PRIMARY KEY (id),
  FOREIGN KEY (id_staff) REFERENCES staff(id),
  FOREIGN KEY (id_kpi) REFERENCES kpi(id),
  FOREIGN KEY (id_periode) REFERENCES periode(id)
);


CREATE TABLE evidence (
  id VARCHAR (36)NOT NULL ,
  id_staff VARCHAR (36)NOT NULL ,
  id_kpi VARCHAR (225) NOT NULL,
  id_periode VARCHAR (255) NOT NULL ,
  filename VARCHAR (255) NOT NULL,
  description VARCHAR (255) NOT NULL ,
  PRIMARY KEY (id),
  FOREIGN KEY (id_staff) REFERENCES staff(id),
  FOREIGN KEY (id_kpi) REFERENCES kpi(id),
  FOREIGN KEY (id_periode) REFERENCES periode(id)

);