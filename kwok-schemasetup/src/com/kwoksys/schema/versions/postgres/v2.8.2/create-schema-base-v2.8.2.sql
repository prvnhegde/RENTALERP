--
-- PostgreSQL database dump
--
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1368 (class 1259 OID 80735)
-- Dependencies: 4
-- Name: access_group; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_group (
    group_id integer NOT NULL,
    group_name character varying(100) NOT NULL,
    group_description text,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone
);


--
-- TOC entry 1369 (class 1259 OID 80740)
-- Dependencies: 4
-- Name: access_group_perm_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_group_perm_map (
    group_id integer NOT NULL,
    perm_id integer NOT NULL
);


--
-- TOC entry 1370 (class 1259 OID 80742)
-- Dependencies: 4
-- Name: access_group_user_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_group_user_map (
    group_id integer NOT NULL,
    user_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone
);


--
-- TOC entry 1371 (class 1259 OID 80744)
-- Dependencies: 4
-- Name: access_page; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_page (
    page_id integer NOT NULL,
    page_name character varying(100) NOT NULL,
    module_id integer
);


--
-- TOC entry 1372 (class 1259 OID 80746)
-- Dependencies: 4
-- Name: access_perm_page_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_perm_page_map (
    perm_id integer NOT NULL,
    page_id integer NOT NULL
);


--
-- TOC entry 1373 (class 1259 OID 80748)
-- Dependencies: 1774 4
-- Name: access_permission; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_permission (
    perm_id integer NOT NULL,
    perm_name character varying(100) NOT NULL,
    perm_is_enabled integer,
    order_num integer DEFAULT 0
);


--
-- TOC entry 1374 (class 1259 OID 80751)
-- Dependencies: 1775 1776 1777 4
-- Name: access_user; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_user (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(32) NOT NULL,
    display_name character varying(50) NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(255),
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    status smallint DEFAULT (1)::smallint,
    hardware_count integer DEFAULT 0,
    is_default_user smallint DEFAULT 0,
    last_logon timestamp(1) without time zone,
    last_visit timestamp(1) without time zone,
    session_key character varying(50)
);


--
-- TOC entry 1440 (class 1259 OID 81336)
-- Dependencies: 1794 1795 4
-- Name: access_user_archive; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_user_archive (
    user_archive_id integer NOT NULL,
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(32) NOT NULL,
    display_name character varying(50) NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(255),
    creator integer,
    creation_date timestamp(1) without time zone,
    archiver integer,
    archive_date timestamp(1) without time zone,
    status smallint DEFAULT (1)::smallint,
    hardware_count integer DEFAULT 0,
    last_logon timestamp(1) without time zone,
    last_visit timestamp(1) without time zone,
    session_key character varying(50)
);


--
-- TOC entry 1375 (class 1259 OID 80761)
-- Dependencies: 4
-- Name: access_user_perm_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE access_user_perm_map (
    user_id integer NOT NULL,
    perm_id integer NOT NULL
);


--
-- TOC entry 1376 (class 1259 OID 80763)
-- Dependencies: 1778 1779 1780 4
-- Name: asset_hardware; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE asset_hardware (
    hardware_id integer NOT NULL,
    hardware_name character varying(100) NOT NULL,
    hardware_number character varying(100),
    hardware_description text,
    hardware_type integer,
    hardware_owner integer,
    hardware_serial_number character varying(50),
    hardware_model_name character varying(50),
    hardware_model_number character varying(50),
    hardware_last_service_date timestamp(1) without time zone,
    hardware_cost double precision,
    hardware_location integer,
    manufacturer_company_id integer,
    vendor_company_id integer,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    hardware_status integer,
    software_count integer DEFAULT 0,
    file_count integer DEFAULT 0,
    hardware_purchase_date timestamp(1) without time zone,
    hardware_warranty_expire_date timestamp(1) without time zone,
    component_count integer DEFAULT 0
);


--
-- TOC entry 1377 (class 1259 OID 80773)
-- Dependencies: 4
-- Name: asset_hardware_component; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE asset_hardware_component (
    comp_id integer NOT NULL,
    comp_description text,
    hardware_id integer NOT NULL,
    hardware_component_type integer,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone
);


--
-- TOC entry 1378 (class 1259 OID 80778)
-- Dependencies: 1781 4
-- Name: asset_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE asset_map (
    map_id integer NOT NULL,
    hardware_id integer NOT NULL,
    software_id integer NOT NULL,
    license_id integer NOT NULL,
    license_entitlement integer DEFAULT 1
);


--
-- TOC entry 1379 (class 1259 OID 80781)
-- Dependencies: 4
-- Name: asset_software; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE asset_software (
    software_id integer NOT NULL,
    software_name character varying(100) NOT NULL,
    software_description text,
    software_type integer,
    manufacturer_company_id integer,
    vendor_company_id integer,
    quoted_retail_price character varying(50),
    quoted_oem_price character varying(50),
    operating_system integer,
    license_count integer,
    bookmark_count integer,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    file_count integer,
    software_owner integer
);


--
-- TOC entry 1380 (class 1259 OID 80788)
-- Dependencies: 4
-- Name: asset_software_licenses; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE asset_software_licenses (
    license_id integer NOT NULL,
    software_id integer NOT NULL,
    license_key character varying(100) NOT NULL,
    license_is_oem character(1),
    license_entitlement integer,
    license_cost double precision,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    license_note text
);


--
-- TOC entry 1381 (class 1259 OID 80791)
-- Dependencies: 1782 1783 1784 4
-- Name: attribute; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE attribute (
    attribute_id integer NOT NULL,
    object_type_id integer NOT NULL,
    attribute_key character varying(50),
    is_editable smallint DEFAULT (1)::smallint,
    default_attribute_field_id integer,
    is_custom_attr smallint DEFAULT (1)::smallint,
    attribute_url text,
    attribute_type smallint,
    attribute_option text,
    attribute_convert_url smallint,
    is_required smallint DEFAULT (-1)::smallint
);


--
-- TOC entry 1382 (class 1259 OID 80799)
-- Dependencies: 4
-- Name: attribute_field; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE attribute_field (
    attribute_field_id integer NOT NULL,
    attribute_id integer NOT NULL,
    field_key character varying(50),
    attribute_field_description text,
    icon_id integer,
    attribute_field_name character varying(50),
    is_disabled smallint
);


--
-- TOC entry 1442 (class 1259 OID 81359)
-- Dependencies: 4
-- Name: attribute_type_field_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE attribute_type_field_map (
    attribute_field_id integer NOT NULL,
    attribute_id integer NOT NULL,
    linked_attribute_id integer NOT NULL
);


--
-- TOC entry 1385 (class 1259 OID 80812)
-- Dependencies: 1785 4
-- Name: blog_post; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE blog_post (
    post_id integer NOT NULL,
    post_name character varying(100) NOT NULL,
    post_description text,
    post_type integer,
    post_ip character varying(100),
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    post_allow_comment smallint DEFAULT (1)::smallint,
    comment_count integer,
    category_id integer NOT NULL
);


--
-- TOC entry 1386 (class 1259 OID 80818)
-- Dependencies: 4
-- Name: blog_post_comment; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE blog_post_comment (
    comment_id integer NOT NULL,
    comment_name character varying(100),
    comment_description text NOT NULL,
    comment_ip character varying(100),
    creator integer,
    creation_date timestamp(1) without time zone,
    post_id integer NOT NULL
);


--
-- TOC entry 1388 (class 1259 OID 80829)
-- Dependencies: 4
-- Name: bookmark; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE bookmark (
    bookmark_id integer NOT NULL,
    bookmark_name character varying(100) NOT NULL,
    bookmark_description text,
    bookmark_path character varying(225) NOT NULL,
    is_public smallint,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone
);


--
-- TOC entry 1389 (class 1259 OID 80834)
-- Dependencies: 4
-- Name: bookmark_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE bookmark_map (
    bookmark_map_id integer NOT NULL,
    bookmark_id integer NOT NULL,
    object_type_id integer NOT NULL,
    object_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone
);


--
-- TOC entry 1387 (class 1259 OID 80823)
-- Dependencies: 1786 4
-- Name: category; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE category (
    category_id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    category_description text,
    object_count integer DEFAULT 0,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    object_type_id integer NOT NULL
);


--
-- TOC entry 1390 (class 1259 OID 80836)
-- Dependencies: 1787 1788 1789 1790 1791 4
-- Name: company; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE company (
    company_id integer NOT NULL,
    company_name character varying(100) NOT NULL,
    company_description text,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    main_contact_count integer DEFAULT 0,
    employee_contact_count integer DEFAULT 0,
    bookmark_count integer DEFAULT 0,
    file_count integer DEFAULT 0,
    note_count integer DEFAULT 0
);


--
-- TOC entry 1391 (class 1259 OID 80846)
-- Dependencies: 4
-- Name: company_note; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE company_note (
    note_id integer NOT NULL,
    note_name character varying(100) NOT NULL,
    note_description text,
    note_type integer,
    company_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone
);


--
-- TOC entry 1392 (class 1259 OID 80851)
-- Dependencies: 4
-- Name: company_tag; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE company_tag (
    tag_id integer NOT NULL,
    tag_name character varying(50) NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone
);


--
-- TOC entry 1393 (class 1259 OID 80853)
-- Dependencies: 4
-- Name: company_tag_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE company_tag_map (
    company_id integer NOT NULL,
    tag_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone
);


--
-- TOC entry 1394 (class 1259 OID 80855)
-- Dependencies: 4
-- Name: contact; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contact (
    contact_id integer NOT NULL,
    company_id integer,
    company_contact_type integer,
    contact_first_name character varying(50),
    contact_last_name character varying(50),
    contact_description text,
    contact_title character varying(100),
    contact_phone_home character varying(50),
    contact_phone_mobile character varying(50),
    contact_phone_work character varying(50),
    contact_fax character varying(50),
    contact_email_primary character varying(50),
    contact_email_secondary character varying(50),
    contact_homepage_url character varying(256),
    address_street_primary character varying(50),
    address_city_primary character varying(50),
    address_state_primary character varying(50),
    address_zipcode_primary character varying(50),
    address_country_primary character varying(50),
    address_street_secondary character varying(50),
    address_city_secondary character varying(50),
    address_state_secondary character varying(50),
    address_zipcode_secondary character varying(50),
    address_country_secondary character varying(50),
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    user_id integer,
    messenger_1_type integer,
    messenger_1_id character varying(50),
    messenger_2_type integer,
    messenger_2_id character varying(50)
);


--
-- TOC entry 1395 (class 1259 OID 80880)
-- Dependencies: 4
-- Name: content_locale; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE content_locale (
    locale_id character varying(50) NOT NULL,
    language character varying(50),
    country character varying(50)
);


--
-- TOC entry 1396 (class 1259 OID 80884)
-- Dependencies: 4
-- Name: contract; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contract (
    contract_id integer NOT NULL,
    contract_name character varying(100) NOT NULL,
    contract_description text,
    contract_type integer,
    contract_effective_date timestamp(1) without time zone,
    contract_expiration_date timestamp(1) without time zone,
    contract_renewal_type integer,
    contract_renewal_date timestamp(1) without time zone,
    contract_provider integer,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    contract_provider_contact integer,
    contract_owner integer
);


--
-- TOC entry 1397 (class 1259 OID 80889)
-- Dependencies: 4
-- Name: file; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE file (
    file_id integer NOT NULL,
    file_name character varying(100) NOT NULL,
    file_friendly_name character varying(100),
    file_description text,
    file_mime_type character varying(100),
    file_byte_size bigint,
    file_physical_name character varying(100),
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone
);


--
-- TOC entry 1398 (class 1259 OID 80897)
-- Dependencies: 4
-- Name: file_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE file_map (
    file_map_id integer NOT NULL,
    file_id integer NOT NULL,
    object_type_id integer NOT NULL,
    object_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone
);


--
-- TOC entry 1399 (class 1259 OID 80899)
-- Dependencies: 4
-- Name: icon; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE icon (
    icon_id integer NOT NULL,
    icon_path character varying(225) NOT NULL,
    is_system_icon smallint,
    attribute_id integer NOT NULL
);


--
-- TOC entry 1400 (class 1259 OID 80901)
-- Dependencies: 4
-- Name: issue; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE issue (
    issue_id integer NOT NULL,
    issue_name character varying(120) NOT NULL,
    issue_description text,
    issue_assignee integer,
    issue_url text,
    issue_type integer,
    issue_status integer,
    issue_priority integer,
    issue_resolution integer,
    duplicate_id integer,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    issue_due_date timestamp(1) without time zone,
    creator_ip character varying(15),
    issue_created_from_email character varying(225),
    issue_modified_from_email character varying(225)
);


