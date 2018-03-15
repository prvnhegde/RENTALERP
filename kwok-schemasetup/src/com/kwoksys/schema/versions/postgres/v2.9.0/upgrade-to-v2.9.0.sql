--
-- Upgrading to 2.9.0
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.9.0.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
-- Allow configuring whether a custom attribute is required.
DROP FUNCTION if exists sp_attribute_add(integer, character varying, character varying, integer, text, integer, text, integer, integer, character varying, character varying);
DROP FUNCTION if exists sp_attribute_update(integer, character varying, character varying, integer, text, integer, text, integer, character varying, character varying);

-- ----------
-- Upgrades for this release
-- ----------
update attribute set is_required=0 where attribute_id>0;

-- Make contract stage attribute editable.
update attribute set is_editable=1 where attribute_id=-21;

-- Make company type attribute editable.
update attribute set is_editable=1, attribute_key='company_types' where attribute_id=-18;

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.9.0' where config_key = 'schema.version';