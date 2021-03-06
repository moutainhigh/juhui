package cc.messcat.gjfeng.common.fastpay.payForOther;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import cc.messcat.gjfeng.common.fastpay.util.Base;
import cc.messcat.gjfeng.common.fastpay.util.DesCrypt;
import cc.messcat.gjfeng.common.fastpay.util.NetXmlUtils;

/***
 * 代付业务、充值申请、充值查询、代付查询 demo
 * 
 * @author Administrator
 *
 */
public class RechargeTest {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	// 证书密码
	private static final String password = "am835u8mpi1lm4ua4d1chp8j";
	private static final String keyStorePath = "E:\\keystore_http\\client.keystore";
	private static final String dyPass_url = "https://gateway.yjf-pay.com/WebRoot/SwitchDynamicPassword";
	private static final String netPay_url = "https://gateway.yjf-pay.com/WebRoot/UserAuth";
	private static final String merid = "6600000000000579";
	private static final String deskey = "wr60pfc9anfhgkwgjgtpc4gt";
	private static final String md5key = "qhyotyqe9ve7r92u4h3floh1bnvcpsgn";

	public static void main(String[] args) {
		String orderId = sdf.format(new Date());
		System.out.println("订单号：" + orderId);
		String dypass = getDyPass(orderId);
		System.out.println("动态秘钥：" + dypass);
		// 充值申请
		// rechargeApply(orderId, dypass);
		// 充值查询
		// rechargeQuery(orderId, dypass, "2017071316464812345678");
		// 代付交易
		// payForOther(orderId, dypass);

		payForOther(orderId, dypass, "100", "张三", "6226090000000048", "www.paidu.com");
		// 代付查询
		// payForOtherQuery("2017071316464812345678", dypass);

	}

