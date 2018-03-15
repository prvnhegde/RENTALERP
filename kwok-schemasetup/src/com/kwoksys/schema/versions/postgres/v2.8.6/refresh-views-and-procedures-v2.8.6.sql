--
-- pk_ for primary keys
-- fk_ for foreign keys
-- uk_ for unique keys

-- sp_ for functions

-- idx_ for indexes
-- seq_ for sequences

-- p_ prefix is for input parameters 
-- o_ for output parameters
-- v_ for variables

-- Keywords: _add, _update, _delete

-- -------------------------------------------------------------------
-- For Views
-- -------------------------------------------------------------------
--
-- View: admin_perm_page_map_view
--
create or replace view admin_perm_page_map_view as 
	select ag.perm_id, ag.perm_name, ap.page_id, ap.page_name, ap.module_id
	from access_perm_page_map gpm
	left outer join access_permission ag on gpm.perm_id = ag.perm_id
	left outer join access_page ap on gpm.page_id = ap.page_id;

--
-- View: asset_hardware_view
--
create or replace view asset_hardware_view as 
	select ah.hardware_id, ah.hardware_number, ah.hardware_name, ah.hardware_description, ah.hardware_type, ah.hardware_status, 
		ho.display_name as hardware_owner_display_name, ho.username as hardware_owner_username, ah.hardware_owner as hardware_owner_id, ah.hardware_model_name, ah.hardware_model_number,
		ah.hardware_serial_number, ah.hardware_cost, ah.hardware_last_service_date, ah.hardware_purchase_date, ah.hardware_warranty_expire_date, 
		ah.hardware_location, ah.manufacturer_company_id, ah.vendor_company_id, ah.software_count, ah.file_count, ah.component_count,
		ah.creator, ah.creation_date, hc.username as creator_username, hc.display_name as creator_display_name, 
		ah.modifier, ah.modification_date, hm.username as modifier_username, hm.display_name as modifier_display_name
	from asset_hardware ah 
    left outer join access_user ho on ah.hardware_owner = ho.user_id 
	left outer join access_user hc on ah.creator = hc.user_id 
	left outer join access_user hm on ah.modifier = hm.user_id; 

--
-- View: asset_software_view
--
create or replace view asset_software_view as 
	select	s.software_id, s.software_name, s.software_description, s.software_owner as software_owner_id, so.display_name as software_owner_display_name, 
			so.username as software_owner_username, s.software_type, s.software_version, s.software_expire_date, 
			s.operating_system, s.quoted_retail_price, s.quoted_oem_price, 
			s.manufacturer_company_id, s.vendor_company_id, s.license_count, s.file_count, s.bookmark_count,
			s.creator, s.creation_date, sc.username as creator_username, sc.display_name as creator_display_name, 
			s.modifier, s.modification_date, sm.username as modifier_username, sm.display_name as modifier_display_name
    from	asset_software s
	left outer join access_user sc on s.creator = sc.user_id 
	left outer join access_user sm on s.modifier = sm.user_id
	left outer join access_user so on so.user_id = s.software_owner;

--
-- View: attribute_field_view
--
create or replace view attribute_field_view as 
	select af.attribute_field_id, af.attribute_field_name, af.attribute_id, af.field_key, af.attribute_field_description, af.icon_id, af.is_disabled
	from attribute_field af;

--
-- View: attribute_view
--
create or replace view attribute_view as 
	select a.attribute_id, a.object_type_id, so.object_key, a.attribute_key as attribute_name, a.attribute_type, a.attribute_option, a.attribute_convert_url,
		a.default_attribute_field_id, a.attribute_group_id, a.is_editable, a.is_required, a.is_custom_attr, a.attribute_url,
		a.input_mask, a.description
	from attribute a, object_type so
	where a.object_type_id = so.object_type_id;

--
-- View: attribute_value_view
--
create or replace view attribute_value_view as 
	select a.attribute_id, a.object_type_id, a.attribute_key as attribute_name, a.attribute_type, av.attr_value, av.object_id, av.attribute_field_id
	from attribute a, object_attribute_value av
	where a.attribute_id = av.attribute_id;

--
-- View: bookmark_view
--
create or replace view bookmark_view as 
	select b.bookmark_id, b.bookmark_name, b.bookmark_description, b.bookmark_path, bm.object_type_id, bm.object_id
	from bookmark b, bookmark_map bm 
	where b.bookmark_id=bm.bookmark_id; 

--
-- View: category
--
create or replace view category_view as 
	select c.category_id, c.category_name, c.category_description, c.object_count, c.object_type_id,
		c.creator, c.creation_date, uc.username as creator_username, uc.display_name as creator_display_name
	from category c
	left outer join access_user uc on c.creator = uc.user_id; 

--
-- View: contract_view
--
create or replace view contract_view as 
	select c.contract_id, c.contract_name, c.contract_description, c.contract_owner as contract_owner_id, co.display_name as contract_owner_display_name, co.username as contract_owner_username, 
		c.contract_type, c.contract_stage, c.contract_effective_date, c.contract_expiration_date, c.contract_renewal_type, c.contract_renewal_date, 
		c.creator, c.creation_date, cc.username as creator_username, cc.display_name as creator_display_name, 
		c.modifier, c.modification_date, cm.username as modifier_username, cm.display_name as modifier_display_name,
        c.contract_provider as contract_provider_id, cp.company_name as contract_provider_name
	from contract c
	left outer join access_user cc on c.creator = cc.user_id 
	left outer join access_user cm on c.modifier = cm.user_id
	left outer join access_user co on c.contract_owner = co.user_id
	left outer join company cp on c.contract_provider = cp.company_id;

--
-- View: company_view
--
create or replace view company_view as 
	select c.company_id, c.company_name, c.company_description, c.main_contact_count, c.employee_contact_count, c.file_count, c.bookmark_count, c.note_count, 
		c.creator, c.creation_date, cc.username as creator_username, cc.display_name as creator_display_name, 
		c.modifier, c.modification_date, cm.username as modifier_username, cm.display_name as modifier_display_name
	from company c
	left outer join access_user cc on c.creator = cc.user_id 
	left outer join access_user cm on c.modifier = cm.user_id; 

--
-- View: company_tag_map_view
--
create or replace view company_tag_map_view as 
	select ct.tag_id, ct.tag_name, ctm.company_id
	from company_tag ct, company_tag_map ctm
	where ct.tag_id = ctm.tag_id; 

--
-- View: contact_view
--
create or replace view contact_view as 
	select c.contact_id, c.contact_first_name, c.contact_last_name, c.contact_title, c.contact_phone_home, c.contact_phone_mobile, c.contact_phone_work, c.contact_description, 
		c.contact_fax, c.contact_email_primary, c.contact_email_secondary, c.contact_homepage_url, co.company_name, c.company_id, c.company_contact_type,
		c.address_street_primary, c.address_city_primary, c.address_state_primary, c.address_zipcode_primary, c.address_country_primary,
		c.messenger_1_type, c.messenger_1_id, c.messenger_2_type, c.messenger_2_id, c.user_id, 
		c.creator, c.creation_date, cc.username as creator_username, cc.display_name as creator_display_name, 
		c.modifier, c.modification_date, cm.username as modifier_username, cm.display_name as modifier_display_name
	from contact c 
	left outer join company co on c.company_id = co.company_id
	left outer join access_user cc on c.creator = cc.user_id 
	left outer join access_user cm on c.modifier = cm.user_id; 

--
-- View: file_view
--
create or replace view file_view as 
	select	f.file_id, f.file_name, f.file_friendly_name, f.file_description, f.file_mime_type, f.file_byte_size, f.file_physical_name, 
		f.creator, f.creation_date, fm.object_type_id, fm.object_id
	from	file f, file_map fm 
	where	f.file_id=fm.file_id; 

--
-- View: issue_view
--
create or replace view issue_view as 
	select	i.issue_id, i.issue_name, i.issue_description, i.issue_assignee as assignee_id, ia.username as assignee_username, ia.display_name as assignee_display_name, 
			i.issue_url, i.issue_type, i.issue_status, i.issue_priority, i.issue_resolution, i.duplicate_id, 
			i.issue_due_date, i.issue_created_from_email, i.creator_ip, 
			i.creator, i.creation_date, ic.username as creator_username, ic.display_name as creator_display_name, 
			i.modifier, i.modification_date, im.username as modifier_username, im.display_name as modifier_display_name 
	from	issue i
	left outer join access_user ic on i.creator = ic.user_id
	left outer join access_user im on i.modifier = im.user_id
	left outer join access_user ia on i.issue_assignee = ia.user_id;

--
-- View: blog_post_view
--
create or replace view blog_post_view as 
	select bp.post_id, bp.post_name, bp.post_description, bp.post_type, bp.post_ip, bp.post_allow_comment, bp.comment_count, bp.category_id, bpc.category_name,
		bp.creator, bp.creation_date, uc.username as creator_username, uc.display_name as creator_display_name,
		bp.modifier, bp.modification_date, uc.username as modifier_username, um.display_name as modifier_display_name
	from blog_post bp
	left outer join access_user uc on bp.creator = uc.user_id 
	left outer join access_user um on bp.modifier = um.user_id
	left outer join category bpc on bp.category_id = bpc.category_id; 

--
-- View: blog_post_comment_view
--
create or replace view blog_post_comment_view as 
	select pc.comment_id, pc.comment_description, pc.comment_ip, pc.post_id, 
		pc.creator, pc.creation_date, uc.username as creator_username, uc.display_name as creator_display_name
	from blog_post_comment pc
	left outer join access_user uc on pc.creator = uc.user_id; 

--
-- View: site_view
--
create or replace view site_view as 
	select s.site_id, s.site_name, s.site_path, s.site_description, s.site_placement, s.site_support_iframe, s.category_id, c.category_name,
		s.creator, s.creation_date, uc.username as creator_username, uc.display_name as creator_display_name,
		s.modifier, s.modification_date, um.username as modifier_username, um.display_name as modifier_display_name
	from portal_site s
	left outer join access_user uc on s.creator = uc.user_id 
	left outer join access_user um on s.modifier = um.user_id 
	left outer join category c on s.category_id = c.category_id; 

--
-- View: user_view
--
create or replace view user_view as 
	select u.user_id, u.username, u.password, u.display_name, u.status, u.first_name, u.last_name, u.email, u.hardware_count, u.is_default_user, 
		u.last_logon, u.last_visit, u.session_key, u.invalid_logon_count, u.invalid_logon_date,
		u.creator, u.creation_date, uc.username as creator_username, uc.display_name as creator_display_name,
		u.modifier, u.modification_date, um.username as modifier_username, um.display_name as modifier_display_name, c.contact_id
	from access_user u
	left outer join access_user uc on u.creator = uc.user_id 
	left outer join access_user um on u.modifier = um.user_id
	left outer join contact c on u.user_id = c.user_id;  

