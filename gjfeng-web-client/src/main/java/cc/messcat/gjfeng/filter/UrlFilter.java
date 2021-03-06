package cc.messcat.gjfeng.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.wechat.popular.api.QrcodeAPI;
import com.wechat.popular.api.SnsAPI;
import com.wechat.popular.api.TokenAPI;
import com.wechat.popular.api.UserAPI;
import com.wechat.popular.bean.QrcodeTicket;
import com.wechat.popular.bean.SnsToken;
import com.wechat.popular.bean.Token;
import com.wechat.popular.bean.User;
import com.wechat.popular.util.ExpireSet;

import cc.messcat.gjfeng.common.constant.CommonStatus;
import cc.messcat.gjfeng.common.util.BeanUtil;
import cc.messcat.gjfeng.common.util.EmojiFilter;
import cc.messcat.gjfeng.common.util.GenerateQrCodeUtil;
import cc.messcat.gjfeng.common.util.ObjValid;
import cc.messcat.gjfeng.common.util.Sha256;
import cc.messcat.gjfeng.common.util.StringUtil;
import cc.messcat.gjfeng.common.util.javaHelp;
import cc.messcat.gjfeng.common.vo.app.MemberInfoVo;
import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.common.constant.CommonProperties;
import cc.messcat.gjfeng.dubbo.core.GjfLoginDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfMemberInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfStoreInfoDubbo;
import cc.messcat.gjfeng.entity.GjfAccessToken;
import cc.messcat.gjfeng.entity.GjfMemberInfo;

public class UrlFilter implements Filter {

	@Autowired
	@Qualifier("memberInfoDubbo")
	private GjfMemberInfoDubbo memberInfoDubbo;

	@Autowired
	@Qualifier("storeInfoDubbo")
	private GjfStoreInfoDubbo storeInfoDubbo;

	@Autowired
	@Qualifier("loginDubbo")
	private GjfLoginDubbo loginDubbo;

	@Value("${dubbo.application.web.client}")
	private String projectNames;

	@Value("${gjfeng.client.project.url}")
	private String projectName;

	@Value("${upload.member.code.path}")
	private String imageFolderNames;

	@Autowired
	@Qualifier("request")
	private HttpServletRequest request;
	// 重复通知过滤 时效60秒
	private static ExpireSet<String> expireSet = new ExpireSet<String>(60);

	public void destroy() {

	}

