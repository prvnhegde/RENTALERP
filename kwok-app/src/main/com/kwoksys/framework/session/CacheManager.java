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
package com.kwoksys.framework.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.SiteSearch;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;

/**
 * This provides APIs to access Apache's Java Caching System.
 * Naming convention is get<Object>Cache(), cache<Object>(), recache<Object>(), purge<Object>Cache().
 * To use one of the methods from other classes, name it in a way as getCached<Object>().
 */
public class CacheManager {

    private static final Logger LOGGER = Logger.getLogger(CacheManager.class.getName());

    // These caches don't change
    private static final String PAGES_INFO_CACHE = "pagesInfo";

    private static final String PERMISSION_PAGES_CACHE = "permissionPages";

    // These caches change but static
    private static final String SYSTEM_CONFIG_CACHE = "sysconfig";

    private static final String MODULE_TABS_CACHE = "moduleTabs";

    private static final String SYSTEM_ATTR_MAP_CACHE = "systemAttrMap";

    // These caches change and are dynamic based on ids
    private static final String SYSTEM_ATTR_FIELDS_CACHE = "systemAttrFields_";

    private static final String CUSTOM_ATTR_GROUPS_CACHE = "customAttrGroups_";

    private static final String GROUP_PERMISSION_CACHE = "groupPermissions_";

    private static final String USER_PERMISSION_CACHE = "userPermissions_";
    
    /**
     * AccessUser cache, syntax: user_<user id>
     */
    private static final String USER_CACHE = "user_";

    private RequestContext requestContext;

    private static JCS jcs;

    public CacheManager() {}
    
