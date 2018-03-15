--
-- Upgrading to 2.9.4
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.9.4.work' where config_key = 'schema.version';

-- ----------
-- Upgrades for this release.
-- For dropping of stored procedures, functions, be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the stored procedures, function exist yet.
-- ----------
-- Make company note field's required option configurable.
insert into object_type (object_type_id, object_key) values (16, 'company_note');
update attribute set object_type_id=16 where attribute_id = -16;

update attribute set is_required=0 where attribute_id = -16;

-- Empty "Allowed domains" if "Domain filtering" is set to "Allow all emails to go out"
update system_config set config_value = null where config_key = 'email.allowedDomains' and (select config_value from system_config where config_key='email.domainFiltering') = '1';

-- Remove domain filter option. Use "Allowed domains" instead.
delete from system_config where config_key='email.domainFiltering';
delete from system_config where config_key='email.domainFiltering.options';

insert into system_config (config_key, config_value) values ('Reports.Type.ContactReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.ContractReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.IssueReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.HardwareReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.HardwareMemberReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.HardwareLicenseReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.SoftwareReport.Filename', null);
insert into system_config (config_key, config_value) values ('Reports.Type.SoftwareUsageReport.Filename', null);

insert into system_config (config_key, config_value) values ('Email.IssueNotificationFromEmailEnabled', 'false');

update system_config set config_key = 'Email.IssueNotificationFromUiEnabled' where config_key = 'Email.IssueNotificationEnabled';

insert into system_config (config_key, config_value) values ('UI.DetailTableStyle', null);

insert into system_config (config_key, config_value) values ('_SpareConfigKey1', null);
insert into system_config (config_key, config_value) values ('_SpareConfigKey2', null);

-- ----------
-- 
-- ----------

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.9.4' where config_key = 'schema.version';
