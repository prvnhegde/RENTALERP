--
-- Upgrading to 2.8.7
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.7.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------

-- ----------
-- Upgrades for this release
-- ----------
update icon set icon_path = replace(icon_path, '/default/', '/');

insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/images/icons/firewall.png', 1, -10);

insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/images/icons/firewall-2.png', 1, -10);
insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/images/icons/load-balancer.png', 1, -10);
insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/images/icons/router.png', 1, -10);
insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/images/icons/switch.png', 1, -10);

INSERT INTO system_config (config_key, config_value) VALUES ('System.DB.MaxConnection', '50');
INSERT INTO system_config (config_key, config_value) VALUES ('Logging.Level.Scheduler', 'FINE');
INSERT INTO system_config (config_key, config_value) VALUES ('Hardware.CheckUniqueSerialNumber', 'false');

delete from system_config where config_key='characterEncoding';

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.7' where config_key = 'schema.version';