	@SuppressWarnings("unused")
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain filterchain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse resp = (HttpServletResponse) arg1;
		HttpSession session = request.getSession();
		String code = (String) request.getParameter("code");
		String account = request.getParameter("account");
		String sign = request.getParameter("sign");
		String openid = (String) session.getAttribute("openid");
		String unionId = (String) session.getAttribute("unionId");
		SnsToken snsToken = new SnsToken();
		 //unionId="oYZdfv8oBmcAZHVUj4DkczwrjJYw";
		// openid="oJZ5wxCDfZJ19MaG_1vGtngqAP2Y";
		// 测试账号用
		System.out.println("微信code:"+code);
		try {
			if (StringUtil.isBlank(openid)) {
				System.out.println("openId不为空");
				if (StringUtil.isNotBlank(code)) {
					System.out.println("微信code不为空");
					try {
						if (expireSet.contains(code)) {// 重复通知不作处理
							filterchain.doFilter(arg0, arg1);
							return;
						} else {
							expireSet.add(code);
						}
						int i = 0;							

						// 获取token

						snsToken = SnsAPI.oauth2AccessToken(CommonProperties.GJFENG_APP_ID,
								CommonProperties.GJFENG_APPSECRET, code);
						while (!ObjValid.isValid(snsToken) && i < 5) { //
							snsToken = SnsAPI.oauth2AccessToken(CommonProperties.GJFENG_APP_ID,
									CommonProperties.GJFENG_APPSECRET, code);
							if (!ObjValid.isValid(snsToken)) { // 若取不到snsToken、则不获取openid;
								Thread.sleep(500); // 睡眠0.5 再取
							}
							i++;
						}

						if (ObjValid.isValid(snsToken)) {
							openid = snsToken.getOpenid();
							session.setAttribute("openid", openid);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("获取openid"+openid);
				if (StringUtil.isNotBlank(openid)) {
					User user = SnsAPI.userinfo(snsToken.getAccess_token(), openid, "zh_CN"); // 获取用户信息
					System.out.println("错误信息："+user.getErrmsg()+"错误码："+user.getErrcode()+"uid:"+user.getOpenid());
					if (ObjValid.isValid(user)) {
						unionId = user.getUnionid();
						GjfMemberInfo member = memberInfoDubbo.findMemberByUnionId(user.getUnionid());
						if (!ObjValid.isValid(member) && ObjValid.isValid(openid)) { // 没有该会员信息时保存
							// EventMessage
							// eventMessage=XMLConverUtil.convertToObject(EventMessage.class,
							// request.getInputStream());
							Long superId = 0L;
							if (StringUtil.isNotBlank(account) && StringUtil.isNotBlank(sign)
									&& Sha256.checkSha256Hash(account, "1", CommonStatus.SIGN_KEY_NUM, sign)) {
								superId = Long.valueOf(account);
							}

							ResultVo resultVo = loginDubbo.register(user.getUnionid(), null, EmojiFilter.filterEmoji(user.getNickname()),
									String.valueOf(user.getSex()), javaHelp.toString(user.getHeadimgurl(), ""), "1",
									superId, openid);
							MemberInfoVo memberInfoVo = (MemberInfoVo) resultVo.getResult();

							// 获取token
							GjfAccessToken accessToken = (GjfAccessToken) memberInfoDubbo.getAccessToken("1")
									.getResult();
							Token tokens = new Token();
							if (BeanUtil.isValid(accessToken)) {
								Date time = new Date();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

								if (sdf.parse(sdf.format(accessToken.getExpirationTime()))
										.compareTo(sdf.parse(sdf.format(time))) == 1) {
									user = UserAPI.userInfo(accessToken.getToken(), openid); // 获取用户信息
									tokens.setAccess_token(accessToken.getToken());
								} else {
									tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
											CommonProperties.GJFENG_APPSECRET);
									//如果请求失败重新获取token
									 int i=0;
									 while (!ObjValid.isValid(tokens.getAccess_token()) && i < 5) { //
										   tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
													CommonProperties.GJFENG_APPSECRET);
											if (!ObjValid.isValid(tokens.getAccess_token())) { // 若取不到snsToken、则不获取openid;
												Thread.sleep(500); // 睡眠0.5 再取
											}
											i++;
										}	
									user = UserAPI.userInfo(tokens.getAccess_token(), openid);
									 GjfAccessToken token=new GjfAccessToken(CommonProperties.GJFENG_APP_ID,CommonProperties.GJFENG_APPSECRET,"1");	
									
									token.setToken(tokens.getAccess_token());
									
									memberInfoDubbo.addAccessToken(token);
								}
							} else {
								tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
										CommonProperties.GJFENG_APPSECRET);
								//如果请求失败重新获取token
								 int i=0;
								 while (!ObjValid.isValid(tokens.getAccess_token()) && i < 5) { //
									   tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
												CommonProperties.GJFENG_APPSECRET);
										if (!ObjValid.isValid(tokens.getAccess_token())) { // 若取不到snsToken、则不获取openid;
											Thread.sleep(500); // 睡眠0.5 再取
										}
										i++;
									}	
								user = UserAPI.userInfo(tokens.getAccess_token(), openid);
								 GjfAccessToken token=new GjfAccessToken(CommonProperties.GJFENG_APP_ID,CommonProperties.GJFENG_APPSECRET,"1");	
								
								token.setToken(tokens.getAccess_token());

								memberInfoDubbo.addAccessToken(token);
							}
							// 生成二维码
							String access_token = tokens.getAccess_token();
							QrcodeTicket qrcodeTicket = QrcodeAPI.qrcodeCreateFinalStr(access_token,
									memberInfoVo.getId().toString());
							if("40001".equals(qrcodeTicket.getErrcode())){
								tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
										CommonProperties.GJFENG_APPSECRET);	
								 GjfAccessToken token=new GjfAccessToken(CommonProperties.GJFENG_APP_ID,CommonProperties.GJFENG_APPSECRET,"1");	
								
								token.setToken(tokens.getAccess_token());	
								memberInfoDubbo.addAccessToken(token);
								qrcodeTicket = QrcodeAPI.qrcodeCreateFinalStr(access_token, memberInfoVo.getId().toString());
							}
							String url = "";
							String path="";
							try {
								url = GenerateQrCodeUtil.generateQrcode(request, qrcodeTicket.getUrl(), projectNames,
										imageFolderNames);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(BeanUtil.isValid(url)){
								path = projectName + imageFolderNames + "/" + url;
							}
							
							GjfMemberInfo info = memberInfoDubbo.findMemberByUnionId(unionId);
							info.setImgQrUrl(path);
							memberInfoDubbo.updateMemberCode(info);
						}
					}
				}
			}else{
				System.out.println("进入获取uid信息*********");
				// 获取token
				GjfAccessToken accessToken = (GjfAccessToken) memberInfoDubbo.getAccessToken("1")
						.getResult();
				Token tokens = new Token();
				if (BeanUtil.isValid(accessToken)) {
					Date time = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					if (sdf.parse(sdf.format(accessToken.getExpirationTime()))
							.compareTo(sdf.parse(sdf.format(time))) == 1) {
						
						tokens.setAccess_token(accessToken.getToken());
					} else {
						tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
								CommonProperties.GJFENG_APPSECRET);
						//如果请求失败重新获取token
						 int i=0;
						 while (!ObjValid.isValid(tokens.getAccess_token()) && i < 5) { //
							   tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
										CommonProperties.GJFENG_APPSECRET);
								if (!ObjValid.isValid(tokens.getAccess_token())) { // 若取不到snsToken、则不获取openid;
									Thread.sleep(500); // 睡眠0.5 再取
								}
								i++;
							}	
						
						 GjfAccessToken token=new GjfAccessToken(CommonProperties.GJFENG_APP_ID,CommonProperties.GJFENG_APPSECRET,"1");	
						
						token.setToken(tokens.getAccess_token());
						
						memberInfoDubbo.addAccessToken(token);
					}
				} else {
					tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
							CommonProperties.GJFENG_APPSECRET);
					//如果请求失败重新获取token
					 int i=0;
					 while (!ObjValid.isValid(tokens.getAccess_token()) && i < 5) { //
						   tokens = TokenAPI.token(CommonProperties.GJFENG_APP_ID,
									CommonProperties.GJFENG_APPSECRET);
							if (!ObjValid.isValid(tokens.getAccess_token())) { // 若取不到snsToken、则不获取openid;
								Thread.sleep(500); // 睡眠0.5 再取
							}
							i++;
						}	
				
					 GjfAccessToken token=new GjfAccessToken(CommonProperties.GJFENG_APP_ID,CommonProperties.GJFENG_APPSECRET,"1");	
					
					token.setToken(tokens.getAccess_token());

					memberInfoDubbo.addAccessToken(token);
				}
				String access_token = tokens.getAccess_token();
				User user = UserAPI.userInfo(access_token, openid); // 获取用户信息
				System.out.println("111错误信息："+user.getErrmsg()+"错误码："+user.getErrcode()+"uid:"+user.getOpenid());
				if(BeanUtil.isValid(user)){
					unionId = user.getUnionid();
				}
				
			}
			System.out.println("unionId:"+unionId+"***"+"openid:"+openid);
			GjfMemberInfo member = memberInfoDubbo.findMemberByUnionId(unionId);
			System.out.println("用户信息："+member);
			if (ObjValid.isValid(member)) {
				System.out.println("进入保存用户信息");
				if("1".equals(member.getIsDel())){
					session.setAttribute("gjfMemberInfo", member);
					if (StringUtil.isNotBlank(member.getMobile())) {	
						session.setAttribute("account", member.getMobile());
						if (member.getType().equals("1")) {
							Object o=storeInfoDubbo.findStoreByAccount(member.getMobile()).getResult();
							if (ObjValid.isValid(o)) {
								session.setAttribute("gjfStoreInfo",o);
							}
						}
					}
					session.setAttribute("unionId", member.getUnionId());
				}else{
					session.removeAttribute("account");
					session.removeAttribute("unionId");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		filterchain.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
