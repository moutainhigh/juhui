<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="/common/wx-shop-top.jsp" %>
<body style="background: #fff">
	<section class="add-addressec">
		<form action="${ctx}/wx/address/update" method="POST" id="add-form">
		    <input type="hidden" name="id" value="${resultVo.result.id}"></input>
		    <input type="hidden" name="goodSource" value="${goodSource}"></input>
			<ul class="add-addresform">
				<li class="clearfix">
					<span>收货人</span>
					<input type="text" value="${resultVo.result.consigneeName}" name="consigneeName" id="consigneeName">
					<div style="margin-left: 165px;line-height: inherit;color: red; display:none" id="nameMsg">aaa</div>
				</li>
				<li class="clearfix">
					<span>手机号码</span>
					<input type="text" value="${resultVo.result.mobile}" name="mobile" id="mobile">
										<div style="margin-left: 165px;line-height: inherit;color: red;display:none" id="mobileMsg">aaa</div>
				</li>
				<li class="clearfix">
					<span>省</span>
					<select id="provice" name="proviceId">
						<!-- <option>请选择省</option> -->
					</select>
				</li>
				<li class="clearfix">
					<span>市</span>
					<select id="city"  name="cityId">
						<!-- <option>请选择市</option> -->
					</select>
				</li>
				<li class="clearfix" >
					<span>区</span>
					<select id="area" name="areaId">
						<!-- <option>请选择区</option> -->
					</select>
				</li>
				<c:if test="${goodSource==1||goodSource==5}">
				  <li class="clearfix" >
					<span>镇</span>
					<select id="town" name="townId">
						<!-- <option>请选择区</option> -->
					</select>
				  </li>
				
				</c:if>
				<li class="clearfix">
					<span>详细地址</span>
					<textarea required name="addressDetail" id="addressDetail">${resultVo.result.addressDetail}</textarea>
					<div style="margin-left: 165px;line-height: inherit;color: red;display:none" id="detailMsg"></div>
				</li>
			</ul>
		</form>
	</section>
	<div class="add-mask"></div>
	<footer class="address-footer">
		<a id="updataAdd" class="address-adda">保存</a>
	</footer>
	<!-- <script src="../js/jquery-2.1.4.min.js"></script>
	<script src="../js/common.js"></script> -->
</body>
<script type="text/javascript">
var proviceId="${resultVo.result.proviceId.provinceId}";
var cityId="${resultVo.result.cityId.cityId}"
var areaId="${resultVo.result.areaId.areaId}"
var townId="${resultVo.result.townId.townId}"
document.title = "地址修改";
//获取所有省份
$.ajax({
	   url:"${ctx}/wx/address/getAllProvince",
	   method:"GET",
	   data:{
		   fatherId:"",
		   addressType:1,
		   goodSource:"${goodSource}"
	   },
	   success:function(data){
		   var str=""
		   for(var i=0;i<data.result.length;i++){
			   if(proviceId==data.result[i].provinceId){
				   str+="<option provinceId="+data.result[i].provinceId+" value="+data.result[i].id+" selected>"+data.result[i].province+"</option>" 
			   }else{
				   str+="<option provinceId="+data.result[i].provinceId+" value="+data.result[i].id+">"+data.result[i].province+"</option>"   
			   }
			   
		   }
		    $("#provice").html(str);
		    
	   }
})

//省份改變
$("#provice").change(function(){
	getCity($("#provice option:selected").attr("provinceId"))
	//alert($("#city option:selected").attr("cityId"))	
    //getArea($("#city option:selected").attr("cityId"))
	
})

//城市改變
$("#city").change(function(){	
	getArea($("#city option:selected").attr("cityId"))
	
	if($("#city option:selected").attr("cityId")==29855||$("#city option:selected").attr("cityId")==29890){
		
		getTown($("#city option:selected").attr("cityId"))
	}
})

//城市改變
$("#area").change(function(){	
	getTown($("#area option:selected").attr("areaId"))
})


 //获取省份下的城市
 getCity(proviceId)
function getCity(proviceIds){
	 $.ajax({
		   url:"${ctx}/wx/address/getAllProvince",
		   method:"GET",
		   data:{
			   fatherId:proviceIds,
			   addressType:2,
			   goodSource:"${goodSource}"
		   },
		   success:function(data){
			   var str=""
			   for(var i=0;i<data.result.length;i++){
				   if(cityId==data.result[i].cityId){
					  str+="<option cityId="+data.result[i].cityId+" value="+data.result[i].id+" selected>"+data.result[i].city+"</option>" 
				   }else{
					  str+="<option cityId="+data.result[i].cityId+" value="+data.result[i].id+">"+data.result[i].city+"</option>"  
				   }
				   
			   }
			   $("#city").html(str);
			   getArea($("#city option:selected").attr("cityId"))
		   }
	     }) 

    }
		 

 //获取城市下的区域
 getArea(cityId)
