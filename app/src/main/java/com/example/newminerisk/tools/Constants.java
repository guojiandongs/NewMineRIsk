package com.example.newminerisk.tools;

import android.content.Context;

public interface Constants {

	String LoginState = "LoginState";
	String UserInfo = "UserInfo";
	String EmployeeList = "EmployeeList";
	String OfLineEmployeeList = "OfLineEmployeeList";
	String NAME = "NAME";
	String PWD = "PWD";
	String ADDHIDDENRECORD = "addhiddenRecord";
	String ADDRECHECK = "addrecheck";
	String ADDPIC = "addPic";
	String CARDRECORD = "cardRecord";
	// JSON status
	String Info = "content";
	String Value = "data";
	String Result = "type";
	String DB_NAME = "WeChat.db";
	String NET_ERROR = "当前为脱机状态！";
	String SAVE_DATA = "网络错误，数据已保存，有网时自动上报！";
	String PAGE = "1";
	String ROWS = "10";
	String ISUPERVISION = "1";
	public static final int TYPE_ITEM = 0;
	public static final int TYPE_FOOTER = 1;

	// 外网地址
	String MAIN_ENGINE = "http://1.63.57.10:18070/group/";

	// 内网地址
	String INNER_MAIN_ENGINE = "123123";
	// 用户登录
	String Login_URL = "mobile/employeeMobileAction!employeeLogin.action";
	// 获取版本号和下载地址
	String UPDATE_VERSION = "mobile/employeeMobileAction!changeVersion.action";
	// 获取所有框列表
	String GET_COLLIERY_LIST = "mobile/collieryMobileAction!getCollieryList.action?companyId=1";
	// 获取总局隐患
	String GET_GROUP_COUNT_ = "mobile/countGroupMobileAction!getGroupCount.action";
	// 获取总局隐患等级
	String GET_GROUP_COUNT_JB = "mobile/countGroupMobileAction!getGroupCountJb.action";
	// 获取隐患分矿统计信息
	String GET_GROUP_RECORD_COUNT = "mobile/countGroupMobileAction!getGroupRecordCount.action";
	// 获取隐患分矿重大统计信息
	String GET_GROUP_IMPORTANT_RECORD_COUNT = "mobile/countGroupMobileAction!getGroupImportantRecordCount.action";
	// 获取单个矿区隐患统计信息
	String GET_KUANGQU_COUNT_GROUP = "mobile/countGroupMobileAction!getKuangQuCountGroup.action";
	// 获取单个矿区级别统计信息
	String GET_RECORD_COUNTNUMBYJB = "mobile/countGroupMobileAction!getRecordCountNumByJb.action";
	// 获取矿区隐患专业统计信息
	String GET_RECORD_COUNTNUMBYSID = "mobile/countGroupMobileAction!getRecordCountNumBySid.action";
	// 获取矿区隐患类型统计信息
	String GET_RECORD_COUNTNUMBYTID = "mobile/countGroupMobileAction!getRecordCountNumByTid.action";
	// 获取隐患总数分页信息列表
	String GET_HIDDENDANGER_RECORD_LISTTOTAL = "mobile/countGroupMobileAction!getHiddenDangerRecordListTotal.action";
	// 获取重大隐患总数分页信息列表
	String GET_HIDDENDANGER_RECORD_LIST_IMPORTANT = "mobile/countGroupMobileAction!getHiddenDangerRecordListImportant.action";
	// 获取督办隐患总数分页信息列表
	String GET_HIDDENDANGER_RECORD_LIST_SUPPER = "mobile/countGroupMobileAction!getHiddenDangerRecordListSupper.action";
	// 获取完成隐患总数分页信息列表
	String GET_HIDDENDANGER_RECORD_LIST_CLOSE = "mobile/countGroupMobileAction!getHiddenDangerRecordListClose.action";
	// 获取未完成隐患总数分页信息列表
	String GET_HIDDENDANGER_RECORD_LIST_OPEN = "mobile/countGroupMobileAction!getHiddenDangerRecordListOpen.action";
	// 获取总局风险
	String GET_GROUP_RISK_COUNT_ = "mobile/groupIdentificationMobileAction!getGroupIdentificationCount.action";
	// 获取重大风险数量统计
	String GET_GROUP_IMPORTANT_RECORD_RISK_COUNT = "mobile/groupIdentificationMobileAction!getGroupImportantIdentificCount.action";
	// 总局-隐患排查列表统计
	String GET_GROUP_IMPORTANT_COUNT_BYKUANG = "mobile/countGroupMobileAction!getGroupCountByKuang.action";
	// 总局-风险管控统计
	String get_Group_Count_MonthTypeNum = "mobile/groupIdentificationMobileAction!getGroupCountMonthTypeNum.action";
	// 总局-月度各矿管理统计
	String get_roup_Count_MonthNum_ByKuang = "mobile/groupIdentificationMobileAction!getGroupCountMonthNumByKuang.action";
	// 各个矿-风险统计信息
	String GET_KUANG_IDENTIFIC_COUNT = "mobile/groupIdentificationMobileAction!getKuangIdentificCount.action";
	// 各个矿-重大风险列表
	String get_KuangImp_Identific_List = "mobile/groupIdentificationMobileAction!getKuangImpIdentificList.action";
	// 各个矿-风险管控统计
	String GET_IDENTIFIC_LIST_BYOPENTYPE = "mobile/groupIdentificationMobileAction!getIdentificListByopenType.action";
	// 各个矿-获取当月管控风险统计列表(当月管控列表)
	String GET_YEARORZHUANG_BILITYIDENTIF_LIST = "mobile/groupIdentificationMobileAction!getYearOrZhuangbilityIdentifList.action";
	// 各个矿-获取年度、专项管风险统计列表
	String get_IdentificMonth_List = "mobile/groupIdentificationMobileAction!getIdentificMonthList.action";
}
