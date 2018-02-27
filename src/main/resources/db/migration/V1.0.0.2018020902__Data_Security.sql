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

-- INSERT INTO s_user (id, active, username, id_role) VALUES
--   ('001', true, 'user', 'pendaftar');
--
-- INSERT INTO s_user (id, active, username, id_role) VALUES
--   ('u002', true, 'admin', 'humas');
--
-- -- password : 123
-- INSERT INTO s_user_password (id, id_user, password) VALUES
--   ('up001', 'u001', '$2a$17$Mhfv.hlqIybDHWqAaTMU/.PKi8RDntt6xe9pTMGQLfnW3phTlhROm');
--
-- -- password : 1234
-- INSERT INTO s_user_password (id, id_user, password) VALUES
--   ('up002', 'u002', '$2a$17$g6pNPoXyIknhS1lax/zIoetaUWWTeG7tP/xV3Fpx1FCY3mjDfWnT.');