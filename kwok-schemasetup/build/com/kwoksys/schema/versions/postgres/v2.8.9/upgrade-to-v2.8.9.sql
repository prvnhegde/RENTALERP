--
-- Upgrading to 2.8.9
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.9.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------

-- ----------
-- Upgrades for this release
-- ----------
update system_config set config_value = '200000' where config_key='kb.article.charLimit';

-- Strip page extension.
update access_page set page_name = replace(page_name, '.dll', '');

-- Cleanup /IT/hardware-category-list, /IT/hardware-license-remove, /contacts/company-bookmark-delete,
-- /admin/usage-survey pages.
delete from access_perm_page_map where page_id in (26, 47, 107, 216);
delete from access_page where page_id in (26, 47, 107, 216);

update access_page set page_name=replace(page_name, '/IT/hardware-', '/hardware/') where page_name like '/IT/hardware-%';
update access_page set page_name=replace(page_name, '/IT/software-', '/software/') where page_name like '/IT/software-%';
update access_page set page_name=replace(page_name, '/IT/contract-', '/contracts/') where page_name like '/IT/contract-%';
update access_page set page_name=replace(page_name, '/issue-tracker/issue-', '/issues/') where page_name like '/issue-tracker/issue-%';
update access_page set page_name=replace(page_name, '/portal/rss-', '/rss/') where page_name like '/portal/rss-%';
update access_page set page_name='/issues/index' where page_name = '/issue-tracker/index';
update access_page set page_name='/reports/report-type-select' where page_name='/reports/report-step1';
update access_page set page_name='/reports/report-search-criteria' where page_name='/reports/report-step2';
update access_page set page_name='/reports/report-ouput-select' where page_name='/reports/report-step3';
update access_page set page_name='/reports/report-results-export' where page_name='/reports/report-step4';

insert into system_config (config_key, config_value) values ('Issues.IssueReportEmailTemplate', null);
insert into system_config (config_key, config_value) values ('Issues.IssueAddEmailTemplate', null);
insert into system_config (config_key, config_value) values ('Issues.IssueUpdateEmailTemplate', null);

insert into system_config (config_key, config_value) values ('Issues.DueDateDiff', null);

-- New Admin user logout page
insert into access_page (page_id, page_name, module_id) values (327, '/admin/user-logout', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 327);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.9' where config_key = 'schema.version';