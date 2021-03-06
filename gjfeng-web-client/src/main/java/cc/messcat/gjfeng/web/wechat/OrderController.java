package cc.messcat.gjfeng.web.wechat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.alipay.direct.config.AlipayConfig;
import com.alipay.direct.util.OrderInfoUtil2_0;
import com.h5pay.api.H5PayUtil;
import com.wechat.WeixinUtil;
import com.wechat.popular.bean.pay.PayJsRequest;

import cc.messcat.gjfeng.common.constant.CommonProperties;
import cc.messcat.gjfeng.common.exception.LoggerPrint;
import cc.messcat.gjfeng.common.jd.JdNewUtil;
import cc.messcat.gjfeng.common.jd.JdUtil;
import cc.messcat.gjfeng.common.jd.bean.TrackResult;
import cc.messcat.gjfeng.common.util.BeanUtil;
import cc.messcat.gjfeng.common.util.DateHelper;
import cc.messcat.gjfeng.common.util.EncryptionUtil;
import cc.messcat.gjfeng.common.util.ObjValid;
import cc.messcat.gjfeng.common.util.RandUtil;
import cc.messcat.gjfeng.common.util.StringUtil;
import cc.messcat.gjfeng.common.vo.app.OrderAddModel;
import cc.messcat.gjfeng.common.vo.app.OrderAddVo;
import cc.messcat.gjfeng.common.vo.app.OrderInfoVo;
import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.common.web.BaseController;
import cc.messcat.gjfeng.dubbo.core.GjfAddressDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfCartInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfMemberInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfOrderInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo;
import cc.messcat.gjfeng.entity.GjfFhTreasureInfo;
import cc.messcat.gjfeng.entity.GjfMemberInfo;
import cc.messcat.gjfeng.entity.GjfMemberTrade;
import cc.messcat.gjfeng.entity.GjfOrderInfo;
import cc.messcat.gjfeng.entity.GjfWeiXinPayInfo;
import cc.messcat.gjfeng.util.SessionUtil;

@Controller
@RequestMapping("wx/order/")
public class OrderController extends BaseController {

	@Autowired
	@Qualifier("request")
	private HttpServletRequest request;

	@Autowired
	@Qualifier("response")
	private HttpServletResponse response;

	@Autowired
	@Qualifier("orderInfoDubbo")
	private GjfOrderInfoDubbo orderInfoDubbo;

	@Autowired
	@Qualifier("addressDubbo")
	private GjfAddressDubbo addressDubbo;

	@Autowired
	@Qualifier("memberInfoDubbo")
	private GjfMemberInfoDubbo memberInfoDubbo;

	@Autowired
	@Qualifier("cartInfoDubbo")
	private GjfCartInfoDubbo cartInfoDubbo;

	@Autowired
	@Qualifier("tradeDubbo")
	private GjfTradeDubbo tradeDubbo;

	/**
	 * @描述 跳转到下单
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "toAdd", method = RequestMethod.GET)
	public ModelAndView toAddOrder(OrderAddModel orderAddModel, Long orderAddressId, String goodSource,String logist) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			/*
			 * if(!BeanUtil.isValid(account)){ return new
			 * ModelAndView("app/app-register", model); }
			 */

			// 查询当前用户的最新信息
			GjfMemberInfo member = memberInfoDubbo.findMember(account);
			if ("1".equals(member.getMerchantType())) {
				model.put("merchantType", "1");
			} else if ("0".equals(member.getMerchantType())) {
				model.put("merchantType", "0");
			} else {
				model.put("merchantType", "2");
			}

			List<OrderAddVo> orderAddVos = orderAddModel.getOrderAddVos();
			model.put("gjfMemberInfo", member);
			// 查询用户凤凰宝信息
			GjfFhTreasureInfo fh = (GjfFhTreasureInfo) tradeDubbo.findFhTreasureInfo(account).getResult();
			if (!BeanUtil.isValid(fh)) {
				model.put("fhTreasureInfo", 0);
			} else {
				model.put("fhTreasureInfo", fh.getFhTreasureMoney());
			}