--
-- TOC entry 1401 (class 1259 OID 80906)
-- Dependencies: 4
-- Name: issue_change; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE issue_change (
    issue_change_id integer NOT NULL,
    issue_change_field character varying(20),
    issue_change_varchar_old character varying(120),
    issue_change_varchar_new character varying(120),
    issue_change_int_old integer,
    issue_change_int_new integer,
    issue_comment_id integer,
    file_id integer,
    issue_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone,
    issue_created_from_email character varying(225)
);


--
-- TOC entry 1402 (class 1259 OID 80908)
-- Dependencies: 4
-- Name: issue_comment; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE issue_comment (
    issue_comment_id integer NOT NULL,
    issue_comment_description text,
    issue_id integer NOT NULL
);


--
-- TOC entry 1403 (class 1259 OID 80913)
-- Dependencies: 4
-- Name: issue_subscription; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE issue_subscription (
    issue_id integer NOT NULL,
    user_id integer NOT NULL,
    modifier integer,
    modification_date timestamp(1) without time zone
);


--
-- TOC entry 1404 (class 1259 OID 80915)
-- Dependencies: 1792 4
-- Name: kb_article; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE kb_article (
    article_id bigint NOT NULL,
    article_name character varying(225) NOT NULL,
    article_text text,
    category_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    view_count bigint DEFAULT 0,
    article_wiki_namespace character varying(225),
    article_syntax_type smallint
);


--
-- TOC entry 1438 (class 1259 OID 81300)
-- Dependencies: 1793 4
-- Name: kb_article_archive; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE kb_article_archive (
    article_history_id bigint NOT NULL,
    article_id bigint NOT NULL,
    article_name character varying(225) NOT NULL,
    article_text text,
    category_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone,
    event_type_id integer,
    view_count bigint DEFAULT 0,
    article_syntax_type smallint,
    article_wiki_namespace character varying(225)
);


--
-- TOC entry 1383 (class 1259 OID 80805)
-- Dependencies: 4
-- Name: object_attribute_value; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE object_attribute_value (
    attribute_id integer NOT NULL,
    attr_value text,
    object_id integer NOT NULL,
    attribute_field_id integer
);


--
-- TOC entry 1405 (class 1259 OID 80925)
-- Dependencies: 4
-- Name: object_map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE object_map (
    object_id integer NOT NULL,
    object_type_id integer NOT NULL,
    linked_object_id integer NOT NULL,
    linked_object_type_id integer NOT NULL,
    creator integer,
    creation_date timestamp(1) without time zone,
    relationship_name character varying(50)
);


--
-- TOC entry 1384 (class 1259 OID 80810)
-- Dependencies: 4
-- Name: object_type; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE object_type (
    object_type_id integer NOT NULL,
    object_key character varying(50) NOT NULL
);


--
-- TOC entry 1406 (class 1259 OID 80927)
-- Dependencies: 4
-- Name: portal_site; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE portal_site (
    site_id integer NOT NULL,
    site_name character varying(100) NOT NULL,
    site_description text,
    site_path character varying(225) NOT NULL,
    site_placement smallint,
    site_support_iframe smallint,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    category_id integer NOT NULL
);


--
-- TOC entry 1407 (class 1259 OID 80932)
-- Dependencies: 4
-- Name: rss_feed; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rss_feed (
    feed_id integer NOT NULL,
    feed_url text,
    creator integer,
    creation_date timestamp(1) without time zone,
    modifier integer,
    modification_date timestamp(1) without time zone,
    feed_cache text,
    feed_cache_date timestamp(1) without time zone,
    feed_name character varying(100) NOT NULL,
    feed_item_count integer
);


--
-- TOC entry 1408 (class 1259 OID 80937)
-- Dependencies: 4
-- Name: seq_asset_hardware_comp_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_asset_hardware_comp_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1989 (class 0 OID 0)
-- Dependencies: 1408
-- Name: seq_asset_hardware_comp_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_asset_hardware_comp_id', 1, false);


--
-- TOC entry 1409 (class 1259 OID 80939)
-- Dependencies: 4
-- Name: seq_asset_hardware_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_asset_hardware_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1990 (class 0 OID 0)
-- Dependencies: 1409
-- Name: seq_asset_hardware_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_asset_hardware_id', 1, false);


--
-- TOC entry 1410 (class 1259 OID 80941)
-- Dependencies: 4
-- Name: seq_asset_map_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_asset_map_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1991 (class 0 OID 0)
-- Dependencies: 1410
-- Name: seq_asset_map_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_asset_map_id', 1, false);


--
-- TOC entry 1411 (class 1259 OID 80943)
-- Dependencies: 4
-- Name: seq_asset_software_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_asset_software_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1992 (class 0 OID 0)
-- Dependencies: 1411
-- Name: seq_asset_software_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_asset_software_id', 1, false);


--
-- TOC entry 1412 (class 1259 OID 80945)
-- Dependencies: 4
-- Name: seq_asset_software_license_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_asset_software_license_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1993 (class 0 OID 0)
-- Dependencies: 1412
-- Name: seq_asset_software_license_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_asset_software_license_id', 1, false);


--
-- TOC entry 1413 (class 1259 OID 80947)
-- Dependencies: 4
-- Name: seq_attribute_field_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_attribute_field_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1994 (class 0 OID 0)
-- Dependencies: 1413
-- Name: seq_attribute_field_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_attribute_field_id', 72, true);


--
-- TOC entry 1414 (class 1259 OID 80949)
-- Dependencies: 4
-- Name: seq_attribute_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_attribute_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1995 (class 0 OID 0)
-- Dependencies: 1414
-- Name: seq_attribute_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_attribute_id', 1, false);


--
-- TOC entry 1415 (class 1259 OID 80951)
-- Dependencies: 4
-- Name: seq_blog_post_comment_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_blog_post_comment_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1996 (class 0 OID 0)
-- Dependencies: 1415
-- Name: seq_blog_post_comment_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_blog_post_comment_id', 1, false);


--
-- TOC entry 1416 (class 1259 OID 80953)
-- Dependencies: 4
-- Name: seq_blog_post_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_blog_post_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1997 (class 0 OID 0)
-- Dependencies: 1416
-- Name: seq_blog_post_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_blog_post_id', 2, true);


--
-- TOC entry 1417 (class 1259 OID 80955)
-- Dependencies: 4
-- Name: seq_bookmark_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_bookmark_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1998 (class 0 OID 0)
-- Dependencies: 1417
-- Name: seq_bookmark_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_bookmark_id', 1, false);


--
-- TOC entry 1418 (class 1259 OID 80957)
-- Dependencies: 4
-- Name: seq_bookmark_map_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_bookmark_map_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1999 (class 0 OID 0)
-- Dependencies: 1418
-- Name: seq_bookmark_map_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_bookmark_map_id', 1, false);


--
-- TOC entry 1419 (class 1259 OID 80959)
-- Dependencies: 4
-- Name: seq_category_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_category_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2000 (class 0 OID 0)
-- Dependencies: 1419
-- Name: seq_category_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_category_id', 4, true);


--
-- TOC entry 1420 (class 1259 OID 80961)
-- Dependencies: 4
-- Name: seq_company_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_company_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2001 (class 0 OID 0)
-- Dependencies: 1420
-- Name: seq_company_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_company_id', 1, false);


--
-- TOC entry 1421 (class 1259 OID 80963)
-- Dependencies: 4
-- Name: seq_company_note_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_company_note_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2002 (class 0 OID 0)
-- Dependencies: 1421
-- Name: seq_company_note_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_company_note_id', 1, false);


--
-- TOC entry 1422 (class 1259 OID 80965)
-- Dependencies: 4
-- Name: seq_company_tag_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_company_tag_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2003 (class 0 OID 0)
-- Dependencies: 1422
-- Name: seq_company_tag_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_company_tag_id', 1, false);


--
-- TOC entry 1423 (class 1259 OID 80967)
-- Dependencies: 4
-- Name: seq_contact_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_contact_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2004 (class 0 OID 0)
-- Dependencies: 1423
-- Name: seq_contact_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_contact_id', 3, true);


--
-- TOC entry 1424 (class 1259 OID 80969)
-- Dependencies: 4
-- Name: seq_contract_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_contract_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2005 (class 0 OID 0)
-- Dependencies: 1424
-- Name: seq_contract_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_contract_id', 1, false);


--
-- TOC entry 1425 (class 1259 OID 80971)
-- Dependencies: 4
-- Name: seq_file_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_file_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2006 (class 0 OID 0)
-- Dependencies: 1425
-- Name: seq_file_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_file_id', 1, false);


--
-- TOC entry 1426 (class 1259 OID 80973)
-- Dependencies: 4
-- Name: seq_file_map_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_file_map_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2007 (class 0 OID 0)
-- Dependencies: 1426
-- Name: seq_file_map_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_file_map_id', 1, false);


--
-- TOC entry 1427 (class 1259 OID 80975)
-- Dependencies: 4
-- Name: seq_group_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_group_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2008 (class 0 OID 0)
-- Dependencies: 1427
-- Name: seq_group_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_group_id', 1, true);


--
-- TOC entry 1428 (class 1259 OID 80977)
-- Dependencies: 4
-- Name: seq_icon_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_icon_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2009 (class 0 OID 0)
-- Dependencies: 1428
-- Name: seq_icon_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_icon_id', 19, true);


--
-- TOC entry 1429 (class 1259 OID 80979)
-- Dependencies: 4
-- Name: seq_issue_change_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_issue_change_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2010 (class 0 OID 0)
-- Dependencies: 1429
-- Name: seq_issue_change_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_issue_change_id', 1, false);


--
-- TOC entry 1430 (class 1259 OID 80981)
-- Dependencies: 4
-- Name: seq_issue_comment_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_issue_comment_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2011 (class 0 OID 0)
-- Dependencies: 1430
-- Name: seq_issue_comment_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_issue_comment_id', 1, false);


--
-- TOC entry 1431 (class 1259 OID 80983)
-- Dependencies: 4
-- Name: seq_issue_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_issue_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2012 (class 0 OID 0)
-- Dependencies: 1431
-- Name: seq_issue_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_issue_id', 1, false);


--
-- TOC entry 1439 (class 1259 OID 81312)
-- Dependencies: 4
-- Name: seq_kb_article_archive_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_kb_article_archive_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2013 (class 0 OID 0)
-- Dependencies: 1439
-- Name: seq_kb_article_archive_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_kb_article_archive_id', 1, false);


--
-- TOC entry 1432 (class 1259 OID 80985)
-- Dependencies: 4
-- Name: seq_kb_article_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_kb_article_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2014 (class 0 OID 0)
-- Dependencies: 1432
-- Name: seq_kb_article_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_kb_article_id', 1, false);


--
-- TOC entry 1433 (class 1259 OID 80987)
-- Dependencies: 4
-- Name: seq_portal_site_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_portal_site_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2015 (class 0 OID 0)
-- Dependencies: 1433
-- Name: seq_portal_site_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_portal_site_id', 1, false);


--
-- TOC entry 1434 (class 1259 OID 80989)
-- Dependencies: 4
-- Name: seq_rss_feed_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_rss_feed_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2016 (class 0 OID 0)
-- Dependencies: 1434
-- Name: seq_rss_feed_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_rss_feed_id', 1, false);


--
-- TOC entry 1441 (class 1259 OID 81345)
-- Dependencies: 4
-- Name: seq_user_archive_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_user_archive_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2017 (class 0 OID 0)
-- Dependencies: 1441
-- Name: seq_user_archive_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_user_archive_id', 1, false);


--
-- TOC entry 1435 (class 1259 OID 80991)
-- Dependencies: 4
-- Name: seq_user_id; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE seq_user_id
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 2018 (class 0 OID 0)
-- Dependencies: 1435
-- Name: seq_user_id; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('seq_user_id', 2, true);


--
-- TOC entry 1443 (class 1259 OID 81436)
-- Dependencies: 4
-- Name: system_cache; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE system_cache (
    cache_key character varying(50) NOT NULL,
    cache_time bigint
);


--
-- TOC entry 1436 (class 1259 OID 80993)
-- Dependencies: 4
-- Name: system_config; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE system_config (
    config_key character varying(50) NOT NULL,
    config_value text
);


--
-- TOC entry 1437 (class 1259 OID 80998)
-- Dependencies: 4
-- Name: system_event; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE system_event (
    user_id integer NOT NULL,
    login_date timestamp(1) without time zone
);


