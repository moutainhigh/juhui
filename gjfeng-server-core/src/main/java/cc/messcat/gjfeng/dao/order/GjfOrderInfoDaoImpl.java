package cc.messcat.gjfeng.dao.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import cc.messcat.gjfeng.common.bean.Pager;
import cc.messcat.gjfeng.common.util.BeanUtil;
import cc.messcat.gjfeng.common.util.StringUtil;
import cc.messcat.gjfeng.common.vo.app.OrderInfosVo;
import cc.messcat.gjfeng.dao.BaseHibernateDaoImpl;
import cc.messcat.gjfeng.entity.GjfOrderInfo;

@Repository("gjfOrderInfoDao")
public class GjfOrderInfoDaoImpl extends BaseHibernateDaoImpl<Serializable> implements GjfOrderInfoDao {

	/*
	 * 修改超时未支付订单为过期状态
	 * 
	 * @see cc.messcat.gjfeng.dao.order.GjfOrderInfoDao#updateOrderToOverdue()
	 */
	@Override
	public void updateOrderToOverdue(String orderSn) {
		String sql = "update gjf_order_info set ORDER_STATUS_='4' where ORDER_STATUS_='0' and ORDER_SN_=?";
		getCurrentSession().createSQLQuery(sql).setParameter(0, orderSn).executeUpdate();
	}

