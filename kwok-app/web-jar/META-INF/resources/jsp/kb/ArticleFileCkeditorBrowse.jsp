<%--
 * Copyright 2017 Kwoksys
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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<jsp:include page="/jsp/common/template/HeaderSimple.jsp"/>

<script>
    // Simulate user action of selecting a file to be returned to CKEditor.
    function returnFileUrl() {
    	var paths = document.getElementsByName('filePath');
    	var fileUrl = '';
    	for (var i = 0; i < paths.length; i++) {
    	    if (paths[i].checked) {
    	    	fileUrl = paths[i].value;
    	    }
    	}
    	
        window.opener.CKEDITOR.tools.callFunction(${ckeditorFuncNum}, fileUrl);
        
        var dialog = window.opener.CKEDITOR.dialog.getCurrent();
        
        if (dialog.getContentElement('info', 'protocol') != undefined) {
            dialog.getContentElement('info', 'protocol').setValue('');
        }
        
        window.close();
    }
</script>

<div style="padding:6px">

<k:foreach id="file" name="files">

<k:define id="fileObj" name="file" property="file" type="com.kwoksys.biz.files.dto.File"/>
    <p><input type="radio" name="filePath" value="${file.filePath}">
    ${file.fileName}&nbsp;
    <k:write value="${fileObj.title}"/>
    (<k:write value="${file.filesize}"/>, <k:write value="${fileObj.creationDate}"/>)
</k:foreach>

<p><button type="button" onclick="returnFileUrl()"><k:message key="files.fileSelect"/></button>
</div>