	/***
	 * 代付查询
	 * 
	 * @param orderId
	 * @param dypass
	 */
	public  static Map<String, String> payForOtherQuery(String orderId, String dypass) {
		String xmlNode = "charCode,Version,TradeType,ChannelID,MerType,bmMerId,timeStamp,trxType,orderId";
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("charCode", "gbk");
		reqMap.put("Version", "2.0.1");
		reqMap.put("TradeType", "0630");
		reqMap.put("ChannelID", merid);
		reqMap.put("bmMerId", merid);
		reqMap.put("trxType", "0250");// 1：鉴权充值；2：代付充值；3：手续费充值
		reqMap.put("timeStamp", sdf.format(new Date()));
		reqMap.put("orderId", orderId);
		reqMap.put("keyMd5", md5key);
		reqMap.put("key3Des", dypass);
		try {
			String xmlStr = NetXmlUtils.genXmlFromMapAndSign2(xmlNode, reqMap, reqMap.get("keyMd5"), "requestData");
			System.out.println("充值【请求】报文（明文）：" + xmlStr);
			byte[] xmlByte = DesCrypt.encrypt(xmlStr.getBytes("GBK"), reqMap.get("key3Des"));
			String xmlSend = reqMap.get("ChannelID") + Base64.encodeBase64String(xmlByte);
			System.out.println("充值【请求】报文（密文）：" + xmlSend);
			// https发起请求
			HttpClient hc = new HttpClient(netPay_url, 60000, keyStorePath, password, keyStorePath, password);
			String res = hc.submitUrl(xmlSend);
			System.out.println("充值【返回】报文（密文）：" + res);
			// 解密报文
			Map<String, String> decryptMap = decryptData(res, dypass, md5key);
			System.err.println("充值【返回】报文（明文）：" + decryptMap);
			System.err.println("充值结果【" + decryptMap.get("resultCode") + "】描述【" + decryptMap.get("resultDesc") + "】");
			return decryptMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 代付交易
	 * 
	 * TODO 代付手续费为内扣；如下发代付金额为：100元，手续费为1元一笔；则实际到账金额为99元
	 * 
	 * @param orderId
	 * @param dypass
	 */
	public static Map<String, String> payForOther(String orderId, String dypass, String txnAmt, String acctName,
			String acctNo, String retUrl) {
		try {
			String xmlNode = "charCode,Version,TradeType,ChannelID,MerType,bmMerId,timeStamp,trxType,orderId,"
					+ "txnAmt,acctType,acctName,acctNo,bankName,bankSettNo,bankCode,province,city,cnapsName,"
					+ "Nbr,certificateCode,retUrl";
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("charCode", "gbk");
			reqMap.put("Version", "2.0.1");
			reqMap.put("TradeType", "0628");
			reqMap.put("ChannelID", merid);
			reqMap.put("MerType", "01");
			reqMap.put("bmMerId", merid);
			reqMap.put("timeStamp", sdf.format(new Date()));
			reqMap.put("orderId", orderId);
			reqMap.put("trxType", "0250");
			float sessionmoney = Float.parseFloat(txnAmt) * 100;
			reqMap.put("txnAmt", String.format("%.0f", sessionmoney));// 分为单位
			reqMap.put("acctType", "1");
			// 测试卡号和户名请勿修改，只有这个账号和户名才能成功
			// reqMap.put("acctName", "张三");
			// reqMap.put("acctNo", "6226090000000048");
			reqMap.put("acctName", acctName);
			reqMap.put("acctNo", acctNo);
			reqMap.put("retUrl", retUrl);
			reqMap.put("keyMd5", md5key);
			reqMap.put("key3Des", dypass);
			try {
				String xmlStr = NetXmlUtils.genXmlFromMapAndSign2(xmlNode, reqMap, reqMap.get("keyMd5"), "requestData");
				System.out.println("代付【请求】报文（明文）：" + xmlStr);
				byte[] xmlByte = DesCrypt.encrypt(xmlStr.getBytes("GBK"), reqMap.get("key3Des"));
				String xmlSend = Base.addZeroForNum(String.valueOf(xmlByte.length + 16), 4) + reqMap.get("ChannelID")
						+ Base64.encodeBase64String(xmlByte);
				System.out.println("代付【请求】报文（密文）：" + xmlSend);
				// https发起请求
				HttpClient hc = new HttpClient(netPay_url, 60000, keyStorePath, password, keyStorePath, password);
				String res = hc.submitUrl(xmlSend);
				System.out.println("代付【返回】报文（密文）：" + res);
				Map<String, String> decryptMap = RechargeTest.decryptData(res, dypass, md5key);
				System.out.println("代付【返回】报文（明文）：" + decryptMap);
				System.out
						.println("代付结果【" + decryptMap.get("resultCode") + "】描述【" + decryptMap.get("resultDesc") + "】");
				return decryptMap;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 充值查询
	 * 
	 * @param orderId
	 * @param dypass
	 */
	private static void rechargeQuery(String orderId, String dypass, String origOrderId) {
		try {
			String xmlNode = "charCode,Version,TradeType,ChannelID,bmMerId,timeStamp,orderId,trxType,origOrderId";
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("charCode", "gbk");
			reqMap.put("Version", "2.0.1");
			reqMap.put("TradeType", "0807");
			reqMap.put("ChannelID", merid);
			reqMap.put("bmMerId", merid);
			reqMap.put("trxType", "2");// 1：鉴权充值；2：代付充值；3：手续费充值
			reqMap.put("timeStamp", sdf.format(new Date()));
			reqMap.put("orderId", orderId);
			reqMap.put("trxType", "2");
			reqMap.put("origOrderId", origOrderId);
			reqMap.put("keyMd5", md5key);
			reqMap.put("key3Des", dypass);
			try {
				String xmlStr = NetXmlUtils.genXmlFromMapAndSign2(xmlNode, reqMap, reqMap.get("keyMd5"), "requestData");
				System.out.println("充值【请求】报文（明文）：" + xmlStr);
				byte[] xmlByte = DesCrypt.encrypt(xmlStr.getBytes("GBK"), reqMap.get("key3Des"));
				String xmlSend = reqMap.get("ChannelID") + Base64.encodeBase64String(xmlByte);
				System.out.println("充值【请求】报文（密文）：" + xmlSend);
				// https发起请求
				HttpClient hc = new HttpClient(netPay_url, 60000, keyStorePath, password, keyStorePath, password);
				String res = hc.submitUrl(xmlSend);
				System.out.println("充值【返回】报文（密文）：" + res);
				// 解密报文
				Map<String, String> decryptMap = decryptData(res, dypass, md5key);
				System.err.println("充值【返回】报文（明文）：" + decryptMap);
				System.err
						.println("充值结果【" + decryptMap.get("resultCode") + "】描述【" + decryptMap.get("resultDesc") + "】");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 充值申请接口
	 * 
	 * @param orderId
	 * @param dypass
	 */
	public static Map<String, String> rechargeApply(String orderId, String dypass,String accno,String name,String txnAmt) {
		try {
			String xmlNode = "charCode,Version,TradeType,ChannelID,bmMerId,timeStamp,trxType,accno,name,txnAmt,remark";
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("charCode", "gbk");
			reqMap.put("Version", "2.0.1");
			reqMap.put("TradeType", "0805");
			reqMap.put("ChannelID", merid);
			reqMap.put("bmMerId", merid);
			reqMap.put("trxType", "2");// 1：鉴权充值；2：代付充值；3：手续费充值
			reqMap.put("timeStamp", sdf.format(new Date()));
			reqMap.put("accno", accno);
			reqMap.put("name", name);
			float sessionmoney = Float.parseFloat(txnAmt) * 100;
			reqMap.put("txnAmt", String.format("%.0f", sessionmoney));// 分为单位
			reqMap.put("remark", "代付充值");
			reqMap.put("keyMd5", md5key);
			reqMap.put("key3Des", dypass);
			try {
				String xmlStr = NetXmlUtils.genXmlFromMapAndSign2(xmlNode, reqMap, reqMap.get("keyMd5"), "requestData");
				System.out.println("充值【请求】报文（明文）：" + xmlStr);
				byte[] xmlByte = DesCrypt.encrypt(xmlStr.getBytes("GBK"), reqMap.get("key3Des"));
				String xmlSend =Base.addZeroForNum(String.valueOf(xmlByte.length + 16), 4)+ reqMap.get("ChannelID") + Base64.encodeBase64String(xmlByte);
				System.out.println("充值【请求】报文（密文）：" + xmlSend);
				// https发起请求
				HttpClient hc = new HttpClient(netPay_url, 60000, keyStorePath, password, keyStorePath, password);
				String res = hc.submitUrl(xmlSend);
				System.out.println("充值【返回】报文（密文）：" + res);
				// 解密报文
				Map<String, String> decryptMap = decryptData(res, dypass, md5key);
				System.err.println("充值【返回】报文（明文）：" + decryptMap);
				System.err.println("充值结果【" + decryptMap.get("resultCode") + "】描述【" + decryptMap.get("resultDesc")
						+ "】充值订单号【" + decryptMap.get("orderId") + "】");
				return decryptMap;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 获取动态秘钥
	 * 
	 * @return
	 */
	public static String getDyPass(String orderId) {
		String xmlNode = "charCode,Version,TradeType,ChannelID,orderId,bmMerId,timeStamp";
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("charCode", "gbk");
		reqMap.put("Version", "2.0.1");
		reqMap.put("TradeType", "0413");
		reqMap.put("ChannelID", merid);
		reqMap.put("bmMerId", merid);
		reqMap.put("orderId", orderId);
		reqMap.put("timeStamp", sdf.format(new Date()));
		reqMap.put("keyMd5", md5key);
		reqMap.put("key3Des", deskey);
		try {
			String xmlStr = NetXmlUtils.genXmlFromMapAndSign2(xmlNode, reqMap, reqMap.get("keyMd5"), "requestData");
			System.out.println("动态秘钥【请求】报文（明文）：" + xmlStr);
			byte[] xmlByte = DesCrypt.encrypt(xmlStr.getBytes("GBK"), reqMap.get("key3Des"));
			String xmlSend = Base.addZeroForNum(String.valueOf(xmlByte.length + 16), 4) + reqMap.get("ChannelID")
					+ Base64.encodeBase64String(xmlByte);
			System.out.println("动态秘钥【请求】报文（密文）：" + xmlSend);
			// https发起请求
			HttpClient hc = new HttpClient(dyPass_url, 60000, keyStorePath, password, keyStorePath, password);
			String res = hc.submitUrl(xmlSend);
			System.out.println("动态秘钥【返回】报文（密文）：" + res);
			// 解密报文
			Map<String, String> decryptMap = decryptData(res, deskey, md5key);
			System.err.println("动态秘钥【返回】报文（明文）：" + decryptMap);
			if (!decryptMap.isEmpty()) {
				if ("00".equals(decryptMap.get("resultCode"))) {
					String random = decryptMap.get("random");
					String decryptStr = deskey + random;
					String md5Value = Base.md5s(decryptStr);
					String retValue = md5Value.substring(4, 28);
					if (StringUtils.isEmpty(retValue)) {
						return "";
					}
					return retValue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> decryptData(String recvXmlStr, String key3Des, String keyMd5) throws Exception {
		String xmlStr = "";
		try {
			// 获取报文字节长度
			int recvXmlLength = Integer.valueOf(recvXmlStr.substring(0, 4));
			recvXmlStr = recvXmlStr.substring(20);

			byte[] xmlByte = Base64.decodeBase64(recvXmlStr);
			if (recvXmlLength != (xmlByte.length + 16)) {
				throw new Exception("验证长度错误");
			}

			xmlByte = DesCrypt.decrypt(xmlByte, key3Des);
			xmlStr = new String(xmlByte, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println("返回的明文Xml：" + xmlStr);
		Map<String, String> busiMap = NetXmlUtils.Xml2MapAndValidate(xmlStr, keyMd5);
		return busiMap;
	}
}
