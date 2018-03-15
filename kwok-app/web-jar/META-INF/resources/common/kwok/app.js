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
var USER_STATUS_ENABLED = -1;
var URL_PARAM_AJAX = '_ajax';
var CONTENT_ELEM_ID = 'content';
var PAGE_LOADING_IMAGE = '<div class="image loadingContentImg">&nbsp;</div>';
var WIDGET_LOADING_IMAGE = '<span class="image loadingImg">&nbsp;</span>'; 
var NOTIFICATION_DELAY = 3000;
var CUSTOM_FIELD_EXPAND_COOKIE = 'customFieldsExpand';

var ATTR_TYPE_STRING = 1;
var ATTR_TYPE_MULTILINE = 2;
var ATTR_TYPE_SELECTBOX = 3;
var ATTR_TYPE_RADIO_BUTTON = 4;
var ATTR_TYPE_DATE = 5;
var ATTR_TYPE_MULTISELECT = 6;
var ATTR_TYPE_CURRENCY = 7;

function App() {}
	
//
// Jquery
//
App.delayFadeOut = function(object) {
    object.delay(NOTIFICATION_DELAY).fadeOut();
};

App.serializeForm = function(object) {
    object.serialize();
};

//
// JqueryUI
//
App.generateDatepicker = function(object, options) {
    object.datepicker(options);
};

//
// Common
//
/*
 * TODO:
 * This is for submiting a form when the user change selectbox value.
 * We'll be submitting to a different page according to formAction.
 */
function changeAction(thisObject, formAction) {
    thisObject.form.action = formAction;
    thisObject.form.submit();
}

App.changeSelectedOption = function(thisObject) {
    App.updateViewHistory(thisObject.form.action + '&' + thisObject.name + '=' + thisObject.value);
};

/*
 * Submits a form when the user change selectbox value.
 * This is different from changeSelectedOption where it invokes an ajax request instead.
 */
App.changeLocaleSelectedOption = function(thisObject) {
    thisObject.form.submit();
};

App.disableButton = function(buttonElem) {
    buttonElem.innerHTML = WIDGET_LOADING_IMAGE + buttonElem.innerHTML;
    buttonElem.disabled = true;
};

App.disableButtonSubmit = function(buttonElem) {
	var buttonDisabled = buttonElem.disabled;
    buttonElem.innerHTML = WIDGET_LOADING_IMAGE + buttonElem.innerHTML;
    if (!buttonDisabled) {
        buttonElem.disabled = true;
        buttonElem.form.submit();
    }
    return false;
};

App.highlightModuleTab = function(moduleIds, selectedModuleId) {
    for (var i = 0; i < moduleIds.length; i++) {
        var elem = document.getElementById('headerModule' + moduleIds[i]);
        if (elem != null) {
            if (moduleIds[i] == selectedModuleId) {
                elem.className = 'themeDarkBg themeBorder';
            } else {
                elem.className = 'themeHoverBg themeBorder';
            }
        }
    }
};

/*
 * App.invokeAjax cfg options:
 *  uri
 * 	callback
 * 	data: for post
 */
App.invokeAjax = function(cfg) {
    var ajax = Js.Ajax.newInstance();
    var request = ajax.request;

    // The appending of ?/& and ajax=true must happen here instead of in java code. Or ajax=true will be
    // pushed to the browser history too.
    cfg.uri += ((cfg.uri.indexOf('?') == -1) ? '?' : '&') + URL_PARAM_AJAX + '=true'

	request.onreadystatechange = function() {
        // Ready State:
        // 0 - Uninitialized
        // 1 - Loading
        // 2 - Loaded
        // 3 - Interactive
        // 4 - Completed
        if (request.readyState == 4) {
        	if (request.responseXML != null) { // We have the response in XML format, which is good.
        		result = request.responseXML.documentElement.getElementsByTagName('result')[0].firstChild.data;
        		
				// Invoke callback function.
    			cfg.callback(result);

        	} else {
            	if (request.status == 0) {
            		result = '<h2>' + SERVER_CONN_ERROR_HEADER + '</h2><div class="section">' + SERVER_CONN_ERROR_BODY + '</div>';
            	} else {
                	// This is not a valid response in XML format, may due to error in JSP.
                    result = request.responseText;
            	}

            	// We may get an ajax response (in the case of "Not Authorized", "Forbidden", etc).
              	Js.Element.setHtml(CONTENT_ELEM_ID, result);
        	}
        }
    };
    if (cfg.data != undefined) {
        ajax.post(cfg.uri, cfg.data);
    } else {
        ajax.get(cfg.uri);
    }
};

/*
 * Updates the element of given ID only instead of refreshing the whole page. 
 */
