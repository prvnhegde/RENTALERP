--
-- Upgrading to 2.8.5
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.5.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP VIEW if exists attribute_view;
DROP FUNCTION if exists sp_attribute_update(integer, character varying, integer, text, integer, text, integer);
DROP FUNCTION if exists sp_attribute_add(integer, character varying, integer, text, integer, text, integer, integer);

-- ----------
-- Upgrades for this release
-- ----------
insert into access_page (page_id, page_name, module_id) values (323, '/software/ajax-get-license-custom-fields.dll', 8);
insert into access_perm_page_map(perm_id, page_id) values (2, 323);

alter table attribute add column input_mask character varying(50);
alter table attribute add column description character varying(255);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.5' where config_key = 'schema.version';