function getArea(cityIds){
	  $.ajax({
		   url:"${ctx}/wx/address/getAllProvince",
		   method:"GET",
		   data:{
			   fatherId:cityIds,
			   addressType:3,
			   goodSource:"${goodSource}"
		   },
		   success:function(data){
			   var str=""
			   for(var i=0;i<data.result.length;i++){
				   if(areaId==data.result[i].areaId){
					   str+="<option areaId="+data.result[i].areaId+" value="+data.result[i].id+" selected>"+data.result[i].area+"</option>"
				   }else{
					   str+="<option areaId="+data.result[i].areaId+" value="+data.result[i].id+">"+data.result[i].area+"</option>" 
				   }
				  
			   }
			     $("#area").html(str)
		   }
	     })   	 
 	  }
 
//获取城市下的区域
 getTown(areaId)
function getTown(areaId){
	  $.ajax({
		   url:"${ctx}/wx/address/getAllProvince",
		   method:"GET",
		   data:{
			   fatherId:areaId,
			   addressType:4,
			   goodSource:"${goodSource}"
		   },
		   success:function(data){
			   var str=""
			   for(var i=0;i<data.result.length;i++){
				   if(townId==data.result[i].townId){
					   str+="<option townId="+data.result[i].townId+" value="+data.result[i].id+" selected>"+data.result[i].townName+"</option>"
				   }else{
					   str+="<option townId="+data.result[i].townId+" value="+data.result[i].id+">"+data.result[i].townName+"</option>" 
				   }
				  
			   }
			     $("#town").html(str)
		   }
	     })   	 
 	  }
 
 
 
 
 //提交表單
        //获取焦点事件
    	  $("#consigneeName").focus(function(){
    		  $("#nameMsg")[0].style.display = "none" 
    	  })
    	  
    	   $("#mobile").focus(function(){
    		  $("#mobileMsg")[0].style.display = "none" 
    	  })
    	     	     	  
    	    $("#addressDetail").focus(function(){
    		  $("#detailMsg")[0].style.display = "none" 
    	  })
    	  
    	  //失去焦点
    	   $("#consigneeName").blur(function(){
    		   if($("#consigneeName").val()==''){
    			   $("#nameMsg").html("用户名不能为空")
    			   $("#nameMsg")[0].style.display = "block"
    		   }
    	  })
    	  
    	   $("#mobile").blur(function(){
    		 //校验电话号码
    		   if($("#mobile").val()==''){
    			   $("#mobileMsg").html("电话号码不能为空")
    			   $("#mobileMsg")[0].style.display = "block"
    		   }else{
    			    var reg = new RegExp("^[1][3578][0-9]{9}$");
    				if (!reg.test($("#mobile").val())) {
    					$("#mobileMsg").html("输入的手机号码不正确")
    					$("#mobileMsg")[0].style.display = "block"
    				}
    		   }
    	  })
    	  
    	  
    	    $("#addressDetail").blur(function(){
    	    	 if($("#addressDetail").val()==''){
      			   $("#detailMsg").html("详细地址不能为空")
      			   $("#detailMsg")[0].style.display = "block"
      		   }
    	  })
    	  
    	  function vilade(){   		  
    		   //校验用户名
    		   if($("#consigneeName").val()==''){
    			   $("#nameMsg").html("用户名不能为空")
    			   $("#nameMsg")[0].style.display = "block"
    			   return false
    		   }else{
    			   $("#nameMsg").html("")
    			   $("#nameMsg")[0].style.display = "none"     				
    		   }
    		   //校验电话号码
    		   if($("#mobile").val()==''){
    			   $("#mobileMsg").html("电话号码不能为空")
    			   $("#mobileMsg")[0].style.display = "block"
    			   return false
    		   }else{
    			    var reg = new RegExp("^[1][3578][0-9]{9}$");
    				if (!reg.test($("#mobile").val())) {
    					$("#mobileMsg").html("输入的手机号码不正确")
    					$("#mobileMsg")[0].style.display = "block"
    					return false
    				}else{
    					$("#mobileMsg").html("")
    					$("#mobileMsg")[0].style.display = "none"   				   
    				}
    				
    		   }
    		   
    		     		    
    		    //校验详细地址
    		    if($("#addressDetail").val()==''){
    			   $("#detailMsg").html("详细地址不能为空")
    			   $("#detailMsg")[0].style.display = "block"
    			   return false
    		   }else{
    			   $("#detailMsg").html("")
    			   $("#detailMsg")[0].style.display = "none" 
    			   
    		   }    		   
    		   return true
    	   }
 
 $("#updataAdd").click(function(){
	 if(vilade()){
		 $("#add-form").submit() 
	 }
	
 })
		 

</script>
</html>