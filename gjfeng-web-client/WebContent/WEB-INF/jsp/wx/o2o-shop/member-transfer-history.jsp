<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="/common/wx-o2o-top.jsp" %>
<body>
	<div class="container">
		<c:if test="${not empty resultVo.result}">
		<div class="cash-item">
			<!-- <div class="cash-month">2017年 2月份</div> -->
			<ul class="cash-list">
			     <c:forEach items="${resultVo.result}" var="list">		     
			       <li class="cash-li clearfix" style="height:9.5rem">
			         <div style="margin-left: -263px;font-size: 1.5rem;margin-bottom: 8px;"><span>用户名称:</span><span>${list.transferMemberName}</span></div>			         
					 <div class="cash-box left">
						<div class="cash-time"><fmt:formatDate value="${list.addTime}" pattern="yyyy-MM-dd"/></div>
						<div class="cash-time"><fmt:formatDate value="${list.addTime}" pattern="HH:mm"/></div>
					  </div>
					  <%-- <div class="cash-box left">
					    <i class="fuli-icon" style="background-image: url('${imagePath}/wx/o2o-shop/授信额度支付.png');background-size: cover;"></i>					     						
					  </div> --%>
					  
					  <c:if test="${list.transferType==1||list.transferType==2}">
					   <div class="cash-box left" style="margin-left: 14px; margin-right: 14px;">
						  <div class="cash-text shouxin-text" style="line-height: 2.5rem;">转移</div>
						  <div class="cash-state shouxin-state">${list.transferData}</div>
					    </div>
					 					  									    
					    <div class="cash-box left" style="margin-left: 14px; margin-right: 14px;">
						  <div class="cash-text shouxin-text" style="line-height: 2.5rem;">实际到账</div>
						  <div class="cash-state shouxin-state">${list.actualTransferMoney}</div>
					    </div>	
					  </c:if>
					  <c:if test="${list.transferType==0}">
					   <div class="cash-box left" style="margin-left: 14px; margin-right: 14px;">
						  <div class="cash-text shouxin-text" style="line-height: 2.5rem;">转移</div>
						  <div class="cash-state shouxin-state">${list.transferData}</div>
					    </div>
					 					  									    
					    <div class="cash-box left" style="margin-left: 14px; margin-right: 14px;">
						  <div class="cash-text shouxin-text" style="line-height: 2.5rem;">实际到账</div>
						  <div class="cash-state shouxin-state">${list.actualTransferMoney}</div>
					    </div>	
					  </c:if>
					   				 					 					    
					 
					  <div class="bonus-state right">
						<span class="bonusState-stand">
						    <c:if test="${list.transferType==0}">分红权</c:if>
						    <c:if test="${list.transferType==1}">余额</c:if>
							<c:if test="${list.transferType==2}">积分</c:if>
						</span>
					  </div>
				   </li>
			     </c:forEach>
			</ul>
		</div>
		</c:if>	
		<c:if test="${empty resultVo.result}">
			<div class="data-state-box">
		        <img src="${imagePath}/wx/o2o-shop/data-null.png" class="data-state-img">
		        <p class="data-state-text">没有数据</p>
		    </div>
		</c:if>
	</div>
</body>
<script>
document.title = "记录列表";
</script>
</html>