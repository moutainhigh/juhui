package com.alipay.direct.util;




import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.alipay.direct.config.AlipayConfig;

public class OrderInfoUtil2_0 {
	
	/**
	 * 构造授权参数列表
	 * 
	 * @param pid
	 * @param app_id
	 * @param target_id
	 * @return
	 */
	public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id) {
		Map<String, String> keyValues = new HashMap<String, String>();

		// 商户签约拿到的app_id，如：2013081700024223
		keyValues.put("app_id", app_id);

		// 商户签约拿到的pid，如：2088102123816631
		keyValues.put("pid", pid);

		// 服务接口名称， 固定值
		keyValues.put("apiname", "com.alipay.account.auth");

		// 商户类型标识， 固定值
		keyValues.put("app_name", "mc");

		// 业务类型， 固定值
		keyValues.put("biz_type", "openservice");

		// 产品码， 固定值
		keyValues.put("product_id", "APP_FAST_LOGIN");

		// 授权范围， 固定值
		keyValues.put("scope", "kuaijie");

		// 商户唯一标识，如：kkkkk091125
		keyValues.put("target_id", target_id);

		// 授权类型， 固定值
		keyValues.put("auth_type", "AUTHACCOUNT");

		// 签名类型
		keyValues.put("sign_type", "RSA");

		return keyValues;
	}
	
	
	


	/**
	 * 构造支付订单参数列表
	 * @param pid
	 * @param app_id
	 * @param target_id
	 * @return
	 */
	public static Map<String, String> buildOrderParamMap(String app_id, String biz_content, String timestamp) {
		Map<String, String> keyValues = new HashMap<String, String>();

		keyValues.put("app_id", app_id);

		keyValues.put("biz_content", biz_content);
//		keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() +  "\"}");
		// 固定值
		keyValues.put("charset", "utf-8");
		// 固定值
		keyValues.put("method", "alipay.trade.app.pay");
		// 固定值
		keyValues.put("sign_type", "RSA");

		keyValues.put("timestamp", timestamp);
//		keyValues.put("timestamp", "2016-07-29 16:55:53");
		// 固定值
		keyValues.put("version", "1.0");
		
		return keyValues;
	}
	
	/**
	 * 构造支付订单参数信息
	 * 
	 * @param map
	 * 支付订单参数
	 * @return
	 */
	public static String buildOrderParam(Map<String, String> map,boolean flag) {
		List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append(buildKeyValue(key, value, flag));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, flag));

		return sb.toString();
	}
	/**
	 * 拼接键值对
	 * //注意：app支付1.0版本 的参数的值要有双引号  key="value" 这样就不用转utf-8码,
	 * 而app支付2.0 没有双引号  key=value 因为没有双引号所以要转码
	 * 
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		
		if (isEncode) {
			try {
				sb.append("=");
				sb.append(URLEncoder.encode(value, "UTF-8"));
				
			} catch (UnsupportedEncodingException e) {
				sb.append(value+"\"");
			}
		} else {
			sb.append("=\"");  
			sb.append(value+"\"");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	private static String buildKeyValueApp(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		
		if (isEncode) {
			try {
				sb.append("=");
				sb.append(URLEncoder.encode(value, "UTF-8"));
				
			} catch (UnsupportedEncodingException e) {
				sb.append(value+"\"");
			}
		} else {
			sb.append("=");  
			sb.append(value);
		}
		return sb.toString();
	}
	/**
	 * 对支付参数信息进行签名
	 * 
	 * @param map
	 *            待签名授权信息
	 * 
	 * @return
	 */
	public static String getSign(Map<String, String> map, String rsaKey) {
		List<String> keys = new ArrayList<String>(map.keySet());
		// key排序
		Collections.sort(keys);

		StringBuilder authInfo = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			authInfo.append(buildKeyValue(key, value, false));
			authInfo.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		authInfo.append(buildKeyValue(tailKey, tailValue, false));
		System.out.println(authInfo.toString());
		String oriSign = SignUtils.sign(authInfo.toString(), rsaKey);
		String encodedSign = "";
        
		try {
			encodedSign = URLEncoder.encode(oriSign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "sign=\"" + encodedSign+"\"";
	}
	
	public static String getSignApp(Map<String, String> map, String rsaKey) {
		List<String> keys = new ArrayList<String>(map.keySet());
		// key排序
		Collections.sort(keys);

		StringBuilder authInfo = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			authInfo.append(buildKeyValueApp(key, value, false));
			authInfo.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		authInfo.append(buildKeyValueApp(tailKey, tailValue, false));
		System.out.println(authInfo.toString());
		String oriSign = SignUtils.sign(authInfo.toString(), rsaKey);
		String encodedSign = "";
        
		try {
			encodedSign = URLEncoder.encode(oriSign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "sign="+encodedSign;
	}
	
	/**
	 * 要求外部订单号必须唯一。
	 * @return
	 */
	private static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
	/**
	 * app支付接口1.0   生成参数
	 * @param orderNum
	 * @param subject
	 * @param money
	 * @param body
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> getAppPlayParams(String orderNum, String subject, String money,
			String body,String notify_url) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		//params.put("app_id", AlipayConfig.APPID);
		params.put("_input_charset", AlipayConfig.input_charset);
		params.put("service", AlipayConfig.SERVICE);
	    params.put("notify_url",notify_url); 
		//params.put("sign_type", AlipayConfig.SING_TYPE);
	    
	    params.put("format", "json");
		params.put("out_trade_no", orderNum);
		params.put("payment_type", "1");
		params.put("seller_id", AlipayConfig.seller_mail);
	
		params.put("total_fee", money);
		params.put("body", body);
		params.put("subject", subject);
		params.put("partner", AlipayConfig.partner);
		params.put("it_b_pay", "30m");
		//params.put("show_url", "http://ylt.messcat.com");
		return params;
	}
    /**
	 * 支付接口2.0生成参数
	 * @param orderNum
	 * @param subject
	 * @param money
	 * @param body
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> getAppPlayParams2_0(String orderNum, String subject, String money,
			String body,String notify_url) throws UnsupportedEncodingException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = df.format(new Date());
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_id", AlipayConfig.APPID);
		params.put("charset", AlipayConfig.input_charset);
		//params.put("sign_type", AlipayConfig.SING_TYPE);
		params.put("method", AlipayConfig.METHOD);	
		params.put("timestamp", timestamp);
		params.put("version", AlipayConfig.VERSION);
		params.put("notify_url",notify_url); 
		params.put("biz_content", createContent(subject, orderNum,money,body));
		//params.put("out_trade_no", orderNum);
		//params.put("total_fee", money);
		//params.put("body", body);
		//params.put("subject", subject);
		//params.put("product_code", "QUICK_MSECURITY_PAY");
		return params;
	}
    /**
     * 构造biz_content
     * @param subject
     * @param orderNum
     * @param money
     * @param body
     * @return
     */
	private static String createContent(String subject, String orderNum,String money,String body) {
		StringBuffer content = new StringBuffer();
		content.append("{");
		content.append("\"timeout_express\":\""+AlipayConfig.TIMEOUT_EXPRESS+"\",");		
		content.append("\"product_code\":\""+AlipayConfig.PRODUCT_CODE+"\",");
		content.append("\"total_amount\":\""+money+"\",");
		content.append("\"subject\":\"" + subject + "\",");
		content.append("\"body\":\""+body+"\",");
		content.append("\"out_trade_no\":\"" + orderNum + "\"");
		content.append("}");
		return content.toString();
	}
}
