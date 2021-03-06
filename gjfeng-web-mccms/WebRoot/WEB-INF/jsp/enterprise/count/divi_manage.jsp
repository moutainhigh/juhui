<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>模板</title>
	<link href="${ctx}/image/style_blue5/style.css" rel="stylesheet"
			type="text/css" />
	<style>
			.data{
				width: 30%;
				margin-left: 2%;
				margin-top: 20px;
				margin-bottom: 20px;
				float: left;
			}
		</style>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#creditLineRecharge").click(function(){
				window.location.href = "countInfoAction!toCreditLineRecharge.action";
			});
			
			$("#unusedCreditLine").click(function(){
				window.location.href = "countInfoAction!storeCreditLine.action";
			});
			/* $("#head1").click(function(){
				$("#list1").toggle();
			}); */
			$("#todayO2O").click(function(){
				window.location.href = "countInfoAction!findTodayO2OOrders.action";
			});
			
			$("#BalanceTotal").click(function(){
				window.location.href = "countInfoAction!retrieveMemberByCondition.action?op=1";
			});
			
			$("#memberBalanceTotal").click(function(){
				window.location.href = "countInfoAction!retrieveMemberByCondition.action?type=0&op=1";
			});
			
			$("#storeBalanceTotal").click(function(){
				window.location.href = "countInfoAction!retrieveMemberByCondition.action?type=1&op=1";
			});
			
			$("#withdrawAmount").click(function(){
				window.location.href = "countInfoAction!retrieveMemberByCondition.action?op=2";
			});
			
			$("#diviGrantTotal").click(function(){
				window.location.href = "countInfoAction!retrieveMemberByCondition.action?op=2";
			});
			
			$("#memberDiviTotal").click(function(){
				window.location.href = "countInfoAction!retrieveMemberByCondition.action?op=3";
			});
			
			$("#sellerDiviTotal").click(function(){
				window.location.href = "storeInfoAction!retrieveStoreByPager.action?op=1";
			});
			
		})
	</script>
	</head>
	<body>
		<div class="rhead">
			<div class="rpos">
				当前位置: 分红管理
			</div>
			<div class="clear"></div>
		</div>	
		<table class="listTable3">
		   <tr id="nc" class="headbg" style="background-color: #1F6D9E;height: 50px">
		   	   <td colspan="2" style="cl">
		   	   		<font color="#FFFFFF">数据列表</font>
		   	   </td>
		   </tr>
		</table>
		
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">今日网上商城订单成交额 
				<c:if test="${empty resultVo.result.todayStoreTurnovers }">0.00</c:if>
				<c:if test="${not empty resultVo.result.todayStoreTurnovers }">${resultVo.result.todayStoreTurnovers }</c:if>
			</font>
		</div>
		<div class="data" id="todayO2O" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">今日O2O订单成交额
				<c:if test="${empty resultVo.result.todayO2OTurnovers }">0.00</c:if>
				<c:if test="${not empty resultVo.result.todayO2OTurnovers }">${resultVo.result.todayO2OTurnovers }</c:if>
			</font>
		</div>
		<div class="data" id="creditLineRecharge" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">今日授信充值额
				<c:if test="${empty resultVo.result.creditLine }">0.00</c:if>
				<c:if test="${not empty resultVo.result.creditLine }">${resultVo.result.creditLine }</c:if>
			</font>
		</div>
		<div class="data" id="unusedCreditLine" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">商户未使用授信 
				${resultVo.result.unusedCreditLine }
			</font>
			
		</div>
		<%-- <div class="data" style="height: 150px;background-color:#7D37BB;text-align:center;line-height:150px;">
			<font color="#FFFFFF">今日分销额 ${resultVo.result.distribution }</font>
		</div> --%>
		<%-- <div class="data" id="diviGrantTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">分红发放总额 ${resultVo.result.diviGrantTotal } </font>
		</div> --%>
		<div class="data" id="BalanceTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">总剩余余额 ${resultVo.result.BalanceToal }</font>
		</div>
		<div class="data" id="memberDiviTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">会员分红权总额 ${resultVo.result.memberDiviTotal }</font>
		</div>
		<div class="data" id="sellerDiviTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
			<font color="#FFFFFF">商铺分红权总额 ${resultVo.result.sellerDiviTotal }</font>
		</div>
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">会员分红实时金额 ${resultVo.result.memberDiviOnTime }</font>
		</div>
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">商户分红实时金额 ${resultVo.result.sellerDiviOnTime }</font>
		</div>
		
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">今日会员池入池金额 ${resultVo.result.memberPoolOnTime }</font>
		</div>
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">今日商户池入池金额 ${resultVo.result.merchantPoolOnTime }</font>
		</div>
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">会员实时参与分红权数 ${resultVo.result.memberDiviNum }</font>
		</div>
		<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
			<font color="#FFFFFF">商户实时参与分红权数 ${resultVo.result.merchantDiviNum }</font>
		</div>
		
		<%-- <div style="margin-top: 50px">
			<table class="listTable3">
		  		<tr id="nc" class="headbg" style="background-color: #1F6D9E;height: 50px">
		  	 	   <td colspan="2" style="cl" id="head1">
		  	 	   		<font color="#FFFFFF">余额总表</font>
		 	  	   </td>
			   </tr>
			</table>
			<div id="list1">
				<div class="data" id="BalanceTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
					<font color="#FFFFFF">总剩余余额 ${resultVo.result.BalanceToal }</font>
				</div>
				<div class="data" id="memberBalanceTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
					<font color="#FFFFFF">普通会员剩余余额 ${resultVo.result.memberBalanceToal }</font>
				</div>
				<div class="data" id="storeBalanceTotal" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
					<font color="#FFFFFF">商户剩余余额 ${resultVo.result.storeBalanceToal }</font>
				</div>
				<div class="data" id="withdrawAmount" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;cursor:pointer;">
					<font color="#FFFFFF">可提现额 ${resultVo.result.withdrawAmount }</font>
				</div>
				<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
					<font color="#FFFFFF">手续费收取 ${resultVo.result.texAmount }</font>
				</div>
				<div class="data" style="height: 150px;background-color:#253B48;text-align:center;line-height:150px;">
					<font color="#FFFFFF">责任消费额 ${resultVo.result.resConsumption }</font>
				</div>
			</div>	
		</div> --%>
		
	</body>
</html>

