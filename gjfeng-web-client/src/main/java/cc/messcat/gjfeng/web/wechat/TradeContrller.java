package cc.messcat.gjfeng.web.wechat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.h5pay.api.H5PayUtil;
import com.wechat.WeixinUtil;
import com.wechat.popular.bean.pay.PayJsRequest;

import cc.messcat.gjfeng.common.constant.CommonProperties;
import cc.messcat.gjfeng.common.exception.LoggerPrint;
import cc.messcat.gjfeng.common.fastpay.payForOther.FastPayDemoTest;
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
import cc.messcat.gjfeng.util.SessionUtil;

@Controller
@RequestMapping("wx/trade/")
public class TradeContrller extends BaseController {

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
	
	@Autowired
	@Qualifier("enterpriseColumnDubbo")
	private GjfEnterpriseColumnDubbo enterpriseColumnDubbo;

	/**
	 * @描述 跳转到我要提现页面
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toDrawCash", method = RequestMethod.GET)
	public ModelAndView toDrawCash() {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			
			GjfFhTreasureInfo fh=(GjfFhTreasureInfo) tradeDubbo.findFhTreasureInfo(account).getResult();
			model.put("treasureMoney", fh.getFhTreasureMoney());
			// 查询用户最新的资金情况和所有的银行卡列表
			resultVo = tradeDubbo.toDrawCash(account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/withdrawal-cash", model);
	}

	/**
	 * 选择
	 * 
	 * @param bankId
	 * @return
	 */
	@RequestMapping(value = "choiceBankDrawCash", method = RequestMethod.GET)
	public ModelAndView choiceBankDrawCash(Long bankId, String type, String orderId, String payMoney, String retUrl) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			
			GjfFhTreasureInfo fh=(GjfFhTreasureInfo) tradeDubbo.findFhTreasureInfo(account).getResult();
			model.put("treasureMoney", fh.getFhTreasureMoney());
			// 查询用户最新的资金情况和所有的银行卡列表
			model.put("bankId", bankId);
			model.put("idCard", gjfMemberInfo.getIdCard());
			model.put("orderId", orderId);
			model.put("payMoney", payMoney);
			model.put("retUrl", retUrl);
			model.put("mobile", gjfMemberInfo.getMobile());
			resultVo = tradeDubbo.toDrawCash(account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		if ("1".equals(type)) {
			return new ModelAndView("wx/o2o-shop/withdrawal-cash", model);
		} else {
			return new ModelAndView("wx/o2o-shop/H5payConfirm", model);
		}

	}

