--
-- Upgrading to 2.9.2
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.9.2.work' where config_key = 'schema.version';

-- ----------
-- Drop stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the stored procedures, function exist yet).
-- ----------
update system_config set config_value = 'false' where config_key = 'email.notification' and config_value='1';
update system_config set config_value = 'true' where config_key = 'email.notification' and config_value='2';

update system_config set config_key = 'Email.IssueNotificationEnabled' where config_key = 'email.notification';
update system_config set config_key = 'Email.ContractExpirationNotificationEnabled', config_value = 'false' where config_key = 'email.notification.options';

update contract set contract_expiration_notified = 0 where contract_expiration_date < now() and contract_owner is not null;

insert into system_config (config_key, config_value) values ('Email.ContractExpireNotificationTemplate', null);
-- ----------
-- Upgrades for this release
-- ----------

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.9.2' where config_key = 'schema.version';