<%@page import="cc.modules.util.DateHelper"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%-- <%@ page contentType="application/vnd.ms-excel;charset=gbk"%> --%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	//HttpServletResponse response = ServletActionContext.getResponse();
	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 	String fileName="订单列表信息.xls";
	response.setContentType("application/vnd.ms-excel;charset=UTF-8");
   	response.setHeader("Content-disposition","attachment; filename= "+ new String( fileName.getBytes("gb2312"), "ISO8859-1")+"");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>订单列表信息</title>
		<style>
			.headbg {/*background: #c9e6f5;*/font-weight: bold;padding-left: 15px;height: 30px;}
			.txt{mso-number-format:"\@";}
			.headbg td {font-weight: bold;/* text-align: left; */}
			.footbg td{font-weight: bold;text-align: left;}
			.footbg1 td{text-align: left;}
		</style>
	</head>
	<body>
	<table class="listTable3" onmouseover="changeto()" onmouseout="changeback()" border="1">
			<thead>
								<tr>
									<td>订单号</td>
									<td>时间</td>
									<td>用户名</td>
									<td>用户昵称</td>
									<td>店铺名称</td>
									<td>商品名称</td>
									<td>商品属性</td>
									<td>购买数量</td>
									<td>商品单价格</td>
									<td>线上支付金额</td>
									<td>线下支付金额</td>
									<td>支付方式</td>
									<td>订单类型</td>
									<td>订单状态</td>
									<td>收货人姓名</td>
									<td>收货人联系号码</td>
									<td>省市区</td>
									<td>详细地址</td>
								</tr>
			</thead>
			      <c:forEach var="bean" items="${pager.resultList}" varStatus="status">
						<tr>
									<td>'${bean.orderSn}</td>
									<td style="vnd.ms-excel.numberformat:yyyy-mm-dd HH:mm:ss"><fmt:formatDate value="${bean.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${bean.memName}</td>
									<td>${bean.nickName}</td>
									<td>${bean.storeName}</td>
									<td>${bean.goosName}</td>
									<td>${bean.goodAttr}</td>
									<td>${bean.goodNum}</td>
									<td>${bean.goodsAmount}</td>
									<td>${bean.onlinePayAmount}</td>
									<td>${bean.offlinePayAmount}</td>
									<c:if test="${bean.payType eq '0'}">
									    <td>线上余额</td>
									</c:if>
									<c:if test="${bean.payType eq '1'}">
									    <td>微信支付</td>
									</c:if>
									<c:if test="${bean.payType eq '2'}">
									    <td>支付宝</td>
									</c:if>
									<c:if test="${bean.payType eq '3'}">
									    <td>银联支付</td>
									</c:if>
									<c:if test="${bean.payType eq '4'}">
									    <td>余额+微信</td>
									</c:if>
									<c:if test="${bean.payType eq '5'}">
									    <td>余额+支付宝</td>
									</c:if>
									<c:if test="${bean.payType eq '6'}">
									    <td>余额+银联</td>
									</c:if>
									<c:if test="${bean.payType eq '7'}">
									    <td>待领消费金额</td>
									</c:if>
									<c:if test="${bean.payType eq '8'}">
									    <td>责任消费金额</td>
									</c:if>
									<c:if test="${bean.payType eq '9'}">
									    <td>天天宝</td>
									</c:if>
									<c:if test="${bean.payType eq '10'}">
									    <td>代金券</td>
									</c:if>
									
									<c:if test="${bean.orderType eq '0'}">
									    <td>o2o</td>
									</c:if>
									<c:if test="${bean.orderType eq '1'}">
									    <td>商城</td>
									</c:if>
									
									<c:if test="${bean.orderStatus eq '0'}">
									    <td>未付款</td>
									</c:if>
									<c:if test="${bean.orderStatus eq '1'}">
									    <td>已付款</td>
									</c:if>
									<c:if test="${bean.orderStatus eq '2'}">
									    <td>已发货</td>
									</c:if>
									<c:if test="${bean.orderStatus eq '3'}">
									    <td>已收货</td>
									</c:if>
									<c:if test="${bean.orderStatus eq '4'}">
									    <td>已过期</td>
									</c:if>
									<c:if test="${bean.orderStatus eq '5'}">
									    <td>已取消</td>
									</c:if>
									<c:if test="${bean.orderStatus eq '6'}">
									    <td>已退款</td>
									</c:if>	
									<td>${bean.reciverName}</td>
									<td>${bean.reciverMobile}</td>
									<td>${bean.province}${bean.city}${bean.area}</td>
									<td>${bean.address}</td>
						</tr>
					</c:forEach>
		</table>
	</body>
</html>