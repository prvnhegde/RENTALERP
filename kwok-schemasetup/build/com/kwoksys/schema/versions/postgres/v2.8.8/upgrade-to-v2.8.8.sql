--
-- Upgrading to 2.8.8
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.8.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP FUNCTION if exists sp_attribute_add(integer, character varying, character varying, integer, text, integer, text, integer, integer, character varying);
DROP FUNCTION if exists sp_attribute_update(integer, character varying, character varying, integer, text, integer, text, integer, character varying);

-- ----------
-- Upgrades for this release
-- ----------
INSERT INTO system_config (config_key, config_value) VALUES ('Logging.Level.Template', 'OFF');

update system_config set config_value='OFF' where config_key in ('logging.database.level', 'logging.ldap.level', 'Logging.Level.Scheduler', 'Logging.Level.Template') and config_value='FINE';

ALTER TABLE attribute ADD COLUMN type_currency_symbol character varying(10);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.8' where config_key = 'schema.version';