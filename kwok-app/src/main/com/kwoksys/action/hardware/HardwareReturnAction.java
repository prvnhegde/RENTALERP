package com.kwoksys.action.hardware;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

public class HardwareReturnAction extends Action2 {
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception{
		System.out.println("Class:HardwareReturnAction :: Method:execute .. START");
	    AccessUser user = requestContext.getUser();
	    HardwareSearchForm actionForm = getSessionBaseForm(HardwareSearchForm.class);

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.HARDWARE_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.HARDWARE_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Getting search criteria map from session variable.
        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_SEARCH_CRITERIA_MAP);
        hardwareSearch.prepareMap(actionForm);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Hardware type filter
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_TYPE);
        List<LabelValueBean> hardwareTypeOptions = new ArrayList<>();
        hardwareTypeOptions.add(new SelectOneLabelValueBean(requestContext));

        QueryCriteria typeQuery = new QueryCriteria();
        typeQuery.addSortColumn(HardwareQueries.getOrderByColumn("attribute_field_name"));

        for (AttributeFieldCount hardware : hardwareService.getHardwareTypeCount(typeQuery)) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());

            String hardwareTypeName = attrField == null ? Localizer.getText(requestContext, "itMgmt.index.na") :
                    attrField.getName();

            hardwareTypeOptions.add(new LabelValueBean(hardwareTypeName, String.valueOf(hardware.getAttrFieldId())));
        }

        QueryCriteria query = new QueryCriteria(hardwareSearch);
        query.setLimit(rowLimit, rowStart);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(orderBy), order);
        }
        

        int rowCount = hardwareService.getHardwareReturnCount(query);
        List<DataRow> dataList = null;

        if (rowCount != 0) {
            List<Hardware> hardwareList = hardwareService.getHardwareReturnList(query);
            dataList = HardwareUtils.formatHardwareList(requestContext, hardwareList, new Counter(rowStart), AppPaths.HARDWARE_DETAIL);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        if (!hardwareSearch.getSearchCriteriaMap().isEmpty()) {
            standardTemplate.setAttribute("searchResultText", Localizer.getText(requestContext, "itMgmt.hardwareList.searchResult"));
        }
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_RETURN);
        standardTemplate.setAttribute("hardwareTypeOptions", hardwareTypeOptions);
        request.setAttribute("returnYearOptions", CalendarUtils.getYearOptions(requestContext));
        request.setAttribute("returnMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("returnDateOptions", CalendarUtils.getDateOptions(requestContext));

        request.setAttribute("hardwareTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.HARDWARE_TYPE));
        
        Calendar now = Calendar.getInstance();
        String dt = "";
        String mn = "";
        String yr = "";
        
        if(now.get(Calendar.DATE)<=9)
        	dt =  "0"+now.get(Calendar.DATE);
        else
        	dt = ""+now.get(Calendar.DATE);

        if((now.get(Calendar.MONTH) + 1)<=9)
        	mn =  "0"+(now.get(Calendar.MONTH) + 1);
        else
        	mn = ""+(now.get(Calendar.MONTH) + 1);

        yr = "" +now.get(Calendar.YEAR);
        
        standardTemplate.setAttribute("returnDate", dt);
        standardTemplate.setAttribute("returnMonth", mn);
        standardTemplate.setAttribute("returnYear", yr);
        
        
        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(HardwareUtils.getColumnHeaderList());
        tableTemplate.setSortableColumnHeaders(HardwareUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.HARDWARE_RENTAL_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");


        
        
        // Enable multiple rentals
        if (ConfigManager.app.isHardwareBulkDeleteEnabled()
                && Access.hasPermission(user, AppPaths.HARDWARE_RENTAL)) {
            tableTemplate.setFormName(HardwareSearchForm.class.getSimpleName());
            tableTemplate.setFormRemoveItemAction(AppPaths.HARDWARE_RETURN);
            tableTemplate.setFormRowIdName("hardwareIds");
            tableTemplate.setFormSelectMultipleRows(true);
            tableTemplate.getFormButtons().put("form.button.return", "common.form.confirmDelete");
        }
        
        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.rentalList.title");
        header.setTitleClassNoLine();
        
        // Link to add hardware.
        if (user.hasPermission(AppPaths.HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ADD);
            link.setTitleKey("itMgmt.cmd.hardwareAdd");
            header.addHeaderCmds(link);
        }
        
        // Link to Rental.
        if (user.hasPermission(AppPaths.HARDWARE_RENTAL_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_RENTAL_LIST + "?cmd=showAll");
            link.setTitleKey("itMgmt.cmd.rental");
            header.addHeaderCmds(link);
        }
        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.hardwareList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.HARDWARE_RENTAL_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.HARDWARE_RENTAL_LIST + "?rowStart=");

        System.out.println("Class:HardwareReturnAction :: Method:execute .. END");
        return standardTemplate.findTemplate(STANDARD_TEMPLATE);

	}
	
	public String returnHardware() throws Exception{
		System.out.println("Class:HardwareReturnAction :: Method:returnHardware .. START");
	    String[] strings = request.getParameterValues("hardwareIds");
        List<Integer> list = new ArrayList<>();
        if (strings != null) {
            for (String value : strings) {
                if (!value.isEmpty()) {
                    list.add(Integer.parseInt(value));
                }
            }
        }
        
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        
        for (Integer returnHardwareId : list) {
        	Integer rentalId = hardwareService.getHardwareReturn(returnHardwareId);
        	ActionMessages errors = hardwareService.hardwareReturn(rentalId);
        	
        	  if (!errors.isEmpty()) {
                  saveActionErrors(errors);
                  
                  /*if (bulkDeleteSelected) {
                      break;
                  } else {
                      return ajaxUpdateView(AppPaths.HARDWARE_DELETE + "?hardwareId=" + hardware.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
                  }*/
      
              } else {
                  /*if (hardware.getOwnerId() != 0) {
                      new CacheManager(requestContext).removeUserCache(hardware.getOwnerId());
                  }*/
              }
        }
		//return "success";
        System.out.println("Class:HardwareReturnAction :: Method:returnHardware .. END");
        return ajaxUpdateView(AppPaths.HARDWARE_LIST);
	}
}
