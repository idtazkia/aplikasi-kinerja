create table staff_role (
  id VARCHAR (255)NOT NULL ,
  role_name VARCHAR (36)NOT NULL ,
  description VARCHAR (255),
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
  id VARCHAR (255)NOT NULL ,
  id_staff VARCHAR (255)NOT NULL ,
  id_role VARCHAR (255)NOT NULL ,
  PRIMARY KEY (id),
  FOREIGN KEY (id_staff) REFERENCES staff(id),
  FOREIGN KEY (id_role) REFERENCES staff_role(id)
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
  weight VARCHAR (36)NOT NULL ,
  base_line VARCHAR(36) ,
  target VARCHAR(36) ,
  id_staff_role_kpi VARCHAR (255),
  PRIMARY KEY (id),
  FOREIGN KEY (id_category) REFERENCES category(id)

);

create table staff_role_kpi (
  id VARCHAR (255)NOT NULL ,
  id_kpi VARCHAR (255)NOT NULL ,
  id_role VARCHAR (255)NOT NULL ,
  PRIMARY KEY (id),
  FOREIGN KEY (id_kpi) REFERENCES kpi(id),
  FOREIGN KEY (id_role) REFERENCES staff_role(id)
);

CREATE TABLE staff_kpi(
id VARCHAR (36)NOT NULL,
id_staff VARCHAR (36)NOT NULL ,
id_kpi VARCHAR (36)NOT NULL,
evidence VARCHAR (255),
PRIMARY KEY (id),
FOREIGN KEY (id_staff) REFERENCES staff(id)
);


CREATE TABLE indicators (
  id VARCHAR(36)not null,
  id_kpi VARCHAR(36) NOT NULL ,
  score VARCHAR(36) NOT NULL ,
  content VARCHAR(255) NOT NULL ,
  PRIMARY KEY (id),
  FOREIGN KEY (id_kpi) REFERENCES kpi(id)
);

CREATE TABLE score (
  id VARCHAR(36)not null,
  id_staff_kpi VARCHAR(36) NOT NULL ,
  score VARCHAR(36)  ,
  remark VARCHAR(255)  ,
  total VARCHAR (255) ,
  employee_comment VARCHAR (255),
  evidence VARCHAR (255),
  PRIMARY KEY (id),
  FOREIGN KEY (id_staff_kpi) REFERENCES staff_kpi(id)
);

CREATE TABLE evidence (
  id VARCHAR (36)NOT NULL ,
  id_staff_kpi VARCHAR (36)NOT NULL ,
  file_name VARCHAR (225) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_staff_kpi) REFERENCES staff_kpi(id)

);

CREATE TABLE staff_superior (
  id VARCHAR (36)NOT NULL ,
  id_staff VARCHAR (36)NOT NULL ,
  id_superior VARCHAR (225) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_staff) REFERENCES staff(id)

);

create table periode (
  id VARCHAR (36)NOT NULL ,
  periode_name VARCHAR (36)NOT NULL,
  description VARCHAR (255) NOT NULL,
  start_date DATE NOT NULL ,
  end_date DATE NOT NULL ,
  active BOOLEAN NOT NULL,
  PRIMARY KEY (id)

);