--
-- View: group_view
--
create or replace view group_view as 
	select g.group_id, g.group_name, g.group_description, 
		g.creator, g.creation_date, gc.username as creator_username, gc.display_name as creator_display_name,
		g.modifier, g.modification_date, gm.username as modifier_username, gm.display_name as modifier_display_name
	from access_group g
	left outer join access_user gc on g.creator = gc.user_id 
	left outer join access_user gm on g.modifier = gm.user_id;

--
-- View: kb_article_view
--
create or replace view kb_article_view as 
	select a.article_id, a.article_name, a.article_text, a.category_id, c.category_name, a.view_count, a.article_syntax_type, a.article_wiki_namespace,
		a.creator, uc.username as creator_username, uc.display_name as creator_display_name, a.creation_date,  
		a.modifier, um.username as modifier_username, um.display_name as modifier_display_name, a.modification_date
	from kb_article a
	left outer join access_user uc on a.creator = uc.user_id 
	left outer join access_user um on a.modifier = um.user_id
	left outer join category c on a.category_id = c.category_id; 

--
-- Stored Procedures Creation SQL Scripts
--

--
-- This is for resetting all sequences.
--
create or replace function sp_admin_reset_all_sequences()
RETURNS void AS $$
begin
	select setval('seq_asset_hardware_id', max(hardware_id)) from asset_hardware;
	select setval('seq_asset_hardware_comp_id', max(comp_id)) from asset_hardware_component;
	select setval('seq_asset_software_id', max(software_id)) from asset_software;
	select setval('seq_asset_map_id', max(map_id)) from asset_map;
	select setval('seq_asset_software_license_id', max(license_id)) from asset_software_licenses;
	select setval('seq_attribute_id', max(attribute_id)) from attribute;
	select setval('seq_attribute_field_id', max(attribute_field_id)) from attribute_field;
	select setval('seq_category_id', max(category_id)) from category;
	select setval('seq_blog_post_id', max(post_id)) from blog_post;
	select setval('seq_blog_post_comment_id', max(comment_id)) from blog_post_comment;
	select setval('seq_bookmark_id', max(bookmark_id)) from bookmark;
	select setval('seq_bookmark_map_id', max(bookmark_map_id)) from bookmark_map;
	select setval('seq_company_id', max(company_id)) from company;
	select setval('seq_company_note_id', max(note_id)) from company_note;
	select setval('seq_company_tag_id', max(tag_id)) from company_tag;
	select setval('seq_contact_id', max(contact_id)) from contact;
	select setval('seq_contract_id', max(contract_id)) from contract;
	select setval('seq_file_id', max(file_id)) from file;
	select setval('seq_file_map_id', max(file_map_id)) from file_map;
	select setval('seq_icon_id', max(icon_id)) from icon;
	select setval('seq_kb_article_id', max(article_id)) from kb_article;
	select setval('seq_issue_id', max(issue_id)) from issue;
	select setval('seq_issue_comment_id', max(issue_comment_id)) from issue_comment;
	select setval('seq_issue_change_id', max(issue_change_id)) from issue_change;
	select setval('seq_portal_site_id', max(site_id)) from portal_site;
	select setval('seq_rss_feed_id', max(feed_id)) from rss_feed;
	select setval('seq_user_id', max(user_id)) from access_user;
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for adding attribute group.
-- 
create or replace function sp_attribute_group_add(out o_attribute_group_id int, in p_object_type_id int, in p_attribute_group_name varchar(50))
RETURNS integer AS $$
begin
	select nextval('seq_attribute_group_id') into o_attribute_group_id;

	insert into attribute_group (attribute_group_id, object_type_id, attribute_group_name)
		values (o_attribute_group_id, p_object_type_id, p_attribute_group_name);
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for updating attribute group.
-- 
create or replace function sp_attribute_group_update(in p_attribute_group_id int, in p_object_type_id int, in p_attribute_group_name varchar(50))
RETURNS void AS $$
begin
	update attribute_group set attribute_group_name = p_attribute_group_name
		where attribute_group_id = p_attribute_group_id and object_type_id = p_object_type_id;
end;
$$ LANGUAGE 'plpgsql';

-- 
-- Delete attribute group.
-- 
create or replace function sp_attribute_group_delete(in p_attribute_group_id int, in p_object_type_id int)
RETURNS void AS $$
begin	
	update attribute set attribute_group_id = null where attribute_group_id = p_attribute_group_id and object_type_id = p_object_type_id;
	delete from attribute_group where attribute_group_id = p_attribute_group_id and object_type_id = p_object_type_id;
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for adding attribute.
-- 
create or replace function sp_attribute_add(out o_attribute_id int, in p_object_type_id int, in p_attribute_name varchar(50),
    in p_description varchar(255), in p_attribute_type int, in p_attribute_option text, in p_attribute_convert_url int,
    in p_attribute_url text, in p_default_attribute_field_id int, in p_attribute_group_id int, in p_input_mask varchar(50))
RETURNS integer AS $$
begin
	select nextval('seq_attribute_id') into o_attribute_id;

	insert into attribute (attribute_id, object_type_id, attribute_key, description, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_editable, default_attribute_field_id, attribute_group_id, input_mask, is_custom_attr)
		values (o_attribute_id, p_object_type_id, p_attribute_name, p_description, p_attribute_url, p_attribute_type, p_attribute_option, p_attribute_convert_url, 1, p_default_attribute_field_id, p_attribute_group_id, p_input_mask, 1);
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for editing attribute.
-- 
create or replace function sp_system_attribute_update(in p_attribute_id int, in p_is_required int, in p_default_attribute_field_id int)
RETURNS void AS $$
begin 
	update attribute set default_attribute_field_id=p_default_attribute_field_id
		where attribute_id = p_attribute_id and default_attribute_field_id is not null;

	update attribute set is_required = p_is_required
		where attribute_id = p_attribute_id and is_required != -1;
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for editing custom attribute.
-- 
create or replace function sp_attribute_update(in p_attribute_id int, in p_attribute_name varchar(50), in p_description varchar(255),
    in p_attribute_type int, in p_attribute_option text, in p_attribute_convert_url int, in p_attribute_url text, in p_attribute_group_id int,
    in p_input_mask varchar(50))
RETURNS void AS $$
begin
	update attribute set attribute_key = p_attribute_name,
	    description = p_description,
	    attribute_type=p_attribute_type, attribute_url=p_attribute_url,
		attribute_convert_url=p_attribute_convert_url,
		attribute_option=p_attribute_option,
		attribute_group_id=p_attribute_group_id,
		input_mask = p_input_mask
		where attribute_id = p_attribute_id and is_custom_attr = 1;
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for deleting attribute.
-- 
create or replace function sp_attribute_delete(in p_attribute_id int)
RETURNS void AS $$
begin
	delete from object_attribute_value where attribute_id = p_attribute_id and p_attribute_id 
		= (select attribute_id from attribute where attribute_id = p_attribute_id and is_custom_attr = 1);
	
	delete from attribute_field_map where linked_attribute_id = p_attribute_id;

	delete from attribute where attribute_id = p_attribute_id and is_custom_attr = 1;
end;
$$ LANGUAGE 'plpgsql';

--
-- Update attribute default field
--
create or replace function sp_attribute_default_field_update(in p_attribute_id int, in p_default_attribute_field_id int)
RETURNS void AS $$
begin
	update attribute set default_attribute_field_id=p_default_attribute_field_id 
		where attribute_id = p_attribute_id;
end; 
$$ LANGUAGE 'plpgsql';

-- 
-- This is for adding attribute field.
-- 
create or replace function sp_attribute_field_add(out o_attribute_field_id int, in p_attribute_id int, in p_attribute_field_name varchar(50), in p_attribute_field_description text, in p_is_disabled int, in p_icon_id int)
RETURNS integer AS $$
declare v_attr_icon_count integer;
begin
	select nextval('seq_attribute_field_id') into o_attribute_field_id;

	insert into attribute_field (attribute_field_id, attribute_field_name, attribute_id, attribute_field_description, is_disabled)
		values (o_attribute_field_id, p_attribute_field_name, p_attribute_id, p_attribute_field_description, p_is_disabled);

	select count(*) into v_attr_icon_count from icon where attribute_id = p_attribute_id;

	if v_attr_icon_count > 0 then 
		update attribute_field set icon_id = p_icon_id where attribute_field_id = o_attribute_field_id;		
	end if;
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for updating attribute field.
-- 
create or replace function sp_attribute_field_update(in p_attribute_id int, in p_attribute_field_id int, in p_attribute_field_name varchar(50), in p_attribute_field_description text, in p_is_disabled int, in p_icon_id int)
RETURNS void AS $$
declare v_attr_icon_count integer;
begin
	update attribute_field set attribute_field_name = p_attribute_field_name, attribute_field_description = p_attribute_field_description, is_disabled = p_is_disabled where attribute_field_id = p_attribute_field_id;

	select count(*) into v_attr_icon_count from icon where attribute_id = p_attribute_id;

	if v_attr_icon_count > 0 then 
		update attribute_field set icon_id = p_icon_id where attribute_field_id = p_attribute_field_id;		
	end if;
end; 
$$ LANGUAGE 'plpgsql';

--
-- This is for updating custom attribute values.
--
create or replace function sp_attribute_value_update(in p_object_id int, in p_attribute_id int, in p_attr_value text)
RETURNS void AS $$
	declare attr_value_count integer;
begin
	select	count(p_attribute_id) into attr_value_count
	from	object_attribute_value
	where	attribute_id = p_attribute_id
	and		object_id = p_object_id;

	if p_attr_value is null then
		delete from object_attribute_value where object_id = p_object_id and attribute_id = p_attribute_id;
	elsif attr_value_count=0 then
		-- If count is 0, we'll do an insert, otherwise, do an update
		insert into object_attribute_value (attribute_id, attr_value, object_id) values (p_attribute_id, p_attr_value, p_object_id);
	else 
		update object_attribute_value set attr_value = p_attr_value where object_id = p_object_id and attribute_id = p_attribute_id;
	end if;
end;
$$ LANGUAGE 'plpgsql';

create or replace function sp_attribute_value_add(in p_object_id int, in p_attribute_id int, in p_attribute_field_id int, in p_attr_value text)
RETURNS void AS $$
begin
	insert into object_attribute_value (object_id, attribute_id, attribute_field_id, attr_value) values (p_object_id, p_attribute_id, p_attribute_field_id, p_attr_value);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for clearing attribute_field_map for a particular attribute type
