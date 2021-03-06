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
			$(document).ready(function(){
				$("#SHOP").click(function(){
					var id = $("#id").val();
					var token = $("#token").val();
					window.location.href="memberInfoAction!findCumulativeMoneyById.action?op=0&id="+id+"&token="+token;
				});
			
			});
		</script>
	</head>
	<body>
		<div class="rhead">
			<div class="rpos">
				当前位置: O2O消费流水
			</div>
				<input type="button" class="defaultButton" id="SHOP" value="网上商城消费流水" style="margin-left: 20px;"/>
			<div class="ropt">
				<input type="button" class="defaultButton" value="返回" onclick="history.back();"/>
			</div>
		</div>					
		<%-- <form action="countInfoAction!storeCreditLine.action" method="post" name="searchForm" id="searchForm">
			<table class="listTable2">
				<tr>
					<td class="pn-flabel" style="text-align:left;padding-left:15px;">
					 商户编号：<input type="text" name="storeNum" value="${storeNum }"/>&nbsp;&nbsp;
					 商户名称：<input type="text" name="storeName" value="${storeName }"/>&nbsp;&nbsp;
					 用户名称：<input type="text" name="memberName" value="${memberName }"/>&nbsp;&nbsp;
					 <input type="submit" style="margin-left:30px;" class="default_button" value='<s:text name="common.word.search" />' />
					 <input type="hidden" name="orderType" id="orderType" value="${orderType }"/>&nbsp;&nbsp;
					 <input type="button" class="defaultButton" id="sequence" value="顺序"/>
					 <input type="button" class="defaultButton" id="reverse" value="倒序"/>
					</td>
				</tr>
			</table>
		</form> --%>
		<input type="hidden" name="id" id="id" value="${id }"/>
		<input type="hidden" name="token" id="token" value="${token }"/>
		<table class="listTable3" onmouseover="changeto()" onmouseout="changeback()">
			<tr id="nc" class="headbg">
				<td>序号</td>
				<td>交易流水号</td>
				<td>商家</td>
				<td>消费金额</td>
				<td>添加时间</td>
				<td>支付类型</td>
				<td>交易状态</td>
			</tr>
			 
			 <c:forEach var="bean" items="${pager.resultList}"
				varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${bean.tradeNo}</td>
					<td>${bean.storeId.storeName }</td>
					<td>${bean.tradeMoney }</td>
					<td><fmt:formatDate value="${bean.addTime }" pattern="yyyy-MM-dd"/></td>
					<td>
						<c:choose>
							<c:when test="${bean.payType eq '0'}">微信支付</c:when>
							<c:when test="${bean.payType eq '1'}">支付宝支付</c:when>
							<c:when test="${bean.payType eq '2'}">银联支付</c:when>
							<c:when test="${bean.payType eq '4'}">授信金额支付</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${bean.tradeStatus eq '0'}">待支付</c:when>
							<c:when test="${bean.tradeStatus eq '1'}">已支付</c:when>
							<c:when test="${bean.tradeStatus eq '2'}">支付失败</c:when>
							<c:when test="${bean.tradeStatus eq '3'}">取消</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>

		<c:if test="${not empty pager.resultList}">
			<s:form action="memberInfoAction!findCumulativeMoneyById.action"
					namespace="/subsystem" method="post" name="form1" theme="simple" id="form1">
					<input type="hidden" name="op"  value="${op }"/>
					<input type="hidden" name="id"  value="${id }"/>
					<input type="hidden" name="token"  value="${token }"/>
				<!-- 分页 -->
				<%@ include file="../../common/pager.jsp"%>
			</s:form>
		</c:if>
	</body>
</html>