--
-- TOC entry 1938 (class 0 OID 80735)
-- Dependencies: 1368
-- Data for Name: access_group; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1939 (class 0 OID 80740)
-- Dependencies: 1369
-- Data for Name: access_group_perm_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1940 (class 0 OID 80742)
-- Dependencies: 1370
-- Data for Name: access_group_user_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1941 (class 0 OID 80744)
-- Dependencies: 1371
-- Data for Name: access_page; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO access_page (page_id, page_name, module_id) VALUES (4, '/admin/index.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (5, '/admin/user-list.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (6, '/admin/user-detail.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (7, '/admin/user-add.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (8, '/admin/user-add-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (9, '/admin/user-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (10, '/admin/user-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (41, '/admin/user-access.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (42, '/admin/user-access-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (43, '/admin/user-access-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (85, '/admin/attribute-list.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (86, '/admin/attribute-detail.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (158, '/admin/user-list-export.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (189, '/admin/group-list.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (190, '/admin/group-detail.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (191, '/admin/group-access.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (192, '/admin/group-add.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (193, '/admin/group-add-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (194, '/admin/group-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (195, '/admin/group-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (196, '/admin/group-delete.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (197, '/admin/group-delete-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (198, '/admin/group-access-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (199, '/admin/group-access-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (84, '/admin/config-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (216, '/admin/usage-survey.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (217, '/admin/cust-attr.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (218, '/admin/cust-attr-add.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (219, '/admin/cust-attr-add-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (220, '/admin/cust-attr-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (221, '/admin/cust-attr-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (222, '/admin/cust-attr-delete.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (223, '/admin/cust-attr-delete-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (224, '/admin/cust-attr-detail.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (118, '/portal/index.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (133, '/portal/blog-post-list.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (134, '/portal/blog-post-detail.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (135, '/portal/blog-post-add.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (136, '/portal/blog-post-add-2.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (139, '/portal/blog-post-comment-add-2.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (140, '/portal/blog-post-list-rss.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (164, '/portal/blog-category-add.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (165, '/portal/blog-category-add-2.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (166, '/portal/blog-category-edit.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (167, '/portal/blog-category-edit-2.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (210, '/portal/blog-category-list.dll', 6);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (48, '/contact-mgmt/index.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (49, '/contact-mgmt/company-list.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (50, '/contact-mgmt/company-detail.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (51, '/contact-mgmt/company-edit.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (52, '/contact-mgmt/company-edit-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (53, '/contact-mgmt/company-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (54, '/contact-mgmt/company-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (55, '/contact-mgmt/contact-list.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (56, '/contact-mgmt/contact-detail.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (57, '/contact-mgmt/contact-edit.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (58, '/contact-mgmt/contact-edit-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (59, '/contact-mgmt/contact-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (60, '/contact-mgmt/contact-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (61, '/contact-mgmt/company-contact.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (62, '/contact-mgmt/company-contact-export.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (63, '/contact-mgmt/contact-delete.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (64, '/contact-mgmt/contact-delete-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (65, '/contact-mgmt/company-delete.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (66, '/contact-mgmt/company-delete-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (74, '/contact-mgmt/contact-detail-vcard.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (75, '/contact-mgmt/company-contact-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (76, '/contact-mgmt/company-contact-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (77, '/contact-mgmt/company-contact-edit.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (78, '/contact-mgmt/company-contact-edit-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (79, '/contact-mgmt/company-contact-delete.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (80, '/contact-mgmt/company-contact-delete-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (102, '/contact-mgmt/company-bookmark.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (103, '/contact-mgmt/company-bookmark-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (104, '/contact-mgmt/company-bookmark-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (105, '/contact-mgmt/company-bookmark-edit.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (106, '/contact-mgmt/company-bookmark-edit-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (107, '/contact-mgmt/company-bookmark-delete.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (108, '/contact-mgmt/company-bookmark-delete-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (149, '/contact-mgmt/company-file.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (150, '/contact-mgmt/company-file-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (151, '/contact-mgmt/company-file-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (152, '/contact-mgmt/company-file-delete.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (153, '/contact-mgmt/company-file-delete-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (154, '/contact-mgmt/company-file-download.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (176, '/contact-mgmt/company-note.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (177, '/contact-mgmt/company-note-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (178, '/contact-mgmt/company-note-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (239, '/contact-mgmt/company-issue.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (240, '/contact-mgmt/company-issue-add.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (241, '/contact-mgmt/company-issue-add-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (242, '/contact-mgmt/company-issue-remove-2.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (168, '/IT/contract-list.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (169, '/IT/contract-detail.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (170, '/IT/contract-add.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (171, '/IT/contract-add-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (172, '/IT/contract-edit.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (173, '/IT/contract-edit-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (174, '/IT/contract-delete.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (175, '/IT/contract-delete-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (201, '/IT/contract-file-download.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (202, '/IT/contract-file-add.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (203, '/IT/contract-file-add-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (204, '/IT/contract-file-delete.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (205, '/IT/contract-file-delete-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (207, '/IT/contract-hardware-add.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (208, '/IT/contract-hardware-add-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (209, '/IT/contract-hardware-remove-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (18, '/IT/hardware-list.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (19, '/IT/hardware-detail.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (20, '/IT/hardware-edit.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (21, '/IT/hardware-edit-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (22, '/IT/hardware-add.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (23, '/IT/hardware-add-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (24, '/IT/hardware-delete-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (25, '/IT/hardware-license-remove-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (26, '/IT/hardware-category-list.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (27, '/IT/hardware-license-add-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (45, '/IT/hardware-delete.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (46, '/IT/hardware-license-add.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (47, '/IT/hardware-license-remove.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (129, '/IT/ajax-get-license-by-software.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (131, '/IT/ajax-get-hardware-detail.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (141, '/IT/hardware-file-add.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (142, '/IT/hardware-file-add-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (143, '/IT/hardware-file.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (144, '/IT/hardware-file-download.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (145, '/IT/hardware-file-delete.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (146, '/IT/hardware-file-delete-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (156, '/IT/hardware-list-export.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (17, '/IT/hardware-index.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (225, '/IT/hardware-comp.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (226, '/IT/hardware-comp-add.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (227, '/IT/hardware-comp-add-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (228, '/IT/hardware-comp-edit.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (229, '/IT/hardware-comp-edit-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (230, '/IT/hardware-comp-delete-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (231, '/IT/hardware-issue.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (232, '/IT/hardware-issue-add.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (233, '/IT/hardware-issue-add-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (234, '/IT/hardware-issue-remove-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (1, '/issue-tracker/index.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (2, '/issue-tracker/issue-list.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (3, '/issue-tracker/issue-detail.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (15, '/issue-tracker/issue-edit.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (16, '/issue-tracker/issue-edit-2.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (91, '/issue-tracker/issue-add.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (92, '/issue-tracker/issue-add-2.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (115, '/issue-tracker/issue-file-download.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (116, '/issue-tracker/issue-file-add.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (117, '/issue-tracker/issue-file-add-2.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (243, '/kb/index.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (244, '/kb/article-list.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (245, '/kb/article-detail.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (246, '/kb/article-add.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (247, '/kb/article-add-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (248, '/kb/article-edit.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (249, '/kb/article-edit-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (250, '/kb/category-list.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (251, '/kb/category-add.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (252, '/kb/category-add-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (253, '/kb/category-edit.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (254, '/kb/category-edit-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (255, '/kb/article-delete.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (256, '/kb/article-delete-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (119, '/portal/site-list-rss.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (211, '/portal/site-category-list.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (212, '/portal/site-category-add.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (213, '/portal/site-category-add-2.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (214, '/portal/site-category-edit.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (215, '/portal/site-category-edit-2.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (132, '/portal/site-list-index.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (120, '/portal/site-list.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (121, '/portal/site-add.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (122, '/portal/site-add-2.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (123, '/portal/site-edit.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (124, '/portal/site-edit-2.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (125, '/portal/site-delete.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (126, '/portal/site-delete-2.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (127, '/portal/site-detail.dll', 7);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (179, '/portal/rss-feed-list.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (180, '/portal/rss-feed-add.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (181, '/portal/rss-feed-add-2.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (182, '/portal/rss-feed-edit.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (183, '/portal/rss-feed-edit-2.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (184, '/portal/rss-feed-delete.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (185, '/portal/rss-feed-delete-2.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (186, '/portal/rss-feed-list-titles.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (187, '/portal/rss-feed-list-items.dll', 8);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (28, '/IT/software-list.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (29, '/IT/software-detail.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (30, '/IT/software-add.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (31, '/IT/software-add-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (32, '/IT/software-delete-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (33, '/IT/software-license-add-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (34, '/IT/software-license-edit-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (35, '/IT/software-license-delete-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (36, '/IT/software-edit.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (37, '/IT/software-edit-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (38, '/IT/software-delete.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (39, '/IT/software-license-add.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (40, '/IT/software-license-edit.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (44, '/IT/software-license-delete.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (93, '/IT/software-bookmark-add.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (94, '/IT/software-bookmark-add-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (95, '/IT/software-bookmark-edit.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (96, '/IT/software-bookmark-edit-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (97, '/IT/software-bookmark-delete.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (98, '/IT/software-bookmark-delete-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (99, '/IT/software-bookmark.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (109, '/IT/software-file-add.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (110, '/IT/software-file-add-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (111, '/IT/software-file.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (112, '/IT/software-file-download.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (113, '/IT/software-file-delete.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (114, '/IT/software-file-delete-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (130, '/IT/ajax-get-software-by-maker.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (157, '/IT/software-list-export.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (200, '/IT/software-index.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (235, '/IT/software-issue.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (236, '/IT/software-issue-add.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (237, '/IT/software-issue-add-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (238, '/IT/software-issue-remove-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (71, '/user-preference/index.dll', 11);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (73, '/user-preference/password-edit-2.dll', 11);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (72, '/user-preference/password-edit.dll', 11);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (257, '/home/index.dll', 13);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (258, '/issue-tracker/issue-list-export.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (259, '/admin/user-pw-reset.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (260, '/admin/user-pw-reset-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (261, '/kb/article-print.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (262, '/IT/contract-index.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (263, '/admin/user-hardware.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (12, '/issue-plugin/issue-add.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (13, '/issue-plugin/issue-add-2.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (14, '/issue-plugin/issue-add-3.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (11, '/admin/config.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (83, '/admin/config-write.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (67, '/reports/report-step1.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (68, '/reports/report-step2.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (69, '/reports/report-step3.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (70, '/reports/report-step4.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (264, '/kb/article-file-download.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (265, '/kb/article-file-add.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (266, '/kb/article-file-add-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (267, '/kb/article-file-delete.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (268, '/kb/article-file-delete-2.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (269, '/admin/user-index.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (270, '/kb/article-search.dll', 14);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (271, '/reports/report-issue.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (272, '/reports/report-hardware.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (273, '/contracts/software-remove-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (274, '/contracts/software-add.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (275, '/contracts/software-add-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (206, '/contracts/item-list.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (276, '/admin/user-delete.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (277, '/admin/user-delete-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (278, '/reports/report-software.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (279, '/reports/report-contract.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (280, '/hardware/hardware-member.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (281, '/hardware/hardware-member-add.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (282, '/hardware/hardware-member-add-2.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (283, '/hardware/hardware-member-remove.dll', 1);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (284, '/issues/issue-relationship.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (285, '/reports/report-software-usage.dll', 15);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (286, '/issues/issue-delete.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (287, '/issues/issue-delete-2.dll', 4);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (288, '/contact-mgmt/company-contracts.dll', 5);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (87, '/admin/attribute-field-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (88, '/admin/attribute-field-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (89, '/admin/attribute-field-add.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (90, '/admin/attribute-field-add-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (289, '/admin/attribute-edit.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (290, '/admin/attribute-edit-2.dll', 10);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (291, '/software/contacts.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (292, '/software/contact-add.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (293, '/software/contact-add-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (294, '/software/contact-remove-2.dll', 2);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (295, '/contracts/contacts.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (296, '/contracts/contact-add.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (297, '/contracts/contact-add-2.dll', 3);
INSERT INTO access_page (page_id, page_name, module_id) VALUES (298, '/contracts/contact-remove-2.dll', 3);


--
-- TOC entry 1942 (class 0 OID 80746)
-- Dependencies: 1372
-- Data for Name: access_perm_page_map; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (1, 1);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (1, 2);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (1, 3);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (2, 15);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (2, 16);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 4);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 11);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 6);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 5);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 8);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 7);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 10);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 9);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 19);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 29);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 41);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 42);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 43);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 48);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 49);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 50);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 51);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 52);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 53);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 54);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 26);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 18);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 17);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 28);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 55);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 56);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 57);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 58);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 59);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 60);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 61);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 62);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 63);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 64);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 65);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 66);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 67);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 68);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 69);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 70);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (14, 71);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (14, 72);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (14, 73);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 74);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 75);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 76);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 77);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 78);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 79);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 80);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 83);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 84);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 85);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 86);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 87);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 88);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 89);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 90);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (2, 91);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (2, 92);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 99);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 102);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 103);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 104);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 105);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 106);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 107);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 108);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 111);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 112);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (1, 115);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (2, 116);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (2, 117);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 129);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 130);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 131);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (16, 135);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (16, 136);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (16, 139);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 143);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 144);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 149);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 150);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 151);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 152);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 153);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 154);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 156);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 157);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 158);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (19, 164);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (19, 165);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (19, 166);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (19, 167);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (12, 168);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (12, 169);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 170);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 171);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 172);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 173);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 174);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 175);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 176);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 177);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 178);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (18, 180);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (18, 181);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (18, 182);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (18, 183);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (18, 184);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (18, 185);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 189);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 190);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 191);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 192);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 193);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 194);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 195);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 196);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 197);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 198);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 199);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (20, 187);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (20, 186);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (20, 179);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (21, 134);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (21, 140);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (21, 133);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (21, 118);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (15, 119);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (15, 132);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 20);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 21);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 22);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 23);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 24);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 25);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 27);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 45);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 46);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 47);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 141);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 142);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 145);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 146);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 31);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 30);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 94);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 93);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 98);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 97);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 96);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 95);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 32);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 38);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 37);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 36);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 110);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 109);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 114);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 113);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 33);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 39);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 35);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 44);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 34);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 40);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 200);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (24, 139);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (12, 201);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 202);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 203);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 204);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 205);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (12, 206);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 207);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 208);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 209);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (19, 210);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 211);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 212);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 213);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 214);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 215);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 120);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 121);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 122);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 123);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 124);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 125);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 126);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (25, 127);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 216);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 217);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 218);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 219);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 220);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 221);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 222);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 223);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 224);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 225);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 226);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 227);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 228);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 229);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 230);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 231);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 232);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 233);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 234);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 235);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 236);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 237);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 238);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 239);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 240);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 241);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (11, 242);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (26, 243);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (26, 244);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (26, 245);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 246);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 247);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 248);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 249);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (28, 250);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (28, 251);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (28, 252);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (28, 253);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (28, 254);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 255);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 256);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (1, 258);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 259);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 260);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (26, 261);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (12, 262);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 263);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (29, 1);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (29, 2);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (29, 3);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (29, 115);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (29, 258);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (26, 264);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 265);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 266);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 267);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (27, 268);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (3, 269);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (26, 270);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 271);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 272);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 273);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 274);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 275);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 276);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 277);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 278);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 279);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (6, 280);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 281);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 282);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (22, 283);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (1, 284);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (29, 284);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (13, 285);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (30, 286);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (30, 287);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (10, 288);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 289);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (4, 290);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (8, 291);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 292);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 293);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (23, 294);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (12, 295);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 296);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 297);
INSERT INTO access_perm_page_map (perm_id, page_id) VALUES (9, 298);


