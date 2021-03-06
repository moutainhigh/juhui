﻿<%--

	gjfMemberDiviHistory.jsp

	Create by MCGT

	Create time 2017-01-20

	Version 1.0

	Copyright 2013 Messcat, Inc. All rights reserved.

 --%>
<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>模板</title>
		<%@ include file="/common/taglibs.jsp"%>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/content.jsp"%>
		<style>
			.headbg1 {
				background: #c9e6f5;
				font-weight: bold;
				padding-left: 15px;
			}
			
			.headbg1 td {
				font-weight: bold;
				text-align: center;
			}
		</style>

		<script>
		</script>
	</head>
	<body>
		<div class="rhead">
			<div class="rpos">
				当前位置: 模块 - 子模块
			</div>
			<!-- <div class="ropt">
				<a href="gjfMemberDiviHistoryAction!newPage.action">新增</a>
			</div> -->
			<div class="clear"></div>
		</div>
		
		<form action="tradeInfoAction!findMemberPointHistory.action" method="post">
			<table class="listTable2">
				<tr>
					<td>
						&nbsp;&nbsp;用户名称：<input type="text" placeholder="请输入用户名称" name="memberName" value="${memberName }"/>
						&nbsp;&nbsp;用户电话：<input type="text" placeholder="请输入用户电话" name="memberMobile" value="${memberMobile }"/>&nbsp;&nbsp;
						&nbsp;&nbsp;转移用户名称：<input type="text" placeholder="请输入转移用户名称" name="transferMemberName" value="${transferMemberName}"/>&nbsp;&nbsp;
						&nbsp;&nbsp;转移用户电话号码：<input type="text" placeholder="请输入转移用户电话号码" name="transferMemberMobile" value="${transferMemberMobile}"/>&nbsp;&nbsp;							                         
		                <input type="submit" style="margin-left:30px;" class="default_button" value='<s:text name="common.word.search" />' />
					</td>
				</tr>
			</table>
		</form>

		<table class="listTable3" onmouseover="changeto()" onmouseout="changeback()">
			<tr id="nc" class="headbg">
				<td>序号</td>
				<td>用户名称</td>
				<td>用户号码</td>
				<td>转移用户名称</td>
				<td>转移用户电话</td>
				<td>转移前积分</td>
				<td>交易积分</td>
				<td>转移后积分</td>
				
				<td>转移时间</td>
				<td>转移类型</td>				
			</tr>

			<c:forEach var="bean" items="${pager.resultList}"
				varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${bean.memberName}</td>
					<td>${bean.memberMobile}</td>
					<td>${bean.transferMemberName}</td>
					<td>${bean.transferMemberMobile}</td>
					<td>${bean.transferMemberDataBf}</td>
					<td>${bean.transferData}</td>
					<td>${bean.transferMemberDataAf}</td>
				
					<td><fmt:formatDate value="${bean.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<c:choose>
							<c:when test="${bean.transferType eq 0}">后台充值</c:when>
							<c:when test="${bean.transferType eq 1}">余额</c:when>
							<c:when test="${bean.transferType eq 2}">前端转移</c:when>
						</c:choose>
					</td>
					

					<%-- <td>
						<a href="gjfMemberDiviHistoryAction!viewPage.action?id=${bean.id}">查看</a>
						&nbsp; &nbsp;
						<a href="gjfMemberDiviHistoryAction!retrieveGjfMemberDiviHistoryById.action?id=${bean.id}">编辑</a>
						&nbsp; &nbsp;
						<a href="gjfMemberDiviHistoryAction!delGjfMemberDiviHistory.action?id=${bean.id}"
								onclick="{if(confirm('你真的要删除吗?')){return true;}return false;}">删除</a>
					</td> --%>
				</tr>
			</c:forEach>

		</table>

		<c:if test="${not empty pager.resultList}">
			<s:form action="tradeInfoAction!findMemberPointHistory.action"
					namespace="/subsystem" method="post" name="form1" theme="simple" id="form1">
				<!-- 分页 -->
				<%@ include file="../../common/pager.jsp"%>
			</s:form>
		</c:if>
	</body>
</html>