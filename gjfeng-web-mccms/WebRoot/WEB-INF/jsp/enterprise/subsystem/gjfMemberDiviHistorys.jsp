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
		<script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
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
				当前位置: 交易管理 - 分红交易记录
			</div>
			<!-- <div class="ropt">
				<a href="gjfMemberDiviHistoryAction!newPage.action">新增</a>
			</div> -->
			<div class="clear"></div>
		</div>
		
		<div>
			<table class="listTable3">
				<tr id="nc" class="headbg" style="background-color: #1F6D9E;height: 50px;">
		  	 	   <td colspan="4" style="cl;font-size: 18px">
		  	 	   		<font color="#FFFFFF">当前条件统计</font>
		 	  	   </td>
			   </tr>
		  		<tr id="nc" class="headbg" style="background-color: #1F6D9E;height: 50px;">
		  	 	   <td colspan="2" style="cl;font-size: 18px">
		  	 	   		<font color="#FFFFFF">交易金额：${object.totalTradeMoney }</font>
		 	  	   </td>
		 	  	     <td colspan="2" style="cl;font-size: 18px">
		  	 	   		<font color="#FFFFFF">交易分红权数：${object.totalTradeDiviNum }</font>
		 	  	   </td>
			   </tr>
			</table>
		</div>
		<form action="tradeInfoAction!retrieveDiviHistoryByPager.action" method="post">
			<table class="listTable2">
				<tr>
					<td>
						&nbsp;&nbsp;用户名称：<input type="text" placeholder="请输入用户名称" name="name" value="${name }"/>&nbsp;&nbsp;
						流水号：<input type="text" placeholder="请输入流水号" name="tradeNo" value="${tradeNo }"/>&nbsp;&nbsp;
						开始时间：<input type="text" placeholder="请选择日期" name="addTime" value="${addTime }" onclick="WdatePicker()" style="width: 75px;"/>&nbsp;&nbsp;
						-&nbsp;&nbsp;
						结束时间：<input type="text" placeholder="请选择日期" name="endTime" value="${endTime }"  onclick="WdatePicker()" style="width: 75px;"/>&nbsp;&nbsp;
						交易类型：
							 <select name="tradeType" id="tradeType">
							  <option value="" <c:if test="${empty tradeType}">selected="selected"</c:if>>全部</option>
							    <option value="0" <c:if test="${tradeType eq '0'}">selected="selected"</c:if>>直推会员分红 </option>
						     	<option value="1" <c:if test="${tradeType eq '1'}">selected="selected"</c:if>>直推商家分红</option>
						     	<option value="2" <c:if test="${tradeType eq '2'}">selected="selected"</c:if>>普通用户分红</option>
						     	<option value="3" <c:if test="${tradeType eq '3'}">selected="selected"</c:if>>普通商家分红</option>
						     	<option value="4" <c:if test="${tradeType eq '4'}">selected="selected"</c:if>>市代分红</option>
						     	<option value="5" <c:if test="${tradeType eq '5'}">selected="selected"</c:if>>区代分红</option>
						     	<option value="6" <c:if test="${tradeType eq '6'}">selected="selected"</c:if>>个代分红</option>
				              </select>&nbsp;&nbsp;
				              <br />
						&nbsp;&nbsp;交易状态：
						 <select name="tradeStatus" id="tradeStatus">
							 	<option value="" <c:if test="${empty tradeStatus}">selected="selected"</c:if>>全部</option>
					   			<option value="0" <c:if test="${tradeStatus eq '0'}">selected="selected"</c:if>>提交中</option>
				  				<option value="1" <c:if test="${tradeStatus eq '1'}">selected="selected"</c:if>>交易成功</option>
				 				<option value="2" <c:if test="${tradeStatus eq '2'}">selected="selected"</c:if>>交易失败</option>
					 			
		                 </select>&nbsp;&nbsp;
		                         
		                <input type="submit" style="margin-left:30px;" class="default_button" value='<s:text name="common.word.search" />' />
					</td>
				</tr>
			</table>
		</form>
		<table class="listTable3" onmouseover="changeto()" onmouseout="changeback()">
			<tr id="nc" class="headbg">
				<td>序号</td>
				<td>用户</td>
				<td>分红交易流水号</td>
				<td>交易金额</td>
				<td>二类交易金额</td>
				<td>三类交易金额</td>				
				<td>交易比例</td>
				<td>二类交易比例</td>
				<td>三类类交易比例</td>
				<td>添加时间</td>
				<td>交易类型</td>
				<td>交易状态</td>
				<td>交易附言</td>
			</tr>

			<c:forEach var="bean" items="${pager.resultList}"
				varStatus="status">
				<tr>	
					<td>${status.count}</td>
					<td>${bean.name}</td>
					<td>${bean.tradeNo}</td>
					<td>${bean.tradeMoney}</td>
					<td>${bean.tradeSecondMoney}</td>
					<td>${bean.tradeThreeMoney}</td>
					
					
					
					<td>
						<c:choose>
							<c:when test="${bean.tradeType eq 0}">${bean.tradeRatio}</c:when>
							<c:when test="${bean.tradeType eq 1}">${bean.tradeRatio}</c:when>
							<c:when test="${bean.tradeType eq 2}">${bean.tradeUnit}</c:when>
							<c:when test="${bean.tradeType eq 3}">${bean.tradeUnit}</c:when>
							<c:when test="${bean.tradeType eq 4}">${bean.tradeRatio}</c:when>
							<c:when test="${bean.tradeType eq 5}">${bean.tradeRatio}</c:when>
							<c:when test="${bean.tradeType eq 6}">${bean.tradeRatio}</c:when>
						</c:choose>
						
					</td>
					
					<td>${bean.tradeSecondUnit}</td>
					<td>${bean.tradeThreeUnit}</td>
					<td><fmt:formatDate value="${bean.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<c:choose>
							<c:when test="${bean.tradeType eq 0}">直推会员分红</c:when>
							<c:when test="${bean.tradeType eq 1}">直推商家分红</c:when>
							<c:when test="${bean.tradeType eq 2}">普通用户分红</c:when>
							<c:when test="${bean.tradeType eq 3}">普通商家分红</c:when>
							<c:when test="${bean.tradeType eq 4}">市代分红</c:when>
							<c:when test="${bean.tradeType eq 5}">区代分红</c:when>
							<c:when test="${bean.tradeType eq 6}">个代分红</c:when>
						</c:choose>
						
					</td>
					<td>
						<c:choose>
							<c:when test="${bean.tradeStatus eq 0}">提交中</c:when>
							<c:when test="${bean.tradeStatus eq 1}">交易成功</c:when>
							<c:when test="${bean.tradeStatus eq 2}">交易失败</c:when>
						</c:choose>
					</td>
					<td>${bean.tradeTrmo}</td>

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
			<s:form action="tradeInfoAction!retrieveDiviHistoryByPager.action"
					namespace="/subsystem" method="post" name="form1" theme="simple" id="form1">
					<input type="hidden" name="name" value="${name }"/>
					<input type="hidden"  name="tradeNo" value="${tradeNo }"/>
					<input type="hidden"  name="addTime" value="${addTime }"/>
					<input type="hidden" name="endTime" value="${endTime }"/>
					<select name="tradeType" style="display: none;">
							  <option value="" <c:if test="${empty tradeType}">selected="selected"</c:if>>全部</option>
							    <option value="0" <c:if test="${tradeType eq '0'}">selected="selected"</c:if>>直推会员分红 </option>
						     	<option value="1" <c:if test="${tradeType eq '1'}">selected="selected"</c:if>>直推商家分红权</option>
						     	<option value="2" <c:if test="${tradeType eq '2'}">selected="selected"</c:if>>普通用户分红权</option>
						     	<option value="3" <c:if test="${tradeType eq '3'}">selected="selected"</c:if>>普通商家分红权</option>
						     	<option value="4" <c:if test="${tradeType eq '4'}">selected="selected"</c:if>>市代分红权</option>
						     	<option value="5" <c:if test="${tradeType eq '5'}">selected="selected"</c:if>>区代分红权</option>
						     	<option value="6" <c:if test="${tradeType eq '6'}">selected="selected"</c:if>>个代分红权</option>
				              </select>
					<select name="tradeStatus" style="display: none;">
							 	<option value="" <c:if test="${empty tradeStatus}">selected="selected"</c:if>>全部</option>
					   			<option value="0" <c:if test="${tradeStatus eq '0'}">selected="selected"</c:if>>提交中</option>
				  				<option value="1" <c:if test="${tradeStatus eq '1'}">selected="selected"</c:if>>交易成功</option>
				 				<option value="2" <c:if test="${tradeStatus eq '2'}">selected="selected"</c:if>>交易失败</option>
					 			
		            </select>
				<!-- 分页 -->
				<%@ include file="../../common/pager.jsp"%>
			</s:form>
		</c:if>
	</body>
</html>