--
-- TOC entry 1943 (class 0 OID 80748)
-- Dependencies: 1373
-- Data for Name: access_permission; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (1, 'issue_read', 1, 21);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (2, 'issue_write', 1, 22);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (3, 'admin_read', 1, 1);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (4, 'admin_write', 1, 2);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (10, 'contact_read', 1, 11);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (11, 'contact_write', 1, 12);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (22, 'hardware_write', 1, 34);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (23, 'software_write', 1, 36);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (6, 'hardware_read', 1, 33);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (8, 'software_read', 1, 35);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (12, 'contract_read', 1, 37);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (9, 'contract_write', 1, 38);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (14, 'user_preference', 1, 151);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (15, 'portal_read', 1, 141);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (18, 'rss_write', 1, 147);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (20, 'rss_read', 1, 146);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (16, 'blog_write', 1, 144);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (21, 'blog_read', 1, 143);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (19, 'blog_admin', 1, 145);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (24, 'blog_comment', 1, 145);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (26, 'kb_read', 1, 80);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (27, 'kb_write', 1, 81);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (28, 'kb_admin', 1, 82);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (29, 'issue_read_limited', 1, 20);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (13, 'report', 1, 149);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (30, 'issue_delete', 1, 23);
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (25, 'portal_admin', 1, 143);


--
-- TOC entry 1944 (class 0 OID 80751)
-- Dependencies: 1374
-- Data for Name: access_user; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO access_user (user_id, username, password, display_name, first_name, last_name, email, creator, creation_date, modifier, modification_date, status, hardware_count, is_default_user, last_logon, last_visit, session_key) VALUES (1, 'admin', '0DPiKuNIrrVmD8IUCuw1hQxNqZc=', 'System Admin', 'System', 'Administrator', 'admin@localhost', 1, now(), NULL, NULL, -1, 0, 1, NULL, NULL, NULL);
INSERT INTO access_user (user_id, username, password, display_name, first_name, last_name, email, creator, creation_date, modifier, modification_date, status, hardware_count, is_default_user, last_logon, last_visit, session_key) VALUES (-1001, 'guest', '0DPiKuNIrrVmD8IUCuw1hQxNqZc=', 'Guest', 'Guest', 'User', 'guest@localhost', 1, now(), NULL, NULL, -2, 0, 1, NULL, NULL, NULL);


--
-- TOC entry 1981 (class 0 OID 81336)
-- Dependencies: 1440
-- Data for Name: access_user_archive; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1945 (class 0 OID 80761)
-- Dependencies: 1375
-- Data for Name: access_user_perm_map; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 1);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 2);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 3);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 4);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 6);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 8);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 9);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 10);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 11);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 12);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 13);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 14);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 15);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 16);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 18);
INSERT INTO access_user_perm_map (user_id, perm_id) VALUES (1, 19);


--
-- TOC entry 1946 (class 0 OID 80763)
-- Dependencies: 1376
-- Data for Name: asset_hardware; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1947 (class 0 OID 80773)
-- Dependencies: 1377
-- Data for Name: asset_hardware_component; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1948 (class 0 OID 80778)
-- Dependencies: 1378
-- Data for Name: asset_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1949 (class 0 OID 80781)
-- Dependencies: 1379
-- Data for Name: asset_software; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1950 (class 0 OID 80788)
-- Dependencies: 1380
-- Data for Name: asset_software_licenses; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1951 (class 0 OID 80791)
-- Dependencies: 1381
-- Data for Name: attribute; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-1, 7, 'issue_type', 1, 2, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-2, 7, 'issue_status', 1, 7, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-3, 7, 'issue_priority', 1, 16, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-4, 7, 'issue_resolution', 1, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-5, 5, 'company_status', 0, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-11, 6, 'contact_im', 1, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-13, 1, 'user_status', 0, 57, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-16, 5, 'company_note_type', 1, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-17, 2, 'hardware_component_type', 1, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-19, 14, 'kb_article_syntax_type', 0, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-6, 2, 'hardware_location', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-8, 3, 'software_type', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-10, 2, 'hardware_type', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-12, 2, 'hardware_status', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-14, 9, 'contract_type', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-15, 9, 'contract_renewal_type', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-18, 5, 'company_type', 0, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-7, 3, 'software_platform', 1, NULL, 0, NULL, 1, NULL, 0, 0);
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-20, 7, 'issue_assignee', 1, NULL, 0, NULL, 5, NULL, 0, 0);


