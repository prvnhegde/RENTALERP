--
-- Upgrading to 2.8.0
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.0.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------

DROP FUNCTION if exists sp_attribute_add(integer, character varying, integer, text, integer, text, integer);
DROP FUNCTION if exists sp_attribute_update(integer, character varying, integer, text, integer, text);
DROP FUNCTION if exists sp_issue_add(character varying, text, text, integer, integer, integer, integer, integer, character varying, character varying, character varying, integer);
DROP FUNCTION if exists sp_attribute_type_field_map_delete(integer, integer, integer);
DROP FUNCTION if exists sp_attribute_type_field_map_update(integer, integer, integer);
DROP FUNCTION if exists sp_user_add(integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, character varying, integer, integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, integer, character varying, character varying, text, character varying, character varying, character varying, character varying, character varying, integer);
DROP FUNCTION if exists sp_user_update(integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, integer, integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, integer, character varying, character varying, text, character varying, character varying, character varying, character varying, character varying, integer);
DROP FUNCTION if exists sp_software_update(integer, character varying, text, integer, integer, integer, character varying, character varying, integer, integer, integer);
DROP FUNCTION if exists sp_software_add(character varying, text, integer, integer, character varying, character varying, integer, integer, integer);
DROP FUNCTION if exists sp_user_login(character varying, integer);
DROP FUNCTION if exists sp_issue_subscribers_update(integer, integer, integer);
DROP VIEW if exists attribute_view;
DROP VIEW if exists asset_software_view;
DROP VIEW if exists user_view;

-- ----------
-- Upgrades for this release
-- ----------
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (31, 'issue_proxy_submit', 1, 23);

ALTER TABLE issue ADD COLUMN proxy_creator integer;

ALTER TABLE attribute_type_field_map RENAME TO attribute_field_map;

CREATE TABLE attribute_group
(
   attribute_group_id integer, 
   attribute_group_name character varying(50) NOT NULL,
   object_type_id integer NOT NULL, 
   CONSTRAINT pk_attribute_group_id PRIMARY KEY (attribute_group_id)
);

ALTER TABLE attribute ADD COLUMN attribute_group_id integer;
ALTER TABLE attribute ADD CONSTRAINT fk_attribute_group_id FOREIGN KEY (attribute_group_id) REFERENCES attribute_group (attribute_group_id);

insert into access_page (page_id, page_name, module_id) values (299, '/admin/attribute-group-add.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 299);

insert into access_page (page_id, page_name, module_id) values (300, '/admin/attribute-group-add-2.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 300);

insert into access_page (page_id, page_name, module_id) values (301, '/admin/attribute-group-edit.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 301);

insert into access_page (page_id, page_name, module_id) values (302, '/admin/attribute-group-edit-2.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 302);

insert into access_page (page_id, page_name, module_id) values (303, '/admin/attribute-group-delete.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 303);

insert into access_page (page_id, page_name, module_id) values (304, '/admin/attribute-group-delete-2.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 304);

CREATE SEQUENCE seq_attribute_group_id
    INCREMENT 1
    NO MAXVALUE
    NO MINVALUE
    START 1
    CACHE 1;

insert into system_config (config_key, config_value) values ('Hardware.CheckUniqueHardwareName', 'false');
insert into system_config (config_key, config_value) values ('System.Security.UserPasswordComplexityEnabled', 'false');
insert into system_config (config_key, config_value) values ('System.Security.AccountLockoutThreshold', '0');
insert into system_config (config_key, config_value) values ('System.Security.AccountLockoutDurationMinutes', '0');

alter table asset_software add column software_expire_date timestamp(1) without time zone;
alter table asset_software add column software_version character varying(50);

alter table access_user add column invalid_logon_count smallint DEFAULT (0)::smallint;
alter table access_user add column invalid_logon_date timestamp(1) without time zone;

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.0' where config_key = 'schema.version';