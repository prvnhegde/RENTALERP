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
package com.kwoksys.biz.blogs.dto;

import com.kwoksys.biz.base.BaseObject;

/**
 * BlogPost
 */
public class BlogPost extends BaseObject {

    public static final String POST_OBJECT_COUNT = "post_object_count";

    /** We only have one post type so far **/
    public static final int POST_TYPE_LIST = 2;
    
    public static final int POST_ALLOW_COMMENT_YES = 1;
    public static final int POST_ALLOW_COMMENT_NO = 0;
    public static final int POST_ALLOW_COMMENT_SELECT_ONE = -1;

    // Actual columns.
    private Integer id;
    private String postTitle;
    private String postBody;
    private Integer postType;
    private String postIp;
    private int postAllowComment;
    private Integer categoryId;
    private String categoryName;
    // Additional information.
    private int postCommentCount;

    public BlogPost() {
        postType = POST_TYPE_LIST;
        postAllowComment = 1;
        categoryId = 0;
    }

    public boolean isPostAllowComment() {
        return (postAllowComment == 1);
    }

    //
    // Getter and Setter
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPostTitle() {
        return postTitle;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
    public String getPostBody() {
        return postBody;
    }
    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }
    public Integer getPostType() {
        return postType;
    }
    public void setPostType(Integer postType) {
        this.postType = postType;
    }
    public int getPostAllowComment() {
        return postAllowComment;
    }
    public void setPostAllowComment(int postAllowComment) {
        this.postAllowComment = postAllowComment;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getPostIp() {
        return postIp;
    }
    public void setPostIp(String postIp) {
        this.postIp = postIp;
    }
    public int getPostCommentCount() {
        return postCommentCount;
    }
    public void setPostCommentCount(int postCommentCount) {
        this.postCommentCount = postCommentCount;
    }
}
