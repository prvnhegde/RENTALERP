--
-- Upgrading to 2.8.1
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.1.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP FUNCTION if exists sp_software_add(character varying, text, integer, integer, integer, character varying, character varying, integer, integer, integer);
DROP FUNCTION if exists sp_user_delete(integer, integer);
DROP VIEW if exists contract_view;
DROP FUNCTION if exists sp_contract_add(character varying, text, integer, character varying, character varying, character varying, integer, integer, integer, integer, integer);
DROP FUNCTION if exists sp_contract_update(integer, character varying, text, integer, character varying, character varying, character varying, integer, integer, integer, integer, integer);
DROP FUNCTION if exists sp_file_delete(integer, integer);
DROP FUNCTION if exists sp_hardware_component_delete(integer, integer);
DROP FUNCTION if exists sp_software_license_delete(integer, integer);

-- ----------
-- Upgrades for this release
-- ----------
insert into access_page (page_id, page_name, module_id) values (305, '/software/ajax-get-license-details.dll', 2);
insert into access_perm_page_map(perm_id, page_id) values (8, 305);

insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/default/images/icons/tablet-apple-ipad.png', 1, -10);

insert into system_config (config_key, config_value) values ('auth.ldap.url.scheme', 'ldap://');

update access_page set page_name = replace(page_name, '/portal/blog-', '/blogs/') where module_id=6;
update access_page set page_name = '/blogs/index.dll' where page_name = '/portal/index.dll' and module_id=6;

insert into access_page (page_id, page_name, module_id) values (306, '/blogs/post-edit.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 306);

insert into access_page (page_id, page_name, module_id) values (307, '/blogs/post-edit-2.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 307);

insert into access_page (page_id, page_name, module_id) values (308, '/blogs/post-delete.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 308);

insert into access_page (page_id, page_name, module_id) values (309, '/blogs/post-delete-2.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 309);

update access_permission set order_num = 132 where perm_id = 25;

-- Add contract stages
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-21, 9, 'contract_stage', 0, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-10, -21, 'contract_stage_draft', NULL, NULL, 'contract_stage_draft', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-11, -21, 'contract_stage_in_effect', NULL, NULL, 'contract_stage_in_effect', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-12, -21, 'contract_stage_archive', NULL, NULL, 'contract_stage_archive', NULL);

ALTER TABLE contract ADD COLUMN contract_stage integer;

-- Report issue link configuration
update system_config set config_key='Issues.ReportIssueFooterEnabled' where config_key='Issues.ReportIssueEnabled';
insert into system_config (config_key, config_value) values ('Issues.ReportIssueModuleEnabled', 'false');

insert into object_type (object_type_id, object_key) values (15, 'hardware_component');

insert into access_page (page_id, page_name, module_id) values (310, '/hardware/ajax-get-component-custom-fields.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (6, 310);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.1' where config_key = 'schema.version';