--
-- TOC entry 1952 (class 0 OID 80799)
-- Dependencies: 1382
-- Data for Name: attribute_field; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (2, -1, 'question', NULL, NULL, 'Question', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (7, -2, 'new', NULL, NULL, 'S1: New', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (12, -2, 'reopened', NULL, NULL, 'S6: Reopened', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (13, -2, 'closed', NULL, NULL, 'S7: Closed', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (16, -3, 'medium', NULL, NULL, 'P3: Medium', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (24, -5, 'approved', NULL, NULL, 'Approved', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (25, -5, 'potential', NULL, NULL, 'Potential', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (1, -1, NULL, NULL, NULL, 'Suggestion', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (3, -1, NULL, NULL, NULL, 'General Request', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (4, -1, NULL, NULL, NULL, 'Software Installation', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (5, -1, NULL, NULL, NULL, 'Hardware Installation', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (6, -1, NULL, NULL, NULL, 'Specification', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (8, -2, NULL, NULL, NULL, 'S2: Assigned', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (9, -2, NULL, NULL, NULL, 'S3: Resovled', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (10, -2, NULL, NULL, NULL, 'S4: Deployed', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (11, -2, NULL, NULL, NULL, 'S5: Verified', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (14, -3, NULL, NULL, NULL, 'P1: Very High', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (15, -3, NULL, NULL, NULL, 'P2: High', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (17, -3, NULL, NULL, NULL, 'P4: Low', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (18, -3, NULL, NULL, NULL, 'P5: Very Low', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (19, -4, NULL, NULL, NULL, 'Fixed', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (20, -4, NULL, NULL, NULL, 'Can''t Reproduce', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (21, -4, NULL, NULL, NULL, 'Invalid', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (22, -4, NULL, NULL, NULL, 'Duplicate', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (23, -4, NULL, NULL, NULL, 'Deferred', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (26, -6, NULL, NULL, NULL, 'Headquarter', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (27, -6, NULL, NULL, NULL, 'Branch Office', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (28, -7, NULL, NULL, NULL, 'Windows', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (29, -7, NULL, NULL, NULL, 'Linux', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (30, -7, NULL, NULL, NULL, 'Mac', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (31, -8, NULL, NULL, NULL, 'Operating System', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (32, -8, NULL, NULL, NULL, 'Application', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (47, -10, NULL, NULL, NULL, 'Server', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (48, -10, NULL, NULL, NULL, 'Laptop', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (49, -10, NULL, NULL, NULL, 'Desktop', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (51, -11, NULL, NULL, NULL, 'Yahoo Messenger', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (52, -11, NULL, NULL, NULL, 'Windows Messenger', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (53, -12, NULL, NULL, NULL, 'Available', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (54, -12, NULL, NULL, NULL, 'In use', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (55, -12, NULL, NULL, NULL, 'Checked-out', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (56, -12, NULL, NULL, NULL, 'Lost or stolen', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (61, -14, NULL, NULL, NULL, 'Purchase Contract', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (62, -14, NULL, NULL, NULL, 'Service Contract', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (63, -15, NULL, NULL, NULL, 'One-Time', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (64, -15, NULL, NULL, NULL, 'Recurring', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (65, -16, NULL, NULL, NULL, 'Call Log', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (66, -16, NULL, NULL, NULL, 'Appointment Reminder', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (67, -16, NULL, NULL, NULL, 'Other', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (69, -17, NULL, NULL, NULL, 'Hard Disk', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (70, -17, NULL, NULL, NULL, 'CD/DVD Drive', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (71, -17, NULL, NULL, NULL, 'Network Card', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (72, -17, NULL, NULL, NULL, 'Memory', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-1, -13, 'user_status_enabled', NULL, NULL, 'user_status_enabled', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-2, -13, 'user_status_disabled', NULL, NULL, 'user_status_disabled', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-3, -18, 'company_type_hardware_manufacturer', NULL, NULL, 'company_type_hardware_manufacturer', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-4, -18, 'company_type_hardware_vendor', NULL, NULL, 'company_type_hardware_vendor', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-5, -18, 'company_type_software_maker', NULL, NULL, 'company_type_software_maker', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-6, -18, 'company_type_software_vendor', NULL, NULL, 'company_type_software_vendor', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-7, -19, 'kb_article_syntax_type_html', NULL, NULL, 'kb_article_syntax_type_html', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-8, -19, 'kb_article_syntax_type_mediawiki', NULL, NULL, 'kb_article_syntax_type_mediawiki', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-9, -19, 'kb_article_syntax_type_twiki', NULL, NULL, 'kb_article_syntax_type_twiki', NULL);


--
-- TOC entry 1982 (class 0 OID 81359)
-- Dependencies: 1442
-- Data for Name: attribute_type_field_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1955 (class 0 OID 80812)
-- Dependencies: 1385
-- Data for Name: blog_post; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1956 (class 0 OID 80818)
-- Dependencies: 1386
-- Data for Name: blog_post_comment; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1958 (class 0 OID 80829)
-- Dependencies: 1388
-- Data for Name: bookmark; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1959 (class 0 OID 80834)
-- Dependencies: 1389
-- Data for Name: bookmark_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1957 (class 0 OID 80823)
-- Dependencies: 1387
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO category (category_id, category_name, category_description, object_count, creator, creation_date, modifier, modification_date, object_type_id) VALUES (1, 'Uncategorized', NULL, 0, 1, now(), NULL, NULL, 12);
INSERT INTO category (category_id, category_name, category_description, object_count, creator, creation_date, modifier, modification_date, object_type_id) VALUES (3, 'Uncategorized', NULL, 0, 1, now(), NULL, NULL, 13);
INSERT INTO category (category_id, category_name, category_description, object_count, creator, creation_date, modifier, modification_date, object_type_id) VALUES (4, 'Uncategorized', NULL, 0, 1, now(), NULL, NULL, 14);


--
-- TOC entry 1960 (class 0 OID 80836)
-- Dependencies: 1390
-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1961 (class 0 OID 80846)
-- Dependencies: 1391
-- Data for Name: company_note; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1962 (class 0 OID 80851)
-- Dependencies: 1392
-- Data for Name: company_tag; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1963 (class 0 OID 80853)
-- Dependencies: 1393
-- Data for Name: company_tag_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1964 (class 0 OID 80855)
-- Dependencies: 1394
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO contact (contact_id, company_id, company_contact_type, contact_first_name, contact_last_name, contact_description, contact_title, contact_phone_home, contact_phone_mobile, contact_phone_work, contact_fax, contact_email_primary, contact_email_secondary, contact_homepage_url, address_street_primary, address_city_primary, address_state_primary, address_zipcode_primary, address_country_primary, address_street_secondary, address_city_secondary, address_state_secondary, address_zipcode_secondary, address_country_secondary, creator, creation_date, modifier, modification_date, user_id, messenger_1_type, messenger_1_id, messenger_2_type, messenger_2_id) VALUES (1, NULL, 11, 'Guest', 'User', NULL, NULL, NULL, NULL, NULL, NULL, 'guest@localhost', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, now(), NULL, NULL, -1001, NULL, NULL, NULL, NULL);
INSERT INTO contact (contact_id, company_id, company_contact_type, contact_first_name, contact_last_name, contact_description, contact_title, contact_phone_home, contact_phone_mobile, contact_phone_work, contact_fax, contact_email_primary, contact_email_secondary, contact_homepage_url, address_street_primary, address_city_primary, address_state_primary, address_zipcode_primary, address_country_primary, address_street_secondary, address_city_secondary, address_state_secondary, address_zipcode_secondary, address_country_secondary, creator, creation_date, modifier, modification_date, user_id, messenger_1_type, messenger_1_id, messenger_2_type, messenger_2_id) VALUES (2, NULL, 11, 'System', 'Administrator', NULL, NULL, NULL, NULL, NULL, NULL, 'admin@localhost', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, now(), NULL, NULL, 1, NULL, NULL, NULL, NULL);


--
-- TOC entry 1965 (class 0 OID 80880)
-- Dependencies: 1395
-- Data for Name: content_locale; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO content_locale (locale_id, language, country) VALUES ('ar_SA', 'Arabic', 'Saudia Arabia');
INSERT INTO content_locale (locale_id, language, country) VALUES ('de_DE', 'German', 'Germany');
INSERT INTO content_locale (locale_id, language, country) VALUES ('en_AU', 'English', 'Australia');
INSERT INTO content_locale (locale_id, language, country) VALUES ('en_CA', 'English', 'Canada');
INSERT INTO content_locale (locale_id, language, country) VALUES ('en_GB', 'English', 'United Kingdom');
INSERT INTO content_locale (locale_id, language, country) VALUES ('en_US', 'English', 'United States');
INSERT INTO content_locale (locale_id, language, country) VALUES ('es_ES', 'Spanish', 'Spain');
INSERT INTO content_locale (locale_id, language, country) VALUES ('fr_CA', 'French', 'Canada');
INSERT INTO content_locale (locale_id, language, country) VALUES ('fr_FR', 'French', 'France');
INSERT INTO content_locale (locale_id, language, country) VALUES ('hi_IN', 'Hindi', 'India');
INSERT INTO content_locale (locale_id, language, country) VALUES ('it_IT', 'Italian', 'Italy');
INSERT INTO content_locale (locale_id, language, country) VALUES ('iw_IL', 'Hebrew', 'Israel');
INSERT INTO content_locale (locale_id, language, country) VALUES ('ja_JP', 'Japanese', 'Japan');
INSERT INTO content_locale (locale_id, language, country) VALUES ('ko_KR', 'Korean', 'South Korea');
INSERT INTO content_locale (locale_id, language, country) VALUES ('nl_NL', 'Dutch', 'Netherlands');
INSERT INTO content_locale (locale_id, language, country) VALUES ('pt_BR', 'Portuguese', 'Brazil');
INSERT INTO content_locale (locale_id, language, country) VALUES ('sv_SE', 'Swedish', 'Sweden');
INSERT INTO content_locale (locale_id, language, country) VALUES ('th_TH', 'Thai (Western digits)', 'Thailand');
INSERT INTO content_locale (locale_id, language, country) VALUES ('th_TH_TH', 'Thai (Thai digits)', 'Thailand');
INSERT INTO content_locale (locale_id, language, country) VALUES ('tr_TR', 'Turkish', 'Turkey');
INSERT INTO content_locale (locale_id, language, country) VALUES ('zh_CN', 'Chinese (Simplified)', 'China');
INSERT INTO content_locale (locale_id, language, country) VALUES ('zh_TW', 'Chinese (Traditional)', 'Taiwan');


--
-- TOC entry 1966 (class 0 OID 80884)
-- Dependencies: 1396
-- Data for Name: contract; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1967 (class 0 OID 80889)
-- Dependencies: 1397
-- Data for Name: file; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1968 (class 0 OID 80897)
-- Dependencies: 1398
-- Data for Name: file_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1969 (class 0 OID 80899)
-- Dependencies: 1399
-- Data for Name: icon; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (5, '/common/default/images/icons/tux.png', 1, -7);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (1, '/common/default/images/icons/computer.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (2, '/common/default/images/icons/printer.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (3, '/common/default/images/icons/server.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (6, '/common/default/images/icons/windows.gif', 1, -7);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (7, '/common/default/images/icons/mac.png', 1, -7);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (8, '/common/default/images/icons/phone_fax.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (9, '/common/default/images/icons/computer_laptop.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (10, '/common/default/images/icons/computer_laptop_vaio.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (11, '/common/default/images/icons/monitor_mac.gif', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (12, '/common/default/images/icons/monitor_pc.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (13, '/common/default/images/icons/network_card.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (14, '/common/default/images/icons/phone_mobile.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (15, '/common/default/images/icons/phone.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (16, '/common/default/images/icons/scanner.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (17, '/common/default/images/icons/camera.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (18, '/common/default/images/icons/television.png', 1, -10);
INSERT INTO icon (icon_id, icon_path, is_system_icon, attribute_id) VALUES (19, '/common/default/images/icons/headset.png', 1, -10);


--
-- TOC entry 1970 (class 0 OID 80901)
-- Dependencies: 1400
-- Data for Name: issue; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1971 (class 0 OID 80906)
-- Dependencies: 1401
-- Data for Name: issue_change; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1972 (class 0 OID 80908)
-- Dependencies: 1402
-- Data for Name: issue_comment; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1973 (class 0 OID 80913)
-- Dependencies: 1403
-- Data for Name: issue_subscription; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1974 (class 0 OID 80915)
-- Dependencies: 1404
-- Data for Name: kb_article; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1980 (class 0 OID 81300)
-- Dependencies: 1438
-- Data for Name: kb_article_archive; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1953 (class 0 OID 80805)
-- Dependencies: 1383
-- Data for Name: object_attribute_value; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1975 (class 0 OID 80925)
-- Dependencies: 1405
-- Data for Name: object_map; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1954 (class 0 OID 80810)
-- Dependencies: 1384
-- Data for Name: object_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO object_type (object_type_id, object_key) VALUES (1, 'user');
INSERT INTO object_type (object_type_id, object_key) VALUES (2, 'hardware');
INSERT INTO object_type (object_type_id, object_key) VALUES (3, 'software');
INSERT INTO object_type (object_type_id, object_key) VALUES (4, 'software_license');
INSERT INTO object_type (object_type_id, object_key) VALUES (5, 'company');
INSERT INTO object_type (object_type_id, object_key) VALUES (6, 'contact');
INSERT INTO object_type (object_type_id, object_key) VALUES (7, 'issue');
INSERT INTO object_type (object_type_id, object_key) VALUES (8, 'access');
INSERT INTO object_type (object_type_id, object_key) VALUES (10, 'company_main_contact');
INSERT INTO object_type (object_type_id, object_key) VALUES (11, 'company_emp_contact');
INSERT INTO object_type (object_type_id, object_key) VALUES (12, 'blog_post');
INSERT INTO object_type (object_type_id, object_key) VALUES (13, 'portal_site');
INSERT INTO object_type (object_type_id, object_key) VALUES (14, 'kb_article');
INSERT INTO object_type (object_type_id, object_key) VALUES (9, 'contract');


--
-- TOC entry 1976 (class 0 OID 80927)
-- Dependencies: 1406
-- Data for Name: portal_site; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1977 (class 0 OID 80932)
-- Dependencies: 1407
-- Data for Name: rss_feed; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1983 (class 0 OID 81436)
-- Dependencies: 1443
-- Data for Name: system_cache; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1978 (class 0 OID 80993)
-- Dependencies: 1436
-- Data for Name: system_config; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO system_config (config_key, config_value) VALUES ('auth.authenticationMethod', 'app');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.domain', '');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.ldapUrl', '');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.sessionTimeoutSeconds', '10800');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.sessionTimeoutSeconds.options', '3600,7200,10800,14400');
INSERT INTO system_config (config_key, config_value) VALUES ('calendar.maxYearPlus', '20');
INSERT INTO system_config (config_key, config_value) VALUES ('calendar.minYear', '1980');
INSERT INTO system_config (config_key, config_value) VALUES ('characterEncoding', 'UTF-8');
INSERT INTO system_config (config_key, config_value) VALUES ('companies.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('companyLogoPath', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('companyName', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('companyPath', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('contacts.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('currency.options', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.base', 'yyyy-MM-dd HH:mm:ss');
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.shortDateFormat', 'yyyy-MM-dd');
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.timeFormat', 'HH:mm:ss');
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.timeFormat.options', 'HH:mm:ss,H:mm:ss,h:mm a,h:mm:ss a');
INSERT INTO system_config (config_key, config_value) VALUES ('email.allowedDomains', 'localhost');
INSERT INTO system_config (config_key, config_value) VALUES ('email.domainFiltering', '1');
INSERT INTO system_config (config_key, config_value) VALUES ('email.domainFiltering.options', '1,2');
INSERT INTO system_config (config_key, config_value) VALUES ('email.notification', '1');
INSERT INTO system_config (config_key, config_value) VALUES ('email.notification.options', '1,2');
INSERT INTO system_config (config_key, config_value) VALUES ('file.company.repositoryPath', 'C:\\Kwok\\Server\\files\\company');
INSERT INTO system_config (config_key, config_value) VALUES ('file.company.uploadFilePrefix', 'COM-');
INSERT INTO system_config (config_key, config_value) VALUES ('file.hardware.repositoryPath', 'C:\\Kwok\\Server\\files\\hardware');
INSERT INTO system_config (config_key, config_value) VALUES ('file.hardware.uploadFilePrefix', 'HW-');
INSERT INTO system_config (config_key, config_value) VALUES ('file.issue.repositoryPath', 'C:\\Kwok\\Server\\files\\issue');
INSERT INTO system_config (config_key, config_value) VALUES ('file.issue.uploadFilePrefix', 'ISSUE-');
INSERT INTO system_config (config_key, config_value) VALUES ('file.software.repositoryPath', 'C:\\Kwok\\Server\\files\\software');
INSERT INTO system_config (config_key, config_value) VALUES ('file.software.uploadFilePrefix', 'SWLIC-');
INSERT INTO system_config (config_key, config_value) VALUES ('hardware.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('issues.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('locale', 'en_US');
INSERT INTO system_config (config_key, config_value) VALUES ('portal.numberOfBlogPostCharactersOptions', '0,200,300,500,1000,2000');
INSERT INTO system_config (config_key, config_value) VALUES ('portal.numberOfBlogPostsOptions', '5,10,15,20,50');
INSERT INTO system_config (config_key, config_value) VALUES ('theme.default', 'blue');
INSERT INTO system_config (config_key, config_value) VALUES ('theme.options', 'blue,green,red,orange,purple');
INSERT INTO system_config (config_key, config_value) VALUES ('timezone.base', 'GMT');
INSERT INTO system_config (config_key, config_value) VALUES ('timezone.local', 'PST');
INSERT INTO system_config (config_key, config_value) VALUES ('url.application', 'http://localhost');
INSERT INTO system_config (config_key, config_value) VALUES ('users.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('ui.stylesheet', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('company.footerNotes', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('blogs.numberOfPostCharsToShow', '300');
INSERT INTO system_config (config_key, config_value) VALUES ('blogs.numberOfPostsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('home.customDescription', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('file.contract.repositoryPath', 'C:\\Kwok\\Server\\files\\contract');
INSERT INTO system_config (config_key, config_value) VALUES ('file.contract.uploadFilePrefix', 'CONTRACT-');
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.numberOfPastYears', '4');
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.numberOfUpcomingYears', '4');
INSERT INTO system_config (config_key, config_value) VALUES ('issues.columnList', 'issue_id,issue_name,issue_status,issue_priority,assignee_name,creation_date');
INSERT INTO system_config (config_key, config_value) VALUES ('hardware.columnList', 'hardware_name,hardware_model_name,hardware_last_service_date,hardware_owner_name');
INSERT INTO system_config (config_key, config_value) VALUES ('contracts.columnList', 'contract_name,contract_expiration_date,contract_effective_date');
INSERT INTO system_config (config_key, config_value) VALUES ('datetime.shortDateFormat.options', 'M/d/yyyy,MM/dd/yyyy,d/M/yyyy,dd/MM/yyyy,yyyy-MM-dd');
INSERT INTO system_config (config_key, config_value) VALUES ('admin.allowBlankUserPassword', '0');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.type', 'form');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.type.options', 'form,basic');
INSERT INTO system_config (config_key, config_value) VALUES ('contracts.expirationCountdown', '90');
INSERT INTO system_config (config_key, config_value) VALUES ('file.db.backup.repositoryPath', 'C:\\Kwok\\Server\\files\\backup');
INSERT INTO system_config (config_key, config_value) VALUES ('db.postgresProgramPath', 'C:\\Program Files\\PostgreSQL\\8.3\\bin\\pg_dump.exe');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.authenticationMethod.options', 'app,ldap,mixed');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.host', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.port', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.username', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.password', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.messages.limit', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.repeatInterval', '300000');
INSERT INTO system_config (config_key, config_value) VALUES ('template.moduleTabs', '13,1,2,4,14,5,3,6,8,7,15');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.senderIgnoreList', 'system@localhost');
INSERT INTO system_config (config_key, config_value) VALUES ('file.kb.repositoryPath', 'C:\\Kwok\\Server\\files\\kb');
INSERT INTO system_config (config_key, config_value) VALUES ('file.kb.uploadFilePrefix', 'KB-');
INSERT INTO system_config (config_key, config_value) VALUES ('hardware.warrantyExpireCountdown', '0');
INSERT INTO system_config (config_key, config_value) VALUES ('expirationCountdown.options', '0,30,90,120,365');
INSERT INTO system_config (config_key, config_value) VALUES ('software.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('contracts.numberOfRowsToShow', '10');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.from', 'system@localhost');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.host', 'your.emailserver.hostname.local');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.password', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.port', '587');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.starttls', 'false');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.to', 'demo@localhost');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.smtp.username', 'system@localhost');
INSERT INTO system_config (config_key, config_value) VALUES ('mail.pop.ssl.enable', 'false');
INSERT INTO system_config (config_key, config_value) VALUES ('system.licenseKey', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('system.cacheKey', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('kb.article.charLimit', '100000');
INSERT INTO system_config (config_key, config_value) VALUES ('software.columnList', 'software_name,software_version,software_manufacturer,license_purchased,license_installed,license_available');
INSERT INTO system_config (config_key, config_value) VALUES ('auth.ldap.securityPrincipal', NULL);
INSERT INTO system_config (config_key, config_value) VALUES ('logging.database.level', 'FINE');
INSERT INTO system_config (config_key, config_value) VALUES ('logging.ldap.level', 'FINE');
INSERT INTO system_config (config_key, config_value) VALUES ('Issues.ReportIssueEnabled', 'true');
INSERT INTO system_config (config_key, config_value) VALUES ('contacts.companyColumnList', 'rownum,company_name');
INSERT INTO system_config (config_key, config_value) VALUES ('contacts.columnList', 'rownum,contact_first_name,contact_last_name,contact_title,company_name');
INSERT INTO system_config (config_key, config_value) VALUES ('portal.columnList', 'rownum,site_name,site_path,site_placement,site_support_iframe');
INSERT INTO system_config (config_key, config_value) VALUES ('kb.article.columns', 'article_name,article_view_count');
INSERT INTO system_config (config_key, config_value) VALUES ('System.MultiAppsInstance', 'false');
INSERT INTO system_config (config_key, config_value) VALUES ('timezone.local.options', 'Etc/GMT+12,Etc/GMT+11,US/Hawaii,US/Alaska,PST,MST,US/Central,EST,Canada/Atlantic,America/Montevideo,Atlantic/South_Georgia,Atlantic/Cape_Verde,Etc/Greenwich,Europe/London,Europe/Amsterdam,Asia/Jerusalem,Europe/Moscow,Asia/Tehran,Asia/Baku,Asia/Kabul,Asia/Karachi,Asia/Calcutta,Asia/Dhaka,Asia/Bangkok,Asia/Hong_Kong,Asia/Seoul,Australia/Brisbane,Australia/Canberra,Asia/Magadan,Pacific/Auckland');
INSERT INTO system_config (config_key, config_value) VALUES ('locale.options', 'en_US,de_DE,es_ES,hu_HU,it_IT,nl_NL,pl_PL,pt_BR,sr_YU,zh_CN');
INSERT INTO system_config (config_key, config_value) VALUES ('software.numberLicenseNotesChars', '0');
INSERT INTO system_config (config_key, config_value) VALUES ('module.numberOfRowsToShow.options', '10,20,50,100,500');
INSERT INTO system_config (config_key, config_value) VALUES ('System.Security.UserPasswordLength', '0');
INSERT INTO system_config (config_key, config_value) VALUES ('Users.NameDisplay', 'user_display_name');
INSERT INTO system_config (config_key, config_value) VALUES ('Files.MaxUploadByteSize', '536870912');
INSERT INTO system_config (config_key, config_value) VALUES ('Files.KilobyteUnits', '1024');
INSERT INTO system_config (config_key, config_value) VALUES ('schema.version', '2.7.9');


--
-- TOC entry 1979 (class 0 OID 80998)
-- Dependencies: 1437
-- Data for Name: system_event; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 1797 (class 2606 OID 81004)
-- Dependencies: 1368 1368
-- Name: pk_access_group_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_group
    ADD CONSTRAINT pk_access_group_id PRIMARY KEY (group_id);


--
-- TOC entry 1803 (class 2606 OID 81006)
-- Dependencies: 1371 1371
-- Name: pk_access_page_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_page
    ADD CONSTRAINT pk_access_page_id PRIMARY KEY (page_id);


--
-- TOC entry 1809 (class 2606 OID 81008)
-- Dependencies: 1373 1373
-- Name: pk_access_perm_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_permission
    ADD CONSTRAINT pk_access_perm_id PRIMARY KEY (perm_id);


--
-- TOC entry 1820 (class 2606 OID 81010)
-- Dependencies: 1377 1377
-- Name: pk_asset_hardware_comp_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY asset_hardware_component
    ADD CONSTRAINT pk_asset_hardware_comp_id PRIMARY KEY (comp_id);


--
-- TOC entry 1818 (class 2606 OID 81012)
-- Dependencies: 1376 1376
-- Name: pk_asset_hardware_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY asset_hardware
    ADD CONSTRAINT pk_asset_hardware_id PRIMARY KEY (hardware_id);


--
-- TOC entry 1822 (class 2606 OID 81014)
-- Dependencies: 1378 1378
-- Name: pk_asset_map_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY asset_map
    ADD CONSTRAINT pk_asset_map_id PRIMARY KEY (map_id);


--
-- TOC entry 1824 (class 2606 OID 81016)
-- Dependencies: 1379 1379
-- Name: pk_asset_software_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY asset_software
    ADD CONSTRAINT pk_asset_software_id PRIMARY KEY (software_id);


--
-- TOC entry 1826 (class 2606 OID 81018)
-- Dependencies: 1380 1380
-- Name: pk_asset_software_license_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY asset_software_licenses
    ADD CONSTRAINT pk_asset_software_license_id PRIMARY KEY (license_id);


--
-- TOC entry 1830 (class 2606 OID 81020)
-- Dependencies: 1382 1382
-- Name: pk_attribute_field_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY attribute_field
    ADD CONSTRAINT pk_attribute_field_id PRIMARY KEY (attribute_field_id);


--
-- TOC entry 1828 (class 2606 OID 81022)
-- Dependencies: 1381 1381
-- Name: pk_attribute_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY attribute
    ADD CONSTRAINT pk_attribute_id PRIMARY KEY (attribute_id);


--
-- TOC entry 1840 (class 2606 OID 81024)
-- Dependencies: 1387 1387
-- Name: pk_blog_post_category_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY category
    ADD CONSTRAINT pk_blog_post_category_id PRIMARY KEY (category_id);


--
-- TOC entry 1838 (class 2606 OID 81026)
-- Dependencies: 1386 1386
-- Name: pk_blog_post_comment_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY blog_post_comment
    ADD CONSTRAINT pk_blog_post_comment_id PRIMARY KEY (comment_id);


--
-- TOC entry 1836 (class 2606 OID 81028)
-- Dependencies: 1385 1385
-- Name: pk_blog_post_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY blog_post
    ADD CONSTRAINT pk_blog_post_id PRIMARY KEY (post_id);


--
-- TOC entry 1842 (class 2606 OID 81030)
-- Dependencies: 1388 1388
-- Name: pk_bookmark_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY bookmark
    ADD CONSTRAINT pk_bookmark_id PRIMARY KEY (bookmark_id);


--
-- TOC entry 1844 (class 2606 OID 81032)
-- Dependencies: 1389 1389
-- Name: pk_bookmark_map_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY bookmark_map
    ADD CONSTRAINT pk_bookmark_map_id PRIMARY KEY (bookmark_map_id);


--
-- TOC entry 1846 (class 2606 OID 81034)
-- Dependencies: 1390 1390
-- Name: pk_company_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY company
    ADD CONSTRAINT pk_company_id PRIMARY KEY (company_id);


--
-- TOC entry 1848 (class 2606 OID 81036)
-- Dependencies: 1391 1391
-- Name: pk_company_note_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY company_note
    ADD CONSTRAINT pk_company_note_id PRIMARY KEY (note_id);


--
-- TOC entry 1850 (class 2606 OID 81038)
-- Dependencies: 1392 1392
-- Name: pk_company_tag_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY company_tag
    ADD CONSTRAINT pk_company_tag_id PRIMARY KEY (tag_id);


--
-- TOC entry 1856 (class 2606 OID 81040)
-- Dependencies: 1394 1394
-- Name: pk_contact_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT pk_contact_id PRIMARY KEY (contact_id);


--
-- TOC entry 1858 (class 2606 OID 81042)
-- Dependencies: 1395 1395
-- Name: pk_content_locale_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY content_locale
    ADD CONSTRAINT pk_content_locale_id PRIMARY KEY (locale_id);


--
-- TOC entry 1860 (class 2606 OID 81044)
-- Dependencies: 1396 1396
-- Name: pk_contract_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY contract
    ADD CONSTRAINT pk_contract_id PRIMARY KEY (contract_id);


--
-- TOC entry 1862 (class 2606 OID 81046)
-- Dependencies: 1397 1397
-- Name: pk_file_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY file
    ADD CONSTRAINT pk_file_id PRIMARY KEY (file_id);


--
-- TOC entry 1864 (class 2606 OID 81048)
-- Dependencies: 1398 1398
-- Name: pk_file_map_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY file_map
    ADD CONSTRAINT pk_file_map_id PRIMARY KEY (file_map_id);


--
-- TOC entry 1866 (class 2606 OID 81050)
-- Dependencies: 1399 1399
-- Name: pk_icon_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY icon
    ADD CONSTRAINT pk_icon_id PRIMARY KEY (icon_id);


--
-- TOC entry 1870 (class 2606 OID 81052)
-- Dependencies: 1401 1401
-- Name: pk_issue_change_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY issue_change
    ADD CONSTRAINT pk_issue_change_id PRIMARY KEY (issue_change_id);


--
-- TOC entry 1872 (class 2606 OID 81054)
-- Dependencies: 1402 1402
-- Name: pk_issue_comment_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY issue_comment
    ADD CONSTRAINT pk_issue_comment_id PRIMARY KEY (issue_comment_id);


--
-- TOC entry 1868 (class 2606 OID 81056)
-- Dependencies: 1400 1400
-- Name: pk_issue_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY issue
    ADD CONSTRAINT pk_issue_id PRIMARY KEY (issue_id);


--
-- TOC entry 1887 (class 2606 OID 81306)
-- Dependencies: 1438 1438
-- Name: pk_kb_article_history_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY kb_article_archive
    ADD CONSTRAINT pk_kb_article_history_id PRIMARY KEY (article_history_id);


--
-- TOC entry 1876 (class 2606 OID 81058)
-- Dependencies: 1404 1404
-- Name: pk_kb_article_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY kb_article
    ADD CONSTRAINT pk_kb_article_id PRIMARY KEY (article_id);


--
-- TOC entry 1881 (class 2606 OID 81060)
-- Dependencies: 1406 1406
-- Name: pk_portal_site_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY portal_site
    ADD CONSTRAINT pk_portal_site_id PRIMARY KEY (site_id);


--
-- TOC entry 1883 (class 2606 OID 81062)
-- Dependencies: 1407 1407
-- Name: pk_rss_feed_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rss_feed
    ADD CONSTRAINT pk_rss_feed_id PRIMARY KEY (feed_id);


--
-- TOC entry 1893 (class 2606 OID 81439)
-- Dependencies: 1443 1443
-- Name: pk_system_cache_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY system_cache
    ADD CONSTRAINT pk_system_cache_key PRIMARY KEY (cache_key);


--
-- TOC entry 1885 (class 2606 OID 81064)
-- Dependencies: 1436 1436
-- Name: pk_system_config_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY system_config
    ADD CONSTRAINT pk_system_config_key PRIMARY KEY (config_key);


--
-- TOC entry 1834 (class 2606 OID 81066)
-- Dependencies: 1384 1384
-- Name: pk_system_object_type_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY object_type
    ADD CONSTRAINT pk_system_object_type_id PRIMARY KEY (object_type_id);


--
-- TOC entry 1889 (class 2606 OID 81344)
-- Dependencies: 1440 1440
-- Name: pk_user_archive_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_user_archive
    ADD CONSTRAINT pk_user_archive_id PRIMARY KEY (user_archive_id);


--
-- TOC entry 1812 (class 2606 OID 81068)
-- Dependencies: 1374 1374
-- Name: pk_user_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_user
    ADD CONSTRAINT pk_user_id PRIMARY KEY (user_id);


--
-- TOC entry 1805 (class 2606 OID 81070)
-- Dependencies: 1371 1371
-- Name: uk_access_page_page_name; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_page
    ADD CONSTRAINT uk_access_page_page_name UNIQUE (page_name);


--
-- TOC entry 1891 (class 2606 OID 81362)
-- Dependencies: 1442 1442 1442 1442
-- Name: uk_attribute_type_field_map; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY attribute_type_field_map
    ADD CONSTRAINT uk_attribute_type_field_map UNIQUE (attribute_field_id, attribute_id, linked_attribute_id);


--
-- TOC entry 1854 (class 2606 OID 81074)
-- Dependencies: 1393 1393 1393
-- Name: uk_company_tag; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY company_tag_map
    ADD CONSTRAINT uk_company_tag UNIQUE (company_id, tag_id);


--
-- TOC entry 1852 (class 2606 OID 81076)
-- Dependencies: 1392 1392
-- Name: uk_company_tag_name; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY company_tag
    ADD CONSTRAINT uk_company_tag_name UNIQUE (tag_name);


--
-- TOC entry 1799 (class 2606 OID 81078)
-- Dependencies: 1369 1369 1369
-- Name: uk_group_perm_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_group_perm_map
    ADD CONSTRAINT uk_group_perm_id UNIQUE (group_id, perm_id);


--
-- TOC entry 1801 (class 2606 OID 81080)
-- Dependencies: 1370 1370 1370
-- Name: uk_group_user_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_group_user_map
    ADD CONSTRAINT uk_group_user_id UNIQUE (group_id, user_id);


--
-- TOC entry 1874 (class 2606 OID 81082)
-- Dependencies: 1403 1403 1403
-- Name: uk_issue_subscription_user; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY issue_subscription
    ADD CONSTRAINT uk_issue_subscription_user UNIQUE (issue_id, user_id);


--
-- TOC entry 1832 (class 2606 OID 81406)
-- Dependencies: 1383 1383 1383 1383
-- Name: uk_object_attribute_value; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY object_attribute_value
    ADD CONSTRAINT uk_object_attribute_value UNIQUE (attribute_id, attribute_field_id, object_id);


--
-- TOC entry 1879 (class 2606 OID 81084)
-- Dependencies: 1405 1405 1405 1405 1405
-- Name: uk_object_map; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY object_map
    ADD CONSTRAINT uk_object_map UNIQUE (object_id, object_type_id, linked_object_id, linked_object_type_id);


--
-- TOC entry 1807 (class 2606 OID 81086)
-- Dependencies: 1372 1372 1372
-- Name: uk_perm_page_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_perm_page_map
    ADD CONSTRAINT uk_perm_page_id UNIQUE (perm_id, page_id);


--
-- TOC entry 1816 (class 2606 OID 81088)
-- Dependencies: 1375 1375 1375
-- Name: uk_user_perm_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_user_perm_map
    ADD CONSTRAINT uk_user_perm_id UNIQUE (user_id, perm_id);


--
-- TOC entry 1814 (class 2606 OID 81092)
-- Dependencies: 1374 1374
-- Name: uk_user_username; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY access_user
    ADD CONSTRAINT uk_user_username UNIQUE (username);


--
-- TOC entry 1810 (class 1259 OID 81093)
-- Dependencies: 1374
-- Name: idx_access_user_password; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX idx_access_user_password ON access_user USING btree ("password");


--
-- TOC entry 1877 (class 1259 OID 81394)
-- Dependencies: 1404
-- Name: uk_kb_article_wiki_namespace; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX uk_kb_article_wiki_namespace ON kb_article USING btree (lower((article_wiki_namespace)::text));


--
-- TOC entry 1894 (class 2606 OID 81095)
-- Dependencies: 1369 1373 1808
-- Name: fk_access_group_perm_perm_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_group_perm_map
    ADD CONSTRAINT fk_access_group_perm_perm_id FOREIGN KEY (perm_id) REFERENCES access_permission(perm_id);


--
-- TOC entry 1895 (class 2606 OID 81100)
-- Dependencies: 1369 1368 1796
-- Name: fk_access_group_perm_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_group_perm_map
    ADD CONSTRAINT fk_access_group_perm_user_id FOREIGN KEY (group_id) REFERENCES access_group(group_id);


--
-- TOC entry 1896 (class 2606 OID 81105)
-- Dependencies: 1370 1368 1796
-- Name: fk_access_group_user_group_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_group_user_map
    ADD CONSTRAINT fk_access_group_user_group_id FOREIGN KEY (group_id) REFERENCES access_group(group_id);


--
-- TOC entry 1897 (class 2606 OID 81110)
-- Dependencies: 1370 1374 1811
-- Name: fk_access_group_user_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_group_user_map
    ADD CONSTRAINT fk_access_group_user_user_id FOREIGN KEY (user_id) REFERENCES access_user(user_id);


--
-- TOC entry 1898 (class 2606 OID 81115)
-- Dependencies: 1372 1371 1802
-- Name: fk_access_perm_page_page_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_perm_page_map
    ADD CONSTRAINT fk_access_perm_page_page_id FOREIGN KEY (page_id) REFERENCES access_page(page_id);


--
-- TOC entry 1899 (class 2606 OID 81120)
-- Dependencies: 1372 1373 1808
-- Name: fk_access_perm_page_perm_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_perm_page_map
    ADD CONSTRAINT fk_access_perm_page_perm_id FOREIGN KEY (perm_id) REFERENCES access_permission(perm_id);


--
-- TOC entry 1900 (class 2606 OID 81125)
-- Dependencies: 1375 1373 1808
-- Name: fk_access_user_perm_perm_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_user_perm_map
    ADD CONSTRAINT fk_access_user_perm_perm_id FOREIGN KEY (perm_id) REFERENCES access_permission(perm_id);


--
-- TOC entry 1901 (class 2606 OID 81130)
-- Dependencies: 1375 1374 1811
-- Name: fk_access_user_perm_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY access_user_perm_map
    ADD CONSTRAINT fk_access_user_perm_user_id FOREIGN KEY (user_id) REFERENCES access_user(user_id);


--
-- TOC entry 1902 (class 2606 OID 81135)
-- Dependencies: 1377 1376 1817
-- Name: fk_asset_hardware_comp_hardware_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY asset_hardware_component
    ADD CONSTRAINT fk_asset_hardware_comp_hardware_id FOREIGN KEY (hardware_id) REFERENCES asset_hardware(hardware_id);


--
-- TOC entry 1903 (class 2606 OID 81140)
-- Dependencies: 1378 1376 1817
-- Name: fk_asset_map_hardware_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY asset_map
    ADD CONSTRAINT fk_asset_map_hardware_id FOREIGN KEY (hardware_id) REFERENCES asset_hardware(hardware_id);


--
-- TOC entry 1904 (class 2606 OID 81145)
-- Dependencies: 1378 1379 1823
-- Name: fk_asset_map_software_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY asset_map
    ADD CONSTRAINT fk_asset_map_software_id FOREIGN KEY (software_id) REFERENCES asset_software(software_id);


--
-- TOC entry 1905 (class 2606 OID 81150)
-- Dependencies: 1380 1379 1823
-- Name: fk_asset_software_licenses_software_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY asset_software_licenses
    ADD CONSTRAINT fk_asset_software_licenses_software_id FOREIGN KEY (software_id) REFERENCES asset_software(software_id);


--
-- TOC entry 1909 (class 2606 OID 81155)
-- Dependencies: 1383 1381 1827
-- Name: fk_attr_value_attr_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY object_attribute_value
    ADD CONSTRAINT fk_attr_value_attr_id FOREIGN KEY (attribute_id) REFERENCES attribute(attribute_id);


--
-- TOC entry 1907 (class 2606 OID 81160)
-- Dependencies: 1382 1381 1827
-- Name: fk_attribute_field_attribute_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY attribute_field
    ADD CONSTRAINT fk_attribute_field_attribute_id FOREIGN KEY (attribute_id) REFERENCES attribute(attribute_id);


--
-- TOC entry 1908 (class 2606 OID 81165)
-- Dependencies: 1382 1399 1865
-- Name: fk_attribute_field_icon_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY attribute_field
    ADD CONSTRAINT fk_attribute_field_icon_id FOREIGN KEY (icon_id) REFERENCES icon(icon_id);


--
-- TOC entry 1906 (class 2606 OID 81395)
-- Dependencies: 1833 1381 1384
-- Name: fk_attribute_object_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY attribute
    ADD CONSTRAINT fk_attribute_object_type_id FOREIGN KEY (object_type_id) REFERENCES object_type(object_type_id);


--
-- TOC entry 1936 (class 2606 OID 81368)
-- Dependencies: 1442 1827 1381
-- Name: fk_attribute_type_attr_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY attribute_type_field_map
    ADD CONSTRAINT fk_attribute_type_attr_id FOREIGN KEY (attribute_id) REFERENCES attribute(attribute_id);


--
-- TOC entry 1935 (class 2606 OID 81363)
-- Dependencies: 1829 1382 1442
-- Name: fk_attribute_type_field_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY attribute_type_field_map
    ADD CONSTRAINT fk_attribute_type_field_id FOREIGN KEY (attribute_field_id) REFERENCES attribute_field(attribute_field_id);


--
-- TOC entry 1937 (class 2606 OID 81373)
-- Dependencies: 1381 1827 1442
-- Name: fk_attribute_type_linked_attr_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY attribute_type_field_map
    ADD CONSTRAINT fk_attribute_type_linked_attr_id FOREIGN KEY (linked_attribute_id) REFERENCES attribute(attribute_id);


--
-- TOC entry 1911 (class 2606 OID 81175)
-- Dependencies: 1385 1387 1839
-- Name: fk_blog_post_category_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY blog_post
    ADD CONSTRAINT fk_blog_post_category_id FOREIGN KEY (category_id) REFERENCES category(category_id);


--
-- TOC entry 1914 (class 2606 OID 81180)
-- Dependencies: 1388 1389 1841
-- Name: fk_bookmark_map_bookmark_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bookmark_map
    ADD CONSTRAINT fk_bookmark_map_bookmark_id FOREIGN KEY (bookmark_id) REFERENCES bookmark(bookmark_id);


--
-- TOC entry 1915 (class 2606 OID 81185)
-- Dependencies: 1389 1384 1833
-- Name: fk_bookmark_map_object_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bookmark_map
    ADD CONSTRAINT fk_bookmark_map_object_type_id FOREIGN KEY (object_type_id) REFERENCES object_type(object_type_id);


--
-- TOC entry 1913 (class 2606 OID 81190)
-- Dependencies: 1387 1384 1833
-- Name: fk_category_object_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY category
    ADD CONSTRAINT fk_category_object_type_id FOREIGN KEY (object_type_id) REFERENCES object_type(object_type_id);


--
-- TOC entry 1916 (class 2606 OID 81195)
-- Dependencies: 1845 1390 1391
-- Name: fk_company_note_company_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY company_note
    ADD CONSTRAINT fk_company_note_company_id FOREIGN KEY (company_id) REFERENCES company(company_id);


--
-- TOC entry 1917 (class 2606 OID 81200)
-- Dependencies: 1390 1393 1845
-- Name: fk_company_tag_map_company_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY company_tag_map
    ADD CONSTRAINT fk_company_tag_map_company_id FOREIGN KEY (company_id) REFERENCES company(company_id);


--
-- TOC entry 1918 (class 2606 OID 81205)
-- Dependencies: 1392 1393 1849
-- Name: fk_company_tag_map_tag_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY company_tag_map
    ADD CONSTRAINT fk_company_tag_map_tag_id FOREIGN KEY (tag_id) REFERENCES company_tag(tag_id);


--
-- TOC entry 1919 (class 2606 OID 81210)
-- Dependencies: 1390 1394 1845
-- Name: fk_contact_company_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT fk_contact_company_id FOREIGN KEY (company_id) REFERENCES company(company_id);


--
-- TOC entry 1920 (class 2606 OID 81215)
-- Dependencies: 1397 1398 1861
-- Name: fk_file_map_locale_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY file_map
    ADD CONSTRAINT fk_file_map_locale_id FOREIGN KEY (file_id) REFERENCES file(file_id);


--
-- TOC entry 1921 (class 2606 OID 81220)
-- Dependencies: 1833 1384 1398
-- Name: fk_file_map_object_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY file_map
    ADD CONSTRAINT fk_file_map_object_type_id FOREIGN KEY (object_type_id) REFERENCES object_type(object_type_id);


--
-- TOC entry 1922 (class 2606 OID 81225)
-- Dependencies: 1381 1399 1827
-- Name: fk_icon_attribute_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY icon
    ADD CONSTRAINT fk_icon_attribute_id FOREIGN KEY (attribute_id) REFERENCES attribute(attribute_id);


--
-- TOC entry 1924 (class 2606 OID 81230)
-- Dependencies: 1861 1401 1397
-- Name: fk_issue_change_file_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue_change
    ADD CONSTRAINT fk_issue_change_file_id FOREIGN KEY (file_id) REFERENCES file(file_id);


--
-- TOC entry 1925 (class 2606 OID 81235)
-- Dependencies: 1871 1401 1402
-- Name: fk_issue_change_issue_comment_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue_change
    ADD CONSTRAINT fk_issue_change_issue_comment_id FOREIGN KEY (issue_comment_id) REFERENCES issue_comment(issue_comment_id);


--
-- TOC entry 1926 (class 2606 OID 81240)
-- Dependencies: 1867 1401 1400
-- Name: fk_issue_change_issue_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue_change
    ADD CONSTRAINT fk_issue_change_issue_id FOREIGN KEY (issue_id) REFERENCES issue(issue_id);


--
-- TOC entry 1927 (class 2606 OID 81245)
-- Dependencies: 1867 1402 1400
-- Name: fk_issue_comment_issue_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue_comment
    ADD CONSTRAINT fk_issue_comment_issue_id FOREIGN KEY (issue_id) REFERENCES issue(issue_id);


--
-- TOC entry 1923 (class 2606 OID 81250)
-- Dependencies: 1400 1400 1867
-- Name: fk_issue_duplicate_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue
    ADD CONSTRAINT fk_issue_duplicate_id FOREIGN KEY (duplicate_id) REFERENCES issue(issue_id);


--
-- TOC entry 1928 (class 2606 OID 81255)
-- Dependencies: 1400 1403 1867
-- Name: fk_issue_subscription_issue_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue_subscription
    ADD CONSTRAINT fk_issue_subscription_issue_id FOREIGN KEY (issue_id) REFERENCES issue(issue_id);


--
-- TOC entry 1929 (class 2606 OID 81260)
-- Dependencies: 1403 1374 1811
-- Name: fk_issue_subscription_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY issue_subscription
    ADD CONSTRAINT fk_issue_subscription_user_id FOREIGN KEY (user_id) REFERENCES access_user(user_id);


--
-- TOC entry 1930 (class 2606 OID 81275)
-- Dependencies: 1839 1387 1404
-- Name: fk_kb_article_category_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY kb_article
    ADD CONSTRAINT fk_kb_article_category_id FOREIGN KEY (category_id) REFERENCES category(category_id);


--
-- TOC entry 1934 (class 2606 OID 81307)
-- Dependencies: 1387 1839 1438
-- Name: fk_kb_article_history_category_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY kb_article_archive
    ADD CONSTRAINT fk_kb_article_history_category_id FOREIGN KEY (category_id) REFERENCES category(category_id);


--
-- TOC entry 1910 (class 2606 OID 81400)
-- Dependencies: 1829 1382 1383
-- Name: fk_object_att_value_field_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY object_attribute_value
    ADD CONSTRAINT fk_object_att_value_field_id FOREIGN KEY (attribute_field_id) REFERENCES attribute_field(attribute_field_id);


--
-- TOC entry 1931 (class 2606 OID 81280)
-- Dependencies: 1384 1405 1833
-- Name: fk_object_map_linked_object_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY object_map
    ADD CONSTRAINT fk_object_map_linked_object_type_id FOREIGN KEY (linked_object_type_id) REFERENCES object_type(object_type_id);


--
-- TOC entry 1932 (class 2606 OID 81285)
-- Dependencies: 1405 1384 1833
-- Name: fk_object_map_object_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY object_map
    ADD CONSTRAINT fk_object_map_object_type_id FOREIGN KEY (object_type_id) REFERENCES object_type(object_type_id);


--
-- TOC entry 1912 (class 2606 OID 81290)
-- Dependencies: 1386 1385 1835
-- Name: fk_portal_blog_post_comment_post_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY blog_post_comment
    ADD CONSTRAINT fk_portal_blog_post_comment_post_id FOREIGN KEY (post_id) REFERENCES blog_post(post_id);


--
-- TOC entry 1933 (class 2606 OID 81295)
-- Dependencies: 1387 1406 1839
-- Name: fk_portal_site_category_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY portal_site
    ADD CONSTRAINT fk_portal_site_category_id FOREIGN KEY (category_id) REFERENCES category(category_id);

--
-- PostgreSQL database dump complete
--

--
-- Upgrading to 2.8.0
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.0.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------

DROP FUNCTION if exists sp_attribute_add(integer, character varying, integer, text, integer, text, integer);
DROP FUNCTION if exists sp_attribute_update(integer, character varying, integer, text, integer, text);
DROP FUNCTION if exists sp_issue_add(character varying, text, text, integer, integer, integer, integer, integer, character varying, character varying, character varying, integer);
DROP FUNCTION if exists sp_attribute_type_field_map_delete(integer, integer, integer);
DROP FUNCTION if exists sp_attribute_type_field_map_update(integer, integer, integer);
DROP FUNCTION if exists sp_user_add(integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, character varying, integer, integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, integer, character varying, character varying, text, character varying, character varying, character varying, character varying, character varying, integer);
DROP FUNCTION if exists sp_user_update(integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, integer, integer, character varying, character varying, character varying, character varying, character varying, integer, character varying, integer, character varying, character varying, text, character varying, character varying, character varying, character varying, character varying, integer);
DROP FUNCTION if exists sp_software_update(integer, character varying, text, integer, integer, integer, character varying, character varying, integer, integer, integer);
DROP FUNCTION if exists sp_software_add(character varying, text, integer, integer, character varying, character varying, integer, integer, integer);
DROP FUNCTION if exists sp_user_login(character varying, integer);
DROP FUNCTION if exists sp_issue_subscribers_update(integer, integer, integer);
DROP VIEW if exists attribute_view;
DROP VIEW if exists asset_software_view;
DROP VIEW if exists user_view;

-- ----------
-- Upgrades for this release
-- ----------
INSERT INTO access_permission (perm_id, perm_name, perm_is_enabled, order_num) VALUES (31, 'issue_proxy_submit', 1, 23);

ALTER TABLE issue ADD COLUMN proxy_creator integer;

ALTER TABLE attribute_type_field_map RENAME TO attribute_field_map;

CREATE TABLE attribute_group
(
   attribute_group_id integer, 
   attribute_group_name character varying(50) NOT NULL,
   object_type_id integer NOT NULL, 
   CONSTRAINT pk_attribute_group_id PRIMARY KEY (attribute_group_id)
);

ALTER TABLE attribute ADD COLUMN attribute_group_id integer;
ALTER TABLE attribute ADD CONSTRAINT fk_attribute_group_id FOREIGN KEY (attribute_group_id) REFERENCES attribute_group (attribute_group_id);

insert into access_page (page_id, page_name, module_id) values (299, '/admin/attribute-group-add.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 299);

insert into access_page (page_id, page_name, module_id) values (300, '/admin/attribute-group-add-2.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 300);

insert into access_page (page_id, page_name, module_id) values (301, '/admin/attribute-group-edit.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 301);

insert into access_page (page_id, page_name, module_id) values (302, '/admin/attribute-group-edit-2.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 302);

insert into access_page (page_id, page_name, module_id) values (303, '/admin/attribute-group-delete.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 303);

insert into access_page (page_id, page_name, module_id) values (304, '/admin/attribute-group-delete-2.dll', 10);
insert into access_perm_page_map(perm_id, page_id) values (4, 304);

CREATE SEQUENCE seq_attribute_group_id
    INCREMENT 1
    NO MAXVALUE
    NO MINVALUE
    START 1
    CACHE 1;

insert into system_config (config_key, config_value) values ('Hardware.CheckUniqueHardwareName', 'false');
insert into system_config (config_key, config_value) values ('System.Security.UserPasswordComplexityEnabled', 'false');
insert into system_config (config_key, config_value) values ('System.Security.AccountLockoutThreshold', '0');
insert into system_config (config_key, config_value) values ('System.Security.AccountLockoutDurationMinutes', '0');

alter table asset_software add column software_expire_date timestamp(1) without time zone;
alter table asset_software add column software_version character varying(50);

alter table access_user add column invalid_logon_count smallint DEFAULT (0)::smallint;
alter table access_user add column invalid_logon_date timestamp(1) without time zone;

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.0' where config_key = 'schema.version';

--
-- Upgrading to 2.8.1
--
-- ----------
-- Start upgrade
-- ----------
update system_config set config_value = '2.8.1.work' where config_key = 'schema.version';

-- ----------
-- Drop views, stored procedures, functions (be sure to use "if exists" as these are also copied to 
-- create-schema script, which doesn't have the views, stored procedures, function exist yet).
-- ----------
DROP FUNCTION if exists sp_software_add(character varying, text, integer, integer, integer, character varying, character varying, integer, integer, integer);
DROP FUNCTION if exists sp_user_delete(integer, integer);
DROP VIEW if exists contract_view;
DROP FUNCTION if exists sp_contract_add(character varying, text, integer, character varying, character varying, character varying, integer, integer, integer, integer, integer);
DROP FUNCTION if exists sp_contract_update(integer, character varying, text, integer, character varying, character varying, character varying, integer, integer, integer, integer, integer);
DROP FUNCTION if exists sp_file_delete(integer, integer);
DROP FUNCTION if exists sp_hardware_component_delete(integer, integer);
DROP FUNCTION if exists sp_software_license_delete(integer, integer);

-- ----------
-- Upgrades for this release
-- ----------
insert into access_page (page_id, page_name, module_id) values (305, '/software/ajax-get-license-details.dll', 2);
insert into access_perm_page_map(perm_id, page_id) values (8, 305);

insert into icon (icon_id, icon_path, is_system_icon, attribute_id) values (nextval('seq_icon_id'), '/common/default/images/icons/tablet-apple-ipad.png', 1, -10);

insert into system_config (config_key, config_value) values ('auth.ldap.url.scheme', 'ldap://');

update access_page set page_name = replace(page_name, '/portal/blog-', '/blogs/') where module_id=6;
update access_page set page_name = '/blogs/index.dll' where page_name = '/portal/index.dll' and module_id=6;

insert into access_page (page_id, page_name, module_id) values (306, '/blogs/post-edit.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 306);

insert into access_page (page_id, page_name, module_id) values (307, '/blogs/post-edit-2.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 307);

insert into access_page (page_id, page_name, module_id) values (308, '/blogs/post-delete.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 308);

insert into access_page (page_id, page_name, module_id) values (309, '/blogs/post-delete-2.dll', 6);
insert into access_perm_page_map(perm_id, page_id) values (16, 309);

update access_permission set order_num = 132 where perm_id = 25;

-- Add contract stages
INSERT INTO attribute (attribute_id, object_type_id, attribute_key, is_editable, default_attribute_field_id, is_custom_attr, attribute_url, attribute_type, attribute_option, attribute_convert_url, is_required) VALUES (-21, 9, 'contract_stage', 0, NULL, 0, NULL, 1, NULL, 0, -1);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-10, -21, 'contract_stage_draft', NULL, NULL, 'contract_stage_draft', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-11, -21, 'contract_stage_in_effect', NULL, NULL, 'contract_stage_in_effect', NULL);
INSERT INTO attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_description, icon_id, attribute_field_name, is_disabled) VALUES (-12, -21, 'contract_stage_archive', NULL, NULL, 'contract_stage_archive', NULL);

ALTER TABLE contract ADD COLUMN contract_stage integer;

-- Report issue link configuration
update system_config set config_key='Issues.ReportIssueFooterEnabled' where config_key='Issues.ReportIssueEnabled';
insert into system_config (config_key, config_value) values ('Issues.ReportIssueModuleEnabled', 'false');

insert into object_type (object_type_id, object_key) values (15, 'hardware_component');

insert into access_page (page_id, page_name, module_id) values (310, '/hardware/ajax-get-component-custom-fields.dll', 1);
insert into access_perm_page_map(perm_id, page_id) values (6, 310);

-- ----------
-- End upgrade
-- ----------
update system_config set config_value = '2.8.1' where config_key = 'schema.version';