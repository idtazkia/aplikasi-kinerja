INSERT INTO s_permission (id, permission_value, permission_label) VALUES
  ('editsuperior', 'EDIT_SUPERIOR', 'Edit Superior'),
  ('viewsuperior', 'VIEW_SUPERIOR', 'View Superior'),
  ('editemployee', 'EDIT_EMPLOYEE', 'Edit Employee'),
  ('viewemployee', 'VIEW_EMPLOYEE', 'View Employee'),
  ('editstaff', 'EDIT_STAFF', 'Edit Staff'),
  ('viewstaff', 'VIEW_STAFF', 'View Staff'),
  ('viewkabag', 'VIEW_KABAG', 'View Kabag SDM'),
  ('editkabag', 'EDIT_KABAG', 'Edit kabag SDM');

INSERT INTO s_role (id, description, name) VALUES
  ('employee', 'EMPLOYEE', 'Employee'),
  ('superior', 'SUPERIOR', 'Superior'),
  ('staff_sdm', 'STAFF SDM', 'Staff SDM'),
  ('kabag_sdm', 'KABAG SDM', 'Kabag SDM');


INSERT INTO s_role_permission (id_role, id_permission) VALUES
  ('employee', 'viewemployee'),
  ('employee', 'editemployee'),
  ('superior', 'viewsuperior'),
  ('superior', 'editsuperior'),
  ('staff_sdm', 'viewstaff'),
  ('staff_sdm', 'editstaff'),
  ('kabag_sdm', 'viewkabag'),
  ('kabag_sdm', 'editkabag');

insert into category (id,name) values
('001','individual_score'),
('002','tazkia_score');

INSERT INTO periode (id,periode_name,description,start_date,end_date,active,status) VALUES
('001','Pengisian Kpi I','Pengisian KPI I','01-07-2017','30-06-2020','AKTIF','AKTIF'),
('002','Pengisian Kpi II','Pengisian KPI II','01-07-2021','30-06-2025','NONAKTIF','AKTIF');