    public CacheManager(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
    
    public static void init() throws Exception {
        try {
            LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Loading " + AppPaths.CACHE_PROPERTIES + " file...");
            JCS.setConfigFilename(AppPaths.CACHE_PROPERTIES);
            jcs = JCS.getInstance(Keywords.JCS_INSTANCE_NAME);

        } catch (Exception e) {
            throw new Exception("Failed to get cache instance.", e);
        }
    }

    public void checkRemoveCaches(Long newCacheTime) {
        try {
            Long cacheTime = ConfigManager.system.getCacheTime();

            if (cacheTime == null) {
                ConfigManager.system.setCacheTime(newCacheTime);
                return;
            }

            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List<String> cacheKeys = systemService.getFlushSystemCacheKeys(cacheTime);

            if (!cacheKeys.isEmpty()) {
                for (String cacheKey : cacheKeys) {
                    removeCache(cacheKey);
                }
                systemService.validateSystemCaches(newCacheTime);
                ConfigManager.system.setCacheTime(newCacheTime);
            }
        } catch (Exception e) {
            LOGGER.severe("Problem removing cache... " + e.getMessage());
        }
    }

    /**
     * Removes local and cluster caches. Cluster caches are removed by updating records in system_cache table.
     * @param cacheKey
     */
    private void removeClusterCache(String cacheKey) {
        try {
            // Leaving the cache removal code here so that we see method removeClusterCache() in log.  
            LOGGER.info(LogConfigManager.CACHE_PREFIX + " Removing cache: " + cacheKey);
            jcs.remove(cacheKey);

            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            systemService.resetSystemCache(cacheKey);

        } catch (Exception e) {
            LOGGER.severe("Problem removing cache... " + e.getMessage());
        }
    }

    private static Object getCache(String cacheKey) {
        return jcs.get(cacheKey);
    }

    private static void addCache(String cacheKey, Object cacheObject) {
        LOGGER.info(LogConfigManager.CACHE_PREFIX + " Creating cache: " + cacheKey);

        try {
            jcs.put(cacheKey, cacheObject);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Problem creating cache.", e);
        }
    }

    private static void removeCache(String cacheKey) throws CacheException {
        LOGGER.info(LogConfigManager.CACHE_PREFIX + " Removing cache: " + cacheKey);
        jcs.remove(cacheKey);
    }
    
    /**
     * Caches system configs.
     * @return
     */
    public Map<String, String> cacheSystemConfigs() {
        Map<String, String> configMap = null;
        try {
            removeCache(SYSTEM_CONFIG_CACHE);

            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            configMap = systemService.getSystemConfig();

            // We put configMap in cache only when it's not null.
            // This helps detect whether database is unavailable in request processor servlet.
            if (!configMap.isEmpty()) {
                addCache(SYSTEM_CONFIG_CACHE, configMap);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Problem creating cache.", e);
        }
        return configMap;
    }

    /**
     * Given an attributeId, returns attribute object.
     *
     * @return ..
     */
    public Attribute getSystemAttrCache(Integer attributeId) throws DatabaseException {
        Map<Integer, Attribute> attrMap = (Map) getCache(SYSTEM_ATTR_MAP_CACHE);

        if (attrMap == null) {
            AdminService adminService = ServiceProvider.getAdminService(requestContext);

            attrMap = new HashMap<>();

            AttributeSearch attributeSearch = new AttributeSearch();
            attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, false);

            for (Attribute attr : adminService.getAttributes(new QueryCriteria(attributeSearch)).values()) {
                attrMap.put(attr.getId(), attr);
            }

            addCache(SYSTEM_ATTR_MAP_CACHE, attrMap);
        }

        return (Attribute)attrMap.get(attributeId);
    }

    public void removeSystemAttrCache() {
        removeClusterCache(SYSTEM_ATTR_MAP_CACHE);
    }

    /**
     * Returns objectKey attributes cache.
     *
     * @return ..
     */
    public Map<Integer, AttributeField> getAttributeFieldsCache(Integer attributeId) throws DatabaseException {
        Map<Integer, AttributeField> attrFields = (Map) getCache(SYSTEM_ATTR_FIELDS_CACHE + attributeId);

        if (attrFields == null) {
            AdminService adminService = ServiceProvider.getAdminService(requestContext);
            attrFields = adminService.getAttributeFields(attributeId);

            addCache(SYSTEM_ATTR_FIELDS_CACHE + attributeId, attrFields);
        }
        return attrFields;
    }

    public void removeAttributeFieldsCache(Integer attributeId) {
        removeClusterCache(SYSTEM_ATTR_FIELDS_CACHE + attributeId);
    }

    public Map<Integer, AttributeGroup> getCustomAttrGroupsCache(Integer objectTypeId) throws DatabaseException {
        Map<Integer, AttributeGroup> groupMap = (Map) getCache(CUSTOM_ATTR_GROUPS_CACHE + objectTypeId);

        if (groupMap == null) {
            groupMap = ServiceProvider.getAdminService(requestContext).getAttributeGroups(objectTypeId);
            addCache(CUSTOM_ATTR_GROUPS_CACHE + objectTypeId, groupMap);
        }
        return groupMap;
    }

    public void removeCustomAttrGroupsCache(Integer objectTypeId) {
        removeClusterCache(CUSTOM_ATTR_GROUPS_CACHE + objectTypeId);
    }

    /**
     * Checks whether permission pages is empty, if so, redo the caching.
     *
     * @return ..
     */
    public Map<Integer, Set<Integer>> getPermissionPagesCache() throws DatabaseException {
        Map<Integer, Set<Integer>> permPages = (Map) getCache(PERMISSION_PAGES_CACHE);

        if (permPages == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            permPages = authService.getAccessPermPages();

            addCache(PERMISSION_PAGES_CACHE, permPages);
        }
        return permPages;
    }

    /**
     * Returns cached group permissions
     *
     * @return ..
     */
    public Set<Integer> getGroupPermissionsCache(Integer groupId) throws DatabaseException {
        Set<Integer> permSet = (Set) getCache(GROUP_PERMISSION_CACHE + groupId);

        if (permSet == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            permSet = authService.getAccessGroupPerms(groupId);

            addCache(GROUP_PERMISSION_CACHE + groupId, permSet);
        }
        return permSet;
    }

    public void removeGroupPermissionsCache(Integer groupId) {
        removeClusterCache(GROUP_PERMISSION_CACHE + groupId);
    }

    /**
     * Returns cached user permissions
     *
     * @return ..
     */
    public Set<Integer> getUserPermissionsCache(Integer userId) throws DatabaseException {
        Set<Integer> permSet = (Set) getCache(USER_PERMISSION_CACHE + userId);

        if (permSet == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            permSet = authService.getAccessUserPerms(userId);

            addCache(USER_PERMISSION_CACHE + userId, permSet);
        }
        return permSet;
    }

    public void removeUserPermissionsCache(Integer userId) {
        removeClusterCache(USER_PERMISSION_CACHE + userId);
    }

    /**
     * Returns cached pages info map.
     *
     * @return ..
     */
    public Map<String, AccessPage> getPagesInfoCache() throws DatabaseException {
        Map pageMap = (Map) getCache(PAGES_INFO_CACHE);

        if (pageMap == null) {
            AuthService authService = ServiceProvider.getAuthService(requestContext);
            pageMap = authService.getAccessPages();

            addCache(PAGES_INFO_CACHE, pageMap);
        }
        return pageMap;
    }
   
    /**
     * Get module tabs cache, mostly for HeaderTemplate's module tabs.
     * @return
     */
    public List<Site> getModuleTabsCache() throws DatabaseException {
        List<Site> list = (List) getCache(MODULE_TABS_CACHE);

        if (list == null) {
            // Show portal sites that are shown as tabs
            SiteSearch siteSearch = new SiteSearch();
            siteSearch.put(SiteSearch.SHOW_ON_TAB, "");

            QueryCriteria query = new QueryCriteria(siteSearch);
            query.addSortColumn(PortalQueries.getOrderByColumn(Site.SITE_NAME));

            PortalService portalService = ServiceProvider.getPortalService(requestContext);

            list = portalService.getSites(query);

            addCache(MODULE_TABS_CACHE, list);
        }
        return list;
    }

    public void removeModuleTabsCache() {
        removeClusterCache(MODULE_TABS_CACHE);
    }
    
    /**
     * Gets user cache.
     * @param userId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public AccessUser getUserCache(Integer userId) throws DatabaseException {
        AccessUser accessUser = (AccessUser) getCache(USER_CACHE + userId);

        if (accessUser == null) {
            try {
                accessUser = ServiceProvider.getAdminService(requestContext).getUser(userId);
            } catch (ObjectNotFoundException e) {
                accessUser = new AccessUser(userId);
                accessUser.setRemoved(true);
            }

            addCache(USER_CACHE + userId, accessUser);
        }
        return accessUser;
    }
    
    /**
     * Gets user cache. Throws ObjectNotFoundException when user doesn't exist.
     * @param userId
     * @return
     * @throws DatabaseException
     */
    public AccessUser getUserCacheValidate(Integer userId) throws DatabaseException, ObjectNotFoundException {
        AccessUser accessUser = (AccessUser) getCache(USER_CACHE + userId);

        if (accessUser == null) {
            accessUser = ServiceProvider.getAdminService(requestContext).getUser(userId);
            addCache(USER_CACHE + userId, accessUser);
        }
        return accessUser;
    }

    /**
     * Removes user cache.
     * @param userId
     */
    public void removeUserCache(Integer userId) {
        removeClusterCache(USER_CACHE + userId);
    }
}
