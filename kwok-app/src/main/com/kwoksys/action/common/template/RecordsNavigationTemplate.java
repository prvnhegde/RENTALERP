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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing prev/next links.
 */
public class RecordsNavigationTemplate extends BaseTemplate {

    private String infoText;
    private int rowCount;
    private int rowOffset;
    private int rowLimit;
    private int rowStart = 0;
    private int rowEnd = 0;
    private String rowCountMsgkey = "";
    private String rowCountText;
    private String showAllRecordsText;
    private String showAllRecordsPath;
    private String path;
    private String firstText;
    private String prevText;
    private String nextText;

    public RecordsNavigationTemplate() {
        super(RecordsNavigationTemplate.class);
    }

    public void init() {}

    @Override
    public String getJspPath() {
        return "/jsp/common/template/RecordsNavigation.jsp";
    }

    public void setShowAllRecordsPath(String showAllRecordsPath) {
        this.showAllRecordsPath = showAllRecordsPath;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInfoText() {
        return infoText;
    }
    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }
    public int getRowCount() {
        return rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    public int getRowOffset() {
        return rowOffset;
    }
    public void setRowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
    }
    public int getRowLimit() {
        return rowLimit;
    }
    public void setRowLimit(int rowLimit) {
        this.rowLimit = rowLimit;
    }
    public String getShowAllRecordsText() {
        return showAllRecordsText;
    }
    public void setShowAllRecordsText(String showAllRecordsText) {
        this.showAllRecordsText = showAllRecordsText;
    }
    public String getShowAllRecordsPath() {
        return showAllRecordsPath;
    }

    public String getFirstText() {
        return firstText;
    }
    public void setFirstText(String firstText) {
        this.firstText = firstText;
    }
    public String getPrevText() {
        return prevText;
    }
    public void setPrevText(String prevText) {
        this.prevText = prevText;
    }
    public String getNextText() {
        return nextText;
    }
    public void setNextText(String nextText) {
        this.nextText = nextText;
    }
    public String getRowCountText() {
        return rowCountText;
    }
    public void setRowCountText(String rowCountText) {
        this.rowCountText = rowCountText;
    }
    public String getRowCountMsgkey() {
        return rowCountMsgkey;
    }
    public void setRowCountMsgkey(String rowCountMsgkey) {
        this.rowCountMsgkey = rowCountMsgkey;
    }
    public int getRowStart() {
        return rowStart;
    }
    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }
    public int getRowEnd() {
        return rowEnd;
    }
    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }

    public void applyTemplate() {
        // Default texts
        if (firstText == null) {
            firstText = Localizer.getText(requestContext, "core.template.recordsNav.first");
        }
        if (prevText == null) {
            prevText = Localizer.getText(requestContext, "core.template.recordsNav.prev");
        }
        if (nextText == null) {
            nextText = Localizer.getText(requestContext, "core.template.recordsNav.next");
        }

        /**
         * For "Show All" link.
         * "Show all" link is available only when the number of rows is larger than rowOffset.
         */
        if (showAllRecordsText != null && rowCount > rowLimit && showAllRecordsPath != null) {
            showAllRecordsText = new Link(requestContext).setAppPath(showAllRecordsPath).setTitle(showAllRecordsText).getString();
        }

        /**
         * For "first" and "prev" link.
         * We append the previous link only if the result is not 1. If it's 1, there is no previous.
         */
        if (rowOffset != 0) {
            int offsetPrev = rowOffset - rowLimit;
            if (offsetPrev <= 0) {
                offsetPrev = 0;
            }
            firstText = new Link(requestContext).setAjaxPath(path + 0).setTitle(firstText).getString();
            prevText = new Link(requestContext).setAjaxPath(path + offsetPrev).setTitle(prevText).getString();
        }

        /**
         * For "next" link.
         * Offset for next link would be the current start plus whatever the offset is.
         */
        int offsetNext = rowOffset + rowLimit;
        if (offsetNext < rowCount) {
            nextText = new Link(requestContext).setAjaxPath(path + offsetNext).setTitle(nextText).getString();
        }

        // This is for building a (0 - 0 of 0) text
        if (rowCount > 0) {
            rowStart = rowOffset + 1;
            rowEnd = offsetNext;
        }
        if (rowEnd > rowCount) {
            rowEnd = rowCount;
        }

        if (!rowCountMsgkey.isEmpty()) {
            // If rowEnd is 0, it could mean no result or showAll, just set it to rowCount. 
            if (rowEnd == 0) {
                rowEnd = rowCount;
            }
            rowCountText = Localizer.getText(requestContext, rowCountMsgkey, new Object[]{
                rowStart, rowEnd, rowCount});
        }
    }
}