			if (ObjValid.isValid(orderAddressId)) {
				model.put("memberDefAddress", addressDubbo.findAddressById(orderAddressId, account, goodSource));
			} else {
				// 查询用户默认的收货地址
				model.put("memberDefAddress", addressDubbo.findMyDefDeliveryAddress(account, goodSource));
			}
			// 查询订单信息
			resultVo = orderInfoDubbo.toAddOrder(orderAddVos,account);
			Object o = resultVo.getResult();
			if (ObjValid.isValid(o)) {
				Map<String, Object> data = (Map<String, Object>) o;
				model.put("orderSn", data.get("orderSn"));
				model.put("customerSn", data.get("customerSn"));
				model.put("totalAmount", data.get("totalAmount"));
				model.put("goodsVos", data.get("goodsVos"));
				model.put("isCanUseCou", data.get("isCanUseCou"));				
				model.put("pos", data.get("pos"));
				model.put("proId", data.get("proId"));
				model.put("logist", logist);
				model.put("isWholesale", data.get("isWholesale"));
				model.put("pointNiceAmount", data.get("pointNiceAmount"));
				model.put("goodSource", goodSource);
				model.put("standardTotalAmount", data.get("standardTotalAmount"));
				model.put("honourTotalAmount", data.get("honourTotalAmount"));
				model.put("honourPreferentialMoney", data.get("honourPreferentialMoney"));
				model.put("standardPreferentialMoney", data.get("standardPreferentialMoney"));
				model.put("pointMoney",data.get("pointMoney"));
				model.put("vocMoney", data.get("vocMoney"));
				model.put("isUpgradePro", data.get("isUpgradePro"));
			}
			
			model.put("orderAddVos", orderAddVos);
						
			// 查询优惠券

