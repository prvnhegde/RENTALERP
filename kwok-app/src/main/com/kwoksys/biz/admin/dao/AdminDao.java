/*
 * Copyright 2016 Kwoksys
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kwoksys.biz.admin.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dto.DbSequence;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;

/**
 * AdminDao
 */
public class AdminDao extends BaseDao {

    public AdminDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets database names.
     *
     * @return ..
     */
    public List<Map<String, String>> getDatabases() throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectDatabases());

        return executeQueryReturnList(queryHelper);
    }

    /**
     * Updates application configuration.
     *
     * @return ..
     */
    public ActionMessages updateConfig(List<SystemConfig> configList) throws DatabaseException {
        if (!configList.isEmpty()) {
            String sqlQuery = AdminQueries.updateApplicationConfigQuery();

            Connection conn = getConnection();

            try {
                QueryHelper queryHelper = new QueryHelper(sqlQuery);
                queryHelper.prepareBatch(conn);
                
                for (SystemConfig systemConfig : configList) {
                    queryHelper.addInputString(systemConfig.getConfigKey());
                    queryHelper.addInputStringConvertNull(systemConfig.getConfigValue());
                    queryHelper.addBatch();
                }

                queryHelper.executeBatch();

            } catch (Exception e) {
                // Database problem
                handleError(e);

            } finally {
                closeConnection(conn);
            }
        }
        return errors;
    }

    public List<DbSequence> getDbSequences() throws DatabaseException {
        List<DbSequence> list = new ArrayList<>();
        list.add(getDbSequence("seq_asset_hardware_comp_id", "asset_hardware_component.comp_id"));
        list.add(getDbSequence("seq_asset_map_id", "asset_map.map_id"));
        list.add(getDbSequence("seq_asset_software_id", "asset_software.software_id"));
        list.add(getDbSequence("seq_asset_software_license_id", "asset_software_licenses.license_id"));
        list.add(getDbSequence("seq_attribute_id", "attribute.attribute_id"));
        list.add(getDbSequence("seq_attribute_field_id", "attribute_field.attribute_field_id"));
        list.add(getDbSequence("seq_blog_post_id", "blog_post.post_id"));
        list.add(getDbSequence("seq_blog_post_comment_id", "blog_post_comment.comment_id"));
        list.add(getDbSequence("seq_bookmark_id", "bookmark.bookmark_id"));
        list.add(getDbSequence("seq_bookmark_map_id", "bookmark_map.bookmark_map_id"));
        list.add(getDbSequence("seq_category_id", "category.category_id"));
        list.add(getDbSequence("seq_company_id", "company.company_id"));
        list.add(getDbSequence("seq_company_note_id", "company_note.note_id"));
        list.add(getDbSequence("seq_company_tag_id", "company_tag.tag_id"));
        list.add(getDbSequence("seq_contact_id", "contact.contact_id"));
        list.add(getDbSequence("seq_contract_id", "contract.contract_id"));
        list.add(getDbSequence("seq_file_id", "file.file_id"));
        list.add(getDbSequence("seq_file_map_id", "file_map.file_map_id"));
        list.add(getDbSequence("seq_icon_id", "icon.icon_id"));
        list.add(getDbSequence("seq_issue_id", "issue.issue_id"));
        list.add(getDbSequence("seq_issue_comment_id", "issue_comment.issue_comment_id"));
        list.add(getDbSequence("seq_issue_change_id", "issue_change.issue_change_id"));
        list.add(getDbSequence("seq_kb_article_id", "kb_article.article_id"));
        list.add(getDbSequence("seq_portal_site_id", "portal_site.site_id"));
        list.add(getDbSequence("seq_rss_feed_id", "rss_feed.feed_id"));
        list.add(getDbSequence("seq_user_id", "access_user.user_id"));

        return list;
    }

    public DbSequence getDbSequence(String sequenceName, String tableColumnName) throws DatabaseException {
        DbSequence dbSequence = new DbSequence();
        
        String[] names = tableColumnName.split("\\.");
        String tableName = names[0];
        String columnName = names[1];

        QueryHelper queryHelper = new QueryHelper("select max(" + columnName + ") as max, " +
                "(select last_value from " + sequenceName + ") as last_value from " + tableName) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                dbSequence.setLastValue(rs.getLong("last_value"));
                dbSequence.setName(sequenceName);
                dbSequence.setTableColumnName(tableColumnName);
                dbSequence.setTableMaxValue(rs.getLong("max"));
            }
        };
        
        executeSingleRecordQuery(queryHelper);

        return dbSequence;
    }
}
