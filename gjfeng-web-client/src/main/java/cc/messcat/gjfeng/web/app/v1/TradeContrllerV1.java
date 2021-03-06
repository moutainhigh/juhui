package cc.messcat.gjfeng.web.app.v1;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.direct.config.AlipayConfig;
import com.alipay.direct.util.OrderInfoUtil2_0;
import com.h5pay.api.H5PayUtil;
import com.newAlipay.sign.RSA;
import com.newAlipay.util.AlipayCore;
import com.newAlipay.util.NewsOrderUtil;
import com.wechat.WeixinUtil;
import com.wechat.popular.bean.pay.PayJsRequest;

import cc.messcat.gjfeng.common.constant.CommonProperties;
import cc.messcat.gjfeng.common.exception.LoggerPrint;
import cc.messcat.gjfeng.common.util.BeanUtil;
import cc.messcat.gjfeng.common.util.EncryptionUtil;
import cc.messcat.gjfeng.common.util.ObjValid;
import cc.messcat.gjfeng.common.util.StringUtil;
import cc.messcat.gjfeng.common.vo.app.MemberTradeBenefitVo;
import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.common.vo.app.StoreInfoVo;
import cc.messcat.gjfeng.common.web.BaseController;
import cc.messcat.gjfeng.dubbo.core.GjfEnterpriseColumnDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfMemberInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfProductInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfStoreInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo;
import cc.messcat.gjfeng.entity.GjfFhTreasureInfo;
import cc.messcat.gjfeng.entity.GjfMemberInfo;
import cc.messcat.gjfeng.entity.GjfMemberTrade;
import cc.messcat.gjfeng.entity.GjfMemberVouchersTradeHistory;
import cc.messcat.gjfeng.entity.GjfMerchantUpgradeHistory;
import cc.messcat.gjfeng.entity.GjfWeiXinPayInfo;
import cc.messcat.gjfeng.util.SessionUtil;
import cc.messcat.gjfeng.web.wechat.MemberController;
import cc.messcat.gjfeng.web.wechat.TradeContrller;

@Controller
@RequestMapping(value = "app/trade/", headers = "app-version=v1.0")
public class TradeContrllerV1 extends BaseController {

	@Autowired
	@Qualifier("request")
	private HttpServletRequest request;

	@Autowired
	@Qualifier("response")
	private HttpServletResponse response;

	@Autowired
	@Qualifier("tradeDubbo")
	private GjfTradeDubbo tradeDubbo;

	@Autowired
	@Qualifier("memberInfoDubbo")
	private GjfMemberInfoDubbo memberInfoDubbo;

	@Autowired
	@Qualifier("storeInfoDubbo")
	private GjfStoreInfoDubbo storeInfoDubbo;
	
	@Autowired
	@Qualifier("productInfoDubbo")
	private GjfProductInfoDubbo productInfoDubbo;


	@Value("${gjfeng.client.project.url}")
	private String projectName;
	
	@Autowired
	@Qualifier("enterpriseColumnDubbo")
	private GjfEnterpriseColumnDubbo enterpriseColumnDubbo;