App.updateView = function(input, url, params) {
	var loadingWidget = input;
	/*
	 * var loadingWidget = {
	 * 		loadImage:,
	 * 		loadElemId:,
	 *	    targetElemId:
	 * };
	 */
	if (typeof input == 'string') {
	    loadingWidget = {loadImage: (input == CONTENT_ELEM_ID ? PAGE_LOADING_IMAGE : WIDGET_LOADING_IMAGE),
	    	    loadElemId: input,
	    	    targetElemId: input};
	}

	if (loadingWidget.loadElemId != null) {
		Js.Element.setHtml(loadingWidget.loadElemId, loadingWidget.loadImage);
    }

	var cfg = {
		uri: url,
        callback: function(result) {
    		//if (loadingWidget.loadElemId != null) {
            //    Js.Element.setHtml(loadingWidget.loadElemId, '');
            //}
        	
            // Replace div area content with result.
        	Js.Element.setHtml(loadingWidget.targetElemId, result);

            // Executes javascript embedded in ajax.
            var scriptElems = document.getElementById(loadingWidget.targetElemId).getElementsByTagName('script');
            for (var i=0; i<scriptElems.length; i++) {
            	// console.log(i + ': ' + scriptElems[i].innerHTML);
            	Js.globalEval(scriptElems[i].innerHTML);
            }
        },
        data: params
	}
	
	App.invokeAjax(cfg);
};

/*
 * Updates the element of given ID only instead of refreshing the whole page.
 */
App.updateViewHistory = function(url) {
    return Js.Response.updateHistory(url, function(){
        App.updateView(CONTENT_ELEM_ID, url);
    });
};

App.submitFormUpdate = function(formElem, cfg) {
    if (formElem.name == 'disabled') {
        return false;
    
    } else {
        // Setting form.action to empty to prevent this form being resubmitted again, e.g. by double-clicking.
        formElem.name = 'disabled';
    }
    
    if (cfg != undefined) {
        if (cfg.url != undefined) {
            formElem.action = cfg.url;
        }
        
        if (cfg.disable != undefined) {
        	App.disableButton(cfg.disable);
        }
    }
    
    var loadingWidget = {
            loadImage: null, 
            loadElemId: null, 
            targetElemId:CONTENT_ELEM_ID};

    App.updateView(loadingWidget, formElem.action, $('#' + formElem.id).serialize());
    
    // Return false to disable default form submit behavior.
    return false;
};

App.updateViewLazy = function(elemId, url) {
    if (Js.Element.isEmpty(elemId)) {
        App.updateView(elemId, url);
    }
};

App.toggleViewUpdate = function(elemId, url) {
    var isHidden = Js.Display.toggle(elemId);
    if (!isHidden) {
        App.updateView(elemId, url);
    }
};

App.toggleCustomFields = function(elemId, collapsedImage, expandedImage) {
	Js.Display.toggle(elemId, function(){
		if (document.getElementById('customFields').style.display == 'none') {
			Js.Cookies.setCookie(CUSTOM_FIELD_EXPAND_COOKIE, false, APP_PATH);
			document.getElementById('customFieldToggleImage').src = expandedImage; 
		} else {
			Js.Cookies.setCookie(CUSTOM_FIELD_EXPAND_COOKIE, true, APP_PATH);
			document.getElementById('customFieldToggleImage').src = collapsedImage;
		}
	});
};

//
// Admin module
//
/*
 * Shows browser size dynamatically. 
 */
App.browserSizeRefresh = function() {
	Js.Element.setHtml('browserSize', Js.Display.getVisibleWidth() + ' x ' + Js.Display.getVisibleHeight());
};

/*
 * This is for creating a new User. We want the display name field to reflect
 * what first name and last name are.
 */
App.refreshDisplayName = function(formFirstName, formLastName, formDisplayName) {
    Js.Form.setValue(formDisplayName, formFirstName.value + ' ' + formLastName.value);
};

/*
 * If user account status is enabled, password/confirm password fields are required, and vice versa. This script is
 * used to toggle the required field indicators "*".
 */
App.togglePasswordFields = function(status, formPassword, formConfirmPassword) {
    if (status == USER_STATUS_ENABLED) {
        Js.Visibility.show(formPassword);
        Js.Visibility.show(formConfirmPassword);
    } else {
        Js.Visibility.hide(formPassword);
        Js.Visibility.hide(formConfirmPassword);
    }
};

/**
 * Select all buttons for edit Permission
 * @param thisForm
 * @param list
 */
App.selectAllAccessItems = function(elem, perms) {
    for (var i=0; i<perms.length; i++) {
        var formFields = elem.form.elements['formAccess_' + perms[i]];
        for(j=0; j<formFields.length; j++){
            if (formFields[j].value == elem.value) {
                formFields[j].checked = true;
                break;
            }
        }
    }
};

