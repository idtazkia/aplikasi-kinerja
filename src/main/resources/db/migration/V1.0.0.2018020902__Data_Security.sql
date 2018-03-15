INSERT INTO s_permission (id, permission_value, permission_label) VALUES
  ('editsuperior', 'EDIT_SUPERIOR', 'Edit Superior'),
  ('viewsuperior', 'VIEW_SUPERIOR', 'View Superior'),
  ('editemployee', 'EDIT_EMPLOYEE', 'Edit Employee'),
  ('viewemployee', 'VIEW_EMPLOYEE', 'View Employee');

INSERT INTO s_role (id, description, name) VALUES
  ('employee', 'EMPLOYEE', 'Employee'),
  ('superior', 'SUPERIOR', 'Superior');

INSERT INTO s_role_permission (id_role, id_permission) VALUES
  ('employee', 'viewemployee'),
  ('employee', 'editemployee'),
  ('superior', 'viewsuperior'),
  ('superior', 'editsuperior');