			// 查询物流费用
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
			model.put("memberDefAddress", null);
			model.put("orderGoods", null);
		}
		return new ModelAndView("wx/online-shop/pay-affirm", model);
	}

	/**
	 * 获取用户某个收货地址
	 */
	@RequestMapping(value = "getMemAddreById", method = RequestMethod.GET)
	@ResponseBody
	public Object getMemAddreById(Long orderAddressId, String goodSource) {
		String account = SessionUtil.getAccountSession(request);
		resultVo = addressDubbo.findAddressById(orderAddressId, account, goodSource);
		return resultVo;
	}

	/**
	 * @描述 结算购物车
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "actCart", method = RequestMethod.POST)
	public ModelAndView actCart(@RequestParam("cartIds") String cartIds) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			/*
			 * if(!BeanUtil.isValid(account)){ return new
			 * ModelAndView("wx/app-register", model); }
			 */
			// 查询当前用户的最新信息
			GjfMemberInfo member = memberInfoDubbo.findMember(account);
			model.put("gjfMemberInfo", member);

			
			// 查询用户凤凰宝信息
			GjfFhTreasureInfo fh = (GjfFhTreasureInfo) tradeDubbo.findFhTreasureInfo(account).getResult();
			if (!BeanUtil.isValid(fh)) {
				model.put("fhTreasureInfo", 0);
			} else {
				model.put("fhTreasureInfo", fh.getFhTreasureMoney());
			}
					
			// 查询当前用户的最新信息

			if ("1".equals(member.getMerchantType())) {
				model.put("merchantType", "1");
			} else if ("0".equals(member.getMerchantType())) {
				model.put("merchantType", "0");
			} else {
				model.put("merchantType", "2");
			}
			
			// 查询订单信息
			resultVo = cartInfoDubbo.caculateCart(account, cartIds);
			// 查询用户默认的收货地址
			
			Object o = resultVo.getResult();
			if (ObjValid.isValid(o)) {
				Map<String, Object> data = (Map<String, Object>) o;
				model.put("memberDefAddress", addressDubbo.findMyDefDeliveryAddress(account,  data.get("goodSource").toString()));
				
				model.put("orderSn", data.get("orderSn"));
				model.put("customerSn", data.get("customerSn"));
				model.put("totalAmount", data.get("totalAmount"));
				model.put("goodsVos", data.get("goodsVos"));
				model.put("orderAddVos", data.get("orderAddVos"));
				model.put("isCanUseCou", data.get("isCanUseCou"));
				model.put("pos", data.get("pos"));
				model.put("isWholesale", data.get("isWohsalse"));
				model.put("goodSource", data.get("goodSource"));
				model.put("pointNiceAmount", data.get("pointNiceAmount"));
				model.put("standardTotalAmount", data.get("standardTotalAmount"));
				model.put("honourTotalAmount", data.get("honourTotalAmount"));
				model.put("honourPreferentialMoney", data.get("honourPreferentialMoney"));
				model.put("standardPreferentialMoney", data.get("standardPreferentialMoney"));
				model.put("logist", data.get("logist"));				
			}

			// 移除购物车
			String[] carIds = cartIds.split(",");
			for (String str : carIds) {
				cartInfoDubbo.delCart(Long.valueOf(str), account);
			}
			// model.put("orderAddVos", orderAddVos);
			// 查询优惠券

			// 查询物流费用
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
			model.put("memberDefAddress", null);
			model.put("orderGoods", null);
		}

		return new ModelAndView("wx/online-shop/pay-affirm", model);

	}

	/**
	 * 购物车结算时判断库存
	 */
	@RequestMapping(value = "isHasStock", method = RequestMethod.GET)
	@ResponseBody
	public Object isHasStock(String cartIds) {
		String account = SessionUtil.getAccountSession(request);
		resultVo = cartInfoDubbo.caculateCart(account, cartIds);
		return resultVo;
	}

	PrimeUnionThread primeUnionThread = null;

	@RequestMapping(value = "payH5", method = RequestMethod.POST)
	public ModelAndView addOrderByH5(OrderAddModel orderAddModel, @RequestParam("payType") String payType,
			String remark, Long couponsId, @RequestParam("orderAddressId") Long orderAddressId, String payPassword,String logist,String commissionType,String orderSn,String customerSn,BigDecimal postage) {
		Map<String, Object> model = new HashMap<>();
		try {
			String account = SessionUtil.getAccountSession(request);
			List<OrderAddVo> orderAddVos = orderAddModel.getOrderAddVos();
			resultVo = orderInfoDubbo.addOrder(account, orderAddVos, "1", payType, remark, couponsId, orderAddressId,logist,commissionType,orderSn,customerSn,postage);

			if (resultVo.getCode() == 200) {
				GjfOrderInfo gjfOrderInfo = (GjfOrderInfo) resultVo.getResult();
				// 调用银联支付1
				/*
				 * Map<String, String> map = H5PayUtil.H5PayResult(request,
				 * response, gjfOrderInfo.getMemberId(),
				 * gjfOrderInfo.getOfflinePayAmount().toString(),
				 * gjfOrderInfo.getOrderSn(), "测试商品", "测试商品描述",
				 * CommonProperties.GJFENG_H5_NOTIFY_ORDER);
				 * resultVo.setResult(map); model.put("obj", map);
				 * 
				 * primeUnionThread=new
				 * PrimeUnionThread(gjfOrderInfo.getOrderSn());
				 * primeUnionThread.start();
				 */
				// 网银支付方式2
				model.put("orderId", gjfOrderInfo.getOrderSn());
				model.put("payMoney", gjfOrderInfo.getOfflinePayAmount().doubleValue());
				model.put("isReadName", gjfOrderInfo.getMemberId().getIsReadName());
				model.put("idCard", gjfOrderInfo.getMemberId().getIdCard());
				model.put("mobile", gjfOrderInfo.getMemberId().getMobile());
				model.put("retUrl", CommonProperties.GJFENG_WANGYIN_PAY_NOTIFY_URL_ORDER);
				// 查询用户最新的资金情况和所有的银行卡列表
				resultVo = tradeDubbo.toDrawCash(account);
				model.put("resultVo", resultVo);

			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
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
						resultVo = orderInfoDubbo.updateOrderStatus(out_trade_no, trade_no, "1", null, null, "1");
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
	 * @描述 下单
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Object addOrder(OrderAddModel orderAddModel, @RequestParam("payType") String payType, String remark,
			Long couponsId, @RequestParam("orderAddressId") Long orderAddressId, String payPassword,double pointNiceMoney,String logist,String commissionType,String orderSn,String customerSn,BigDecimal postage) {
		//return new ResultVo(400, "系统模式升级中，现暂时停止下单，其他业务照常运作，7月1号恢复正常下单", null);
		try {
			String account = SessionUtil.getAccountSession(request);

			if (payType.equals("0") || (payType.equals("8")&&pointNiceMoney==0) || payType.equals("9")|| (payType.equals("10")&&pointNiceMoney==0)) {
				// 如果是全余额支付，则主要校检其支付密码
				GjfMemberInfo info = memberInfoDubbo.findMember(account);
				if (StringUtil.isBlank(info.getPayPassword())) {
					resultVo = new ResultVo(402, "请先设置支付密码", null);
					return resultVo;
				}
				if (!info.getPayPassword().equals(EncryptionUtil.getMD5Code(info.getMobile() + payPassword))) {
					resultVo = new ResultVo(401, "支付密码错误", null);
					return resultVo;
				}
			}
			List<OrderAddVo> orderAddVos = orderAddModel.getOrderAddVos();
			resultVo = orderInfoDubbo.addOrder(account, orderAddVos, "1", payType, remark, couponsId, orderAddressId,logist,commissionType,orderSn,customerSn,postage);

			if (resultVo.getCode() == 200) {
				GjfOrderInfo gjfOrderInfo = (GjfOrderInfo) resultVo.getResult();
				if (payType.equals("1")
						|| ("7".equals(payType) && gjfOrderInfo.getOfflinePayAmount().doubleValue() != 0)
						|| ("8".equals(payType) && gjfOrderInfo.getOfflinePayAmount().doubleValue() != 0)|| ("10".equals(payType) && gjfOrderInfo.getOfflinePayAmount().doubleValue() != 0)) {
					// 调用微信支付
					GjfWeiXinPayInfo payInfo = (GjfWeiXinPayInfo) tradeDubbo.findWeiXinBaseInfo("0").getResult();
					// 聚合付
					
					 String token_id = H5PayUtil.weiXinPay(payInfo,request,
					  gjfOrderInfo.getOrderSn(),
					 gjfOrderInfo.getOfflinePayAmount().toString(), "购买商品",
					 CommonProperties.GJFENG_WEIXIN_NOTIFY_ORDER,
					 CommonProperties.GJFENG_WEIXIN_CALLBACK_URL_ORDER,
					 gjfOrderInfo.getMemberId().getOpenId());
					 resultVo.setResult(token_id);
					 
					// 利楚
					// PosPrepayRe
					// posPrepayRe=PrePayUtil.posPrePayRe(payInfo,request,gjfOrderInfo.getOrderSn(),gjfOrderInfo.getOfflinePayAmount().toString(),"购买商品",gjfOrderInfo.getMemberId().getOpenId(),"http://xj.gjfeng.net/gjfeng-web-client/wx/notify/payOrderNotifyLiChu");
					// resultVo.setResult(posPrepayRe);
					// 微信原生
					PayJsRequest json = WeixinUtil.unifiedorderResult(request, gjfOrderInfo.getOrderSn(), "购买商品",
							"JSAPI", gjfOrderInfo.getOfflinePayAmount().toString(),
							CommonProperties.GJFENG_WEIXIN_JSPAY_NOTIFY_URL_ORDER,
							gjfOrderInfo.getMemberId().getOpenId());
					Map<String, Object> resultMap=new HashMap<>();
					resultMap.put("orderInfo", resultVo.getResult());
					resultMap.put("payInfo", json);
					resultVo.setResult(resultMap);

				} else if (payType.equals("2") && gjfOrderInfo.getSuoceGood() != "1") {
					Map<String, String> params = OrderInfoUtil2_0.getAppPlayParams(gjfOrderInfo.getOrderSn(), "下单成功",
							gjfOrderInfo.getOfflinePayAmount().toString(), "购买商品",CommonProperties.GJFENG_APLY_PAY_NOTIFY_URL_ORDER);
					String orderParam = OrderInfoUtil2_0.buildOrderParam(params, false);
					String sign = OrderInfoUtil2_0.getSign(params, AlipayConfig.RSA_PRIVATE);
					final String orderInfo = orderParam + "&" + sign + "&sign_type=\"RSA\"";
					return orderInfo;
				} else if (payType.equals("3")) {
					// 调用银联支付

				}
			}

		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 跳转到我的订单
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value = "my", method = RequestMethod.GET)
	public ModelAndView myOrder(Integer pageNo, Integer pageSize, Integer reqType, String status) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			model.put("resultVo", orderInfoDubbo.findMyOrder(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account, status));
			model.put("status", status);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView(ObjValid.isNotValid(reqType) ? "wx/o2o-shop/order-all" : "wx/o2o-shop/order-all-ajax",
				model);
	}

	/**
	 * @描述 根据状态获取订单信息
	 *
	 */
	@RequestMapping(value = "getOrder", method = RequestMethod.GET)
	@ResponseBody
	public Object getOrder(Integer pageNo, Integer pageSize, Integer reqType, String status) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.findMyOrder(ObjValid.isNotValid(pageNo) ? 1 : pageNo,
					ObjValid.isNotValid(pageSize) ? 10 : pageSize, account, status);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 我的订单详情
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public ModelAndView orderDetail(@RequestParam("orderSn") String orderSn) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.findOrderDetail(orderSn, account);
			model.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
			model.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/history-detail", model);
	}

	/**
	 * @描述 修改订单的状态
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderSn
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "updateStatus", method = RequestMethod.POST)
	@ResponseBody
	public Object updateOrderStatus(@RequestParam("orderSn") String orderSn, @RequestParam("status") String status) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.updateOrderStatus(orderSn, "", status, account, null, "1");
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 未支付订单调用支付
	 * @author Karhs
	 * @date 2017年1月23日
	 * @param orderSn
	 * @return
	 */
	@RequestMapping(value = "payOrderSign", method = RequestMethod.POST)
	@ResponseBody
	public Object payOrderSign(@RequestParam("orderSn") String orderSn, String orderType) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.findOrderDetail(orderSn, account);
			if (resultVo.getCode() == 200) {
				OrderInfoVo infoVo = (OrderInfoVo) resultVo.getResult();
				if (infoVo.getOrderStatus().equals("1")) {
					resultVo.setCode(400);
					resultVo.setResult("订单已经付款，无需重复");
					return resultVo;
				} else if (!infoVo.getOrderStatus().equals("0")) {
					resultVo.setCode(400);
					resultVo.setResult("订单不是待付款状态，不能进行支付");
					return resultVo;
				}
				// 有余额的先判断用户余额是否充足
				if ("4".equals(infoVo.getPayType()) || "6".equals(infoVo.getPayType())) {
					GjfMemberInfo info = memberInfoDubbo.findMember(account);
					if (infoVo.getOnlinePayAmount().compareTo(info.getBalanceMoney()) == 1) {
						// 如果余额不足就进行资金的调整
						String newOrderSn = DateHelper.dataToString(new Date(),
								"yyyyMMddHHmmss" + RandUtil.getRandomStr(6));
						infoVo.setOnlinePayAmount(info.getBalanceMoney());
						infoVo.setOfflinePayAmount(infoVo.getOrderTotalAmount().subtract(info.getBalanceMoney()));
						orderInfoDubbo.updateOrderPayMoney(infoVo.getOrderSn(),
								infoVo.getOnlinePayAmount().doubleValue(), infoVo.getOfflinePayAmount().doubleValue(),
								newOrderSn);
						infoVo.setOrderSn(newOrderSn);
					}
				}

				if (infoVo.getPayType().equals("1") || infoVo.getPayType().equals("4")) {
					// 调用微信支付
					GjfWeiXinPayInfo payInfo = (GjfWeiXinPayInfo) tradeDubbo.findWeiXinBaseInfo("0").getResult();
					String token_id = "";
					if ("1".equals(orderType)) {
						token_id = H5PayUtil.weiXinPay(payInfo, request, infoVo.getOrderSn(),
								infoVo.getOfflinePayAmount().toString(), "购买商品",
								CommonProperties.GJFENG_WEIXIN_NOTIFY_ORDER,
								CommonProperties.GJFENG_WEIXIN_CALLBACK_URL_ORDER, infoVo.getMemberId().getOpenId());
					} else {
						// 调用微信支付
						token_id = H5PayUtil.weixinSweepPay(infoVo.getStoreId().getPayMchId(),
								infoVo.getStoreId().getPayKey(), request,
								infoVo.getOfflinePayAmount().toString().toString(), infoVo.getOrderSn(), "购买商品",
								CommonProperties.GJFENG_SWEEP_NOTIFY_ORDER);
						resultVo.setResult(token_id);
					}

					resultVo.setResult(token_id);
				} else if (infoVo.getPayType().equals("2")) {
					// 调用支付宝支付
					String token_id = H5PayUtil.alipaySweepPay(infoVo.getStoreId().getPayMchId(),
							infoVo.getStoreId().getPayKey(), request,
							infoVo.getOfflinePayAmount().toString().toString().toString(), infoVo.getOrderSn(), "购买商品",
							CommonProperties.GJFENG_SWEEP_NOTIFY_ORDER);
					resultVo.setResult(token_id);
				} else if (infoVo.getPayType().equals("3")) {
					// 调用银联支付
				}
			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
		}
		return resultVo;
	}

	/**
	 * @描述 未支付订单调用支付
	 * @param orderSn
	 * @return
	 */
	@RequestMapping(value = "payOrderSignByH5", method = RequestMethod.GET)
	public ModelAndView payOrderSignByH5(@RequestParam("orderSn") String orderSn) {
		Map<String, Object> model = new HashMap<>();
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.findOrderDetail(orderSn, account);
			if (resultVo.getCode() == 200) {
				OrderInfoVo infoVo = (OrderInfoVo) resultVo.getResult();

				// 有余额的先判断用户余额是否充足
				if ("4".equals(infoVo.getPayType()) || "6".equals(infoVo.getPayType())) {
					GjfMemberInfo info = memberInfoDubbo.findMember(account);
					if (infoVo.getOnlinePayAmount().compareTo(info.getBalanceMoney()) > 1) {
						// 如果余额不足就进行资金的调整
						String newOrderSn = DateHelper.dataToString(new Date(),
								"yyyyMMddHHmmss" + RandUtil.getRandomStr(6));
						infoVo.setOnlinePayAmount(info.getBalanceMoney());
						infoVo.setOfflinePayAmount(infoVo.getOrderTotalAmount().subtract(info.getBalanceMoney()));
						orderInfoDubbo.updateOrderPayMoney(infoVo.getOrderSn(),
								infoVo.getOnlinePayAmount().doubleValue(), infoVo.getOfflinePayAmount().doubleValue(),
								newOrderSn);
						infoVo.setOrderSn(newOrderSn);
					}
				}

				if (infoVo.getPayType().equals("3")) {
					// 调用微信支付
					Map<String, String> map = H5PayUtil.H5PayResult(request, response, infoVo.getMemberId(),
							infoVo.getOfflinePayAmount().toString(), infoVo.getOrderSn(), "测试商品", "测试商品描述",
							CommonProperties.GJFENG_H5_NOTIFY_ORDER);
					resultVo.setResult(map);
					model.put("obj", map);
				}

			}
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
		}
		return new ModelAndView("wx/online-shop/H5cashier", model);
	}

	/**
	 * @描述 删除订单
	 * @author Karhs
	 * @date 2017年1月9日
	 * @param orderSn
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "del", method = RequestMethod.POST)
	@ResponseBody
	public Object delOrderStatus(@RequestParam("orderSn") String orderSn) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.delOrder(orderSn, account, null, 1);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, OrderController.class);
		}
		return resultVo;
	}

	/**
	 * 根据用户信息获取店铺信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "addStorePayOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object addStorePayOrder(Long storeId, Double payMoney) {
		try {
			String account = SessionUtil.getAccountSession(request);
			resultVo = orderInfoDubbo.addO2oOrderInfo(account, payMoney, storeId, "0", "3", "", 0l);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, StoreController.class);
		}
		return resultVo;
	}

	/**
	 * 获取店铺订单信息
	 */
	@RequestMapping(value = "findO2oOrder", method = RequestMethod.GET)
	@ResponseBody
	public Object findO2oOrder(String status, Long storeId, String beginTime, String endTime, Integer pageNo,
			Integer pageSize) {
		try {

			resultVo = orderInfoDubbo.findO2oOrderInfo(status, storeId, beginTime, endTime, pageNo, pageSize);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, StoreController.class);
		}
		return resultVo;
	}

	/**
	 * 获取店铺订单信息
	 */
	@RequestMapping(value = "findO2oOrderByPage", method = RequestMethod.GET)
	public ModelAndView findO2oOrderByPage(String status, Long storeId, String beginTime, String endTime,
			Integer pageNo, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		try {
			resultVo = orderInfoDubbo.findO2oOrderInfo(status, storeId, beginTime, endTime, pageNo, pageSize);
			map.put("storeId", storeId);
			map.put("status", status);
			map.put("resultVo", resultVo);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, StoreController.class);
			map.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/store_order", map);
	}

	/**
	 * 获取店铺订单信息
	 */
	@RequestMapping(value = "getJdTrack", method = RequestMethod.GET)
	public ModelAndView getJdTrack(String jdOrderSn) {
		Map<String, Object> map = new HashMap<>();
		try {
			TrackResult trackResult = JdNewUtil.getTrack(jdOrderSn);
			map.put("trackResult", trackResult);
		} catch (Exception e) {
			resultVo = LoggerPrint.getResult(e, StoreController.class);
			map.put("resultVo", resultVo);
		}
		return new ModelAndView("wx/o2o-shop/order-track", map);
	}
	
	/**
	 * 根据地址获取商品邮费
	 * @param goodIds
	 * @param goodNums
	 * @param addsId
	 * @return
	 */
	@RequestMapping(value = "findOrderPos", method = RequestMethod.GET)
	@ResponseBody
	public Object findOrderPos(String goodIds,String goodNums,String addsId){
		try{
			resultVo=orderInfoDubbo.findOrderPos(goodIds, goodNums, addsId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}

}