App.updateAttrOptions = function(attrTypeId) {
	Js.Display.hide(['attrOptions', 'attrCurrencySymbol', 'convertUrl']);

    if (attrTypeId == ATTR_TYPE_STRING) {
    	Js.Display.show(['convertUrl']);
    } else if (attrTypeId == ATTR_TYPE_MULTILINE) {
    	Js.Display.show(['convertUrl']);
    } else if (attrTypeId == ATTR_TYPE_SELECTBOX) {
    	Js.Display.show(['attrOptions']);
    } else if (attrTypeId == ATTR_TYPE_RADIO_BUTTON) {
    	Js.Display.show(['attrOptions']);
    } else if (attrTypeId == ATTR_TYPE_DATE) {
    	// nothing to show
    } else if (attrTypeId == ATTR_TYPE_CURRENCY) {
    	Js.Display.show(['attrCurrencySymbol']);
    }
};

App.toggleImportType = function(importType) {
    if (importType == 'hardware_import') {
    	Js.Display.show('allowDuplicate');
    } else {
    	Js.Display.hide('allowDuplicate');
    }
};

App.dbBackup = function(url) {
	var loadingWidget = {
			loadImage:WIDGET_LOADING_IMAGE, 
			loadElemId:"backExecLoading", 
			targetElemId:CONTENT_ELEM_ID};

    App.updateView(loadingWidget, url);
};

//
// Auth module
//
App.focusLogin = function(formUsername, formPassword) {
    // If the email field is empty, move the cursor there.
    // If the user has a saved email address, move the cursor to the password field.
	if (formUsername.value == '' ) {
		formUsername.focus();
	} else {
		formPassword.focus();
	}
};

//
// Blogs module
//
/*
 * Allows HTML preview when composing a new post.
 */
App.refreshPostPreview = function(thisObject, id) {
	Js.Element.setHtml(id, Js.String.replace(Js.String.replace(thisObject.value,'\r\n','<br>'),'\n','<br>'));
};

//
// Hardware module
//
App.setWarrantyExpireYear = function(formName, year) {
    if (year == 0) {
        return;
    }

    document.forms[formName].warrantyMonth.value = document.forms[formName].purchaseMonth.value;
    document.forms[formName].warrantyDate.value = document.forms[formName].purchaseDate.value;

    var purchaseYear = document.forms[formName].purchaseYear.value;

    if (purchaseYear != 0) {
        purchaseYear = parseInt(purchaseYear) + parseInt(year);
    }
    document.forms[formName].warrantyYear.value = purchaseYear;
};

/*
 * Gets hardware details in-page popup.
 */
var hardwarePopupAjax = {
    divPos: 'right',
	popupDiv: '',
	url: '',
    input: '',
	show: function(thisObject, input) {
		var thisDiv = document.getElementById(this.popupDiv);
        if (!Js.Display.isHidden(thisDiv) && (this.input == input)) {
        	Js.Display.hide(thisDiv);
        } else {
            // Clears out old result
        	Js.Element.setHtml(thisDiv, '');
            // Keeps track of last input
            this.input = input;
            
        	var cfg = {
    			uri: this.url + input,
    	        callback: function(result) {
	                var topPdding = 20;
	                
	                // Replace div area content with result.
	                thisDiv.innerHTML = result;

	                // Shows div
	                Js.Display.show(thisDiv);

	                var top = Js.Element.getPositionTop(thisObject) - thisDiv.offsetHeight + topPdding;
	               	if (top < 0) {
	               		top = 0;
	               	}

	               	var left = Js.Element.getPositionLeft(thisObject) + 20;
	                
	                thisDiv.style.top = top + 'px';
	                thisDiv.style.left = left + 'px';
    	        }
        	}
            App.invokeAjax(cfg);
        }
    },
    // Clear previously values, especially when using ajax to navigate from one page to another.
    clearCache:function(){
        divPos = 'right';
        popupDiv = '';
        url = '', 
        input = '';
    }
};

//
// Issues module
//
App.toggleIssueDueDate = function(elem) {
    var disabled = (elem.value==1) ? false : true;
    
    elem.form.dueDateMonth.disabled = disabled;
    elem.form.dueDateDate.disabled = disabled;
    elem.form.dueDateYear.disabled = disabled;
};

App.issueDisplayHtml = function() {
    Js.Display.hide('issueText');
    Js.Display.show('issueHtml');
    Js.Element.setHtml('issueHtmlContent', Js.String.decodeHtml(document.getElementById('issueTextContent').innerHTML));
};

App.issueDisplayText = function() {
    Js.Display.show('issueText');
    Js.Display.hide('issueHtml');
};

//
// Reports module
//
App.checkReportTypeEnabled = function(reportTypeField) {
    reportTypeField.form.submitBtn.disabled = (reportTypeField.value == '');
};
