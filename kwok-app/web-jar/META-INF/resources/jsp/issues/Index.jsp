<%--
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
--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<div class="section"><k:message key="issueMgmt.numIssues" arg0="${numIssueRecords}"/>
<k:equal name="hasIssueReadPermission" value="false"><k:message key="issues.numAllowedIssues" arg0="${numIssuesHasPerm}"/></k:equal>
</div>

<%-- Issue filter --%>
<div class="box" style="width:40%; min-width:400px; margin-bottom:20px">
    <h3><k:message key="issueMgmt.index.filterHeader"/></h3>
    <ul>
        <k:foreach id="link" name="links">
            <li>${link}</li>
        </k:foreach>
    </ul>
    <p class="section"><span class="formFieldDesc"><k:message key="issues.searchDesc"/></span>
</div>
<div class="box standardBorder" style="width:59%; min-width:500px">
    <h3 class="noLine" style="text-align:center"><k:message key="issues.numRecentIssues" arg0="${numDays}"/></h3>
    <canvas id="chartRecentIssues" height="100" width="220"></canvas>
    <script>
       	var barChartData = {
       		labels : [${chartLabels}],
       		datasets : [
       			{
       				fillColor : "rgba(151,187,205,0.5)",
       				strokeColor : "rgba(151,187,205,0.8)",
       				highlightFill : "rgba(151,187,205,0.75)",
       				highlightStroke : "rgba(151,187,205,1)",
       				data : [${chartData}]
       			}
       		]
       	}
  		var ctx = document.getElementById("chartRecentIssues").getContext("2d");
   		window.myBar = new Chart(ctx).Bar(barChartData, {
   			responsive : true, animation : false
   		});
	</script>
</div>
    
<div style="clear:both"></div>

<%-- Issue summary --%>
<k:equal name="hasIssueReadPermission" value="true">
<h3 class="noLine"><k:message key="issueMgmt.index.issueCountHeader"/></h3>

<div class="box borderTop lightGrayBorder" style="width:24%; min-width:200px; padding:6px;">
    <b><k:message key="issueMgmt.index.countIssueByStatus"/></b>
    <p><table class="stats standard stripedListTable">
    <k:foreach id="row" name="statusCountList">
        <tr class="dataRowHoverOff">
            <th><div>${row.countKey}</div></th>
            <td><div>${row.countValue}</div></td>
        </tr>
    </k:foreach>
    </table>
</div>
<div class="box borderTop lightGrayBorder" style="width:24%; min-width:200px; padding:6px;">
    <b><k:message key="issueMgmt.index.countOpenIssueByPriority"/></b>
    <p><table class="stats standard stripedListTable">
    <k:foreach id="row" name="priorityCountList">
        <tr class="dataRowHoverOff">
            <th><div>${row.countKey}</div></th>
            <td><div>${row.countValue}</div></td>
        </tr>
    </k:foreach>
    </table>
</div>
<div class="box borderTop lightGrayBorder" style="width:24%; min-width:200px; padding:6px;">
    <b><k:message key="issueMgmt.index.countOpenIssueByType"/></b>
    <p><table class="stats standard stripedListTable">
    <k:foreach id="row" name="typeCountList">
        <tr class="dataRowHoverOff">
            <th><div>${row.countKey}</div></th>
            <td><div>${row.countValue}</div></td>
        </tr>
    </k:foreach>
    </table>
</div>
<div class="box borderTop lightGrayBorder" style="width:24%; min-width:200px; padding:6px;">
    <b><k:message key="issueMgmt.index.countOpenIssueByAssignee"/></b>
    <p><table class="stats standard stripedListTable">
    <k:foreach id="row" name="assigneeCountList">
        <tr class="dataRowHoverOff">
            <th><div>${row.countKey}</div></th>
            <td><div>${row.countValue}</div></td>
        </tr>
    </k:foreach>
    </table>
</div>

<div style="clear:both"></div>
</k:equal>

<h3><k:message key="issueMgmt.index.searchHeader"/></h3>
<jsp:include page="/jsp/issues/IssueSearchTemplate.jsp"/>
