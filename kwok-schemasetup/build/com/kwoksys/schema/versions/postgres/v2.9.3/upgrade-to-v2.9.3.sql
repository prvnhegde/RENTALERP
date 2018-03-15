--
-- Upgrading to 2.9.3
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.9.3.work' where config_key = 'schema.version';

-- ----------
-- Upgrades for this release.
-- For dropping of stored procedures, functions, be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the stored procedures, function exist yet.
-- ----------
ALTER TABLE attribute_field ADD COLUMN field_1 character varying(255);

insert into system_config (config_key, config_value) values ('Hardware.BulkDeleteEnabled', 'false');

-- ----------
-- 
-- ----------

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.9.3' where config_key = 'schema.version';
