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

		<script type="text/javascript">
				function doYouWantToExport() {
					if(confirm('确定要导出所有记录吗?')) {
						return true;
					}
					return false;
				}
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
			 <div class="ropt"><a href="${ctx}/subsystem/tradeInfoAction!findFhTreasureTradeHistory.action?tradeType=${tradeType}&memberName=${memberName }&mobile=${mobile}&ste=1" onclick="return doYouWantToExport();">导出</a></div> 
			<div class="clear"></div>
		</div>
		
		<form action="tradeInfoAction!findFhTreasureTradeHistory.action" method="post">
			<table class="listTable2">
				<tr>
					<td>
						&nbsp;&nbsp;用户名称：<input type="text" placeholder="请输入用户名称" name="memberName" value="${memberName }"/>
						&nbsp;&nbsp;用户电话：<input type="text" placeholder="请输入用户电话" name="mobile" value="${mobile }"/>&nbsp;&nbsp;												                         
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
				<td>交易金额</td>
				<td>交易类型</td>
				<td>交易状态</td>
			
				<td>添加时间</td>	
				<!-- <td>操作</td>		 -->		
			</tr>

			<c:forEach var="bean" items="${pager.resultList}"
				varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${bean.memberName}</td>
					<td>${bean.memebrMobile}</td>
					<td>${bean.memberTreasureTradeMoney}</td>
					<td>
					  <c:if test="${bean.tradeType==0}">储值</c:if>
					  <c:if test="${bean.tradeType==1}">用户转移</c:if>
					  <c:if test="${bean.tradeType==2}">让利支付</c:if>
					  <c:if test="${bean.tradeType==3}">余额转入</c:if>
					  <c:if test="${bean.tradeType==4}">用户提现</c:if>
					  <c:if test="${bean.tradeType==5}">商城支付</c:if>
					  <c:if test="${bean.tradeType==6}">提现退回</c:if>
					  <c:if test="${bean.tradeType==7}">赠送代金券</c:if>
					  <c:if test="${bean.tradeType==8}">充值商家联盟</c:if>
					  <c:if test="${bean.tradeType==9}">赠送商家联盟</c:if>
					  <c:if test="${bean.tradeType==10}">奖励转入</c:if>
					  <c:if test="${bean.tradeType==11}">商城退款</c:if>
					</td>
					<td>					
					   <c:if test="${bean.tradeType==4}">
					       <c:if test="${bean.tradeStatus==0}">待审核</c:if>
                       </c:if>
					   <c:if test="${bean.tradeStatus==1}">交易成功</c:if>
					   <c:if test="${bean.tradeStatus==2}">交易失败</c:if>
					</td>					
					<td><fmt:formatDate value="${bean.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>									
					
				</tr>
			</c:forEach>

		</table>

		<c:if test="${not empty pager.resultList}">
			<s:form action="tradeInfoAction!findFhTreasureTradeHistory.action"
					namespace="/subsystem" method="post" name="form1" theme="simple" id="form1">
				<!-- 分页 -->
				<input type="hidden" name="memberName" value="${memberName }"/>
				<input type="hidden" name="mobile" value="${mobile }"/>
				<%@ include file="../../common/pager.jsp"%>
			</s:form>
		</c:if>
	</body>
</html>