--
-- Upgrading to 2.8.4
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.4.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------

-- ----------
-- Upgrades for this release
-- ----------
insert into access_page (page_id, page_name, module_id) values (319, '/admin/data-import-index.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 319);

insert into access_page (page_id, page_name, module_id) values (320, '/admin/data-import-validate.dll', 10);
insert into access_perm_page_map (perm_id, page_id) values (4, 320);

insert into access_page (page_id, page_name, module_id) values (321, '/admin/data-import-execute.dll', 10);
insert into access_perm_page_map (perm_id, page_id) values (4, 321);

insert into access_page (page_id, page_name, module_id) values (322, '/admin/data-import-results.dll', 10);
insert into access_perm_page_map (perm_id, page_id) values (4, 322);

insert into system_config (config_key, config_value) values ('Issues.multipleDeleteEnabled', 'false');

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.4' where config_key = 'schema.version';