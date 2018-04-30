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