	/**
	 * @描述 申请提现
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "addDrawCash", method = RequestMethod.POST)
	@ResponseBody
	public Object addDrawCash(@RequestParam("myBankId") Long myBankId, @RequestParam("money") String money,
			@RequestParam("remark") String remark) {
		try {
			// 先判断用户是否实名制
			String account = SessionUtil.getAccountSession(request);
			Double money0 = Double.valueOf(money);
			resultVo = tradeDubbo.addDrawCash(account, remark, myBankId, money0);

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到提现历史
	 * @author Karhs
	 * @date 2017年1月10日
	 * @param pageNo
	 * @param pageSize
	 * @param reqType
	 *            请求数据方式：0默认跳转到页面 1:页面下拉加载异步请求返回html
	 * @return
	 */
	@RequestMapping(value = "toDrawHistory", method = RequestMethod.GET)
	public ModelAndView toDrawHistory(Integer pageNo, Integer pageSize, Integer reqType) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			model.put("resultVo", tradeDubbo.findDrawCashHistory(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account));

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView(ObjValid.isNotValid(reqType) ? "wx/o2o-shop/withdraw-cash-history"
				: "wx/o2o-shop/withdraw-cash-history-ajax", model);
	}

	/**
	 * @描述 提现历史详情
	 * @author Karhs
	 * @date 2017年3月8日
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "toDrawHistoryDetail", method = RequestMethod.GET)
	public ModelAndView toDrawHistoryDetail(@RequestParam("tradeNo") String tradeNo) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			model.put("resultVo", tradeDubbo.findDrawCashHistoryDetail(tradeNo, account));
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/withdraw-cash-history-detail", model);
	}

	/**
	 * @描述 跳转到我的银行卡列表
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "myBanks", method = RequestMethod.GET)
	public ModelAndView myBanks(String type) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			// 先判断用户是否实名制
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			if (gjfMemberInfo.getIsReadName().equals("0")) {
				return new ModelAndView("wx/o2o-shop/my-real-name");
			}
			resultVo = tradeDubbo.findMyBankCard(account);
			model.put("type", type);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}

		if ((ObjValid.isValid(type) && Integer.parseInt(type) == 1)
				|| (ObjValid.isValid(type) && Integer.parseInt(type) == 3)) {
			return new ModelAndView("wx/o2o-shop/my-bank-card-withdrawal", model);
		} else {
			return new ModelAndView("wx/o2o-shop/my-bank-card", model);
		}

	}

	/**
	 * 获取用户银行卡号
	 * 
	 * @param myBankId
	 * @param money
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "getMyBank", method = RequestMethod.GET)
	@ResponseBody
	public Object getMyBank(Long bankId) {
		try {
			String account = SessionUtil.getAccountSession(request);
			if (BeanUtil.isValid(bankId)) {
				resultVo = tradeDubbo.findMemberBankById(bankId, account);
			} else {
				resultVo = tradeDubbo.findMyBankCard(account);
			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到绑定银行卡
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toBindBank", method = RequestMethod.GET)
	public ModelAndView toBindBank(String type, String orderId, String retUrl, String payMoney) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			// 先判断用户是否实名制
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			if (gjfMemberInfo.getIsReadName().equals("0")) {
				return new ModelAndView("wx/o2o-shop/my-real-name");
			}
			resultVo = tradeDubbo.findAllBank();
			model.put("orderId", orderId);
			model.put("payMoney", payMoney);
			model.put("mobile", gjfMemberInfo.getMobile());
			model.put("retUrl", retUrl);
			model.put("resultVo", resultVo);
			model.put("type", type);
			model.put("gjfMemberInfo", gjfMemberInfo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/my-bank-card-add", model);
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
	@RequestMapping(value = "bindBank", method = RequestMethod.POST)
	@ResponseBody
	public Object bindBank(@RequestParam("bankId") Long bankId, @RequestParam("bankSub") String bankSub,
			@RequestParam("bankCard") String bankCard, @RequestParam("holder") String holder,
			@RequestParam("cityValue") String cityValue) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.bindMyBankCard(account, bankId, bankSub, bankCard.trim(), holder, cityValue);
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
	@RequestMapping(value = "deleteMemBank", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteMemBank(@RequestParam("bankId") Long bankId) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.delMyBank(account, bankId);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到消费规则
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toTradeRule", method = RequestMethod.GET)
	public ModelAndView toTradeRule() {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		resultVo = memberInfoDubbo.findMemberByMobile(account);
		model.put("resultVo", resultVo);
		return new ModelAndView("wx/o2o-shop/my-wallet", model); // TODO
	}

	/**
	 * @描述 查询我的分红历史
	 * @author Karhs
	 * @date 2017年1月10日
	 * @param pageNo
	 * @param pageSize
	 * @param reqType
	 *            请求数据方式：0默认跳转到页面 1:页面下拉加载异步请求返回html
	 * @return
	 */
	@RequestMapping(value = "diviHis", method = RequestMethod.GET)
	public ModelAndView toDiviHistory(Integer pageNo, Integer pageSize, Integer reqType) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			model.put("resultVo", tradeDubbo.findTradeDiviHistory(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account, "1"));
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView(
				ObjValid.isNotValid(reqType) ? "wx/o2o-shop/my-divi-history" : "wx/o2o-shop/my-divi-history-ajax",
				model);
	}

	/**
	 * @描述 跳转到购买分红权
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toBuyDivi", method = RequestMethod.GET)
	public ModelAndView toBuyDivi() {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			// 查询当前商家的让利金额
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("resultVo", gjfMemberInfo.getCumulativeMoney().doubleValue());
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", 0.00);
		}
		return new ModelAndView("wx/o2o-shop/my-wallet", model); // TODO
	}

	/**
	 * @描述 购买分红权
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "buyDivi", method = RequestMethod.POST)
	@ResponseBody
	public Object buyDivi(@RequestParam("diviNum") Double diviNum, @RequestParam("payType") String payType,
			@RequestParam("reqStatus") String reqStatus) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.addDivi(account, diviNum, payType, reqStatus);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 商家购买分红权记录
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "buyDiviHistory", method = RequestMethod.GET)
	public ModelAndView buyDiviHistory(Integer pageNo, Integer pageSize, Integer reqType) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findTradeDivi(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account, "1");
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView(
				ObjValid.isNotValid(reqType) ? "wx/o2o-shop/buyBonus-history" : "wx/o2o-shop/buyBonus-history-ajax",
				model); // TODO
	}

	/**
	 * @描述 跳转到商家让利
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toBenefit", method = RequestMethod.GET)
	public ModelAndView toBenefit() {
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getType().equals("0")) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
			model.put("resultStatus", "1");
			return new ModelAndView("wx/o2o-shop/apply-waiting", model);
		}
		return new ModelAndView("wx/o2o-shop/my-benefit-give");
	}

	/**
	 * @描述 商家让利
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "addBenefit", method = RequestMethod.POST)
	@ResponseBody
	public Object addBenefit(@RequestParam("amount") double amount, @RequestParam("mobile") String mobile,
			@RequestParam("payType") String payType,String merchantGrade) {
		try {
			//获取用户电话号码
			String account = SessionUtil.getAccountSession(request);
			//获取商户店铺信息
			Object o = storeInfoDubbo.findStoreByAccount(account).getResult();
			//如果店铺信息为空返回提示
			if (ObjValid.isNotValid(o)) {
				resultVo = new ResultVo(400, "店铺不存在", null);
				//返回信息
				return resultVo;
			}
			//把Object 转为StoreInfoVo
			StoreInfoVo store = (StoreInfoVo) o;
			//添加商家让利信息
			resultVo = tradeDubbo.addStoreBenefit(account, store.getId(), mobile, amount, payType,merchantGrade);
			// 如果支付方式为微信
			if (payType.equals("0") && resultVo.getCode() == 200) {
				//获取商家让利细心
				MemberTradeBenefitVo benefitVo = (MemberTradeBenefitVo) resultVo.getResult();
				//获取用户细心
				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				
				// 利楚
				// PosPrepayRe
				// posPrepayRe=PrePayUtil.posPrePayRe(payInfo,request,benefitVo.getTradeNo(),benefitVo.getBenefitMoney().toString(),"商户让利",info.getOpenId(),"http://xj.gjfeng.net/gjfeng-web-client/wx/notify/payBenefitNotifyLiChu");
				// resultVo.setResult(posPrepayRe);
				//获取调起微信支付需要的参数
				PayJsRequest json = WeixinUtil.unifiedorderResult(request, benefitVo.getTradeNo(), "商户让利", "JSAPI",
						benefitVo.getBenefitMoney().toString(), CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_BENEFIT,
						info.getOpenId());
				System.out.println(json);
				//把参数添加到结果集
				resultVo.setResult(json);
			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}

	/**
	 * @描述 商家让利(网银)
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	PrimeUnionThread primeUnionThread = null;

	@RequestMapping(value = "addBenefitByH5", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addBenefitByH5(@RequestParam("amount") double amount, @RequestParam("mobile") String mobile,
			@RequestParam("payType") String payType,String merchartGrade) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			// 添加让利记录并调支付接口
			String account = SessionUtil.getAccountSession(request);
			StoreInfoVo store = (StoreInfoVo) storeInfoDubbo.findStoreByAccount(account).getResult();
			resultVo = tradeDubbo.addStoreBenefit(account, store.getId(), mobile, amount, payType,merchartGrade);

			if (payType.equals("2") && resultVo.getCode() == 200) {
				MemberTradeBenefitVo benefitVo = (MemberTradeBenefitVo) resultVo.getResult();

				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				// 网银支付方式1
				/*
				 * Map<String, String> map = H5PayUtil.H5PayResult(request,
				 * response, info, benefitVo.getBenefitMoney().toString(),
				 * benefitVo.getTradeNo(), "商家让利", "商家让利描述",
				 * CommonProperties.GJFENG_H5_NOTIFY_BENETI);
				 * resultVo.setResult(map); model.put("obj", map);
				 * primeUnionThread=new
				 * PrimeUnionThread(benefitVo.getTradeNo());
				 * primeUnionThread.start();
				 */
				// 网银支付方式2
				model.put("orderId", benefitVo.getTradeNo());
				model.put("payMoney", benefitVo.getBenefitMoney().doubleValue());
				model.put("isReadName", info.getIsReadName());
				model.put("idCard", info.getIdCard());
				model.put("mobile", info.getMobile());
				model.put("retUrl", CommonProperties.GJFENG_WANGYIN_PAY_NOTIFY_URL_BENERFIT);
				// 查询用户最新的资金情况和所有的银行卡列表
				resultVo = tradeDubbo.toDrawCash(account);
				model.put("resultVo", resultVo);
			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		// return new ModelAndView("wx/online-shop/H5cashier", model);
		return new ModelAndView("wx/o2o-shop/H5payConfirm", model);
	}

	// 线程
	class PrimeUnionThread extends Thread {
		private String out_trade_on;

		PrimeUnionThread(String out_trade_on) {
			this.out_trade_on = out_trade_on;
		}

		public void run() {
			try {
				for (int i = 0; i < 3; i++) {
					if (i == 0) {
						Thread.sleep(180000);
					}
					if (i == 1) {
						Thread.sleep(480000);
					}

					String xml = H5PayUtil.queryH5Order(request, out_trade_on);
					String[] str = xml.split("&");
					Map<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < str.length; j++) {
						String str0 = str[j];
						String[] str1 = str0.split("=");
						if (str1.length == 1) {
							map.put(str1[0], "");
						} else {
							for (int k = 0; k < str1.length; k++) {
								map.put(str1[0], str1[1]);
							}
						}
					}
					/* "000000".equals(map.get("retCode"))&& */
					if ("0".equals(map.get("status"))) {
						String out_trade_no = map.get("merchOrderId");
						String trade_no = map.get("orderId");
						resultVo = tradeDubbo.updateStoreBenefitPayStatus(out_trade_no, trade_no, "1");
						if (resultVo.getCode() == 200) {
							primeUnionThread.interrupt();
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @描述 商家让利记录
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "benefits", method = RequestMethod.GET)
	public ModelAndView benefits(Integer pageNo, Integer pageSize, Integer reqType) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getType().equals("0")) {
				model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
				model.put("resultStatus", "1");
				return new ModelAndView("wx/o2o-shop/apply-waiting", model);
			}
			Object o = storeInfoDubbo.findStoreByAccount(account).getResult();
			if (ObjValid.isNotValid(o)) {
				model.put("resultVo", new ResultVo(400, "店铺不存在", null));
				return new ModelAndView("wx/o2o-shop/apply-waiting", model);
			}
			StoreInfoVo store = (StoreInfoVo) o;
			resultVo = tradeDubbo.findStoreBenefit(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, store.getId());
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", null);
		}
		return new ModelAndView(ObjValid.isNotValid(reqType) ? "wx/o2o-shop/my-benefit-give-history"
				: "wx/o2o-shop/my-benefit-give-history-ajax", model);
	}

	/**
	 * @描述 消费列表
	 * @author Karhs
	 * @date 2017年3月1日
	 * @param pageNo
	 * @param pageSize
	 * @param reqType
	 * @return
	 */
	@RequestMapping(value = "cumulative", method = RequestMethod.GET)
	public ModelAndView cumulative(Integer pageNo, Integer pageSize, Integer reqType) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findMemberBenefit(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", null);
		}
		return new ModelAndView(ObjValid.isNotValid(reqType) ? "wx/o2o-shop/my-benefit-give-history"
				: "wx/o2o-shop/my-benefit-give-history-ajax", model);
	}

	/**
	 * @描述 跳转到我的代理
	 * @author Karhs
	 * @date 2017年2月20日
	 * @return
	 */
	@RequestMapping(value = "agent", method = RequestMethod.GET)
	public ModelAndView agent() {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getIdentity().equals("0")) {
				model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
				model.put("resultStatus", "1");
				return new ModelAndView("wx/o2o-shop/apply-waiting", model);
			}
			resultVo = tradeDubbo.findAgentInfo(account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/agent", model);
	}

	/**
	 * @描述 跳转代理分红记录
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toAgentHistory", method = RequestMethod.GET)
	public ModelAndView agentHistory() {
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getIdentity().equals("0")) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
			model.put("resultStatus", "1");
			return new ModelAndView("wx/o2o-shop/apply-waiting", model);
		}
		return new ModelAndView("wx/o2o-shop/agent-history");
	}

	/**
	 * @描述 代理分红记录
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "agentHistory", method = RequestMethod.GET)
	public ModelAndView agentHistory(Integer pageNo, Integer pageSize) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findAgentHistory(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", null);
		}
		return new ModelAndView("wx/o2o-shop/agent-history-ajax", model);
	}

	/**
	 * @描述 查询下级代理
	 * @author Karhs
	 * @date 2017年2月27日
	 * @param pageNo
	 * @param pageSize
	 * @param reqType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "findNextAgent", method = RequestMethod.GET)
	public ModelAndView findNextAgent(Integer pageNo, Integer pageSize, Integer reqType) {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findNextAgent(account, ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize);
			dataMap = (Map<String, Object>) resultVo.getResult();
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", null);
		}
		String urlPath = "wx/o2o-shop/my-agent-list(qu)";
		if (!dataMap.isEmpty() && dataMap.get("agentType").equals("6")) {
			urlPath = "wx/o2o-shop/my-agent-list(ge)";
		}
		return new ModelAndView(ObjValid.isNotValid(reqType) ? urlPath : urlPath + "-ajax", model);
	}

	/**
	 * @描述 我的钱包---福利记录
	 * @return
	 */
	@RequestMapping(value = "getSalesWelfare", method = RequestMethod.GET)
	public ModelAndView getSalesWelfare() {
		Map<String, Object> model = new HashMap<>();
		try {
			String account = SessionUtil.getAccountSession(request);
			String type = "0";
			resultVo = tradeDubbo.findMemberSalesWelfare(account, type);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/person-2", model);
	}

	/**
	 * @描述 我的钱包---福利权益
	 * @return
	 */
	@RequestMapping(value = "getParticipate", method = RequestMethod.GET)
	public ModelAndView getParticipate(String type) {
		Map<String, Object> model = new HashMap<>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findMemberParticipate(account, type);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/person-1", model);
	}

	/**
	 * @描述 我的钱包---累积消费
	 * @return
	 */
	@RequestMapping(value = "getInterests", method = RequestMethod.GET)
	public ModelAndView getInterests(String type) {
		Map<String, Object> model = new HashMap<>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findMemberInterests(account, type);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/person-3", model);
	}

	/**
	 * 查询用户最近七天之内的让利数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "findBenefitByTime", method = RequestMethod.GET)
	@ResponseBody
	public Object findBenefitByTime() {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.findBenefitByTime(account);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
		}
		return resultVo;
	}

	/**
	 * 用户确认让利提示信息
	 * 
	 * @param tradeNo
	 * @return
	 */
	@RequestMapping(value = "updateBenefitConfirmStatus", method = RequestMethod.GET)
	@ResponseBody
	public Object updateBenefitConfirmStatus(String tradeNo) {
		try {

			resultVo = tradeDubbo.modifyBenefitConfirmStatus(tradeNo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, MemberController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到我的银行卡列表
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "myBanksForPay", method = RequestMethod.GET)
	public ModelAndView myBanksForPay(String orderId, String payMoney, String mobile, String retUrl,String account) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			// 先判断用户是否实名制
			
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			if (gjfMemberInfo.getIsReadName().equals("0")) {
				return new ModelAndView("wx/o2o-shop/my-real-name");
			}
			resultVo = tradeDubbo.findMyBankCard(account);
			model.put("orderId", orderId);
			model.put("payMoney", payMoney);
			model.put("mobile", mobile);
			model.put("retUrl", retUrl);
			model.put("type", "3");
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/my-bank-card-withdrawal", model);

	}
	
	/**
	 * 分红转移
	 * @return
	 */
	@RequestMapping(value = "transferDividendsMoney", method = RequestMethod.POST)
	@ResponseBody
	public Object transferDividendsMoney(){
		try{
			String account = SessionUtil.getAccountSession(request);
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
	@RequestMapping(value = "transferPage", method = RequestMethod.GET)
	public Object transferPage(){
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			if (member.getIsReadName().equals("0")) {
				return new ModelAndView("wx/o2o-shop/my-real-name");
			}
			memberInfoDubbo.findSetBaseInfoByKey("POUNDAGE").getResult();
			model.put("member", member);
			model.put("baseInfo", memberInfoDubbo.findSetBaseInfoByKey("POUNDAGE").getResult());
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("wx/o2o-shop/my-transfer-point", model);
		
	}
	
	/**
	 * 转移积分
	 * @param type
	 * @param memberMobile
	 * @param transferMemberMobile
	 * @param transferMoney
	 * @return
	 */
	@RequestMapping(value = "memberPointTransfer", method = RequestMethod.POST)
	@ResponseBody
	public Object memberPointTransfer(String type,String payPassword,String transferMemberMobile,BigDecimal transferMoney){
		try{			
			String account = SessionUtil.getAccountSession(request);
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
	 * 跳转到合并用户信息页面
	 * @return
	 */
	@RequestMapping(value = "mergePage", method = RequestMethod.GET)
	public Object mergePage(){
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			if (member.getIsReadName().equals("0")) {
				return new ModelAndView("wx/o2o-shop/my-real-name");
			}
			memberInfoDubbo.findSetBaseInfoByKey("POUNDAGE").getResult();
			model.put("member", member);
			model.put("baseInfo", memberInfoDubbo.findSetBaseInfoByKey("POUNDAGE").getResult());
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("wx/o2o-shop/my-merge-memberInfo", model);
		
	}
	
	/**
	 * 获取转移历史记录
	 * @param pageNo
	 * @param pageSize
	 * @param type
	 * @return
	 */
	@RequestMapping(value="findTransferHistory",method=RequestMethod.GET)
	public Object findTransferHistory(Integer pageNo,Integer pageSize,String type){
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo=tradeDubbo.findTransferHistoryByPager(pageNo, pageSize, account, type);
			model.put("resultVo", resultVo);
		}catch(Exception e){
			e.printStackTrace();
		}
		if("0".equals(type)){
			return new ModelAndView("wx/o2o-shop/member-transfer-history", model);
		}else{
			return new ModelAndView("wx/o2o-shop/member-merge-history", model);
		}
	}
	
	/**
	 * 跳转到凤凰宝页面
	 */
	@RequestMapping(value = "toFhTreasurePage", method = RequestMethod.GET)
	public ModelAndView toFhTreasurePage() {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		model.put("isReadName", gjfMemberInfo.getIsReadName());
		if (gjfMemberInfo.getIsReadName().equals("0")) {
			return new ModelAndView("wx/o2o-shop/my-real-name");
		}
		resultVo = tradeDubbo.findFhTreasureInfo(account);
		model.put("resultVo", resultVo);
		return new ModelAndView("wx/o2o-shop/fh-treasure/fh-treasure-index", model); // TODO
	}
	
	
	/**
	 * @描述 跳转到转移余额到凤凰宝
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toBalanceTransferPage", method = RequestMethod.GET)
	public ModelAndView toBalanceTransferPage() {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("memberInfo", gjfMemberInfo);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			// 查询用户最新的资金情况和所有的银行卡列表
			resultVo = tradeDubbo.toDrawCash(account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/fh-treasure/banlance-to-fh-treasure", model);
	}

			
	/**
	 *把余额转移到凤凰宝
	 */
	@RequestMapping(value = "transderBalanceToTreasure", method = RequestMethod.POST)
	@ResponseBody
	public Object transderBalanceToTreasure(double money) {
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.addBalanceToFhTreasure(account, money);
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; // TODO
	}
	
	/**
	 * 跳转到凤凰宝转移页面
	 * @return
	 */
	@RequestMapping(value = "toTransferFhTreasurePage", method = RequestMethod.GET)
	public ModelAndView toTransferFhTreasurePage() {
		Map<String, Object> model = new HashMap<String, Object>();		
		return new ModelAndView("wx/o2o-shop/fh-treasure/fh-treasure-transfer", model);
	}
	
	
	/**
	 *把余额转移到凤凰宝
	 */
	@RequestMapping(value = "transferFhTreasurePage", method = RequestMethod.POST)
	@ResponseBody
	public Object transferFhTreasurePage(String mobile,String payPassword,double tradeMoney) {
		try{
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo info = memberInfoDubbo.findMember(account);
			if (StringUtil.isBlank(info.getPayPassword())) {
				resultVo = new ResultVo(402, "请先设置支付密码", null);
				return resultVo;
			}
			if (!info.getPayPassword().equals(EncryptionUtil.getMD5Code(info.getMobile() + payPassword))) {
				resultVo = new ResultVo(401, "支付密码错误", null);
				return resultVo;
			}
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
	@RequestMapping(value = "toFhTreasureDrawCash", method = RequestMethod.GET)
	public ModelAndView toFhTreasureDrawCash() {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			GjfFhTreasureInfo fh=(GjfFhTreasureInfo) tradeDubbo.findFhTreasureInfo(account).getResult();
			model.put("treasureMoney", fh.getFhTreasureMoney());
			
			GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
			model.put("isReadName", gjfMemberInfo.getIsReadName());
			// 查询用户最新的资金情况和所有的银行卡列表
			resultVo = tradeDubbo.toDrawCash(account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/withdrawal-cash", model);
	}
	
	/**
	 * 凤凰宝提现
	 * @param myBankId
	 * @param money
	 * @param remark
	 * @return
	 */
	@RequestMapping(value = "addFhTreasureDrawCash", method = RequestMethod.POST)
	@ResponseBody
	public Object addFhTreasureDrawCash(@RequestParam("myBankId") Long myBankId, @RequestParam("money") String money,
			@RequestParam("remark") String remark) {
		try {
			// 先判断用户是否实名制
			String account = SessionUtil.getAccountSession(request);
			Double money0 = Double.valueOf(money);
			resultVo = tradeDubbo.addFhTreasureDrawCash(account, remark, myBankId, money0);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
		}
		return resultVo;
	}
	
	
	/**
	 * @描述 跳转到凤凰宝提现页面
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toFhTreasureTradeHistory", method = RequestMethod.GET)
	public ModelAndView toFhTreasureTradeHistory(Integer pageNo,Integer pageSize) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo=tradeDubbo.findFhTreaureTradeHistory(pageNo, pageSize, account);						
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/fh-treasure/fh-treasure-trade-history", model);
	}
	
	
	/**
	 *统计用户复消金额
	 */
	@RequestMapping(value = "countMemberNiceConMoney", method = RequestMethod.GET)
	@ResponseBody
	public Object countMemberNiceConMoney() {
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo = memberInfoDubbo.countMemberNiceConMoney(account);
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; // TODO
	}
	
	
	/**
	 *获取用户信息
	 */
	@RequestMapping(value = "findMemberInfo", method = RequestMethod.GET)
	@ResponseBody
	public Object findMemberInfo() {
		try{
			String account = SessionUtil.getAccountSession(request);
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			resultVo = new ResultVo(200, "获取成功", member);
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; // TODO
	}
	
	
	
	/**
	 * 跳转商家联盟采购页面
	 * @return
	 */
	@RequestMapping(value="goMerchantProcurementPage",method=RequestMethod.GET)
	public ModelAndView goMerchantProcurementPage(String discount){
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String account = SessionUtil.getAccountSession(request);
			//获取用户信息
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			System.out.println(member.getMerchantType());
			if("1".equals(member.getMerchantType())){//如果为标准版商户
				model.put("products", productInfoDubbo.findMerchantProcurementProductInfo(null,null, 1, 20,null));
				model.put("column", enterpriseColumnDubbo.findProductModelColumn("MODELPRODUCT","0"));
				model.put("merchantType", "1");	
				model.put("discount", discount);
				return new ModelAndView("wx/online-shop/voucher/mechantModel", model);
			}else if("2".equals(member.getMerchantType())||"3".equals(member.getMerchantType())||"4".equals(member.getMerchantType())||"5".equals(member.getMerchantType())||"6".equals(member.getMerchantType())||"7".equals(member.getMerchantType())){//如果为尊享版商户
				model.put("products", productInfoDubbo.findMerchantProcurementProductInfo(null,null, 1, 20,discount));
				model.put("merchantType", "2");
				model.put("discount", discount);
				model.put("column", enterpriseColumnDubbo.findProductModelColumn("MODELPRODUCT","0"));
				return new ModelAndView("wx/online-shop/voucher/mechantModel", model);
			}else if("0".equals(member.getMerchantType())){//如果为普通商户
				model.put("member", member);
				return new ModelAndView("wx/o2o-shop/top-up-merchant-model", model);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 跳转商家联盟采购页面
	 * @return
	 */
	@RequestMapping(value="goMerchantUpgrade",method=RequestMethod.GET)
	public ModelAndView goMerchantUpgrade(){
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String account = SessionUtil.getAccountSession(request);
			//获取用户信息
			GjfMemberInfo member=memberInfoDubbo.findMember(account);	
			/*if (ObjValid.isNotValid(member) || member.getType().equals("0")) {				
				model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
				model.put("resultStatus", "1");
				return new ModelAndView("wx/o2o-shop/apply-waiting", model);
			}*/
			model.put("member", member);					
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("wx/o2o-shop/top-up-merchant-model", model);
	}
	
	/**
	 * 查询分类商品
	 * @param columnId
	 * @param pageNo
	 * @param pageSize
	 * @param likeName
	 * @return
	 */
	@RequestMapping(value="findModelProduct",method=RequestMethod.GET)
	@ResponseBody
	public Object findModelProduct(Long columnId,Integer pageNo,Integer pageSize,String likeName,String discount){		
		try{
			 resultVo=productInfoDubbo.findMerchantProcurementProductInfo(columnId,likeName, pageNo, pageSize,discount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 采购商品分类
	 * @return
	 */
	@RequestMapping(value="getVouchersByPage",method=RequestMethod.GET)
	public ModelAndView getVouchersByPage(Long columnId,Integer pageNo,Integer pageSize,String likeName,String type,String discount){
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String account = SessionUtil.getAccountSession(request);
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
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("wx/online-shop/voucher/goods-list-ajax", model);
	}
	
	
	/**
	 *添加商户充值记录
	 */
	@RequestMapping(value = "addMerchantRechargeHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMerchantRechargeHistory(String merchantType,String payType,double tradeMoney) {
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo=tradeDubbo.addMerchantRechargeHistory(account, tradeMoney, merchantType, payType);	
			if(resultVo.getCode()==200&&"0".equals(payType)){
				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				GjfMerchantUpgradeHistory upgradeHistory=(GjfMerchantUpgradeHistory) resultVo.getResult();
				PayJsRequest json = WeixinUtil.unifiedorderResult(request, upgradeHistory.getTradeNo(), "商户充值", "JSAPI",
						upgradeHistory.getTradeMoney().toString(), CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_MERCAHNET_RECHARGE,
						info.getOpenId());
				System.out.println(json);
				resultVo.setResult(json);
			}
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; 
	}
	
	/**
	 * @描述 跳转到商家代金券页面
	 * @author Karhs
	 * @date 2017年1月9日
	 * @return
	 */
	@RequestMapping(value = "toVouchers", method = RequestMethod.GET)
	public ModelAndView toVouchers() {
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		Map<String, Object> model = new HashMap<String, Object>();
		if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getType().equals("0")) {
			
			model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
			model.put("resultStatus", "1");
		
			return new ModelAndView("wx/o2o-shop/apply-waiting", model);
		}
		model.put("merchantType", gjfMemberInfo.getMerchantType());
		return new ModelAndView("wx/o2o-shop/my-vouchers-give",model);
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
	@RequestMapping(value = "addMemberVonchersHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMemberVonchersHistory(String mobile, Double amount, String payType){
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo=tradeDubbo.addMemberVouchersHistory(account, mobile, amount, payType);
			if(resultVo.getCode()==200&&"0".equals(payType)){
				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				GjfMemberVouchersTradeHistory vouchersHistory=(GjfMemberVouchersTradeHistory) resultVo.getResult();
				PayJsRequest json = WeixinUtil.unifiedorderResult(request, vouchersHistory.getTradeNo(), "商户赠送代金券", "JSAPI",
						vouchersHistory.getRealPayMoney().toString(), CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_VOUCHERS,
						info.getOpenId());
				System.out.println(json);
				resultVo.setResult(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 跳转到商家赠送页面
	 * @return
	 */
	@RequestMapping(value = "toMerchantGive", method = RequestMethod.GET)
	public ModelAndView toMerchantGive(String type) {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		/*GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getType().equals("0")) {		
			model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
			model.put("resultStatus", "1");			
			return new ModelAndView("wx/o2o-shop/apply-waiting", model);
		}*/
		model.put("type", type);
		return new ModelAndView("wx/o2o-shop/my-merchants-give",model);
	}
	
	/**
	 * 添加商家赠送记录
	 * @return
	 */
	@RequestMapping(value = "addMerchantGiveHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMerchantGiveHistory(String mobile,String type){
		try{
			String account = SessionUtil.getAccountSession(request);
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
	@RequestMapping(value = "findMerchantGiveHistory", method = RequestMethod.GET)
	public ModelAndView toMerchantGive() {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		
		resultVo=tradeDubbo.findGiveMerchantModelByType("",gjfMemberInfo.getId());
		model.put("resultVo", resultVo);
		return new ModelAndView("wx/o2o-shop/my-merchants-give-history",model);
	}
	
	
	
	/**
	 * 跳转到商家赠送页面
	 * @return
	 */
	@RequestMapping(value = "toMerchantGiveToMember", method = RequestMethod.GET)
	public ModelAndView toMerchantGiveToMember() {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		
		model.put("gjfMemberInfo", gjfMemberInfo);
		return new ModelAndView("wx/o2o-shop/member-upgrade-give",model);
	}
	
	/**
	 * 添加商家赠送记录
	 * @return
	 */
	@RequestMapping(value = "addMerchantRechargeToMemberHistory", method = RequestMethod.POST)
	@ResponseBody
	public Object addMerchantRechargeToMemberHistory(String mobile,String merchantType,double tradeMoney,String payType){
		try{
			String account = SessionUtil.getAccountSession(request);
			
			resultVo=tradeDubbo.addMerchantRechargeToMemberHistory(account, mobile, tradeMoney, merchantType, payType);
			if(resultVo.getCode()==200&&"0".equals(payType)){
				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				GjfMerchantUpgradeHistory upgradeHistory=(GjfMerchantUpgradeHistory) resultVo.getResult();
				PayJsRequest json = WeixinUtil.unifiedorderResult(request, upgradeHistory.getTradeNo(), "会员升级赠送", "JSAPI",
						upgradeHistory.getTradeMoney().toString(), CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_MERCHANT_GIVE,
						info.getOpenId());
				System.out.println(json);
				resultVo.setResult(json);
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
	@RequestMapping(value = "findMerchantGiveToMemberHistory", method = RequestMethod.GET)
	public ModelAndView findMerchantGiveToMemberHistory() {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		
		resultVo=tradeDubbo.findMerchantRechargeToMemberHistory(gjfMemberInfo.getId());
		model.put("resultVo", resultVo);
		return new ModelAndView("wx/o2o-shop/member-upgrade-give-history",model);
	}
	
	/**
	 * 获取用户赠送代金券历史记录
	 * @return
	 */
	@RequestMapping(value = "findMemberVoucherHistory", method = RequestMethod.GET)
	public ModelAndView findMemberVoucherHistory() {
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		
		if (ObjValid.isNotValid(gjfMemberInfo) || gjfMemberInfo.getType().equals("0")) {
			model.put("resultVo", new ResultVo(400, "您没有权限访问该页面", null));
			model.put("resultStatus", "1");
			return new ModelAndView("wx/o2o-shop/apply-waiting", model);
		}
		Object o = storeInfoDubbo.findStoreByAccount(account).getResult();
		if (ObjValid.isNotValid(o)) {
			model.put("resultVo", new ResultVo(400, "店铺不存在", null));
			return new ModelAndView("wx/o2o-shop/apply-waiting", model);
		}
		StoreInfoVo store = (StoreInfoVo) o;
		resultVo=tradeDubbo.findMemberVoucherHistory(store.getId());
		model.put("resultVo", resultVo);
		return new ModelAndView("wx/o2o-shop/my-vouchers-give-history",model);
	}
	
	/**
	 * 获取用户推荐奖励历史记录
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "findMemberDirectMemberMoney", method = RequestMethod.GET)
	public ModelAndView findMemberDirectMemberMoney(Long memberId){
		Map<String, Object> model = new HashMap<String, Object>();
		String account = SessionUtil.getAccountSession(request);
		GjfMemberInfo gjfMemberInfo = memberInfoDubbo.findMember(account);
		
		resultVo=tradeDubbo.findMemberDirectMemberMoney(gjfMemberInfo.getId());
		model.put("resultVo", resultVo);
		return new ModelAndView("wx/o2o-shop/member-direct-money-history",model);
	}
	
	
	/**
	 *把联盟商家奖励金额到凤凰宝
	 */
	@RequestMapping(value = "transderMerchantDirectMoneyToTreasure", method = RequestMethod.POST)
	@ResponseBody
	public Object transderMerchantDirectMoneyToTreasure(double money) {
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo = tradeDubbo.addMerchantDirectMoneyToFhTreasure(account, money);
		}catch(Exception e){
			resultVo = LoggerPrint.getResult(e, TradeContrller.class);			
		}				
		return resultVo; // TODO
	}
	
	
	/**
	 * 跳到指定联盟商家页面
	 * @return
	 */
	@RequestMapping(value = "goSpecifiedMerchantOnilne", method = RequestMethod.GET)
	public ModelAndView goSpecifiedMerchantOnilne(String merchantType){
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			System.out.println("ddd"+account);
			//获取用户信息
			GjfMemberInfo member=memberInfoDubbo.findMember(account);
			//System.out.println(member.getMerchantType());
			if("1".equals(member.getMerchantType())){//如果为标准版商户
				model.put("products", productInfoDubbo.findMerchantProcurementProductInfo(null,null, 1, 20,null));
				model.put("column", enterpriseColumnDubbo.findProductModelColumn("MODELPRODUCT","0"));
				model.put("merchantType", "1");					
				return new ModelAndView("wx/online-shop/voucher/mechantModel", model);
			}else if("2".equals(member.getMerchantType())||"3".equals(member.getMerchantType())||"4".equals(member.getMerchantType())||"5".equals(member.getMerchantType())||"6".equals(member.getMerchantType())||"7".equals(member.getMerchantType())){//如果为尊享版商户
				model.put("products", productInfoDubbo.findMerchantProcurementProductInfo(null,null, 1, 20,null));
				model.put("merchantType", "2");			
				model.put("column", enterpriseColumnDubbo.findProductModelColumn("MODELPRODUCT","0"));
				return new ModelAndView("wx/online-shop/voucher/mechantModel", model);
			}else if("0".equals(member.getMerchantType())){//如果为普通商户
				model.put("member", member);
				model.put("type", merchantType);
				return new ModelAndView("wx/online-shop/voucher/top-up-merchant-model", model);
			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, IndexController.class);			
			model.put("resultVo",null);
		}
		return null;
	}
	
	/**
	 * 获取用户升级vip推荐奖励
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "findMemberUpgradeDirectHistory", method = RequestMethod.GET)
	public ModelAndView findMemberUpgradeDirectHistory(Integer pageNo,Integer pageSize,String type){
		Map<String, Object> model=new HashMap<>();
		try{
			String account = SessionUtil.getAccountSession(request);
			resultVo=tradeDubbo.findMemberUpgradeVipDirectHistory(pageNo, pageSize, account); 
			model.put("resultVo", resultVo);
			model.put("type", type);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("wx/o2o-shop/member-upgrade-vip-direct-history", model);
	}
	
}
