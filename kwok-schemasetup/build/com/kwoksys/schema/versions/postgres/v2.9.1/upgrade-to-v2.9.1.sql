--
-- Upgrading to 2.9.1
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.9.1.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP FUNCTION if exists sp_kb_article_add(character varying, text, integer, integer, integer, integer);
DROP FUNCTION if exists sp_kb_article_update(integer, character varying, text, integer, integer, integer, integer);

-- ----------
-- Upgrades for this release
-- ----------
delete from access_perm_page_map where page_id=69;
delete from access_page where page_id=69;
insert into system_config (config_key, config_value) values ('KB.Article.MediaWikiSyntaxEnabled', 'false');

-- Add file edit pages
insert into access_page (page_id, page_name, module_id) values (328, '/hardware/file-edit-2', 1);
insert into access_perm_page_map(perm_id, page_id) values (22, 328);

insert into access_page (page_id, page_name, module_id) values (329, '/hardware/file-edit', 1);
insert into access_perm_page_map(perm_id, page_id) values (22, 329);

insert into access_page (page_id, page_name, module_id) values (330, '/software/file-edit', 2);
insert into access_perm_page_map(perm_id, page_id) values (23, 330);

insert into access_page (page_id, page_name, module_id) values (331, '/software/file-edit-2', 2);
insert into access_perm_page_map(perm_id, page_id) values (23, 331);

insert into access_page (page_id, page_name, module_id) values (332, '/contacts/company-file-edit', 5);
insert into access_perm_page_map(perm_id, page_id) values (11, 332);

insert into access_page (page_id, page_name, module_id) values (333, '/contacts/company-file-edit-2', 5);
insert into access_perm_page_map(perm_id, page_id) values (11, 333);

insert into access_page (page_id, page_name, module_id) values (334, '/contracts/file-edit', 3);
insert into access_perm_page_map(perm_id, page_id) values (9, 334);

insert into access_page (page_id, page_name, module_id) values (335, '/contracts/file-edit-2', 3);
insert into access_perm_page_map(perm_id, page_id) values (9, 335);

alter table contract add column contract_expiration_notified smallint;

insert into access_page (page_id, page_name, module_id) values (336, '/kb/article-wiki-preview', 14);
insert into access_perm_page_map(perm_id, page_id) values (27, 336);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.9.1' where config_key = 'schema.version';