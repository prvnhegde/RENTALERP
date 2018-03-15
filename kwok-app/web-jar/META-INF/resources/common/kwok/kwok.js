
var Js = new _Js();

function _Js() {
	this.Ajax = _Ajax;
	this.Cookies = _Cookies;
	this.Display = _Display;
	this.Element = _Element;
	this.Form = _Form;
	this.Response = _Response;
	this.String = _String;
	this.Visibility = _Visibility;
	this.Modal = _Modal;
}

Js.globalEval = function(object) {
	// Example from https://community.oracle.com/blogs/driscoll/2009/09/08/eval-javascript-global-context
	if (!Js.String.isEmpty(object)) {
	    if (window.execScript) {
	        window.execScript(object);
 	    } else {
 		    window.eval.call(window, object);
        }
	}
}

//
// _Ajax
//
function _Ajax() {
    this.http = new XMLHttpRequest();
    this.request = this.http;
    this.get = function(url) {
        this.http.open("GET", url, true);
        this.http.send(null);
    }
    this.post = function(url, params) {
        this.http.open("POST", url, true);
        this.http.setRequestHeader("charset", "utf-8");
        this.http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        //this.http.setRequestHeader("Content-length", params.length);
        //this.http.setRequestHeader("Connection", "close");
        this.http.send(params);
    }
}

_Ajax.newInstance = function() {
    return new _Ajax();
}

//
// _Element
//
function _Element() {}

_Element.isEmpty = function(elemId) {
    return _String.isEmpty(document.getElementById(elemId).innerHTML);
}

_Element.setHtml = function(obj, string) {
	if (typeof obj == 'string') {
		document.getElementById(obj).innerHTML = string;
	} else {
		obj.innerHTML = string;
	}
}

_Element.setSrc = function(elemId, src) {
	document.getElementById(elemId).src = src;
}

_Element.getPositionTop = function(elem) {
    var offset = elem.offsetTop;
    
    var parentElem = elem.offsetParent;
    
    while (parentElem != null) {
    	offset += parentElem.offsetTop;
        parentElem = parentElem.offsetParent;
    }
    
	return offset;
}

_Element.getPositionLeft = function(elem) {
    var offset = elem.offsetLeft;
    
    var parentElem = elem.offsetParent;
    
    while (parentElem != null) {
    	offset += parentElem.offsetLeft;
        parentElem = parentElem.offsetParent;
    }
    
	return offset;
}

//
// _String
//
function _String() {}

_String.isEmpty = function(string) {
	return !string || string.replace(/^\s+|\s+$/g,"") == '';
}

_String.decodeHtml = function(string) {
    var div = document.createElement('div');
    div.innerHTML = string;
    return div.textContent;
}

/*
 * Replaces replaceThis with replacedBy in input
 */
_String.replace = function(input, replaceThis, replacedBy) {
    var strLength = input.length;
	var txtLength = replaceThis.length;

    if ((strLength == 0) || (txtLength == 0)) {
		return input;
	}
    var i = input.indexOf(replaceThis);
    if ((!i) && (replaceThis != input.substring(0,txtLength))) {
		return input;
	}
    if (i == -1) {
		return input;
	}

    var newString = input.substring(0,i) + replacedBy;

    if (i+txtLength < strLength) {
        newString += _String.replace(input.substring(i+txtLength,strLength),replaceThis,replacedBy);
	}
    return newString;
}

//
// _Display
//
function _Display() {}

_Display.isHidden = function(elem) {
	return elem.style.display == 'none';
}

/*
 * Set style.display to ''.
 */
_Display.show = function(obj) {
	if (typeof obj == 'string') {
		obj = document.getElementById(obj);
	}
	if (!obj.length) {
		obj = [obj];
	}
    for (var i = 0; i < obj.length; i++) {
    	var thisObj = obj[i];
    	if (typeof thisObj == 'string') {
    		thisObj = document.getElementById(thisObj);
    	}
    	
    	if (thisObj.tagName == 'TR') {
    		thisObj.style.display = 'table-row';
    	} else {
    		thisObj.style.display = 'inline';
    	}
    }
}

/*
 * Set style.display to 'none'.
 */
_Display.hide = function(obj) {
	if (typeof obj == 'string') {
		obj = document.getElementById(obj);
	}
	if (!obj.length) {
		obj = [obj];
	}
    for (var i = 0; i < obj.length; i++) {
    	var thisObj = obj[i];
    	if (typeof thisObj == 'string') {
    		thisObj = document.getElementById(thisObj);
    	}
    	
  		thisObj.style.display = 'none';
    }
}