--
create or replace function sp_attribute_field_map_delete(in p_attribute_id int, in p_attribute_field_id int, in p_linked_attribute_id int)
RETURNS void AS $$
begin
	if (p_linked_attribute_id is not null) then
		delete from attribute_field_map where attribute_id = p_attribute_id
			and linked_attribute_id = p_linked_attribute_id;

	else
		delete from attribute_field_map where attribute_id = p_attribute_id
			and attribute_field_id = p_attribute_field_id;
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating attribute_field_map for a attribute type field mapping
--
create or replace function sp_attribute_field_map_update(in p_attribute_id int, in p_attribute_field_id int, in p_linked_attribute_id int)
RETURNS void AS $$
	declare attr_value_count integer;
begin
	insert into attribute_field_map (attribute_field_id, attribute_id, linked_attribute_id)
		values (p_attribute_field_id, p_attribute_id, p_linked_attribute_id);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for inserting a new User.
--
create or replace function sp_user_add(out o_user_id int, in p_username varchar(50), in p_first_name varchar(50), in p_last_name varchar(50), in p_display_name varchar(50), in p_email varchar(255), in p_status int, in p_password varchar(32), in p_creator int)
RETURNS integer AS $$
begin 
	select nextval('seq_user_id') into o_user_id;

	-- Insert a record into access_user table.
	insert into access_user (user_id, username, first_name, last_name, display_name, email, status, password, creator, creation_date)
		values (o_user_id, p_username, p_first_name, p_last_name, p_display_name, p_email, p_status, p_password, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

-- 
-- This is for updating user contact.
--
create or replace function sp_user_contact_update(in p_user_id int, in p_contact_id int, in p_first_name varchar(50), in p_last_name varchar(50), in p_email varchar(255), in p_contact_title varchar(100), in p_company_id int, in p_company_contact_type int, in p_contact_phone_home varchar(50), in p_contact_phone_mobile varchar(50), in p_contact_phone_work varchar(50), in p_contact_fax varchar(50), in p_contact_email_secondary varchar(50), in p_messenger_1_type int, in p_messenger_1_id varchar(50), in p_messenger_2_type int, in p_messenger_2_id varchar(50), in p_contact_homepage_url varchar(50), in p_contact_description text, in p_address_street_primary varchar(50), in p_address_city_primary varchar(50), in p_address_state_primary varchar(50), in p_address_zipcode_primary varchar(50), in p_address_country_primary varchar(50), in p_creator int)
RETURNS void AS $$
	declare v_company_id integer;
begin

	if p_contact_id = 0 then 
		-- insert a record into contact table.
		insert into contact (contact_id, user_id, contact_first_name, contact_last_name, contact_title, company_id, company_contact_type, contact_phone_home, contact_phone_mobile, contact_phone_work, contact_fax, contact_email_primary, contact_email_secondary, messenger_1_type, messenger_1_id, messenger_2_type, messenger_2_id, contact_homepage_url, contact_description, address_street_primary, address_city_primary, address_state_primary, address_zipcode_primary, address_country_primary, creator, creation_date) 
			values (nextval('seq_contact_id'), p_user_id, p_first_name, p_last_name, p_contact_title, p_company_id, p_company_contact_type, p_contact_phone_home, p_contact_phone_mobile, p_contact_phone_work, p_contact_fax, p_email, p_contact_email_secondary, p_messenger_1_type, p_messenger_1_id, p_messenger_2_type, p_messenger_2_id, p_contact_homepage_url, p_contact_description, p_address_street_primary, p_address_city_primary, p_address_state_primary, p_address_zipcode_primary, p_address_country_primary, p_creator, now());
	else 
		-- Need to do the select into first.
		select company_id into v_company_id from contact where user_id = p_user_id;

		update contact set contact_first_name = p_first_name, contact_last_name = p_last_name, contact_title=p_contact_title, company_id=p_company_id, contact_phone_home=p_contact_phone_home, contact_phone_mobile = p_contact_phone_mobile, contact_phone_work = p_contact_phone_work,
			contact_fax=p_contact_fax, contact_email_primary = p_email, contact_email_secondary = p_contact_email_secondary, 
			messenger_1_type = p_messenger_1_type, messenger_1_id = p_messenger_1_id, messenger_2_type = p_messenger_2_type, messenger_2_id = p_messenger_2_id,
			contact_homepage_url = p_contact_homepage_url, contact_description = p_contact_description,
	        address_street_primary = p_address_street_primary, address_city_primary=p_address_city_primary, address_state_primary=p_address_state_primary, address_zipcode_primary=p_address_zipcode_primary, address_country_primary=p_address_country_primary,
			user_id = p_user_id, modifier=p_creator, modification_date=now()
	        where contact_id = p_contact_id;

		if v_company_id is not null then
			-- Update contact count for old company_id
			perform sp_company_count_contact_update(v_company_id, p_company_contact_type);
		end if;
	end if;

	-- Update contact count.
	perform sp_company_count_contact_update(p_company_id, p_company_contact_type);
end;
$$ LANGUAGE 'plpgsql';
	
--
-- This is for updating User password.
--
create or replace function sp_user_password_update(in p_password varchar(32), in p_user_id int)
RETURNS void AS $$
begin
	update access_user set password=p_password where user_id =p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating User access.
--
create or replace function sp_user_perm_map_update(in p_user_id int, in p_perm_id int, in p_cmd int)
RETURNS void AS $$
	declare map_count integer;
begin
    -- p_cmd: 1 means adding access, 2 means removing access.
	select	count(*) into map_count
	from	access_user_perm_map
	where	user_id = p_user_id
	and		perm_id = p_perm_id;

	-- We're going to grant access.
	if p_cmd=1 then
		if map_count=0 then
			insert into access_user_perm_map (user_id, perm_id) values (p_user_id, p_perm_id);
		end if;
	end if;

	if p_cmd=0 then
		if map_count!=0 then
			delete from access_user_perm_map where user_id = p_user_id and perm_id = p_perm_id;
		end if;
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating User detail.
--
create or replace function sp_user_update(in p_user_id int, in p_username varchar(50), in p_first_name varchar(50), in p_last_name varchar(50), in p_display_name varchar(50), in p_email varchar(255), in p_status int, in p_modifier int)
RETURNS void AS $$
begin

	update access_user set username = p_username, first_name = p_first_name, last_name = p_last_name, display_name=p_display_name, email=p_email, status=p_status, modifier=p_modifier, modification_date=now() 
		where user_id=p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a user.
--
create or replace function sp_user_delete(in p_object_type_id int, in p_user_id int, in p_archiver int)
RETURNS void AS $$
	declare v_is_default_user integer;
begin
	-- Don't let anyone delete system user.
	select is_default_user into v_is_default_user
	from access_user where user_id = p_user_id;
	
	if v_is_default_user= 1 then
		return;
	end if;

	-- Archive the current user into archive table
	insert into access_user_archive (user_archive_id, user_id, username, first_name, last_name, display_name, email, status, password, hardware_count, last_logon, last_visit, session_key, creator, creation_date, archiver, archive_date)
		(select nextval('seq_user_archive_id'), user_id, username, first_name, last_name, display_name, email, status, password, hardware_count, last_logon, last_visit, session_key, creator, creation_date, p_archiver, now() from access_user where user_id = p_user_id);

	-- Delete contact if it's not attached to any company
	delete from contact where company_id is null and user_id=p_user_id;

	-- Empty contact user_id
	update contact set user_id=null where user_id=p_user_id;

	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_user_id);

	-- Remove group/user mappings
	delete from access_group_user_map where user_id = p_user_id;

	-- Remove user/permission mappings
	delete from access_user_perm_map where user_id = p_user_id;

	-- Remove user/issue subscription mappings
	delete from issue_subscription where user_id = p_user_id;

	delete from access_user where user_id = p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for inserting a new group.
--
create or replace function sp_group_add(out o_group_id int, in p_group_name varchar(50), in p_group_description text, in p_creator int)
RETURNS integer AS $$
	declare v_company_id integer;
