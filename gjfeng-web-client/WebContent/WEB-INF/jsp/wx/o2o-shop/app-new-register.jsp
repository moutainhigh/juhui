<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	 <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="author" content="Lucky"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/><!-- 是否启用 WebApp 全屏模式 -->
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/><!-- 设置状态栏的背景颜色 -->
    <meta name="viewport" content="initial-scale=1.0,user-scalable=no,width=device-width,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telephone=no,email=no"/><!-- 禁止数字识自动别为电话号码 --><!-- 不让android识别邮箱 -->
	<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1">
	<title>注册</title>
	<style>
		.register-container{
			padding:10% 5%;
		}
		.register-form{
			margin-top: 5%;
			box-sizing: border-box;
		}
		.register-form .register-input{
			border:none;
			outline: none;
			border-bottom: 1px solid #f7f5f5;
			font-size: 14px;
			width:100%;
			padding:15px 5px;
			margin-bottom: 5%;
		}

		.register-form .register-btn{
			display: block;
			width: 100%;
			background-color: #ea56af;
			color: #fff;
			border:1px solid #ea56af;
			border-radius: 5px;
			font-size: 16px;
			padding: 10px;
		}
	</style>
</head>
<body>
	<section class="register-container">
		<h2>用户注册</h2>
	<form class="register-form" id="set-form">
	    <input type="hidden" name="superId" class="pwd-input" value="${superId}">
	     <div >
               <input type="tel" name="mobile" placeholder="请输入电话号码" class="register-input" id="mobile">
         </div>
         <div>
               <input type="password" name="password" placeholder="请输入密码" class="register-input">
         </div>				
		<input type="hidden" name="nickName" value="">
		<button type="submit" class="register-btn">注册</button>
	</form>
	</section>
</body>
<script src="${jsPath}/wx/o2o-shop/jquery-1.11.3.min.js?ver=1.1"></script>
<script src="${jsPath}/wx/o2o-shop/jquery.validate.min.js?ver=1.1"></script>
 <script src="${plugInPath}/layer_mobile/layer.js?ver=1.1"></script>
<script type="text/javascript">

jQuery.validator.addMethod("isPhone",function(value,element){
    var length = value.length;
    //支持13 15 17 18(056789)开头,同时支持xxx-xxxx-xxxx格式手机输入
    var mobile = /^13\d{9}$|15\d{9}$|17\d{9}$|18\d{1}\d{8}|13\d{1}-\d{4}-\d{4}$|15\d{1}-\d{4}-\d{4}$|17\d{1}-\d{4}-\d{4}$|18\d{1}-\d{4}-\d{4}$/; 
    var result = this.optional(element) || (mobile.test(value));
    var mobile_send = $("#mobile").val()
    if(result && mobile_send != ''){//发送过验证码之后
        if(mobile_send != value){//手机号码不等于之前的手机号码的话
            //$("#pro-code").val("");
        	result = "false";
        }
    }
    return result;
},"请输入正确的手机号码");


$(".register-form").validate({
    rules:{
    	mobile:{
            required:true,
            isPhone:true,
        },
        password:{
            required:true,          
        },
 
    },
    messages:{
    	mobile:{
            required:"请输入手机号码",
            isPhone:"请输入正确的手机号码",
        },
        password:{
            required:"请输入密码",          
        },
       
    },
    highlight: function (e) {
        $(e).parent().addClass('has-error');
    },
    success: function (e) {
        $(e).parents(".form-item").removeClass('has-error');
        $(e).remove();
    },
    submitHandler: function (e) {
    	bindMobile();
    },
    errorElement:'div',
    errorPlacement:function(error,element) {  
        if(element.hasClass("code-input")){
            error.insertAfter(element.parents(".form-codeItem"));
            error.addClass('error-code');
        }else{
            error.insertAfter(element.parent());
        }
    }
});

//绑定手机
function bindMobile(){
	$.ajax({
		url:"${ctx}/wx/member/memberRegister",
		type: "POST", //用POST方式传输
        dataType: "json", //数据格式:JSON
        data:$('#set-form').serialize(),
			success:function(data){   				                 
			layer.open({
                content: data.msg,
                btn: ['确定', '取消'],
                yes: function(index){//跳转到个人中心
                	//if(data.code==200){                        		
                		window.location.href="${ctx}/appNeed/appDownLoadGuide?superId=${superId}"                       		
                	//}
                    layer.close(index);
                },
                no:function(index){
                    layer.close(index);
                }
            }); 
		},
		error:function(xhr,type ,e){
			layer.open({
                content:"网络异常，请重试",
                btn:'我知道了'
           });
		}
	})
}

</script>
</html>