_Display.toggle = function(elemId, callback) {
    var elem = document.getElementById(elemId);

    var isHidden = _Display.isHidden(elem);
    
    if (isHidden) {
    	_Display.show(elem);
        isHidden = false;
	} else {
		_Display.hide(elem);
	}
    
    // Optional callback.
    if (callback != null) {
    	callback();	
    }

    return isHidden;
}

_Display.getWidth = function() {
    var width = 0;

    if (typeof document.width !== 'undefined') {
        width = document.width; // For webkit browsers
    } else {
        width = Math.max(document.body.scrollWidth, document.body.offsetWidth, document.documentElement.clientWidth,
                document.documentElement.scrollWidth, document.documentElement.offsetWidth);
    }

    return width;
}

_Display.getHeight = function() {
    var height = 0;

    if (typeof document.height !== 'undefined') {
        height = document.height; // For webkit browsers
    } else {
        height = Math.max(document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight,
            document.documentElement.scrollHeight, document.documentElement.offsetHeight);
    }

    return height;
}

_Display.getVisibleWidth = function() {
	return window.innerWidth;
}

_Display.getVisibleHeight = function() {
	return window.innerHeight;
}

function _Visibility() {}

/*
 * Sets style.visibility to 'visible'.
 */
_Visibility.show = function(obj) {
	if (typeof obj == 'string') {
		obj = document.getElementById(obj);
	}
	obj = obj.style.visibility='visible';
}

/*
 * Sets style.visibility to 'hidden'.
 */
_Visibility.hide = function(obj) {
	if (typeof obj == 'string') {
		obj = document.getElementById(obj);
	}
	obj = obj.style.visibility='hidden';
}

//
// _Form
//
function _Form() {}

_Form.setValue = function(formField, value) {
    formField.value = value;
}

_Form.checkAll = function(formField, items) {
    for (i = 0; i < items.length; i++){
        items[i].checked = formField.checked;
    }
}

/*
 * Select all selectbox options.
 */
_Form.selectAllOptions = function(items) {
    for (i = 0; i < items.options.length; i++) {
    	items.options[i].selected = true;
    }
}

_Form.moveOptions = function(fromList, toList) {
	for (var i = 0; i < fromList.options.length; i++) {
		var current = fromList.options[i];
		if (current.selected) {
			toList.options[toList.length] = new Option(current.text, current.value);
			fromList.options[i] = null;
			i--;
		}
	}
}

//
// _Response
//
function _Response() {}

/*
 * Invokes confirm message, mostly for form submit.
 */
_Response.confirm = function(message) {
    return confirm(message);
}

_Response.log = function(message) {
    return console.log(message);
}

_Response.redirect = function(url) {
	document.location.href = url;
}

/*
 * Updates URL in address bar using window.history API if supported.
 */
_Response.updateHistory = function(url, callback) {
    if (history && history.pushState) {
        window.history.pushState(null, null, url);

        window.onpopstate = function(stackstate) {
            location.reload();
        };

        callback();
    } else {
        // For browsers not supporting pushState().
        Js.Response.redirect(url);
    }
    return false;
}

//
// _Cookies
//
function _Cookies() {}

_Cookies.setCookie = function(cname, cvalue, cpath) {
	document.cookie = cname + '=' + cvalue + '; path=' + cpath;
}

_Cookies.getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

//
//_Modal
//
function _Modal() {
    this.elemId = 'modal';
    this.render = function(formElem, callback) {
        // Register submit.
        document.getElementById(this.submitBtnId).onclick = function() {
            if (callback == undefined) {
                formElem.form.submit();
            } else {
                callback(formElem.form);
                _Display.hide('modal');
            }
        };
        document.getElementById(this.elemId).onclick = function(event) {
            // Conditions to hide modal.
            if (event.target.id == this.id || event.target.id == this.id + '-close' || event.target.id == this.id + '-btn-close') {
                _Display.hide(this.id);
            }
        };
        _Display.show(this.elemId); 
    };
    this.setBody = function(bodyText) {
    	_Element.setHtml(this.elemId + '-body', bodyText); 
        return this;
    };
    this.submitBtnId = this.elemId + '-btn-submit';
}

_Modal.newInstance = function() {
    return new _Modal();
}