begin 
	select nextval('seq_group_id') into o_group_id;	

	-- Insert a record into access_user table.
	insert into access_group (group_id, group_name, group_description, creator, creation_date)
		values (o_group_id, p_group_name, p_group_description, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- Delete groups
--
create or replace function sp_group_delete(in p_group_id int)
RETURNS void AS $$
begin 
	delete from access_group_perm_map where group_id = p_group_id;
	delete from access_group_user_map where group_id = p_group_id;
	delete from access_group where group_id = p_group_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating group detail.
--
create or replace function sp_group_update(in p_group_id int, in p_group_name varchar(50), in p_group_description text, in p_modifier int)
RETURNS void AS $$
begin

	update access_group set group_name = p_group_name, group_description = p_group_description, modifier=p_modifier, modification_date=now() 
		where group_id = p_group_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating group access.
--
create or replace function sp_group_perm_map_update(in p_group_id int, in p_perm_id int, in p_cmd int)
RETURNS void AS $$
	declare map_count integer;
begin
    -- p_cmd: 1 means adding access, 2 means removing access.
	select	count(*) into map_count
	from	access_group_perm_map
	where	group_id = p_group_id
	and		perm_id = p_perm_id;

	-- We're going to grant access.
	if p_cmd=1 then
		if map_count=0 then
			insert into access_group_perm_map (group_id, perm_id) values (p_group_id, p_perm_id);
		end if;
	end if;

	if p_cmd=0 then
		if map_count!=0 then
			delete from access_group_perm_map where group_id = p_group_id and perm_id = p_perm_id;
		end if;
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating group members.
--
create or replace function sp_group_members_add(in p_group_id int, in p_user_id int, p_creator int)
RETURNS void AS $$
begin
	insert into access_group_user_map (group_id, user_id, creator, creation_date) 
		values (p_group_id, p_user_id, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

create or replace function sp_group_members_remove(in p_group_id int, in p_user_id int)
RETURNS void AS $$
begin
	delete from access_group_user_map where group_id=p_group_id and user_id=p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating application configuration.
--
create or replace function sp_system_config_update(in p_config_key varchar(50), in p_config_value varchar(200))
RETURNS void AS $$
begin
	update system_config set config_value=p_config_value where config_key=p_config_key;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Hardware.
--
create or replace function sp_hardware_add(out o_hardware_id int, in p_hardware_name varchar(100), in p_hardware_number varchar(100), in p_hardware_description text, in p_manufacturer_company_id int, in p_vendor_company_id int, in p_hardware_type int, in p_hardware_status int, in p_hardware_owner int, in p_hardware_location int, in p_hardware_model_name varchar(50), in p_hardware_model_number varchar(50), in p_hardware_serial_number varchar(50), in p_hardware_cost double precision, in p_hardware_reset_last_service_date int, in p_hardware_purchase_date varchar, in p_hardware_warranty_expire_date varchar, in p_creator int)
RETURNS integer AS $$
	declare v_hardware_last_service_date timestamp(1);
begin

	if p_hardware_reset_last_service_date=1 then
		v_hardware_last_service_date=now();
	else 
		v_hardware_last_service_date=null;
	end if;

	select nextval('seq_asset_hardware_id') into o_hardware_id;

	insert into asset_hardware(hardware_id, hardware_name, hardware_number, hardware_description, manufacturer_company_id, vendor_company_id, hardware_type, hardware_status, hardware_owner, hardware_location, hardware_model_name, hardware_model_number, hardware_serial_number, hardware_cost, hardware_last_service_date, hardware_purchase_date, hardware_warranty_expire_date, creator, creation_date)
		values (o_hardware_id, p_hardware_name, p_hardware_number, p_hardware_description, p_manufacturer_company_id, p_vendor_company_id, p_hardware_type, p_hardware_status, p_hardware_owner, p_hardware_location, p_hardware_model_name, p_hardware_model_number, p_hardware_serial_number, p_hardware_cost, v_hardware_last_service_date, to_timestamp(p_hardware_purchase_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_hardware_warranty_expire_date, 'YYYY-MM-DD HH24:MI:SS'), p_creator, now());

	perform sp_user_count_hardware_update(p_hardware_owner);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating an existing Hardware.
--
create or replace function sp_hardware_update(in p_hardware_id int, in p_hardware_name varchar(100), in p_hardware_number varchar(100), in p_hardware_description text, in p_manufacturer_company_id int, in p_vendor_company_id int, in p_hardware_type int, in p_hardware_status int, in p_hardware_owner int, in p_hardware_location int, in p_hardware_model_name varchar(50), in p_hardware_model_number varchar(50), in p_hardware_serial_number varchar(50), in p_hardware_cost double precision, in p_hardware_reset_last_service_date int, in p_hardware_purchase_date varchar, in p_hardware_warranty_expire_date varchar, in p_modifier int)
RETURNS void AS $$
	declare v_hardware_owner integer;
begin
	-- Need to keep track of original owner for resetting user hardware count
	select hardware_owner into v_hardware_owner from asset_hardware where hardware_id = p_hardware_id;

	update asset_hardware set hardware_name = p_hardware_name, hardware_number = p_hardware_number, 
		hardware_description = p_hardware_description, manufacturer_company_id = p_manufacturer_company_id, vendor_company_id = p_vendor_company_id, hardware_type = p_hardware_type, hardware_status = p_hardware_status,
		hardware_owner = p_hardware_owner, hardware_location = p_hardware_location, hardware_model_name = p_hardware_model_name, 
		hardware_model_number = p_hardware_model_number, hardware_serial_number = p_hardware_serial_number, hardware_cost = p_hardware_cost,
		hardware_purchase_date = to_timestamp(p_hardware_purchase_date, 'YYYY-MM-DD HH24:MI:SS'), hardware_warranty_expire_date = to_timestamp(p_hardware_warranty_expire_date, 'YYYY-MM-DD HH24:MI:SS'),
		modifier = p_modifier, modification_date = now()
        where hardware_id =p_hardware_id;

	-- We need to reset last service date.
	if p_hardware_reset_last_service_date=1 then
		update asset_hardware set hardware_last_service_date=now() where hardware_id =p_hardware_id;
	end if;

	-- Reset user hardware count, one for original owner, one for new owner
	perform sp_user_count_hardware_update(v_hardware_owner);
	perform sp_user_count_hardware_update(p_hardware_owner);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a hardware.
--
create or replace function sp_hardware_delete(in p_object_type_id int, in p_hardware_id int)
RETURNS void AS $$
	declare v_hardware_owner integer;
begin
	select hardware_owner into v_hardware_owner from asset_hardware where hardware_id = p_hardware_id;

	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_hardware_id);

	-- Delete all related objects
	perform sp_hardware_components_delete(p_hardware_id);
	
	delete from asset_map where hardware_id = p_hardware_id;

	-- Delete the hardware itself
	delete from asset_hardware where hardware_id = p_hardware_id;

	perform sp_user_count_hardware_update(v_hardware_owner);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for assigning a Software license to a Hardware.
--
create or replace function sp_software_license_assign(out o_map_id int, in p_hardware_id int, in p_software_id int, in p_license_id int, in p_license_entitlement int)
RETURNS integer AS $$
begin
	insert into asset_map(map_id, hardware_id, software_id, license_id, license_entitlement) 
		values (nextval('seq_asset_map_id'), p_hardware_id, p_software_id, p_license_id, p_license_entitlement);

	select currval('seq_asset_map_id') into o_map_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding a hardware component.
--
create or replace function sp_hardware_component_add(out o_comp_id int, p_hardware_id in int, in p_comp_description text, p_hardware_component_type int, in p_creator int)
RETURNS integer AS $$
begin
	select nextval('seq_asset_hardware_comp_id') into o_comp_id;

	insert into asset_hardware_component (comp_id, hardware_id, comp_description, hardware_component_type, creator, creation_date) 
		values (o_comp_id, p_hardware_id, p_comp_description, p_hardware_component_type, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating a hardware component.
--
create or replace function sp_hardware_component_update(in p_hardware_id int, in p_comp_id int, in p_comp_description text, p_hardware_component_type int, in p_modifier int)
RETURNS void AS $$
begin
	update	asset_hardware_component set comp_description = p_comp_description, hardware_component_type = p_hardware_component_type,
		modifier = p_modifier, modification_date = now()
        where	hardware_id =p_hardware_id
	and	comp_id = p_comp_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a hardware component.
--
create or replace function sp_hardware_component_delete(in p_comp_object_type_id int, in p_hardware_id int, in p_comp_id int)
RETURNS void AS $$
begin
	delete from asset_hardware_component where hardware_id = p_hardware_id and comp_id = p_comp_id;
	
	-- Delete object's custom field values. Make sure p_comp_object_type_id is the component object type, not hardware object type.
	delete from object_attribute_value where object_id = p_comp_id and attribute_id in 
		(select attribute_id from attribute where object_type_id = p_comp_object_type_id);
end;
$$ LANGUAGE 'plpgsql';

create or replace function sp_hardware_components_delete(in p_hardware_id int)
	RETURNS void AS 
$BODY$
	declare _record record;
	declare v_comp_id integer;
	declare v_comp_object_type_id integer;
begin
	select object_type_id into v_comp_object_type_id from object_type where object_key='hardware_component';

	FOR _record IN select comp_id from asset_hardware_component where hardware_id = p_hardware_id
	
	-- Delete components in the cursor
	LOOP 
		v_comp_id := _record.comp_id; 
		perform sp_hardware_component_delete(v_comp_object_type_id, p_hardware_id, v_comp_id);
	end loop;
end;
$BODY$
LANGUAGE 'plpgsql';

--
-- This is for resetting Hardware File count.
--
create or replace function sp_hardware_count_file_update(in p_object_type_id int, in p_hardware_id int)
RETURNS void AS $$
begin
	update asset_hardware set file_count = (select count(file_map_id) from file_map where object_type_id=p_object_type_id and object_id = p_hardware_id)
		where hardware_id = p_hardware_id;
end;
$$ LANGUAGE 'plpgsql';	

--
-- This is for resetting Hardware assigned Software count.
--
create or replace function sp_hardware_count_software_update(in p_hardware_id int)
RETURNS void AS $$
begin
	update asset_hardware set software_count = (select count(software_id) from asset_map where hardware_id = p_hardware_id)
		where hardware_id = p_hardware_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Hardware Component count.
--
create or replace function sp_hardware_count_component_update(in p_hardware_id int)
RETURNS void AS $$
begin
	update asset_hardware set component_count = (select count(comp_id) from asset_hardware_component where hardware_id = p_hardware_id)
		where hardware_id = p_hardware_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding software.
--
create or replace function sp_software_add(out o_software_id int, in p_software_name varchar(100), in p_software_description text, in p_software_owner int, 
	in p_software_type int, in p_operating_system int, in p_quoted_retail_price varchar(50), in p_quoted_oem_price varchar(50), in p_manufacturer_company_id int, 
	in p_vendor_company_id int, in p_software_version varchar(50), in p_software_expire_date varchar, in p_creator int)
RETURNS integer AS $$
begin
	select nextval('seq_asset_software_id') into o_software_id;

	insert into asset_software(software_id, software_name, software_description, software_owner, software_type, operating_system, quoted_retail_price, quoted_oem_price, manufacturer_company_id, vendor_company_id, software_version, software_expire_date, creator, creation_date) 
		values (o_software_id, p_software_name, p_software_description, p_software_owner, p_software_type, p_operating_system, p_quoted_retail_price, p_quoted_oem_price, p_manufacturer_company_id, p_vendor_company_id, p_software_version, to_timestamp(p_software_expire_date, 'YYYY-MM-DD HH24:MI:SS'), p_creator, now());
end; 
$$ LANGUAGE 'plpgsql';

--
-- This is for updating Software detail.
--
create or replace function sp_software_update(in p_software_id int, in p_software_name varchar(100), in p_software_description text, in p_software_owner int, in p_software_type int, in p_operating_system int, in p_quoted_retail_price varchar(50), in p_quoted_oem_price varchar(50), in p_manufacturer_company_id int, in p_vendor_company_id int, in p_software_version varchar(50), in p_software_expire_date varchar, in p_modifier int)
RETURNS void AS $$
begin
	update asset_software set software_name=p_software_name, software_description=p_software_description, software_owner=p_software_owner,
		software_type=p_software_type, operating_system=p_operating_system, quoted_retail_price=p_quoted_retail_price, 
		quoted_oem_price=p_quoted_oem_price, manufacturer_company_id=p_manufacturer_company_id, vendor_company_id=p_vendor_company_id,
		software_version = p_software_version, software_expire_date = to_timestamp(p_software_expire_date, 'YYYY-MM-DD HH24:MI:SS'),
		modifier=p_modifier, modification_date=now()
		where software_id = p_software_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting software.
--
create or replace function sp_software_delete(in p_object_type_id int, in p_software_id int)
RETURNS void AS $$
	declare license_record record;
	declare v_license_id integer;
	declare v_lic_object_type_id integer;
begin
	select object_type_id into v_lic_object_type_id from object_type where object_key='software_license';

	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_software_id);

	-- Loops through hardware to reset count.
	FOR license_record IN select distinct license_id from asset_map where software_id = p_software_id
	loop
		v_license_id := license_record.license_id; 
		perform sp_software_license_delete(v_lic_object_type_id, p_software_id, v_license_id);
	end loop;

    delete from asset_software where software_id = p_software_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding a Software license.
--
create or replace function sp_software_license_add(out o_license_id int, in p_software_id int, in p_license_key varchar(100), in p_license_note varchar(50), in p_license_entitlement int, in p_creator int)
RETURNS integer AS $$
begin
	insert into asset_software_licenses(license_id, software_id, license_key, license_note, license_entitlement, creator, creation_date) 
		values (nextval('seq_asset_software_license_id'), p_software_id, p_license_key, p_license_note, p_license_entitlement, p_creator, now());
	select currval('seq_asset_software_license_id') into o_license_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating Software license detail.
--
create or replace function sp_software_license_update(in p_software_id int, in p_license_id int, in p_license_key varchar(100), in p_license_note varchar(50), in p_license_entitlement int, in p_modifier int)
RETURNS void AS $$
begin
	update asset_software_licenses set license_key=p_license_key, license_note = p_license_note, license_entitlement=p_license_entitlement,
		modifier=p_modifier, modification_date=now()
		where software_id=p_software_id and license_id=p_license_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a license from a software.
--
create or replace function sp_software_license_delete(in p_lic_object_type_id int, in p_software_id int, in p_license_id int)
RETURNS void AS $$
	declare hardware_record record;
	declare v_hardware_id integer;
begin
	-- Loops through hardware to reset count.
	FOR hardware_record IN select distinct hardware_id from asset_map where software_id = p_software_id and license_id = p_license_id
	loop
		v_hardware_id := hardware_record.hardware_id; 
		delete from asset_map where hardware_id = v_hardware_id and software_id = p_software_id and license_id = p_license_id;
		perform sp_hardware_count_software_update(v_hardware_id);
	end loop;

	-- Delete object's custom field values. Make sure p_lic_object_type_id is the component object type, not hardware object type.
	delete from object_attribute_value where object_id = p_license_id and attribute_id in 
		(select attribute_id from attribute where object_type_id = p_lic_object_type_id);

	delete from asset_software_licenses where software_id = p_software_id and license_id = p_license_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Software License count.
--
create or replace function sp_software_count_license_update(in p_software_id int)
RETURNS void AS $$
begin
	update asset_software set license_count=(select count(license_id) from asset_software_licenses where software_id=p_software_id) where software_id=p_software_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Software Bookmark count.
--
create or replace function sp_software_count_bookmark_update(in p_object_type_id int, in p_software_id int)
RETURNS void AS $$
begin
	update asset_software set bookmark_count=(select count(bookmark_map_id) from bookmark_map where object_type_id=p_object_type_id and object_id = p_software_id)
		where software_id = p_software_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Software File count.
--
create or replace function sp_software_count_file_update(in p_object_type_id int, in p_software_id int)
RETURNS void AS $$
begin
	update asset_software set file_count=(select count(file_map_id) from file_map where object_type_id=p_object_type_id and object_id = p_software_id)
		where software_id = p_software_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting an association between hardware and software license.
--
create or replace function sp_asset_map_delete(in p_map_id int)
RETURNS void AS $$
begin
	delete from asset_map where map_id = p_map_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding contract.
--
create or replace function sp_contract_add(out o_contract_id int, in p_contract_name varchar(100), in p_contract_description text, in p_contract_type int, in p_contract_stage int, in p_contract_effective_date varchar, in p_contract_expiration_date varchar, in p_contract_renewal_date varchar, in p_contract_renewal_type int, in p_contract_provider_id int, in p_contract_owner int, in p_contract_provider_contact int, in p_creator int)
RETURNS integer AS $$
begin
	select nextval('seq_contract_id') into o_contract_id;

	insert into contract(contract_id, contract_name, contract_description, contract_type, contract_stage, contract_effective_date, contract_expiration_date, contract_renewal_date, contract_renewal_type, contract_provider, contract_owner, contract_provider_contact, creator, creation_date) 
		values (o_contract_id, p_contract_name, p_contract_description, p_contract_type, p_contract_stage, to_timestamp(p_contract_effective_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_contract_expiration_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_contract_renewal_date, 'YYYY-MM-DD HH24:MI:SS'), p_contract_renewal_type, p_contract_provider_id, p_contract_owner, p_contract_provider_contact, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating contract.
--
create or replace function sp_contract_update(in p_contract_id int, in p_contract_name varchar(100), in p_contract_description text, in p_contract_type int, in p_contract_stage int, in p_contract_effective_date varchar, in p_contract_expiration_date varchar, in p_contract_renewal_date varchar, in p_contract_renewal_type int, in p_contract_provider_id int, in p_contract_owner int, in p_contract_provider_contact int, in p_modifier int)
RETURNS void AS $$
begin
	update contract set contract_name=p_contract_name, contract_description=p_contract_description, contract_type=p_contract_type, 
		contract_stage=p_contract_stage, contract_effective_date=to_timestamp(p_contract_effective_date, 'YYYY-MM-DD HH24:MI:SS'),
		contract_expiration_date=to_timestamp(p_contract_expiration_date, 'YYYY-MM-DD HH24:MI:SS'), contract_renewal_date=to_timestamp(p_contract_renewal_date, 'YYYY-MM-DD HH24:MI:SS'), contract_renewal_type=p_contract_renewal_type, contract_provider=p_contract_provider_id, 
		contract_owner=p_contract_owner,
		contract_provider_contact=p_contract_provider_contact,
		modifier=p_modifier, modification_date=now()
		where contract_id=p_contract_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting contract.
--
create or replace function sp_contract_delete(in p_object_type_id int, in p_contract_id int)
RETURNS void AS $$
begin
	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_contract_id);

	delete from contract where contract_id = p_contract_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- Updates successful logon.
--
create or replace function sp_user_logon_valid_update(in p_session_key varchar(50), in p_user_id int)
RETURNS void AS $$
begin
	update access_user set session_key=p_session_key, last_logon=now(), last_visit=now()
		where user_id = p_user_id;

	perform sp_user_logon_invalid_reset(p_user_id);

	insert into system_event (user_id, login_date) values (p_user_id, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- Updates failed logon.
--
create or replace function sp_user_logon_invalid_update(in p_user_id int, in p_invalid_logon_count int)
RETURNS void AS $$
begin
	update access_user set invalid_logon_count=p_invalid_logon_count, invalid_logon_date=now()
		where user_id = p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- Updates invalid logon count.
--
create or replace function sp_user_logon_invalid_reset(in p_user_id int)
RETURNS void AS $$
begin
	update access_user set invalid_logon_count=0, invalid_logon_date=null
		where user_id = p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- Updates logout.
--
create or replace function sp_user_logout(in p_user_id int)
RETURNS void AS $$
begin
	update access_user set session_key =null, last_visit=now() 
		where user_id = p_user_id;
end;
$$ LANGUAGE 'plpgsql';


create or replace function sp_user_session_validate(out match_count int, in p_user_id int, in p_session_key varchar(50), in p_session_timeout int)
RETURNS integer AS $$
begin
	-- The purpose of validating is to see if the user_id and session_id 
	-- match our records. 
	select	count(user_id) into match_count
	from	access_user
	where	user_id = p_user_id
	and		session_key = p_session_key
	and     extract(epoch from (now() - last_visit)) < p_session_timeout;

	-- If the combination matches, we update the last_visit field.
    if match_count=1 then
	    update access_user set last_visit=now() where user_id = p_user_id;
    end if;
	
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting User's Hardware count.
--
create or replace function sp_user_count_hardware_update(in p_user_id int)
RETURNS void AS $$
begin
	if p_user_id!=0 then 
		update access_user set hardware_count = (select count(hardware_id) from asset_hardware where hardware_owner = access_user.user_id) where user_id=p_user_id;
	end if;
end;
$$ LANGUAGE 'plpgsql';	

--
-- This is for adding bookmark.
--
create or replace function sp_bookmark_add(out o_bookmark_id int, in p_bookmark_name varchar(100), in p_bookmark_path varchar(225), in p_bookmark_description text, in p_object_type_id int, in p_object_id int, in p_creator int)
RETURNS integer AS $$
begin
	select nextval('seq_bookmark_id') into o_bookmark_id;

	-- Insert an entry into bookmark table
	insert into bookmark(bookmark_id, bookmark_name, bookmark_path, bookmark_description, creator, creation_date) 
		values (o_bookmark_id, p_bookmark_name, p_bookmark_path, p_bookmark_description, p_creator, now());

	-- Insert the corresponding entry in bookmark_map table.
	insert into bookmark_map(bookmark_map_id, bookmark_id, object_type_id, object_id, creator, creation_date) 
		values (nextval('seq_bookmark_map_id'), o_bookmark_id, p_object_type_id, p_object_id, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating bookmark.
--
create or replace function sp_bookmark_update(in p_bookmark_id int, in p_bookmark_name varchar(100), in p_bookmark_path varchar(225), in p_bookmark_description text, in p_object_type_id int, in p_object_id int, in p_modifier int)
RETURNS void AS $$
begin
	update bookmark set bookmark_name=p_bookmark_name, bookmark_path=p_bookmark_path, bookmark_description=p_bookmark_description, 
		modifier=p_modifier, modification_date=now()
		where bookmark_id=(select bookmark_id from bookmark_map where object_type_id=p_object_type_id and object_id=p_object_id and bookmark_id=p_bookmark_id);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting bookmark.
--
create or replace function sp_bookmark_delete(in p_object_type_id int, in p_object_id int, in p_bookmark_id int)
RETURNS void AS $$
	declare v_bookmark_id integer;
begin

	select bookmark_id into v_bookmark_id from bookmark_map where object_type_id=p_object_type_id and object_id=p_object_id and bookmark_id=p_bookmark_id;
	delete from bookmark_map where bookmark_id=v_bookmark_id;
	delete from bookmark where bookmark_id=v_bookmark_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding company.
--
create or replace function sp_company_add(out o_company_id int, in p_company_name varchar(100), in p_company_description text, in p_creator int)
RETURNS integer AS $$
begin
    select nextval('seq_company_id') into o_company_id;

	insert into company (company_id, company_name, company_description, creator, creation_date) 
		values (o_company_id, p_company_name, p_company_description, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding a company_note record.
--
create or replace function sp_company_note_add(out o_note_id int, in p_note_name varchar(100), in p_note_description text, in p_note_type int, in p_company_id int, in p_creator int)
RETURNS integer AS $$
begin
	insert into company_note(note_id, note_name, note_description, note_type, company_id, creator, creation_date) 
		values (nextval('seq_company_note_id'), p_note_name, p_note_description, p_note_type, p_company_id, p_creator, now());
	select currval('seq_company_note_id') into o_note_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding a company_tag record.
--
create or replace function sp_company_tag_add(out o_tag_id int, in p_company_id int, in p_tag_name varchar(50), in p_creator int)
RETURNS integer AS $$
declare v_tag_count int;
begin
	o_tag_id = 0;
	
	-- Given a tag_name, company_id, insert a record into company_tag table if not exist.
	select count(tag_id) into v_tag_count
	from company_tag
	where lower(tag_name) = lower(p_tag_name);
	
	if (v_tag_count = 0) then
		insert into company_tag (tag_id, tag_name, creator, creation_date) 
			values (nextval('seq_company_tag_id'), p_tag_name, p_creator, now());
			
		select currval('seq_company_tag_id') into o_tag_id;
	end if;	
	
	if o_tag_id = 0 then
		-- This means the tag already exists, we'll do a select tag_id.
		select tag_id into o_tag_id from company_tag where tag_name=p_tag_name;
    end if;

	select count(company_id) into v_tag_count
	from company_tag_map
	where company_id = p_company_id
	and tag_id = o_tag_id;
	
	-- Now, we have a o_tag_id we can use.
	if (v_tag_count = 0) then
		insert into company_tag_map (company_id, tag_id, creator, creation_date) values 
			(p_company_id, o_tag_id, p_creator, now());
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for removing a company_tag_map record.
--
create or replace function sp_company_tag_delete(in p_company_id int, in p_tag_name varchar(50))
RETURNS void AS $$
begin
	delete from company_tag_map where company_id = p_company_id 
		and tag_id = (select tag_id from company_tag where tag_name = p_tag_name);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating Company detail.
--
create or replace function sp_company_update(in p_company_id int, in p_company_name varchar(100), in p_company_description text, in p_modifier int)
RETURNS void AS $$
begin
	update company set company_name=p_company_name, company_description=p_company_description, 
		modifier=p_modifier, modification_date=now()
		where company_id=p_company_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Company contact count.
--
create or replace function sp_company_count_contact_update(in p_company_id int, in p_company_contact_type int)
RETURNS void AS $$
begin
	if p_company_contact_type = '10' then 
		update company set main_contact_count=(select count(contact_id) from contact where company_id=p_company_id and company_contact_type=p_company_contact_type)
		where company_id=p_company_id;
	else 
		update company set employee_contact_count=(select count(contact_id) from contact where company_id = p_company_id and company_contact_type=p_company_contact_type)
		where company_id=p_company_id;	
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Company file count.
--
create or replace function sp_company_count_file_update(in p_object_type_id int, in p_company_id int)
RETURNS void AS $$
begin
	update company set file_count = (select count(file_map_id) from file_map where object_type_id = p_object_type_id and object_id = p_company_id)
		where company_id = p_company_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Company Bookmark count.
--
create or replace function sp_company_count_bookmark_update(in p_object_type_id int, in p_company_id int)
RETURNS void AS $$
begin
	update company set bookmark_count =(select count(bookmark_map_id) from bookmark_map where object_type_id = p_object_type_id and object_id = p_company_id)
	where company_id = p_company_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for resetting Company Note count.
--
create or replace function sp_company_count_note_update(in p_company_id int)
RETURNS void AS $$
begin
	update company set note_count =(select count(note_id) from company_note where company_id = p_company_id) 
	where company_id = p_company_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Contact.
--
create or replace function sp_contact_add(out o_contact_id int, in p_company_contact_type int, in p_contact_first_name varchar(50), in p_contact_last_name varchar(50), in p_contact_title varchar(100), in p_company_id int, in p_contact_phone_home varchar(50), in p_contact_phone_mobile varchar(50), in p_contact_phone_work varchar(50), in p_contact_fax varchar(50), in p_contact_email_primary varchar(50), in p_contact_email_secondary varchar(50), in p_messenger_1_type int, in p_messenger_1_id varchar(50), in p_messenger_2_type int, in p_messenger_2_id varchar(50), in p_contact_homepage_url varchar(50), in p_contact_description text, in p_address_street_primary varchar(50), in p_address_city_primary varchar(50), in p_address_state_primary varchar(50), in p_address_zipcode_primary varchar(50), in p_address_country_primary varchar(50), in p_creator int)
RETURNS integer AS $$
begin
    insert into contact (contact_id, contact_first_name, contact_last_name, contact_title, company_id, company_contact_type, contact_phone_home, contact_phone_mobile, contact_phone_work, contact_fax, contact_email_primary, contact_email_secondary, messenger_1_type, messenger_1_id, messenger_2_type, messenger_2_id, contact_homepage_url, contact_description, address_street_primary, address_city_primary, address_state_primary, address_zipcode_primary, address_country_primary, creator, creation_date) 
		values (nextval('seq_contact_id'), p_contact_first_name, p_contact_last_name, p_contact_title, p_company_id, p_company_contact_type, p_contact_phone_home, p_contact_phone_mobile, p_contact_phone_work, p_contact_fax, p_contact_email_primary, p_contact_email_secondary, p_messenger_1_type, p_messenger_1_id, p_messenger_2_type, p_messenger_2_id, p_contact_homepage_url, p_contact_description, p_address_street_primary, p_address_city_primary, p_address_state_primary, p_address_zipcode_primary, p_address_country_primary, p_creator, now());

	select currval('seq_contact_id') into o_contact_id;

	-- Update contact count.
	perform sp_company_count_contact_update(p_company_id, p_company_contact_type);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating a Contact.
--
create or replace function sp_contact_update(in p_contact_id int, in p_company_id int, in p_company_contact_type int, in p_contact_first_name varchar(50), in p_contact_last_name varchar(50), in p_contact_title varchar(100), in p_contact_phone_home varchar(50), in p_contact_phone_mobile varchar(50), in p_contact_phone_work varchar(50), in p_contact_fax varchar(50), in p_contact_email_primary varchar(50), in p_contact_email_secondary varchar(50), in p_messenger_1_type int, in p_messenger_1_id varchar(50), in p_messenger_2_type int, in p_messenger_2_id varchar(50), in p_contact_homepage_url varchar(50), in p_contact_description text, in p_address_street_primary varchar(50), in p_address_city_primary varchar(50), in p_address_state_primary varchar(50), in p_address_zipcode_primary varchar(50), in p_address_country_primary varchar(50), in p_modifier int)
RETURNS void AS $$
	declare v_company_id integer;
begin

	-- Need to do the select into first.
	select company_id into v_company_id from contact where contact_id = p_contact_id;

	update contact set contact_first_name=p_contact_first_name, contact_last_name=p_contact_last_name, contact_title=p_contact_title, company_id=p_company_id, contact_phone_home=p_contact_phone_home, contact_phone_mobile=p_contact_phone_mobile, contact_phone_work=p_contact_phone_work,
		contact_fax=p_contact_fax, contact_email_primary=p_contact_email_primary, contact_email_secondary=p_contact_email_secondary, 
		messenger_1_type = p_messenger_1_type, messenger_1_id = p_messenger_1_id, messenger_2_type = p_messenger_2_type, messenger_2_id = p_messenger_2_id, contact_homepage_url = p_contact_homepage_url, contact_description=p_contact_description,
        address_street_primary=p_address_street_primary, address_city_primary=p_address_city_primary, address_state_primary=p_address_state_primary, address_zipcode_primary=p_address_zipcode_primary, address_country_primary=p_address_country_primary,
		modifier=p_modifier, modification_date=now()
        where contact_id=p_contact_id
		and user_id is null;

	if v_company_id is not null then
		-- Update contact count for old company_id
		perform sp_company_count_contact_update(v_company_id, p_company_contact_type);
	end if;

	-- Update contact count for new company_id
	perform sp_company_count_contact_update(p_company_id, p_company_contact_type);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a Contact.
--
create or replace function sp_contact_delete(in p_object_type_id int, in p_contact_id int, in p_company_id int, in p_company_contact_type int)
RETURNS void AS $$
begin
	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_contact_id);

    delete from contact where contact_id = p_contact_id;
	
	-- Update contact count.
	perform sp_company_count_contact_update(p_company_id, p_company_contact_type);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting company.
--
create or replace function sp_company_delete(in p_company_object_type_id int, in p_contact_object_type_id int, in p_company_id int)
RETURNS void AS $$
begin
	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_company_object_type_id, p_company_id);
	
	-- Update company_id to null or foreign key constraint would fail.
	update contact set company_id = null where company_id = p_company_id and user_id is not null;

	-- Delete all associated records for company contacts.
	delete from object_map where object_type_id = p_contact_object_type_id and object_id in (select contact_id from contact where company_id=p_company_id);
	delete from object_map where linked_object_type_id = p_contact_object_type_id and linked_object_id in (select contact_id from contact where company_id=p_company_id);

	delete from contact where company_id = p_company_id;

	delete from company_tag_map where company_id = p_company_id;
	delete from company_note where company_id = p_company_id;

	delete from company where company_id = p_company_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding file.
--
create or replace function sp_file_add(out o_file_id int, in p_file_name varchar(100), in p_creator int)
RETURNS integer AS $$
begin
	-- Insert an entry into file table
	insert into file(file_id, file_name, creator, creation_date) 
		values (nextval('seq_file_id'), p_file_name, p_creator, now());

	select currval('seq_file_id') into o_file_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating file.
--
create or replace function sp_file_add_update(in p_file_id int, in p_file_name varchar(100), in p_file_friendly_name varchar(100), in p_file_description text, in p_file_mime_type varchar(100), in p_file_byte_size double precision, in p_file_physical_name varchar(100), in p_object_type_id int, in p_object_id int, p_creator int)
RETURNS void AS $$
begin
	update file set file_name=p_file_name, file_friendly_name=p_file_friendly_name, file_description=p_file_description, file_mime_type=p_file_mime_type, file_byte_size=p_file_byte_size, file_physical_name=p_file_physical_name
		where file_id=p_file_id;

	-- Insert the corresponding entry in file_map table.
	insert into file_map(file_map_id, file_id, object_type_id, object_id, creator, creation_date) 
		values (nextval('seq_file_map_id'), p_file_id, p_object_type_id, p_object_id, p_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting files associated with an object
--
create or replace function sp_files_delete(in p_object_type_id int, in p_object_id int)
RETURNS void AS $$
	declare _record record;
	declare v_file_id integer;
begin
	FOR _record IN select distinct file_id from file_map where object_type_id = p_object_type_id and object_id = p_object_id

	-- Delete files in the File cursor.
	LOOP 
		v_file_id := _record.file_id; 

		delete from file_map where file_id = v_file_id;
		delete from file where file_id = v_file_id;
	end loop;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a File.
--
create or replace function sp_file_delete(in p_object_type_id int, in p_object_id int, in p_file_id int)
RETURNS void AS $$
	declare v_file_id integer;
begin
	select file_id into v_file_id from file_map where object_type_id = p_object_type_id and object_id = p_object_id and file_id = p_file_id;
	delete from file_map where file_id = v_file_id;
	delete from file where file_id = v_file_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a dummy File, probably created during failed file upload.
--
create or replace function sp_file_delete_dummy(in p_file_id int)
RETURNS void AS $$
begin
    delete from file where file_id = p_file_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding a new Issue.
-- 
create or replace function sp_issue_add(out o_issue_id int, in p_issue_name varchar(100), in p_issue_description text, in p_url text, in p_type int, in p_status int, in p_priority int, in p_resolution int, in p_assignee int, in p_issue_due_date varchar, in p_creator_ip varchar(15), in p_issue_created_from_email varchar(225), in p_creator int, in p_proxy_creator int)
RETURNS integer AS $$
begin
	select nextval('seq_issue_id') into o_issue_id;

	insert into issue (issue_id, issue_name, issue_description, issue_url, issue_type, issue_status, issue_priority, issue_resolution, issue_assignee, issue_due_date, creator_ip, issue_created_from_email, creator, proxy_creator, creation_date)
		values (o_issue_id, p_issue_name, p_issue_description, p_url, p_type, p_status, p_priority, p_resolution, p_assignee, to_timestamp(p_issue_due_date, 'YYYY-MM-DD HH24:MI:SS'), p_creator_ip, p_issue_created_from_email, p_creator, p_proxy_creator, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating an Issue.
--
CREATE OR REPLACE FUNCTION sp_issue_update(p_issue_id integer, p_issue_name character varying, p_issue_description text, p_issue_type integer, p_issue_status integer, p_issue_priority integer, p_issue_resolution integer, p_issue_assignee integer, p_issue_due_date character varying, p_issue_from_email character varying, p_user_id integer)
  RETURNS void AS
$$
	declare v_now timestamp(1);
	declare v_issue_name character varying;
	declare v_issue_type integer;
    declare v_issue_status integer;
    declare v_issue_priority integer;
    declare v_issue_resolution integer;
    declare v_issue_assignee integer;
begin
	select now() into v_now;

	-- Query the existing Issue for comparison.
	select	issue_name, issue_type, issue_status, issue_priority, issue_resolution, issue_assignee 
	into	v_issue_name, v_issue_type, v_issue_status, v_issue_priority, v_issue_resolution, v_issue_assignee 
	from	issue
	where	issue_id = p_issue_id;

	-- Update the corresponding Issue.
	update issue set issue_name=p_issue_name, issue_type = p_issue_type, issue_status = p_issue_status, issue_priority = p_issue_priority,
		issue_resolution = p_issue_resolution, issue_assignee = p_issue_assignee, issue_due_date = to_timestamp(p_issue_due_date, 'YYYY-MM-DD HH24:MI:SS'), 
		modifier = p_user_id, modification_date= v_now, issue_modified_from_email = p_issue_from_email
		where issue_id = p_issue_id;
	
	-- Always insert a new record for issue_description.
	insert into issue_comment (issue_comment_id, issue_id, issue_comment_description) values (nextval('seq_issue_comment_id'), p_issue_id, p_issue_description);
	insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_comment_id, issue_created_from_email, creator, creation_date) values 
		(nextval('seq_issue_change_id'), p_issue_id, 'comment', currval('seq_issue_comment_id'), p_issue_from_email, p_user_id, v_now);

	-- Check changes in issue_name.
	if v_issue_name != p_issue_name then
		insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_change_varchar_old, issue_change_varchar_new, issue_created_from_email, creator, creation_date) values 
			(nextval('seq_issue_change_id'), p_issue_id, 'subject', v_issue_name, p_issue_name, p_issue_from_email, p_user_id, v_now);
	end if;

	-- Check changes in issue_type.
	if v_issue_type != p_issue_type then
		insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_change_int_old, issue_change_int_new, issue_created_from_email, creator, creation_date) values 
			(nextval('seq_issue_change_id'), p_issue_id, 'type', v_issue_type, p_issue_type, p_issue_from_email, p_user_id, v_now);
	end if;

	-- Check changes in issue_status.
	if v_issue_status != p_issue_status then
		insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_change_int_old, issue_change_int_new, issue_created_from_email, creator, creation_date) values 
			(nextval('seq_issue_change_id'), p_issue_id, 'status', v_issue_status, p_issue_status, p_issue_from_email, p_user_id, v_now);
	end if;

	-- Check changes in issue_priority.
	if v_issue_priority != p_issue_priority then
		insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_change_int_old, issue_change_int_new, issue_created_from_email, creator, creation_date) values 
			(nextval('seq_issue_change_id'), p_issue_id, 'priority', v_issue_priority, p_issue_priority, p_issue_from_email, p_user_id, v_now);
	end if;

	-- Check changes in issue_resolution.
	if (v_issue_resolution is null and p_issue_resolution is not null)
		or (v_issue_resolution is not null and p_issue_resolution is null)
		or (v_issue_resolution != p_issue_resolution) then
		insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_change_int_old, issue_change_int_new, issue_created_from_email, creator, creation_date) values 
			(nextval('seq_issue_change_id'), p_issue_id, 'resolution', v_issue_resolution, p_issue_resolution, p_issue_from_email, p_user_id, v_now);
	end if;

	-- Check changes in p_issue_assignee.
	if (v_issue_assignee is null and p_issue_assignee is not null) 
		or (v_issue_assignee is not null and p_issue_assignee is null)
		or (v_issue_assignee != p_issue_assignee) then
		insert into issue_change (issue_change_id, issue_id, issue_change_field, issue_change_int_old, issue_change_int_new, issue_created_from_email, creator, creation_date) values 
			(nextval('seq_issue_change_id'), p_issue_id, 'assignee', v_issue_assignee, p_issue_assignee, p_issue_from_email, p_user_id, v_now);
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting issue.
--
create or replace function sp_issue_delete(in p_object_type_id int, in p_issue_id int)
RETURNS void AS $$
begin
	delete from issue_change where issue_id = p_issue_id;

	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_issue_id);
	
	delete from issue_subscription where issue_id = p_issue_id;
	delete from issue_comment where issue_id = p_issue_id;
	delete from issue where issue_id = p_issue_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- Add issue subscriber.
