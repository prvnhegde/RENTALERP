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
 * Portal Blog Post Comment Object.
 */
public class BlogPostComment extends BaseObject {

    private Integer commentId;
    private String commentDescription;
    private String commentIp;
    private Integer postId;

    public BlogPostComment() {
        commentId = 0;
        postId = 0;
    }

    //
    // Getter and Setter
    //
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public void setCommentIp(String commentIp) {
        this.commentIp = commentIp;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getCommentId() {
        return commentId;
    }
    public String getCommentDescription() {
        return commentDescription;
    }
    public String getCommentIp() {
        return commentIp;
    }
    public Integer getPostId() {
        return postId;
    }
}
