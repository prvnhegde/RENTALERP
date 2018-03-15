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
package com.kwoksys.biz.auth.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.biz.auth.core.Permissions;

/**
 * BlogPostAccess
 */
public class BlogPostAccess {

    /**
     * Only original creator has edit permission to his/her own posts.
     * @param accessUser
     * @param post
     * @return
     * @throws DatabaseException
     */
    public static boolean hasEditPermission(AccessUser accessUser, BlogPost post) {
        return isPostCreator(accessUser, post);
    }

    /**
     * Original creator or Blogs admin has delete permission.
     * @param accessUser
     * @param post
     * @return
     */
    public static boolean hasDeletePermission(AccessUser accessUser, BlogPost post) throws DatabaseException {
        return accessUser.hasPermission(Permissions.BLOGS_ADMIN)
            || isPostCreator(accessUser, post);
    }

    public static boolean isPostCreator(AccessUser accessUser, BlogPost post) {
        return accessUser.getId().equals(post.getCreator().getId());
    }
}