	/**
	 * @描述 跳转到我要提现页面
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "v1_0/toDrawCash", method = RequestMethod.POST)
	@ResponseBody
	public Object toDrawCash(String account) {
		try {
			// 查询用户最新的资金情况和所有的银行卡列表
			resultVo = tradeDubbo.toDrawCash(account);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 申请提现
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "v1_0/addDrawCash", method = RequestMethod.POST)
	@ResponseBody
	public Object addDrawCash(@RequestParam("account") String account, @RequestParam("myBankId") Long myBankId,
			@RequestParam("money") String money, @RequestParam("remark") String remark) {
		try {
			 //先判断用户是否实名制
			Double money0 = Double.valueOf(money);
			resultVo = tradeDubbo.addDrawCash(account, remark, myBankId, money0);
			//resultVo=new ResultVo(200,"app暂时是停止提现，如要提现请使用微信公众号进行提现");
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * 获取用户银行卡
	 * 
	 * @param myBankId
	 * @param money
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "v1_0/getMyBank", method = RequestMethod.POST)
	@ResponseBody
	public Object getMyBank(@RequestParam("account") String account, @RequestParam("bankId") Long bankId) {
		try {
			resultVo = tradeDubbo.findMemberBankById(bankId, account);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 获取提现历史记录
	 * @author Karhs
	 * @date 2017年1月10日
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "v1_0/getDrawHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object toDrawHistory(String account, Integer pageNo, Integer pageSize) {
		try {
			resultVo = tradeDubbo.findDrawCashHistory(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到我的银行卡列表
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "v1_0/myBanks", method = RequestMethod.POST)
	@ResponseBody
	public Object myBanks(String account) {
		try {
			resultVo = tradeDubbo.findMyBankCard(account);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 绑定银行卡
	 * @author Karhs
	 * @date 2017年1月10日
	 * @param bankId
	 * @param bankSub
	 * @param bankCard
	 * @param holder
	 * @return
	 */
	@RequestMapping(value = "v1_0/bindBank", method = RequestMethod.POST)
	@ResponseBody
	public Object bindBank(@RequestParam("bankId") Long bankId, @RequestParam("bankSub") String bankSub,
			@RequestParam("bankCard") String bankCard, @RequestParam("holder") String holder,
			@RequestParam("cityValue") String cityValue, String account) {
		try {
			resultVo = tradeDubbo.bindMyBankCard(account, bankId, bankSub, bankCard.trim(), holder, cityValue);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 获取所有银行卡信息
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "v1_0/getAllBank", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllBank() {
		try {
			resultVo = tradeDubbo.findAllBank();
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * 删除银行卡
	 * 
	 * @param bankId
	 * @return
	 */
	@RequestMapping(value = "v1_0/deleteMemBank", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteMemBank(@RequestParam("bankId") Long bankId, String account) {
		try {
			resultVo = tradeDubbo.delMyBank(account, bankId);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 查询我的福利记录
	 * @author Karhs
	 * @date 2017年1月10日
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "v1_0/diviHis", method = RequestMethod.POST)
	@ResponseBody
	public Object toDiviHistory(Integer pageNo, Integer pageSize, String account) {
		try {
			resultVo = tradeDubbo.findTradeDiviHistory(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account, "1");
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);

		}
		return resultVo;
	}

	/**
	 * @描述 我的钱包---累积消费(消费记录)
	 * @return
	 */
	@RequestMapping(value = "v1_0/getInterests", method = RequestMethod.POST)
	@ResponseBody
	public Object getInterests(String type, String account) {
		try {
			resultVo = tradeDubbo.findMemberInterests(account, type);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 我的钱包---福利权益
	 * @return
	 */
	@RequestMapping(value = "v1_0/getParticipate", method = RequestMethod.POST)
	@ResponseBody
	public Object getParticipate(String type, String account) {
		try {
			resultVo = tradeDubbo.findMemberParticipate(account, type);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 商家让利
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "v1_0/addBenefit", method = RequestMethod.POST)
	@ResponseBody
	public Object addBenefit(@RequestParam("amount") double amount, @RequestParam("mobile") String mobile,
			@RequestParam("payType") String payType, String account,String merchantGrade) {
		try {
			// 添加让利记录并调支付接口
			System.out.println("销售录入用户" + account);
			Object o = storeInfoDubbo.findStoreByAccount(account).getResult();
			if (ObjValid.isNotValid(o)) {
				resultVo = new ResultVo(400, "店铺不存在", null);
				return resultVo;
			}
			StoreInfoVo store = (StoreInfoVo) o;

			resultVo = tradeDubbo.addStoreBenefit(account, store.getId(), mobile, amount, payType,merchantGrade);
			// 根据支付方式调用相对应的支付
			if (payType.equals("0") && resultVo.getCode() == 200) {
				// 微信支付
				MemberTradeBenefitVo benefitVo = (MemberTradeBenefitVo) resultVo.getResult();
				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				// 获取微信配置信息
				/*
				 * GjfWeiXinPayInfo payInfo = (GjfWeiXinPayInfo)
				 * tradeDubbo.findWeiXinBaseInfo("0").getResult(); Map<String,
				 * String> map = H5PayUtil.weixinAppPay(payInfo, request, info,
				 * benefitVo.getBenefitMoney().toString().toString(),
				 * benefitVo.getTradeNo(),
				 * CommonProperties.GJFENG_WEIXIN_NOTIFY_BENETI);
				 * System.out.println("-----app微信支付返回成功" + map);
				 */
				Map map = WeixinUtil.unifiedorderResultInApp(request, benefitVo.getTradeNo(), "充值授信额度",
						benefitVo.getBenefitMoney().toString(),
						CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_BENEFIT);
				resultVo.setResult(map);
			}

			// 支付宝
			if ("1".equals(payType) && resultVo.getCode() == 200) {
				Map<String, String> resultMap = new HashMap<>();
				MemberTradeBenefitVo benefitVo = (MemberTradeBenefitVo) resultVo.getResult();
				Map<String, String> params = OrderInfoUtil2_0.getAppPlayParams(benefitVo.getTradeNo(), "下单成功",
						String.valueOf(benefitVo.getBenefitMoney().doubleValue()), "购买商品", CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_BENEFIT);
				String orderParam = OrderInfoUtil2_0.buildOrderParam(params, false);
				String sign = OrderInfoUtil2_0.getSign(params, AlipayConfig.RSA_PRIVATE);
				final String orderInfo = orderParam + "&" + sign + "&sign_type=\"RSA\"";
				resultMap.put("alyString", orderInfo);
				resultVo.setResult(resultMap);
			}

			// 网银支付
			if ("2".equals(payType) && resultVo.getCode() == 200) {
				MemberTradeBenefitVo benefitVo = (MemberTradeBenefitVo) resultVo.getResult();
				GjfMemberInfo member=memberInfoDubbo.findMember(account);
				Map<String, Object> model=new HashMap<>();
				//网银支付方式2
				model.put("orderId", benefitVo.getTradeNo());
				model.put("payMoney", benefitVo.getBenefitMoney().doubleValue());
				model.put("isReadName", member.getIsReadName());
				model.put("idCard", member.getIdCard());
				model.put("mobile", member.getMobile());
				model.put("retUrl", CommonProperties.GJFENG_WANGYIN_PAY_NOTIFY_URL_ORDER);
				// 查询用户最新的资金情况和所有的银行卡列表
				model.put("bankCardInfo",  tradeDubbo.toDrawCash(account).getResult());
				resultVo.setResult(model);
			}

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * 充值授信额度
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "v1_0/addShouXin", method = RequestMethod.POST)
	@ResponseBody
	public Object addShouXin(String type, Double tradeMoney, String account,String shouType,String fileImage) {
		try {
			if (!BeanUtil.isValid(type) && !BeanUtil.isValid(tradeMoney)) {
				return new ResultVo(400, "参数为空", null);
			}

			// 支付宝支付
			/*
			 * if ("2".equals(type)) { resultVo = new ResultVo(200, "查询成功",
			 * projectName + "/appNeed/addShouXinByAlipay?type=" + type +
			 * "&tradeMoney=" + tradeMoney + "&account=" + account); return
			 * resultVo; }
			 */

			resultVo = tradeDubbo.addShouXin(type, account, tradeMoney,shouType,fileImage);
			if ("1".equals(type) && resultVo.getCode() == 200) {
				GjfMemberTrade trade = (GjfMemberTrade) resultVo.getResult();
				// 获取微信配置信息
				GjfWeiXinPayInfo payInfo = (GjfWeiXinPayInfo) tradeDubbo.findWeiXinBaseInfo("0").getResult();
				// 微信扫呗
				/*
				 * Map<String, String> map = H5PayUtil.weixinAppPay(payInfo,
				 * request, trade.getMemberId(),
				 * trade.getTradeMoney().toString(), trade.getTradeNo(),
				 * CommonProperties.GJFENG_WEIXIN_NOTIFY_SHOUXIN);
				 * System.out.println("-----app微信支付返回成功" + map);
				 * resultVo.setResult(map);
				 */
				Map map = WeixinUtil.unifiedorderResultInApp(request, trade.getTradeNo(), "商家让利",
						trade.getTradeMoney().toString(), CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_SHOUXIN);
				resultVo.setResult(map);
			}

			// 支付宝
			if ("2".equals(type) && resultVo.getCode() == 200) {
				Map<String, String> resultMap = new HashMap<>();
				GjfMemberTrade trade = (GjfMemberTrade) resultVo.getResult();
				Map<String, String> params = OrderInfoUtil2_0.getAppPlayParams(trade.getTradeNo(), "下单成功",
						String.valueOf(trade.getTradeMoney().doubleValue()), "购买商品", CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_SHOUXIN);
				String orderParam = OrderInfoUtil2_0.buildOrderParam(params, false);
				String sign = OrderInfoUtil2_0.getSign(params, AlipayConfig.RSA_PRIVATE);
				final String orderInfo = orderParam + "&" + sign + "&sign_type=\"RSA\"";
				resultMap.put("alyString", orderInfo);
				resultVo.setResult(resultMap);
			}

			// 网银支付
			if ("3".equals(type) && resultVo.getCode() == 200) {
				GjfMemberTrade trade = (GjfMemberTrade) resultVo.getResult();
				Map<String, Object> model=new HashMap<>();
				//网银支付方式2
				model.put("orderId", trade.getTradeNo());
				model.put("payMoney", trade.getTradeMoney().doubleValue());
				model.put("isReadName", trade.getMemberId().getIsReadName());
				model.put("idCard", trade.getMemberId().getIdCard());
				model.put("mobile", trade.getMemberId().getMobile());
				model.put("retUrl", CommonProperties.GJFENG_WANGYIN_PAY_NOTIFY_URL_ORDER);
				// 查询用户最新的资金情况和所有的银行卡列表
				model.put("bankCardInfo",  tradeDubbo.toDrawCash(account).getResult());
				resultVo.setResult(model);								
			}

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 商家让利记录
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "v1_0/benefits", method = RequestMethod.POST)
	@ResponseBody
	public Object benefits(Integer pageNo, Integer pageSize, String account) {
		try {
			Object o = storeInfoDubbo.findStoreByAccount(account).getResult();
			if (ObjValid.isNotValid(o)) {
				return new ResultVo(400, "店铺不存在", null);
			}
			StoreInfoVo store = (StoreInfoVo) o;
			resultVo = tradeDubbo.findStoreBenefit(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, store.getId());
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到我的代理
	 * @author Karhs
	 * @date 2017年2月20日
	 * @return
	 */
	@RequestMapping(value = "v1_0/agent", method = RequestMethod.POST)
	@ResponseBody
	public Object agent(String account) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			resultVo = tradeDubbo.findAgentInfo(account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return resultVo;
	}

	/**
	 * @描述 查询下级代理
	 * @date 2017年2月27日
	 * @param pageNo
	 * @param pageSize
	 * @param reqType
	 * @return
	 */
	@RequestMapping(value = "v1_0/findNextAgent", method = RequestMethod.POST)
	@ResponseBody
	public Object findNextAgent(Integer pageNo, Integer pageSize, String account) {
		try {
			resultVo = tradeDubbo.findNextAgent(account, ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize);

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);

		}
		return resultVo;
	}

	/**
	 * @描述 代理分红记录
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "v1_0/agentHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object agentHistory(Integer pageNo, Integer pageSize, String account) {
		try {
			resultVo = tradeDubbo.findAgentHistory(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account);

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}
	
	
	/**
	 * 分红转移
	 * @return
	 */
	@RequestMapping(value = "/v1_0/transferDividendsMoney", method = RequestMethod.POST)
	@ResponseBody
	public Object transferDividendsMoney(String account){
		try{			
			resultVo=tradeDubbo.modifyTransferDividendsMoney(account);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	
	/**
	 * 跳转到积分转移页面
	 * @return
	 */
	@RequestMapping(value = "/v1_0/transferPage", method = RequestMethod.POST)
	@ResponseBody
	public Object transferPage(){		
		try{			
			resultVo=memberInfoDubbo.findSetBaseInfoByKey("POUNDAGE");
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
		
	}
	
	/**
	 * 转移积分
	 * @param type
	 * @param memberMobile
	 * @param transferMemberMobile
	 * @param transferMoney
	 * @return
	 */
	@RequestMapping(value = "/v1_0/memberPointTransfer", method = RequestMethod.POST)
	@ResponseBody
	public Object memberPointTransfer(String account,String type,String payPassword,String transferMemberMobile,BigDecimal transferMoney){
		try{						
			GjfMemberInfo info = memberInfoDubbo.findMember(account);
			if (StringUtil.isBlank(info.getPayPassword())) {
				resultVo = new ResultVo(402, "请先设置支付密码", null);
				return resultVo;
			}
			if (!info.getPayPassword().equals(EncryptionUtil.getMD5Code(info.getMobile() + payPassword))) {
				resultVo = new ResultVo(401, "支付密码错误", null);
				return resultVo;
			}
			resultVo=tradeDubbo.updateMemberPointTransfer(type, account, transferMemberMobile, transferMoney);
		}catch(Exception e){
			e.printStackTrace();
			resultVo=new ResultVo(500, "网络异常", null);
		}
		return resultVo;
	}
	
			
	/**
	 * 获取转移历史记录
	 * @param pageNo
	 * @param pageSize
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/v1_0/findTransferHistory",method=RequestMethod.POST)
	@ResponseBody
	public Object findTransferHistory(String account,Integer pageNo,Integer pageSize,String type){	
		try{			
			resultVo=tradeDubbo.findTransferHistoryByPager(pageNo, pageSize, account, type);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * @描述 我的钱包---福利记录
	 * @return
	 */
	@RequestMapping(value = "/v1_0/getSalesWelfare", method = RequestMethod.POST)
	@ResponseBody
	public Object getSalesWelfare(String account) {		
		try {			
			String type = "0";
			resultVo = tradeDubbo.findMemberSalesWelfare(account, type);		
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);		
		}
	   return resultVo;
	}
	
	
	/**
	 * 跳转到凤凰宝页面
	 */
	@RequestMapping(value = "/v1_0/toFhTreasurePage", method = RequestMethod.POST)
	@ResponseBody
	public Object toFhTreasurePage(String account) {
		Map<String, Object> model = new HashMap<String, Object>();	
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		model.put("isReadName", gjfMemberInfo.getIsReadName());
		
		resultVo = tradeDubbo.findFhTreasureInfo(account);
		model.put("fhTreasureInfo", resultVo.getResult());
		resultVo.setResult(model);
		return  resultVo; // TODO
	}
	
	
	/**
	 * @描述 跳转到转移余额到凤凰宝
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "/v1_0/toBalanceTransferPage", method = RequestMethod.POST)
	@ResponseBody
	public Object toBalanceTransferPage(String account) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {			
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			// 查询用户最新的资金情况和所有的银行卡列表
			resultVo = tradeDubbo.toDrawCash(account);
			model.put("allBankCard", resultVo.getResult());
			resultVo.setResult(model);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			
		}
		return resultVo;
	}

			
	/**
	 *把余额转移到凤凰宝
	 */
	@RequestMapping(value = "/v1_0/transderBalanceToTreasure", method = RequestMethod.POST)
	@ResponseBody
	public Object transderBalanceToTreasure(double money,String account) {
		try{			
			resultVo = tradeDubbo.addBalanceToFhTreasure(account, money);
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo;
	}
	
			
	/**
	 *把余额转移到凤凰宝
	 */
	@RequestMapping(value = "/v1_0/transferFhTreasurePage", method = RequestMethod.POST)
	@ResponseBody
	public Object transferFhTreasurePage(String mobile,String payPassword,double tradeMoney,String account) {
		try{		
			GjfMemberInfo info = memberInfoDubbo.findMember(account);
			if (StringUtil.isBlank(info.getPayPassword())) {
				resultVo = new ResultVo(402, "请先设置支付密码", null);
				return resultVo;
			}
			if (!info.getPayPassword().equals(EncryptionUtil.getMD5Code(info.getMobile() + payPassword))) {
				resultVo = new ResultVo(401, "支付密码错误", null);
				return resultVo;
			}
			//resultVo=new ResultVo(400, "转移失败");
			resultVo = tradeDubbo.addTransferFhTreasureHistory(account, mobile, tradeMoney);
		}catch(Exception e){
			e.printStackTrace();
		}				
		return resultVo; 
	}
	
	
	
	/**
	 * @描述 跳转到凤凰宝提现页面
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "/v1_0/toFhTreasureDrawCash", method = RequestMethod.POST)
	@ResponseBody
	public Object toFhTreasureDrawCash(String account) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {			
			GjfFhTreasureInfo fh=(GjfFhTreasureInfo) tradeDubbo.findFhTreasureInfo(account).getResult();
			model.put("treasureMoney", fh.getFhTreasureMoney());
			
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			// 查询用户最新的资金情况和所有的银行卡列表
			resultVo = tradeDubbo.toDrawCash(account);
			model.put("allBackCard", resultVo.getResult());
			resultVo.setResult(model);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			
		}
		return resultVo;
	}
	
	/**
	 * 凤凰宝提现
	 * @param myBankId
	 * @param money
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "/v1_0/addFhTreasureDrawCash", method = RequestMethod.POST)
	@ResponseBody
	public Object addFhTreasureDrawCash(@RequestParam("myBankId") Long myBankId, @RequestParam("money") String money,
			@RequestParam("remark") String remark,String account) {
		try {
			// 先判断用户是否实名制		
			Double money0 = Double.valueOf(money);
			resultVo = tradeDubbo.addFhTreasureDrawCash(account, remark, myBankId, money0);
			
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}
	
	
	/**
	 * @描述 跳转到凤凰宝交易记录
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "/v1_0/toFhTreasureTradeHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object toFhTreasureTradeHistory(Integer pageNo,Integer pageSize,String account) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			
			resultVo=tradeDubbo.findFhTreaureTradeHistory(pageNo, pageSize, account);						
			model.put("tradeHistory", resultVo.getResult());
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			
		}
		return resultVo;
	}
	
	/**
	 * 获取联盟商家栏目
	 * @return
	 */
	@RequestMapping(value="/v1_0/findModelProductColumn",method=RequestMethod.POST)
	@ResponseBody
	public Object findModelProductColumn(){
		try{
			resultVo=enterpriseColumnDubbo.findProductModelColumn("MODELPRODUCT","0");
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 采购商品分类
	 * @return
	 */
	@RequestMapping(value="/v1_0/findVoucherProduct",method=RequestMethod.POST)
	@ResponseBody
	public Object findVoucherProduct(Long columnId,String account,Integer pageNo,Integer pageSize,String likeName,String discount){
		Map<String, Object> model = new HashMap<String, Object>();
		try{			
			//获取用户信息
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			if("0".equals(member.getMerchantType())){
				model.put("merchantType", "0");
			}else if("1".equals(member.getMerchantType())){
				model.put("merchantType", "1");
			}else{
				model.put("merchantType", "2");	
			}
			
		   model.put("products", productInfoDubbo.findMerchantProcurementProductInfo(columnId,likeName, pageNo, pageSize,discount));	
		   resultVo=new ResultVo(200, "查询成功", model);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 *添加商户充值记录
	 */
	@RequestMapping(value = "/v1_0/addMerchantRechargeHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMerchantRechargeHistory(String account,String merchantType,String payType,double tradeMoney) {
		try{		
			resultVo=tradeDubbo.addMerchantRechargeHistory(account, tradeMoney, merchantType, payType);	
			if(resultVo.getCode()==200&&"1".equals(payType)){
				
				Map<String, String> resultMap = new HashMap<>();
				GjfMerchantUpgradeHistory upgradeHistory=(GjfMerchantUpgradeHistory) resultVo.getResult();
				/*Map<String, String> params = OrderInfoUtil2_0.getAppPlayParams(upgradeHistory.getTradeNo(), "商户充值",
						String.valueOf(upgradeHistory.getTradeMoney().doubleValue()), "联盟商家充值", CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_MERCAHNET_RECHARGE);
				String orderParam = OrderInfoUtil2_0.buildOrderParam(params, false);
				String sign = OrderInfoUtil2_0.getSign(params, AlipayConfig.RSA_PRIVATE);
				final String orderInfo = orderParam + "&" + sign + "&sign_type=\"RSA\"";
				resultMap.put("alyString", orderInfo.trim());*/
				String alyString=NewsOrderUtil.getPayBody(upgradeHistory.getTradeNo(), "商户充值",
						String.valueOf(upgradeHistory.getTradeMoney().doubleValue()), "联盟商家充值", CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_MERCAHNET_RECHARGE);
				//String alyString="alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017062707581858&biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0%E5%95%86%E5%93%81%22%2C%22out_trade_no%22%3A%2220180427101301486574%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E4%B8%8B%E5%8D%95%E6%88%90%E5%8A%9F%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%221400.0%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Ftest.gzfzsw.top%2Fgjfeng-web-client%2Fwx%2Fnotify%2FpayOrderNotifyAply&sign=nJLSTDm2p%2BeYlKpYJIijSGCBPULgVhEX14zHHVCXP%2FUQGooPyiHq%2BzQjXT3xooYJIMhLauUUxigUe1jmZlrW0rZ63jFwn4omog58R9Hycm9cTldkt7Zr8Diiie3ORUCce2DnzKn8qvKzeNst%2BdplhQ8U5npK%2BZAluz%2BM5ss2S9HXYnqYKIhMAxY2btEpZaRaQimGioxrAJNTIrWK%2FiVn6hxx%2BuO%2BwLgDLgEKjBVXNpcJ2OIxO0%2FCICq5MAgY1eAi5xqP8xTcvJYZXubZkeKTI%2F%2BiztIfl2uwoVxzHOkVxZf8gHptcz33YlPLlMNUJKGAGYt878gDFBsD0MA2pQXe2Q%3D%3D&sign_type=RSA2&timestamp=2018-04-27+10%3A13%3A05&version=1.0&sign=nJLSTDm2p%2BeYlKpYJIijSGCBPULgVhEX14zHHVCXP%2FUQGooPyiHq%2BzQjXT3xooYJIMhLauUUxigUe1jmZlrW0rZ63jFwn4omog58R9Hycm9cTldkt7Zr8Diiie3ORUCce2DnzKn8qvKzeNst%2BdplhQ8U5npK%2BZAluz%2BM5ss2S9HXYnqYKIhMAxY2btEpZaRaQimGioxrAJNTIrWK%2FiVn6hxx%2BuO%2BwLgDLgEKjBVXNpcJ2OIxO0%2FCICq5MAgY1eAi5xqP8xTcvJYZXubZkeKTI%2F%2BiztIfl2uwoVxzHOkVxZf8gHptcz33YlPLlMNUJKGAGYt878gDFBsD0MA2pQXe2Q%3D%3D";				
				resultMap.put("alyString", alyString);
				resultVo.setResult(resultMap);
								
			}
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; 
	}
	
	
	/**
	 * 添加商家代金券交易记录
	 * @param account
	 * @param storeId
	 * @param mobile
	 * @param amount
	 * @param payType
	 * @return
	 */
	@RequestMapping(value = "/v1_0/addMemberVonchersHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMemberVonchersHistory(String account,String mobile, Double amount, String payType){
		try{
			
			resultVo=tradeDubbo.addMemberVouchersHistory(account, mobile, amount, payType);
			if(resultVo.getCode()==200&&"1".equals(payType)){				
				Map<String, String> resultMap = new HashMap<>();
				GjfMemberVouchersTradeHistory vouchersHistory=(GjfMemberVouchersTradeHistory) resultVo.getResult();
				/*Map<String, String> params = OrderInfoUtil2_0.getAppPlayParams2_0(vouchersHistory.getTradeNo(), "赠送代金券",
						String.valueOf(vouchersHistory.getRealTreadeMoney().doubleValue()), "联盟商家赠送代金券",CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_VOUCHERS);
				//String orderParam = OrderInfoUtil2_0.buildOrderParam(params, false);
				String orderParam=AlipayCore.createLinkString(params);
				System.out.println("参数"+orderParam);
				
				String sign=RSA.sign(orderParam, AlipayConfig.RSA_PRIVATE, "utf-8");
				//String sign = OrderInfoUtil2_0.getSignApp(params, AlipayConfig.RSA_PRIVATE);
				params.put("sign_type", "RSA2");					
				String resultStr=OrderInfoUtil2_0.buildOrderParam(params,true);
				final String orderInfo = resultStr +"&sign=" +sign ;
				
				resultMap.put("alyString", orderInfo.trim());
				System.out.println("结果"+orderInfo.trim());*/
				String alyString=NewsOrderUtil.getPayBody(vouchersHistory.getTradeNo(), "赠送代金券",
						String.valueOf(vouchersHistory.getRealPayMoney().doubleValue()), "联盟商家赠送代金券",CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_VOUCHERS);
				//String alyString="alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017062707581858&biz_content=%7B%22body%22%3A%22%E8%B4%AD%E4%B9%B0%E5%95%86%E5%93%81%22%2C%22out_trade_no%22%3A%2220180427101301486574%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E4%B8%8B%E5%8D%95%E6%88%90%E5%8A%9F%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%221400.0%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Ftest.gzfzsw.top%2Fgjfeng-web-client%2Fwx%2Fnotify%2FpayOrderNotifyAply&sign=nJLSTDm2p%2BeYlKpYJIijSGCBPULgVhEX14zHHVCXP%2FUQGooPyiHq%2BzQjXT3xooYJIMhLauUUxigUe1jmZlrW0rZ63jFwn4omog58R9Hycm9cTldkt7Zr8Diiie3ORUCce2DnzKn8qvKzeNst%2BdplhQ8U5npK%2BZAluz%2BM5ss2S9HXYnqYKIhMAxY2btEpZaRaQimGioxrAJNTIrWK%2FiVn6hxx%2BuO%2BwLgDLgEKjBVXNpcJ2OIxO0%2FCICq5MAgY1eAi5xqP8xTcvJYZXubZkeKTI%2F%2BiztIfl2uwoVxzHOkVxZf8gHptcz33YlPLlMNUJKGAGYt878gDFBsD0MA2pQXe2Q%3D%3D&sign_type=RSA2&timestamp=2018-04-27+10%3A13%3A05&version=1.0&sign=nJLSTDm2p%2BeYlKpYJIijSGCBPULgVhEX14zHHVCXP%2FUQGooPyiHq%2BzQjXT3xooYJIMhLauUUxigUe1jmZlrW0rZ63jFwn4omog58R9Hycm9cTldkt7Zr8Diiie3ORUCce2DnzKn8qvKzeNst%2BdplhQ8U5npK%2BZAluz%2BM5ss2S9HXYnqYKIhMAxY2btEpZaRaQimGioxrAJNTIrWK%2FiVn6hxx%2BuO%2BwLgDLgEKjBVXNpcJ2OIxO0%2FCICq5MAgY1eAi5xqP8xTcvJYZXubZkeKTI%2F%2BiztIfl2uwoVxzHOkVxZf8gHptcz33YlPLlMNUJKGAGYt878gDFBsD0MA2pQXe2Q%3D%3D";
				resultMap.put("alyString", alyString);
				resultVo.setResult(resultMap);				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 添加商家赠送记录
	 * @return
	 */
	@RequestMapping(value = "/v1_0/addMerchantGiveHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMerchantGiveHistory(String account,String mobile,String type){
		try{			
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			resultVo=tradeDubbo.addMerchantGiveHistory(mobile, gjfMemberInfo.getId(), type);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 获取用户赠送历史记录
	 * @return
	 */
	@RequestMapping(value = "/v1_0/findMerchantGiveHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object findMerchantGiveHistory(String account) {
		try{
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			resultVo=tradeDubbo.findGiveMerchantModelByType("",gjfMemberInfo.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
		
	}
	
	/**
	 * 添加商家赠送记录
	 * @return
	 */
	@RequestMapping(value = "/v1_0/addMerchantRechargeToMemberHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMerchantRechargeToMemberHistory(String account,String mobile,String merchantType,double tradeMoney,String payType){
		try{
			
			resultVo=tradeDubbo.addMerchantRechargeToMemberHistory(account, mobile, tradeMoney, merchantType, payType);
			if(resultVo.getCode()==200&&"1".equals(payType)){
				Map<String, String> resultMap = new HashMap<>();
				GjfMerchantUpgradeHistory upgradeHistory=(GjfMerchantUpgradeHistory) resultVo.getResult();
				/*Map<String, String> params = OrderInfoUtil2_0.getAppPlayParams(upgradeHistory.getTradeNo(), "商家升级赠送",
						String.valueOf(upgradeHistory.getTradeMoney().doubleValue()), "联盟商家升级赠送", CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_MERCHANT_GIVE);
				String orderParam = OrderInfoUtil2_0.buildOrderParam(params, false);
				//String orderParam=AlipayCore.createLinkString(params);
				String sign = OrderInfoUtil2_0.getSign(params, AlipayConfig.RSA_PRIVATE);
				//String sign=RSA.sign(orderParam, AlipayConfig.RSA_PRIVATE, "utf-8");
				final String orderInfo = orderParam + "&" + sign + "&sign_type=\"RSA\"";
				resultMap.put("alyString", orderInfo.trim());*/
				String alyString=NewsOrderUtil.getPayBody(upgradeHistory.getTradeNo(), "商家升级赠送",
						String.valueOf(upgradeHistory.getTradeMoney().doubleValue()), "联盟商家升级赠送", CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_MERCHANT_GIVE);
				resultMap.put("alyString", alyString);
				resultVo.setResult(resultMap);	
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 获取用户赠送历史记录
	 * @return
	 */
	@RequestMapping(value = "/v1_0/findMerchantGiveToMemberHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object findMerchantGiveToMemberHistory(String account) {
		try{
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			resultVo=tradeDubbo.findMerchantRechargeToMemberHistory(gjfMemberInfo.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return 	resultVo;	
		
	}
	
	

	/**
	 * 获取用户赠送代金券历史记录
	 * @return
	 */
	@RequestMapping(value = "/v1_0/findMemberVoucherHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object findMemberVoucherHistory(String account) {
				
		Object o = storeInfoDubbo.findStoreByAccount(account).getResult();
		if (ObjValid.isNotValid(o)) {
			resultVo=new ResultVo(400, "店铺不存在", null);						
		}
		StoreInfoVo store = (StoreInfoVo) o;
		resultVo=tradeDubbo.findMemberVoucherHistory(store.getId());
		
		return resultVo;
	}
	
	/**
	 * 获取用户推荐奖励历史记录
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "/v1_0/findMemberDirectMemberMoney", method = RequestMethod.POST)
	@ResponseBody
	public Object findMemberDirectMemberMoney(String account){
		try{
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);			
			resultVo=tradeDubbo.findMemberDirectMemberMoney(gjfMemberInfo.getId());
		}catch(Exception e){
			e.printStackTrace();
		}			
		return resultVo;
	}
	
	/**
	 *把联盟商家奖励金额到凤凰宝
	 */
	@RequestMapping(value = "/v1_0/transderMerchantDirectMoneyToTreasure", method = RequestMethod.POST)
	@ResponseBody
	public Object transderMerchantDirectMoneyToTreasure(String account,double money) {
		try{			
			resultVo = tradeDubbo.addMerchantDirectMoneyToFhTreasure(account, money);
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; // TODO
	}
	
	
	/**
	 * 获取用户升级vip推荐奖励
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/v1_0/findMemberUpgradeDirectHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object findMemberUpgradeDirectHistory(Integer pageNo,Integer pageSize,String type,String account){
		Map<String, Object> model=new HashMap<>();
		try{			
			resultVo=tradeDubbo.findMemberUpgradeVipDirectHistory(pageNo, pageSize, account); 
			model.put("resultVo", resultVo.getResult());
			model.put("type", type);
			resultVo.setResult(model);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo; 
	}
	


}