--
create or replace function sp_issue_subscriber_add(in p_issue_id int, in p_user_id int, p_modifier int)
RETURNS void AS $$
begin
	insert into issue_subscription (issue_id, user_id, modifier, modification_date) 
		values (p_issue_id, p_user_id, p_modifier, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- Delete issue subscriber.
--
create or replace function sp_issue_subscriber_delete(in p_issue_id int, in p_user_id int)
RETURNS void AS $$
begin
	delete from issue_subscription where issue_id=p_issue_id and user_id=p_user_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Issue File.
--
create or replace function sp_issue_file_add(in p_issue_id int, in p_file_id int, in p_user_id int)
RETURNS void AS $$
begin	
	insert into issue_change (issue_change_id, issue_id, issue_change_field, file_id, creator, creation_date) values 
		(nextval('seq_issue_change_id'), p_issue_id, 'file', p_file_id, p_user_id, now());
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting all associated records for an object.
--
create or replace function sp_object_cleanup(in p_object_type_id int, in p_object_id int)
RETURNS void AS $$
	-- For deleting bookmarks
	declare bookmark_record record;
	declare v_bookmark_id integer;
begin	
	-- Delete file and file_map records
	perform sp_files_delete(p_object_type_id, p_object_id);

	-- Delete bookmarks
	FOR bookmark_record IN select distinct bookmark_id from bookmark_map where object_type_id = p_object_type_id and object_id = p_object_id
	LOOP 	
		v_bookmark_id := bookmark_record.bookmark_id; 
		delete from bookmark_map where bookmark_id = v_bookmark_id;
		delete from bookmark where bookmark_id = v_bookmark_id;
	end loop;

	-- Delete object linking. For each object_id/object_type_id, we also delete linked_object_id/linked_object_type_id records.
	delete from object_map where object_id = p_object_id and object_type_id = p_object_type_id;
	delete from object_map where linked_object_id = p_object_id and linked_object_type_id = p_object_type_id;

	-- Delete object's custom field values. 
	delete from object_attribute_value where object_id = p_object_id and attribute_id in 
		(select attribute_id from attribute where object_type_id = p_object_type_id);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding a single object map.
--
create or replace function sp_object_map_add(in p_object_type_id int, in p_object_id int, in p_linked_object_type_id int, in p_linked_object_id int, in p_creator int,
	in p_relationship_name varchar(50))
RETURNS void AS $$
declare v_count int;
begin
	select count(object_id) into v_count
	from object_map
	where object_id = p_object_id
	and	object_type_id = p_object_type_id
	and linked_object_id = p_linked_object_id
	and linked_object_type_id = p_linked_object_type_id;
	
	if (v_count = 0) then
		insert into object_map (object_type_id, object_id, linked_object_type_id, linked_object_id, creator, relationship_name, creation_date) 
			values (p_object_type_id, p_object_id, p_linked_object_type_id, p_linked_object_id, p_creator, p_relationship_name, now());
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting a single object map.
--
create or replace function sp_object_map_delete(in p_object_type_id int, in p_object_id int, in p_linked_object_type_id int, in p_linked_object_id int)
RETURNS void AS $$
begin
	delete from object_map 	
	where object_id = p_object_id
	and	object_type_id = p_object_type_id
	and linked_object_id = p_linked_object_id
	and linked_object_type_id = p_linked_object_type_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Blog Post.
--
create or replace function sp_blog_post_add(out o_post_id int, in p_post_name varchar(100), in p_post_description text, in p_post_type int, in p_post_ip varchar(100), in p_post_allow_comment int, in p_category_id int, in p_creator int)
RETURNS integer AS $$
begin
	select nextval('seq_blog_post_id') into o_post_id;

	insert into blog_post (post_id, post_name, post_description, post_type, post_ip, post_allow_comment, category_id, creator, creation_date)
		values (o_post_id, p_post_name, p_post_description, p_post_type, p_post_ip, p_post_allow_comment, p_category_id, p_creator, now());

	update category set object_count = (select count(post_id) from blog_post where category_id = p_category_id)
		where category_id = p_category_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating Blog Post.
--
create or replace function sp_blog_post_update(in p_post_id int, in p_post_name varchar(100), in p_post_description text, in p_post_type int, in p_post_allow_comment int, in p_category_id int, in p_modifier int)
RETURNS void AS $$
	declare v_old_category_id integer;
begin
	select category_id into v_old_category_id from blog_post where post_id = post_id;	

	update blog_post set post_name = p_post_name, post_description = p_post_description, post_type = p_post_type, 
		post_allow_comment = p_post_allow_comment, category_id = p_category_id, modifier = p_modifier, modification_date = now()
	where post_id = p_post_id;

	update category set object_count = (select count(post_id) from blog_post where category_id = p_category_id)
		where category_id = p_category_id;

	if p_category_id != v_old_category_id then
		update category set object_count = (select count(post_id) from blog_post where category_id = v_old_category_id)
			where category_id = v_old_category_id;
	end if;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting Blog Post.
--
create or replace function sp_blog_post_delete(in p_post_id int, in p_category_id int)
RETURNS void AS $$
begin
	delete from blog_post_comment where post_id = p_post_id;
	delete from blog_post where post_id = p_post_id;

	update category set object_count = (select count(post_id) from blog_post where category_id = p_category_id)
		where category_id = p_category_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Blog Post Comment.
--
create or replace function sp_blog_post_comment_add(out o_comment_id int, in p_comment_description text, p_comment_ip varchar(100), p_post_id int, in p_creator int)
RETURNS integer AS $$
begin
	insert into blog_post_comment (comment_id, comment_description, comment_ip, post_id, creator, creation_date)
		values (nextval('seq_blog_post_comment_id'), p_comment_description, p_comment_ip, p_post_id, p_creator, now());

	update blog_post set comment_count = (select count(comment_id) from blog_post_comment where post_id = p_post_id) where post_id = p_post_id;
	
	select currval('seq_blog_post_comment_id') into o_comment_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding category.
--
create or replace function sp_category_add(out o_category_id int, in p_category_name varchar(100), in p_category_description text, in p_object_type_id int, in p_creator int)
RETURNS integer AS $$
begin
	insert into category (category_id, category_name, category_description, object_type_id, creator, creation_date)
		values (nextval('seq_category_id'), p_category_name, p_category_description, p_object_type_id, p_creator, now());
	select currval('seq_category_id') into o_category_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating category.
--
create or replace function sp_category_update(in p_category_id int, in p_category_name varchar(100), in p_category_description text, in p_object_type_id int, in p_modifier int)
RETURNS void AS $$
begin
	update category 
	set category_name = p_category_name, 
		category_description = p_category_description, 
		modifier = p_modifier,
		modification_date = now()
	where category_id = p_category_id
	and object_type_id = p_object_type_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Knowledge Base Article.
--
create or replace function sp_kb_article_add(out o_article_id int, in p_article_name varchar(100), in p_article_text text, in p_article_syntax_type int, in p_category_id int, in p_event_type_id int, in p_creator int)
RETURNS int AS $$
	declare v_now timestamp(1);
begin
	select now() into v_now;

	insert into kb_article(article_id, article_name, article_text, article_syntax_type, category_id, creator, creation_date)
		values (nextval('seq_kb_article_id'), p_article_name, p_article_text, p_article_syntax_type, p_category_id, p_creator, v_now);
	
	select currval('seq_kb_article_id') into o_article_id;

	update category set object_count = (select count(article_id) from kb_article where category_id = p_category_id)
		where category_id = p_category_id;

	-- Do some archiving here
	perform sp_kb_article_archive(o_article_id, p_event_type_id, p_creator, v_now);
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating Knowledge Base Article.
--
create or replace function sp_kb_article_update(in p_article_id int, in p_article_name varchar(100), in p_article_text text, in p_article_syntax_type int, in p_category_id int, in p_event_type_id int, in p_modifier int)
RETURNS void AS $$
	-- Existing category id
	declare v_old_category_id integer;
	declare v_now timestamp(1);
begin
	select now() into v_now;

	select category_id into v_old_category_id from kb_article where article_id = p_article_id;

	-- Update the article
	update kb_article set article_name=p_article_name, article_text=p_article_text, article_syntax_type=p_article_syntax_type, 
			category_id=p_category_id, modifier=p_modifier, modification_date=v_now
		where article_id = p_article_id;
	
	-- Do some archiving here, we want to archive new contents, not original contents
	perform sp_kb_article_archive(p_article_id, p_event_type_id, p_modifier, v_now);

	-- Update count for original category
	update category set object_count = (select count(article_id) from kb_article where category_id = v_old_category_id)
		where category_id = v_old_category_id;

	-- Update count for new category
	update category set object_count = (select count(article_id) from kb_article where category_id = p_category_id)
		where category_id = p_category_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting Knowledge Base Article.
--
create or replace function sp_kb_article_delete(in p_object_type_id int, in p_article_id int, in p_event_type_id int, in p_modifier int)
RETURNS void AS $$
	-- Existing category id
	declare v_old_category_id integer;
	declare v_now timestamp(1);
begin
	select now() into v_now;
	select category_id into v_old_category_id from kb_article where article_id = p_article_id;

	-- Delete all associated records for an object.
	perform sp_object_cleanup(p_object_type_id, p_article_id);

	-- Do some archiving here
	perform sp_kb_article_archive(p_article_id, p_event_type_id, p_modifier, v_now);

	delete from kb_article where article_id = p_article_id;

	-- Update count for category
	update category set object_count = (select count(article_id) from kb_article where category_id = v_old_category_id)
		where category_id = v_old_category_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for incrementing Knowledge Base Article view count.
--
create or replace function sp_kb_article_increment_view_count(in p_article_id int)
RETURNS void AS $$
begin
	update kb_article set view_count = view_count+1 where article_id = p_article_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for archiving Knowledge Base Article.
--
create or replace function sp_kb_article_archive(in p_article_id int, in p_event_type_id int, in p_creator int, p_now timestamp(1))
RETURNS void AS $$
begin
	-- Archive the current article version into archive table
	insert into kb_article_archive(article_history_id, article_id, article_name, article_text, category_id, view_count, article_syntax_type, article_wiki_namespace, event_type_id, creator, creation_date)
		select nextval('seq_kb_article_archive_id'), article_id, article_name, article_text, category_id, view_count, article_syntax_type, article_wiki_namespace, p_event_type_id, p_creator, p_now from kb_article where article_id = p_article_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding Site.
--
create or replace function sp_site_add(out o_site_id int, in p_site_name varchar(100), in p_site_path varchar(225), in p_site_description text, in p_site_placement int, in p_site_support_iframe int, in p_category_id int, in p_creator int)
RETURNS integer AS $$
begin
	insert into portal_site (site_id, site_name, site_path, site_description, site_placement, site_support_iframe, category_id, creator, creation_date)
		values (nextval('seq_portal_site_id'), p_site_name, p_site_path, p_site_description, p_site_placement, p_site_support_iframe, p_category_id, p_creator, now());
		
	select currval('seq_portal_site_id') into o_site_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating Site.
--
create or replace function sp_site_update(in p_site_id int, in p_site_name varchar(100), in p_site_path varchar(225), in p_site_description text, in p_site_placement int, in p_site_support_iframe int, in p_category_id int, in p_modifier int)
RETURNS void AS $$
begin
	update portal_site set site_name = p_site_name, site_path = p_site_path, site_description = p_site_description, site_placement = p_site_placement, site_support_iframe = p_site_support_iframe, category_id = p_category_id, modifier = p_modifier, modification_date=now()
	where site_id = p_site_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting Site.
--
create or replace function sp_site_delete(in p_site_id int)
RETURNS void AS $$
begin
	delete from portal_site where site_id = p_site_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding RSS feed.
--
create or replace function sp_rss_feed_add(out o_feed_id int, in p_feed_url text, in p_feed_name varchar(100), in p_feed_item_count int, in p_feed_cache text, in p_creator int)
RETURNS integer AS $$
begin
	insert into rss_feed (feed_id, feed_url, feed_name, feed_item_count, feed_cache, creator, creation_date)
		values (nextval('seq_rss_feed_id'), p_feed_url, p_feed_name, p_feed_item_count, p_feed_cache, p_creator, now());
	
	select currval('seq_rss_feed_id') into o_feed_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for updating RSS feed.
--
create or replace function sp_rss_feed_update(in p_feed_id int, in p_feed_url text, in p_feed_name varchar(100), in p_modifier int)
RETURNS void AS $$
begin
	update rss_feed set feed_url = p_feed_url, feed_name = p_feed_name, feed_item_count = 0, feed_cache = '', modifier = p_modifier, modification_date=now()
	where feed_id = p_feed_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for adding RSS feed cache (xml).
--
create or replace function sp_rss_feed_cache_update(in p_feed_id int, in p_feed_item_count int, in p_feed_cache text)
RETURNS void AS $$
begin
	update rss_feed set feed_item_count = p_feed_item_count, feed_cache = p_feed_cache, feed_cache_date=now()
	where feed_id = p_feed_id;
end;
$$ LANGUAGE 'plpgsql';

--
-- This is for deleting RSS feed.
--
create or replace function sp_rss_feed_delete(in p_feed_id int)
RETURNS void AS $$
begin
	delete from rss_feed where feed_id = p_feed_id;
end;
$$ LANGUAGE 'plpgsql';

--
--
create or replace function sp_system_cache_add(in p_cache_key varchar(100), in p_cache_time bigint)
RETURNS void AS $$
	declare match_count integer;
begin
	select	count(cache_key) into match_count
	from	system_cache
	where	cache_key = p_cache_key;

	if match_count=0 then
		insert into system_cache (cache_key, cache_time) values (p_cache_key, p_cache_time);
	else
		update system_cache set cache_time=p_cache_time where cache_key=p_cache_key;
	end if;	
end;
$$ LANGUAGE 'plpgsql';

--
--
create or replace function sp_system_cache_validate(in p_cache_time bigint)
RETURNS void AS $$
begin
	update system_cache set cache_time = p_cache_time where cache_time > p_cache_time;
end;
$$ LANGUAGE 'plpgsql';
