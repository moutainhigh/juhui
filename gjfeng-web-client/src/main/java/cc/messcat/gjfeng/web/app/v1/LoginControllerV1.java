package cc.messcat.gjfeng.web.app.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.messcat.gjfeng.common.exception.LoggerPrint;
import cc.messcat.gjfeng.common.util.EmojiFilter;
import cc.messcat.gjfeng.common.util.javaHelp;
import cc.messcat.gjfeng.common.vo.app.MemberInfoVo;
import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.common.web.BaseController;
import cc.messcat.gjfeng.dubbo.core.GjfLoginDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfMemberInfoDubbo;
import cc.messcat.gjfeng.entity.GjfMemberInfo;

@Controller
@RequestMapping(value = "app/", headers = "app-version=v1.0")
public class LoginControllerV1 extends BaseController {

	@Autowired
	@Qualifier("loginDubbo")
	private GjfLoginDubbo loginDubbo;
	
	@Autowired
	@Qualifier("request")
	private HttpServletRequest request;
	
	@Autowired
	@Qualifier("memberInfoDubbo")
	private GjfMemberInfoDubbo memberInfoDubbo;

	/**
	 * @描述 注册
	 * @author Karhs
	 * @date 2017年3月14日
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "v1_0/register", method = RequestMethod.POST)
	@ResponseBody
	public Object register(@RequestParam("account")String account,@RequestParam("password")String password,@RequestParam("nickname")String nickname) {
		try {
			resultVo =loginDubbo.register(account,password, nickname, "0", "", "0",0L, "");
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, LoginControllerV1.class);
		}
		return resultVo;
	}

	/**
	 * @描述 登陆
	 * @author Karhs
	 * @date 2017年3月14日
	 * @param account
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "v1_0/login", method = RequestMethod.POST)
	@ResponseBody
	public Object login(@RequestParam("account") String account, @RequestParam("password") String password) {
		try {
			resultVo =loginDubbo.login(account,password,"0",null);
			System.out.println("app登录acount"+account);
			request.getSession().setAttribute("account", account);
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			request.getSession().setAttribute("gjfMemberInfo", member);
			System.out.println("********"+request.getSession().getAttribute("account"));
			System.out.println("********member"+request.getSession().getAttribute("gjfMemberInfo"));
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, LoginControllerV1.class);
		}
		return resultVo;
	}

	/**
	 * @描述 绑定手机号
	 * @author Karhs
	 * @date 2017年3月14日
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "v1_0/bind", method = RequestMethod.POST)
	@ResponseBody
	public Object bindMobie(String uid,@RequestParam("mobile") String mobile, @RequestParam("password") String password) {
		try {
			resultVo =loginDubbo.upateBindMobieInApp(uid,mobile,password);
			
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, LoginControllerV1.class);
		}
		return resultVo;
	}

	/**
	 * @描述 忘记密码
	 * @author Karhs
	 * @date 2017年3月14日
	 * @param account
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "v1_0/forgetPassWord", method = RequestMethod.POST)
	@ResponseBody
	public Object forget(@RequestParam("mobile") String mobile, @RequestParam("newPassword") String newPassword,
		@RequestParam("reNewPassword") String reNewPassword) {
		try {
			resultVo=loginDubbo.forgetPwd(mobile, newPassword, reNewPassword);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, LoginControllerV1.class);
		}
		return resultVo;
	}
	
	/**
	 * 
	 * @param uid
	 * @return
	 */
	
	@RequestMapping(value="v1_0/getMemberByUid")
	@ResponseBody
	public Object getMemberByUid(String uid){
		try {
			GjfMemberInfo member=memberInfoDubbo.findMemberByUnionId(uid);
			resultVo=new ResultVo(200,"查询成功",member);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, LoginControllerV1.class);
		}
		return resultVo;
	}
	
	/**
	 * app微信登录
	 * @param uid
	 * @param nickName
	 * @param sex
	 * @param openid
	 * @return
	 */
	@RequestMapping(value="v1_0/weixinRegister")
	@ResponseBody
	public Object weixinRegister(String uid,String nickName,String img,String openid){
		try{
			resultVo=loginDubbo.register(uid, null, EmojiFilter.filterEmoji(nickName),
					"", javaHelp.toString(img, ""), "1",
					0L, openid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	/**
	 * 修改用户登录密码
	 * @param account
	 * @param oldPassword
	 * @param newPassword
	 * @param rePassword
	 * @return
	 */
	@RequestMapping(value="v1_0/updataLoginPassword")
	@ResponseBody
	public Object updataLoginPassword(String account,String oldPassword,String newPassword,String rePassword){
		try{
			resultVo=loginDubbo.updatePwd(account, oldPassword, newPassword, rePassword);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
		
}
