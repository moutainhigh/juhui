﻿<%--

	gjfBankInfo_edit.jsp

	Create by MCGT

	Create time 2017-01-10

	Version 1.0

	Copyright 2013 Messcat, Inc. All rights reserved.

 --%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/content.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>更新模板</title>
		<script>
			$(document).ready(function(){
				$("#gjfBankInfoForm").validate({
					 rules: { 
			
					},
					success: function(label) {
						label.html("&nbsp;").addClass("checked");
			        }	
				});
				
			});		
		</script>
	</head>
	<body>
		<div class="rhead">
			<div class="rpos">当前位置: 模块 - 子模块</div>
			<div class="ropt"><input type="button" class="defaultButton" value="返回" onclick="location.href='${ctx}/subsystem/gjfBankInfoAction!retrieveAllGjfBankInfos.action'"/></div>
		</div>

		<form action="gjfBankInfoAction!editGjfBankInfo.action" method="post" id="gjfBankInfoForm" name="gjfBankInfoForm" enctype="multipart/form-data">
			<table  align="center" class="listTable3">
				<input type="hidden" name="id" value="${gjfBankInfo.id}"/>
				<tr>
					<td class="pn-flabel" width="100px">银行代码</td>
					<td><input type="text" id="bankCode" name="gjfBankInfo.bankCode" value="${gjfBankInfo.bankCode}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">银行名称</td>
					<td><input type="text" id="bankName" name="gjfBankInfo.bankName" value="${gjfBankInfo.bankName}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">银行图片</td>
					<td><input type="text" id="bankPic" name="gjfBankInfo.bankPic" value="${gjfBankInfo.bankPic}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">银行url</td>
					<td><input type="text" id="bankUrl" name="gjfBankInfo.bankUrl" value="${gjfBankInfo.bankUrl}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">银行颜色</td>
					<td><input type="text" id="bankColor" name="gjfBankInfo.bankColor" value="${gjfBankInfo.bankColor}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">排序</td>
					<td><input type="text" id="orderBy" name="gjfBankInfo.orderBy" value="${gjfBankInfo.orderBy}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">添加时间</td>
					<td><input type="text" id="addTime" name="gjfBankInfo.addTime" value="${gjfBankInfo.addTime}" /></td>
				</tr>
				<tr>
					<td class="pn-flabel" width="100px">状态</td>
					<td><input type="text" id="status" name="gjfBankInfo.status" value="${gjfBankInfo.status}" /></td>
				</tr>

				<tr>
					<td></td>
					<td>
						<input type="submit" class="defaultButton" value=" 提 交 "/>
					</td>
				</tr>
		 	</table>
		</form>
	</body>
</html>