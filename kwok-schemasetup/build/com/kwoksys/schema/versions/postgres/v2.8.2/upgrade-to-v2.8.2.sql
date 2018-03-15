--
-- Upgrading to 2.8.2
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.2.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP FUNCTION if exists sp_system_attribute_update(integer, integer);

-- ----------
-- Upgrades for this release
-- ----------
-- Hardware contacts tab
insert into access_page (page_id, page_name, module_id) values (311, '/hardware/contacts.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (6, 311);

insert into access_page (page_id, page_name, module_id) values (312, '/hardware/contact-add.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (22, 312);

insert into access_page (page_id, page_name, module_id) values (313, '/hardware/contact-add-2.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (22, 313);

insert into access_page (page_id, page_name, module_id) values (314, '/hardware/contact-remove.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (22, 314);

insert into access_page (page_id, page_name, module_id) values (315, '/reports/report-contact.dll', 15);
insert into access_perm_page_map(perm_id, page_id) values (13, 315);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.2' where config_key = 'schema.version';