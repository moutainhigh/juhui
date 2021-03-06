<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="/common/wx-o2o-top.jsp" %>
<body>
    <div class="container">
        <ul class="agentList-ul">
            <li class="agentList-li">
                <div class="agentList-box cleafix">
                    <div class="agentList-item left file-name-item">
                        <img src="${imagePath}/wx/o2o-shop/download-head.png" class="agentList-icon">
                        <a href="${ctx}/upload/file/巨惠云商会员注册协议.docx" class="download-link">巨惠云商会员注册协议.docx</a>
                    </div>
                    <div class="agentList-item right file-arrow-btn">
                        <img src="${imagePath}/wx/o2o-shop/arrow-btn.png" class="file-arrow">
                    </div>
                </div>
                <div class="agentList-box agentList-middle cleafix" style="display:none;">
                    <div class="file-row">
                        <label class="file-label">下载地址：</label>
                        <span class="file-text">${ctx}/upload/file/巨惠云商会员注册协议.docx</span>
                    </div>
                </div>
            </li>
            <li class="agentList-li">
                <div class="agentList-box cleafix">
                    <div class="agentList-item left file-name-item">
                        <img src="${imagePath}/wx/o2o-shop/download-head.png" class="agentList-icon">
                        <a href="http://jfh.jfeimao.com/gjfeng-web-client/upload/file/广东凤凰网络科技股份有限公司商家入驻合同.doc" class="download-link">广东凤凰网络科技股份有限公司商家入驻合同.doc</a>
                    </div>
                    <div class="agentList-item right file-arrow-btn">
                        <img src="${imagePath}/wx/o2o-shop/arrow-btn.png" class="file-arrow">
                    </div>
                </div>
                <div class="agentList-box agentList-middle cleafix" style="display:none;">
                    <div class="file-row">
                        <label class="file-label">下载地址：</label>
                        <span class="file-text">http://jfh.jfeimao.com/gjfeng-web-client/upload/file/广东凤凰网络科技股份有限公司商家入驻合同.doc</span>
                    </div>
                </div>
            </li>
        </ul>
        <!-- <div class="loading-more">上拉加载更多</div> -->
    </div>
    <script type="text/javascript">
        $(function(){
        	document.title = "下载";
            $(".file-arrow-btn").on("click",function(){
                var that = $(this);
                var state = that.parents(".agentList-box").siblings().css("display");
                if(state == "none"){
                    that.css('-webkit-transform', 'rotate(180deg)');
                    that.parents(".agentList-box").siblings().slideDown();
                }else{
                    that.css('-webkit-transform', 'rotate(0deg)');
                    that.parents(".agentList-box").siblings().slideUp(); 
                }
            })
        })
    </script>
</body>
</html>