--
-- Upgrading to 2.8.3
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.3.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------

-- ----------
-- Upgrades for this release
-- ----------
insert into access_page (page_id, page_name, module_id) values (316, '/hardware/custom-fields.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (6, 316);

insert into access_page (page_id, page_name, module_id) values (317, '/software/custom-fields.dll', 2);
insert into access_perm_page_map(perm_id, page_id) values (8, 317);

insert into access_page (page_id, page_name, module_id) values (318, '/contracts/custom-fields.dll', 3);
insert into access_perm_page_map(perm_id, page_id) values (12, 318);

insert into system_config (config_key, config_value) values ('CustomFields.Expand', 'true');

insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/default/images/icons/platform_android.png', 1, -7);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.3' where config_key = 'schema.version';