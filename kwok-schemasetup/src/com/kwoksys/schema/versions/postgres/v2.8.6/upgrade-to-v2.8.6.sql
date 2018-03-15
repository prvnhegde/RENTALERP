--
-- Upgrading to 2.8.6
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.6.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP VIEW if exists attribute_value_view;

-- ----------
-- Upgrades for this release
-- ----------
update system_config set config_value = 'Etc/GMT+12,Etc/GMT+11,US/Hawaii,US/Alaska,PST,US/Arizona,US/Mountain,US/Central,US/Eastern,Canada/Atlantic,America/Montevideo,Atlantic/South_Georgia,Atlantic/Cape_Verde,Etc/Greenwich,Europe/London,Europe/Amsterdam,Asia/Jerusalem,Europe/Moscow,Asia/Tehran,Asia/Baku,Asia/Kabul,Asia/Karachi,Asia/Calcutta,Asia/Dhaka,Asia/Bangkok,Asia/Hong_Kong,Asia/Seoul,Australia/Brisbane,Australia/Canberra,Asia/Magadan,Pacific/Auckland' where config_key = 'timezone.local.options';
update system_config set config_value = 'US/Mountain' where config_key='timezone.local' and config_value='MST';
update system_config set config_value = 'US/Eastern' where config_key='timezone.local' and config_value='EST';

insert into access_permission (perm_id, perm_name, perm_is_enabled, order_num) values (32, 'user_preference_contact_write', 1, 152);

insert into access_page (page_id, page_name, module_id) values (324, '/user-preference/contact-edit.dll', 11);
insert into access_perm_page_map(perm_id, page_id) values (32, 324);

insert into access_page (page_id, page_name, module_id) values (325, '/user-preference/contact-edit-2.dll', 11);
insert into access_perm_page_map(perm_id, page_id) values (32, 325);

insert into access_page (page_id, page_name, module_id) values (326, '/contacts/company-list-export.dll', 5);
insert into access_perm_page_map(perm_id, page_id) values (10, 326);

update access_page set page_name = replace(page_name, 'contact-mgmt', 'contacts') where page_name like '/contact-mgmt/%';

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.6' where config_key = 'schema.version';