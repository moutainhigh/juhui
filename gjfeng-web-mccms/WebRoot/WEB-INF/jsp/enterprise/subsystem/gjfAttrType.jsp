﻿<%--

	gjfAttrType.jsp

	Create by MCGT

	Create time 2017-01-10

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
			function updateStatus(id,status,obj){
				var r = confirm("确定该操作吗？");
				if(r == true){
					$.ajax({
	      				type: "post",
	      				url: 'attrTypeAction!modifyAttrTypeStatus.action',
	      				dataType: "text",
	      				data:{
	      					"id":id,
							"status":status,
							"token":$(obj).siblings(".security-token").val()
							},
	      				success:function(data){
	      					 location.reload();
	      				},
	      				error:function(){
	  
	      				}
					});
				}
				return false;
			}
		
		</script>
	</head>
	<body>
		<div class="rhead">
			<div class="rpos">
				当前位置: 商品管理 - 属性类型列表
			</div>
			<div class="ropt">
				<input type="button" value='<s:text name="common.word.new" />' onclick="location.href='attrTypeAction!newArrtType.action'"/>
			</div>
			<div class="clear"></div>
		</div>
		
		<table class="listTable3" onmouseover="changeto()" onmouseout="changeback()">
			<tr id="nc" class="headbg">
				<td>序号</td>
				<td>属性名</td>
				<td>属性排序</td>
				<td>新增时间</td>
				<td>编辑时间</td>
				<td>状态</td>
				<td>操作</td>
			</tr>

			<c:forEach var="bean" items="${gjfAttrTypes}"
				varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${bean.attrName}</td>
					<td>${bean.sortOrder}</td>
					<td><fmt:formatDate value="${bean.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${bean.editTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<c:if test="${bean.status eq 0}">
							<p>停用</p>
						</c:if>
						<c:if test="${bean.status eq 1}">
							<p>启用</p>
						</c:if>
					</td>

					<td>
						<a href="attrTypeAction!retrieveAttrTypeById.action?id=${bean.id}">编辑</a>
						&nbsp; &nbsp;
						<c:if test="${bean.status eq 0}">
							<%-- <a href="javascript:void(0);" onclick="ajaxUpdateStatus(${bean.id},1,this,'ajaxAttrTypeAction!modifyAttrTypeStatus')">启用</a> --%>
							<a href="javascript:void(0);" onclick="updateStatus(${bean.id},1,this)">启用</a>
							<input type="hidden" class="security-token" value="${bean.token}"/>
						</c:if>
						<c:if test="${bean.status eq 1}">
							<%-- <a href="javascript:void(0);" onclick="ajaxUpdateStatus(${bean.id},0,this,'ajaxAttrTypeAction!modifyAttrTypeStatus')">停用</a> --%>
							<a href="javascript:void(0);" onclick="updateStatus(${bean.id},0,this)">停用</a>
							<input type="hidden" class="security-token" value="${bean.token}"/>
						</c:if>
					</td>
				</tr>
			</c:forEach>

		</table>

		<c:if test="${not empty gjfAttrTypes}">
			<s:form action="attrTypeAction!retrieveAttrTypeByPage.action"
				namespace="/subsystem"	 method="post" name="form1" theme="simple" id="form1">
				<!-- 分页 -->
				<%@ include file="../../common/pager.jsp"%>
			</s:form>
		</c:if>
	</body>
</html>