	/*
	 * 根据账户和订单状态查询订单
	 * 
	 * @see cc.messcat.gjfeng.dao.order.GjfOrderInfoDao#findMyOrder(int, int,
	 * java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderInfosVo> findMyOrder(int pageNo, int pageSize, String account, String status) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String sql = "select o.orderSn orderSn,o.realPayAmount realPayAmount,o.addTime addTime,o.orderType orderType,o.evaluationStatus evaluationStatus,"
			+ "o.orderStatus orderStatus,o.refundStatus refundStatus,g.goodsId.id goodsId,g.goodsName goodsName,g.goodsImageUrl goodsImg,g.goodsAmount goodsAmount,"
			+ "g.goodsAttr from GjfOrderGoods g left join g.orderId o left join g.goodsAttrId ga where o.memberId.mobile=:mobile and o.isDel='1'";
		dataMap.put("mobile", account);
		if (StringUtil.isBlank(status)) {
			dataMap.put("orderStatus", status);
			sql += " and o.orderStatus=:orderStatus";
		}
		sql += " order by o.addTime desc";
		List<OrderInfosVo> infosVos = getCurrentSession().createSQLQuery(sql.toString()).addScalar("orderSn", StringType.INSTANCE)
			.addScalar("realPayAmount", BigDecimalType.INSTANCE).addScalar("addTime", DateType.INSTANCE)
			.addScalar("orderType", StringType.INSTANCE).addScalar("evaluationStatus", StringType.INSTANCE)
			.addScalar("orderStatus", StringType.INSTANCE).addScalar("refundStatus", StringType.INSTANCE)
			.addScalar("goodsId", LongType.INSTANCE).addScalar("goodsName", StringType.INSTANCE)
			.addScalar("goodsImg", StringType.INSTANCE).addScalar("goodsAmount", BigDecimalType.INSTANCE)
			.addScalar("goodsAttr", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(OrderInfosVo.class))
			.setProperties(dataMap).setMaxResults(pageSize).setFirstResult((pageNo - 1) * pageSize).list();
		return infosVos;
	}

	/*
	 * 
	 * 分页查询全部订单
	 * 
	 * @see cc.messcat.gjfeng.dao.order.GjfOrderInfoDao#findAllOrder(int, int,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pager findAllOrder(int pageNo,int pageSize,String orderSn,String storeName,String goodsName,
			String name,String nickName,String orderStatus,String payType,String orderType ,String startDate,String endDate,String ste,String jdOrderSn,String goodSource){
		StringBuffer sql = new StringBuffer();
		Map<String,Object> arrMap = new HashMap<String,Object>();
		List infosVos = null;
		BigInteger count = new BigInteger("0");
		sql.append("select g.GOODS_NAME_,g.GOODS_AMOUNT_,g.GOODS_PAY_AMOUNT_,g.GOODS_NUM_,g.GOODS_IMAGE_URL_,"
			+ "o.EVALUATION_STATUS_,o.ORDER_STATUS_,o.ORDER_SN_,o.PAY_TYPE_,o.ORDER_TYPE_,o.ONLINE_PAY_AMOUNT_,o.JD_ORDERSN_,o.SUORCE_ORDER_,o.IS_WHOLESALE_,o.LOGIST_,"
			+ " o.OFFLINE_PAY_AMOUNT_,o.TOKEN_,o.ADD_TIME_,s.STORE_NAME_,m.NAME_,m.NICK_NAME_,m.MOBILE_,g.GOODS_ATTR_,a.RECIVER_DETAIL_ADDRESS_,p.PROVINCE_,c.CITY_,e.AREA_,a.RECIVER_NAME_,a.RECIVER_MOBILE_ "
			+ " from gjf_order_goods g left join gjf_order_info o on g.ORDER_ID_=o.ID_ left join gjf_store_info s on o.STORE_ID_=s.ID_ "
			+ " left join gjf_member_info m on m.ID_=o.MEMBER_ID_ LEFT JOIN gjf_order_address a on a.ORDER_ID_ = o.ID_ "
			+ " LEFT JOIN gjf_address_province p on a.RECIVER_PROVINCE_ID_ = p.ID_ LEFT JOIN gjf_address_city c on a.RECIVER_CITY_ID_ = c.ID_ "
			+ " LEFT JOIN gjf_address_area e on a.RECIVER_AREA_ID_ = e.ID_ where 1 = 1 ");
		if (StringUtil.isNotBlank(orderStatus)) {
			arrMap.put("orderStatus", orderStatus);
			sql.append(" and o.ORDER_STATUS_ = :orderStatus ");
		}
		if (StringUtil.isNotBlank(payType)) {
			arrMap.put("payType", payType);
			sql.append(" and o.PAY_TYPE_ = :payType ");
		}
		if (StringUtil.isNotBlank(orderType)) {
			arrMap.put("orderType", orderType);
			sql.append(" and o.ORDER_TYPE_ = :orderType ");
		}
		if (StringUtil.isNotBlank(startDate)) {
			arrMap.put("startDate", startDate);
			sql.append(" and o.ADD_TIME_ >= :startDate ");
		}
		if (StringUtil.isNotBlank(endDate)) {
			arrMap.put("endDate", endDate);
			sql.append(" and o.ADD_TIME_ <= :endDate ");
		}
		if (StringUtil.isNotBlank(orderSn)) {
			arrMap.put("orderSn", "%"+orderSn+"%");
			sql.append(" and o.ORDER_SN_ like :orderSn ");	
		}
		if (StringUtil.isNotBlank(storeName)) {
			arrMap.put("storeName", "%"+storeName+"%");
			sql.append(" and s.STORE_NAME_ like :storeName ");
		}
		if (StringUtil.isNotBlank(goodsName)) {
			arrMap.put("goodsName", "%"+goodsName+"%");
			sql.append(" and g.GOODS_NAME_ like :goodsName ");
		}
		if (StringUtil.isNotBlank(name)) {
			arrMap.put("name", "%"+name+"%");
			sql.append(" and m.NAME_ like :name ");
		}
		if (StringUtil.isNotBlank(nickName)) {
			arrMap.put("nickName", "%"+nickName+"%");
			sql.append(" and m.NICK_NAME_ like :nickName ");
		}
		
		if (StringUtil.isNotBlank(jdOrderSn)) {
			arrMap.put("jdOrderSn", "%"+jdOrderSn+"%");
			sql.append(" and o.JD_ORDERSN_ like :jdOrderSn");
		}
		
		if (StringUtil.isNotBlank(goodSource)) {
			arrMap.put("goodSource", "%"+goodSource+"%");
			sql.append(" and o.SUORCE_ORDER_ like :goodSource");
		}
		
		sql.append(" and o.IS_DEL_=1 order by o.ADD_TIME_ desc ");
		if ("1".equals(ste)) {
			infosVos = getCurrentSession().createSQLQuery(sql.toString()).setProperties(arrMap).list();
		}else {
			infosVos = getCurrentSession().createSQLQuery(sql.toString()).setProperties(arrMap).setMaxResults(pageSize)
					.setFirstResult((pageNo - 1) * pageSize).list();
		}
		String[] param = { "goosName", "goodsAmount", "goodsPayAcmount", "goodNum", "goodsImg", "evaluationStatus", "orderStatus",
			"orderSn", "payType", "orderType", "onlinePayAmount","jdOrderSn","suorceOrder","isWholesalse","logist","offlinePayAmount", "token","addTime", "storeName", "memName", "nickName",
			"mobile", "goodAttr","address","province","city","area","reciverName","reciverMobile" };
		infosVos = BeanUtil.changeObjectArrayToMaps(infosVos, param);
		String sql0 = sql.toString().replace("select g.GOODS_NAME_,g.GOODS_AMOUNT_,g.GOODS_PAY_AMOUNT_,g.GOODS_NUM_,g.GOODS_IMAGE_URL_,o.EVALUATION_STATUS_,o.ORDER_STATUS_,o.ORDER_SN_,o.PAY_TYPE_,o.ORDER_TYPE_,o.ONLINE_PAY_AMOUNT_,o.JD_ORDERSN_,o.SUORCE_ORDER_,o.IS_WHOLESALE_,o.LOGIST_, o.OFFLINE_PAY_AMOUNT_,o.TOKEN_,o.ADD_TIME_,s.STORE_NAME_,m.NAME_,m.NICK_NAME_,m.MOBILE_,g.GOODS_ATTR_,a.RECIVER_DETAIL_ADDRESS_,p.PROVINCE_,c.CITY_,e.AREA_,a.RECIVER_NAME_,a.RECIVER_MOBILE_",
				"select count(1)");
		count = (BigInteger) getCurrentSession().createSQLQuery(sql0).setProperties(arrMap).uniqueResult();
		return new Pager(pageSize, pageNo, count.intValue(), infosVos);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List findOrderByStatus(int pageNo, int pageSize, String account, String status) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> arrMap = new HashMap<String,Object>();
		sql.append(
			"select g.GOODS_ID_,g.GOODS_NAME_,g.GOODS_AMOUNT_,g.GOODS_PAY_AMOUNT_,g.GOODS_NUM_,g.GOODS_IMAGE_URL_,o.PAY_TYPE_,o.EVALUATION_STATUS_,o.ORDER_STATUS_,o.ORDER_SN_,s.STORE_NAME_,o.TOKEN_ from gjf_order_goods as g , gjf_order_info as o ,gjf_store_info as s,"
				+ "gjf_member_info as m where g.ORDER_ID_=o.ID_ and o.STORE_ID_=s.ID_ and m.ID_=o.MEMBER_ID_ and m.MOBILE_=:account and o.IS_DEL_=1 ");
		if (StringUtil.isNotBlank(status) && Integer.parseInt(status) != 7) {
			arrMap.put("status", status);
			sql.append("and o.ORDER_STATUS_= :status ");
		}
		sql.append(" order by o.ADD_TIME_ desc");
		List infosVos = getCurrentSession().createSQLQuery(sql.toString()).setProperties(arrMap).setParameter("account", account).setMaxResults(pageSize)
			.setFirstResult((pageNo - 1) * pageSize).list();
		String[] param = { "goodId", "goosName", "goodsAmount", "goodsPayAcmount", "goodNum", "goodsImg", "payType",
			"evaluationStatus", "orderStatus", "orderSn", "storeName", "token" };
		infosVos = BeanUtil.changeObjectArrayToMaps(infosVos, param);
		return infosVos;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager findOrderByMemberId(int pageNo, int pageSize, Long memberId, String startTime, String endTime) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> arrMap = new HashMap<String,Object>();
		sql.append(
			"select o.ORDER_SN_,o.MEMBER_ID_,s.STORE_NAME_,o.ORDER_TOTAL_AMOUNT_,o.STORE_BENEFIT_AMOUNT_,o.ADD_TIME_,o.FINISHED_TIME_,o.ORDER_TYPE_,o.PAY_TYPE_,o.ORDER_STATUS_ from gjf_order_info as o,gjf_store_info as s where o.STORE_ID_ = s.ID_ and o.IS_DEL_ = '1' and o.MEMBER_ID_ = "
				+ memberId + "  and o.ORDER_STATUS_ IN ('1','2','3')");
		if (StringUtil.isNotBlank(startTime)) {
			arrMap.put("startTime", startTime);
			sql.append(" and o.ADD_TIME_ >= :startTime ");
		}
		if (StringUtil.isNotBlank(endTime)) {
			arrMap.put("endTime", endTime);
			sql.append(" and o.ADD_TIME_ <=:endTime ");
		}
		sql.append(" order by o.FINISHED_TIME_ desc ");
		List list = getCurrentSession().createSQLQuery(sql.toString()).setProperties(arrMap).setMaxResults(pageSize)
			.setFirstResult((pageNo - 1) * pageSize).list();
		String[] param = { "orderSn", "memberId", "storeName", "totalAmount", "benefitAmount", "addTime", "finishedTime", "type",
			"payType","orderStatus" };
		String sql0 = sql.toString().replace(
			"select o.ORDER_SN_,o.MEMBER_ID_,s.STORE_NAME_,o.ORDER_TOTAL_AMOUNT_,o.STORE_BENEFIT_AMOUNT_,o.ADD_TIME_,o.FINISHED_TIME_,o.ORDER_TYPE_,o.PAY_TYPE_,o.ORDER_STATUS_",
			"select count(1)");
		list = BeanUtil.changeObjectArrayToMaps(list, param);
		BigInteger count = (BigInteger) getCurrentSession().createSQLQuery(sql0.toString()).setProperties(arrMap).uniqueResult();
		Pager pager = new Pager(pageSize, pageNo, count.intValue(), list);
		return pager;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map findOrderInfoById(Long orderId) {
		String sql = "select o.ORDER_SN_,o.PAY_SN_,o.PICKUP_CODE_,o.GOODS_TOTAL_AMOUNT_,o.ORDER_TOTAL_AMOUNT_,o.REAL_PAY_AMOUNT_,o.ONLINE_PAY_AMOUNT_,o.OFFLINE_PAY_AMOUNT_,o.STORE_REC_AMOUNT_,"
			+ "o.STORE_BENEFIT_AMOUNT_,o.REFUND_AMOUNT_,o.PAY_TYPE_,o.ADD_TIME_,o.PAY_TIME_,o.DELIVERY_TIME_,o.FINISHED_TIME_,o.OVERDUE_TIME_,o.REFUND_TIME_,o.CANCEL_TIME_,"
			+ "o.REMARK_,o.CANCEL_REASON_,o.ORDER_TYPE_,o.EVALUATION_STATUS_,o.ORDER_STATUS_,o.REFUND_STATUS_,g.GOODS_ATTR_,g.GOODS_NAME_,g.GOODS_AMOUNT_,g.GOODS_PAY_AMOUNT_,g.GOODS_NUM_,"
			+ "g.GOODS_IMAGE_URL_,g.GOODS_TYPE_,g.STORE_REC_AMOUNT_,g.STORE_BENEFIT_AMOUNT_,a.RECIVER_NAME_,a.RECIVER_MOBILE_,a.SHIPPING_FEE_AMOUNT_,a.COURIER_NAME_,a.SHIPPING_CODE_"
			+ "from gjf_order_info as o left join gjf_order_goods as g on o.ID_=g.ORDER_ID_ left join gjf_order_address as a on o.ID_=a.ORDER_ID_ where o.ID_=:orderId";
		Object[] obj = (Object[]) getCurrentSession().createSQLQuery(sql.toString()).uniqueResult();
		String[] pam = { "orderSn", "paySn", "pickupCode", "goodTotalAmount", "orederTotal", "realPayAmount", "onlinePayAmount",
			"offLinePayAmount", "storeRecAmount" + "storeBenefitAmount", "refundAmount", "payType", "addTime", "payTime",
			"deliveryTime", "finishedTime", "overdueTime", "refundTime", "cancelTime" + "remark", "cancelReson", "orderType",
			"evaluationStatus" };
		return BeanUtil.changeObjectArrayToMap(obj, pam);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findLowersOrderGoodsById(Long id) {
		StringBuffer sb = new StringBuffer();
		sb.append(
			"select g.ORDER_ID_,g.GOODS_ID_,g.GOODS_NAME_,g.GOODS_AMOUNT_,g.GOODS_PAY_AMOUNT_,g.GOODS_NUM_ from gjf_order_info as o,gjf_order_goods as g where o.ID_ = g.ORDER_ID_ and o.MEMBER_ID_=")
			.append(id);
		sb.append(" order by o.ADD_TIME_ desc");
		List list = getCurrentSession().createSQLQuery(sb.toString()).list();
		String[] param = { "orderId", "goodsId", "goodsName", "goodsAmount", "goodsPayAmount", "goodsNum" };
		list = BeanUtil.changeObjectArrayToMaps(list, param);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pager findOrderByCondition(int pageNo, int pageSize, Long id,String token, String startTime, String endTime) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> arrMap = new HashMap<String,Object>();
		sql.append(
			"select o.TRADE_NO_,m.NAME_,o.TRADE_MONEY_,o.BENEFIT_MONEY_,o.ADD_TIME_,o.PAY_TYPE_,o.TRADE_STATUS_ "
				+ "from gjf_member_trade_benefit o LEFT JOIN gjf_member_info m on o.MEMBER_ID_ = m.ID_ LEFT JOIN gjf_store_info s on o.STORE_ID_ = s.ID_ where "
				+ " s.ID_ = ").append(id).append(" and s.TOKEN_ = '").append(token).append("' ");
		if (StringUtil.isNotBlank(startTime)) {
			arrMap.put("startTime", startTime);
			sql.append(" and o.ADD_TIME_ >= :startTime ");
		}
		if (StringUtil.isNotBlank(endTime)) {
			arrMap.put("endTime", endTime);
			sql.append(" and o.ADD_TIME_ <= :endTime ");
		}
		sql.append(" order by o.ADD_TIME_ desc");
		String sql0 = sql.toString().replace("select o.TRADE_NO_,m.NAME_,o.TRADE_MONEY_,o.BENEFIT_MONEY_,o.ADD_TIME_,o.PAY_TYPE_,o.TRADE_STATUS_","select count(1)");
		BigInteger count = (BigInteger) getCurrentSession().createSQLQuery(sql0).setProperties(arrMap).uniqueResult();
		List list = getCurrentSession().createSQLQuery(sql.toString()).setProperties(arrMap).setMaxResults(pageSize)
			.setFirstResult((pageNo - 1) * pageSize).list();
		String[] param = { "tradeNo", "name", "tradeMoney","benefitMoney", "addTime", "payType", "tradeStatus"};
		list = BeanUtil.changeObjectArrayToMaps(list, param);
		Pager pager = new Pager(pageSize, pageNo, count.intValue(), list);
		return pager;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findCountOrderByCondition(Long id, String token, String startTime, String endTime) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> arrMap = new HashMap<String,Object>();
		sql.append(
			"select IFNULL(SUM(o.TRADE_MONEY_),0),IFNULL(SUM(o.BENEFIT_MONEY_),0) "
				+ "from gjf_member_trade_benefit o LEFT JOIN gjf_member_info m on o.MEMBER_ID_ = m.ID_ LEFT JOIN gjf_store_info s on o.STORE_ID_ = s.ID_ where "
				+ " s.ID_ = ").append(id).append(" and s.TOKEN_ = '").append(token).append("' ");
		if (StringUtil.isNotBlank(startTime)) {
			arrMap.put("startTime", startTime);
			sql.append(" and o.ADD_TIME_ >= :startTime ");
		}
		if (StringUtil.isNotBlank(endTime)) {
			arrMap.put("endTime", endTime);
			sql.append(" and o.ADD_TIME_ <= :endTime ");
		}
		List list = getCurrentSession().createSQLQuery(sql.toString()).setProperties(arrMap).list();
		String[] param = {  "totalTradeMoney","totalBenefitMoney"};
		list = BeanUtil.changeObjectArrayToMaps(list, param);
		return list;
	}

	/**
	 * 统计购买积分商品的次数
	 */
	@Override
	public int countMemberOrder(Long memId, Long proId) {
		String sql="select count(*) from gjf_order_info as o left join gjf_order_goods as g on o.ID_=g.ORDER_ID_ where o.MEMBER_ID_=:memId and g.GOODS_ID_=:proId and (o.ORDER_STATUS_=1 or o.ORDER_STATUS_=2 or o.ORDER_STATUS_=3)";
		BigInteger bInt=(BigInteger) getCurrentSession().createSQLQuery(sql).setParameter("memId", memId).setParameter("proId", proId).uniqueResult();
		return bInt.intValue();
	}

	/**
	 * 结算订单积分
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GjfOrderInfo> findSettlementOrder() {
		String hql="from GjfOrderInfo WHERE orderStatus=5 and TIMESTAMPDIFF(DAY,addTime,now())>16";
		List<GjfOrderInfo> orList=getCurrentSession().createQuery(hql).list();
		return orList;
	}

	/**
	 * 查询用户订单支付金额
	 * @param memberId
	 * @return
	 */
	@Override
	public BigDecimal findMemberOrderPayMoney(Long memberId) {
		String sql="select IFNULL(SUM(GOODS_TOTAL_AMOUNT_),0) from gjf_order_info where ORDER_STATUS_=3 and MEMBER_ID_=:memberId and IS_UPGRADE_PRO_=1";
		BigDecimal goodMoney=(BigDecimal) getCurrentSession().createSQLQuery(sql.toString()).setParameter("memberId", memberId).uniqueResult();
		return goodMoney;
	}

	/**
	 * 获取上个月代金券金额
	 * @param memberId
	 * @return
	 */
	@Override
	public BigDecimal findMemberTotalVoucherLastMonth(Long memberId) {
		String sql="SELECT IFNULL(sum(TRADE_MONEY_),0)  FROM  gjf_member_vouchers_trade_history"
                     +" where PAY_TYPE_=6 and TRADE_STATUS_=1  and PERIOD_DIFF(DATE_FORMAT(now(),'%Y%m') ,DATE_FORMAT(ADD_TIME_, '%Y%m')) =1 AND MEMBER_ID_=:memberId";
		BigDecimal voucherMoney=(BigDecimal) getCurrentSession().createSQLQuery(sql.toString()).setParameter("memberId", memberId).uniqueResult();
		return voucherMoney;
	}

}
