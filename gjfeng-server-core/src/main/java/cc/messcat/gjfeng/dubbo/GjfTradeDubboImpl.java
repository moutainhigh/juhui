package cc.messcat.gjfeng.dubbo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo;
import cc.messcat.gjfeng.entity.GjfRechargePaid;
import cc.messcat.gjfeng.entity.GjfSetDividends;
import cc.messcat.gjfeng.entity.GjfWeiXinPayInfo;
import cc.messcat.gjfeng.service.benefit.GjfBenefitInfoService;
import cc.messcat.gjfeng.service.member.GjfTradeService;

public class GjfTradeDubboImpl implements GjfTradeDubbo {

	@Autowired
	@Qualifier("gjfTradeService")
	private GjfTradeService gjfTradeService;
	
	@Autowired
	@Qualifier("gjfBenefitInfoService")
	private GjfBenefitInfoService gjfBenefitInfoService;

	/*
	 * 跳转到提现
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#toDrawCash(java.lang.String)
	 */
	@Override
	public ResultVo toDrawCash(String account) {
		return gjfTradeService.toDrawCash(account);
	}

	/*
	 * 添加提现
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#addDrawCash(java.lang.String,
	 * java.lang.String, java.lang.Long, java.lang.Double)
	 */
	@Override
	public ResultVo addDrawCash(String account, String remark, Long myBankId, Double money) {
		return gjfTradeService.addDrawCash(account, remark, myBankId, money);
	}

	/*
	 * 审核提现记录
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#updateDrawCashStatus(java.lang
	 * .Long, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo updateDrawCashStatus(Long id, String token, String tradeStatus,String userName) {
		return gjfTradeService.updateDrawCashStatus(id, token, tradeStatus,userName);
	}
	
	/**
	 * 退回审核状态
	 * @param id
	 * @param token
	 * @param userName
	 * @return
	 */
	public ResultVo preDrawCashStatus(Long id,String token,String userName){
		return gjfTradeService.preDrawCashStatus(id, token,userName);
	}

	/*
	 * 查询提现记录信息
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findDrawCashHistory(java.lang.
	 * Long, java.lang.String)
	 */
	@Override
	public ResultVo findDrawCashHistory(Long id, String token) {
		return gjfTradeService.findDrawCashHistory(id, token);
	}

	/*
	 * 根据用户Id查询提现记录
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findDrawCashHistoryById(java.
	 * lang.Long)
	 */
	@Override
	public ResultVo findDrawCashHistoryById(Long id) {
		return gjfTradeService.findDrawCashHistoryById(id);
	}

	/*
	 * 查询我的提现历史记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findDrawCashHistory(int,
	 * int, java.lang.String)
	 */
	@Override
	public ResultVo findDrawCashHistory(int pageNo, int pageSize, String account) {
		return gjfTradeService.findDrawCashHistory(pageNo, pageSize, account);
	}
	
	/* 
	 * 查询提现流水详情
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findDrawCashHistoryDetail(java.lang.Long, java.lang.String)
	 */
	public ResultVo findDrawCashHistoryDetail(String tradeNo, String account){
		return gjfTradeService.findDrawCashHistoryDetail(tradeNo, account);
	}

	/*
	 * 绑定银行卡
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#bindMyBankCard(java.lang.
	 * String, java.lang.Long, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ResultVo bindMyBankCard(String account, Long bankId, String bankSub, String bankCard, String holder, String cityValue) {
		return gjfTradeService.addMyBankCard(account, bankId, bankSub, bankCard, holder,cityValue);
	}

	/*
	 * 删除我的银行卡
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#delMyBank(java.lang.String,
	 * java.lang.Long)
	 */
	public ResultVo delMyBank(String account, Long bankId) {
		return gjfTradeService.delMyBank(account, bankId);
	}

	/*
	 * 查找我的银行卡
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findMyBankCard(java.lang.
	 * String)
	 */
	@Override
	public ResultVo findMyBankCard(String account) {
		return gjfTradeService.findMyBankCard(account);
	}

	/*
	 * 查询所有的银行
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findAllBank()
	 */
	@Override
	public ResultVo findAllBank() {
		return gjfTradeService.findAllBank();
	}

	/*
	 * 购买分红权
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#addDivi(java.lang.String,
	 * java.lang.Double, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo addDivi(String account, Double diviNum, String payType, String reqStatus) {
		return gjfTradeService.addDivi(account, diviNum, payType, reqStatus);
	}

	/*
	 * 修改支付状态
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#updateDiviPayStatus(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public ResultVo updateDiviPayStatus(String diviNo, String payStatus) {
		return gjfTradeService.updateDiviPayStatus(diviNo, payStatus);
	}

	/*
	 * 根据流水号查询分红权信息
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findDivi(java.lang.String)
	 */
	@Override
	public ResultVo findDivi(String diviNo) {
		return gjfTradeService.findDivi(diviNo);
	}

	/*
	 * 根据流水号和token查询分红权信息
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findDivi(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ResultVo findDivi(String diviNo, String token) {
		return gjfTradeService.findDivi(diviNo, token);
	}

	/*
	 * 查询我的分红权购买历史记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findTradeDivi(int, int,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo findTradeDivi(int pageNo, int pageSize, String account, String status) {
		return gjfTradeService.findTradeDivi(pageNo, pageSize, account, status);
	}

	/*
	 * 查找所有分红权购买记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findAllTradeDivi()
	 */
	@Override
	public ResultVo findTradeDiviByPage( int pageNo,int pageSize,String name, String payStatus, String diviStatus,String startDate,String endDate) {
		return gjfTradeService.findAllTradeDivi(pageNo, pageSize, name,payStatus,diviStatus,startDate,endDate);
	}

	/**
	 * 统计分红权购买记录
	 * @return
	 */
	public ResultVo findCountTradeDivi(String name, String payStatus, String diviStatus){
		return gjfTradeService.findCountTradeDivi(name,payStatus,diviStatus);
	}
	
	/*
	 * 查询我的分红历史记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findTradeDiviHistory(int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo findTradeDiviHistory(int pageNo, int pageSize, String account, String tradeStatus) {
		return gjfTradeService.findTradeDiviHistory(pageNo, pageSize, account, tradeStatus);
	}

	/*
	 * 查询会员所有分红记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findAllTradeDiviHistory()
	 */
	@Override
	public ResultVo findMemberTradeDiviHistoryByPage(int pageNo,int pageSize,String name,String addTime,String endTime,String tradeNo,String tradeType,String tradeStatus) {
		return gjfTradeService.findMemberTradeDiviHistoryByPage(pageNo, pageSize, name,addTime,endTime,tradeNo,tradeType,tradeStatus);
	}
	
	/*
	 * 当前条件统计分红记录
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findCountTradeDiviHistory(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultVo findCountTradeDiviHistory(String name,String addTime,String endTime,String tradeNo,String tradeType,String tradeStatus){
		return gjfTradeService.findCountTradeDiviHistory( name,addTime,endTime,tradeNo,tradeType,tradeStatus);
	}
	
	/*
	 * 查询所有发放成功分红记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findAllTradeDiviHistory()
	 */
	@Override
	public ResultVo findALLTradeDiviHistoryByPage(int pageNo, int pageSize) {
		return gjfTradeService.findAllTradeDiviHistory(pageNo, pageSize);
	}

	/*
	 * 添加商家让利
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#addStoreBenefit(java.lang.
	 * Long, java.lang.String, java.lang.Double, java.lang.String)
	 */
	@Override
	public ResultVo addStoreBenefit(String account, Long storeId, String mobile, Double amount, String payType,String merchantGrade) {
		return gjfTradeService.addStoreBenefit(account, storeId, mobile, amount, payType,merchantGrade);
	}

	/*
	 * 修改商家让利支付状态
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#updateStoreBenefitPayStatus(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo updateStoreBenefitPayStatus(String tradeNo, String payTradeNo, String payStatus) {
		return gjfTradeService.updateStoreBenefitPayStatus(tradeNo, payTradeNo, payStatus);
	}

	/*
	 * 查找前端商家查询自己的让利流水记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findStoreBenefit(int,
	 * int, java.lang.Long)
	 */
	@Override
	public ResultVo findStoreBenefit(int pageNo, int pageSize, Long storeId) {
		return gjfTradeService.findStoreBenefit(pageNo, pageSize, storeId);
	}
	
	/* 
	 * 查询用户的商家给他的让利记录
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findMemberBenefit(int, int, java.lang.String)
	 */
	public ResultVo findMemberBenefit(int pageNo,int pageSize,String account){
		return gjfTradeService.findMemberBenefit(pageNo, pageSize, account);
	}

	/*
	 * 查询后台查询所有的商家让利记录
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findStoreBenefitPager(int,
	 * int, java.lang.Long)
	 */
	@Override
	public ResultVo findStoreBenefitPager(int pageNo, int pageSize, Long storeId) {
		return gjfTradeService.findStoreBenefitPager(pageNo, pageSize, storeId);
	}

	@Override
	public ResultVo findAllTradeInBack(Integer pageNo, Integer pageSize, Long memberId,String holder,String bankCard,
			String mobile,String addTime,String endTime,String tradeStatus,String ste,Long id)throws Exception {
		return gjfTradeService.findAllTradeInBack(pageNo, pageSize, memberId,holder,bankCard,mobile,addTime,endTime,tradeStatus,ste,id);
	}
	
	@Override
	public ResultVo findTradeAmountInBack(String holder,String bankCard,
			String mobile,String addTime,String endTime,String tradeStatus,Long id) throws Exception {
		return gjfTradeService.findTradeAmountInBack(holder,bankCard,mobile,addTime,endTime,tradeStatus,id);
	}

	@Override
	public ResultVo findBenefitByPage(int pageNo,int pageSize,String name,String storeName,String payType,String tradeStatus,String addTime,String endTime,String ste,String directMemberName,String directMerchantsName)  throws Exception{
		return gjfTradeService.findAllBenefit(pageNo, pageSize, name,storeName,payType,tradeStatus,addTime,endTime,ste,directMemberName,directMerchantsName);
	}
	
	@Override
	public ResultVo findBenefitAmountInBack(String name, String storeName, String addTime,String endTime, String directMemberName,
			String directMerchantsName,String tradeStatus,String payType) {
		return gjfTradeService.findBenefitAmountInBack(name, storeName, addTime,endTime, directMemberName, directMerchantsName,tradeStatus,payType);
	}

	@Override
	public ResultVo findMemberBankById(Long bankId, String mobile) {
		return gjfTradeService.finMemberBankById(bankId, mobile);
	}

	/*
	 * 根据时间段查询商家让利记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findBenefitByTime(int,
	 * int, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo findBenefitByTime(int pageNo, int pageSize, Long id, String startTime, String endTime) {
		return gjfTradeService.findBenefitByTime(pageNo, pageSize, id, startTime, endTime);
	}

	/*
	 * 查询支付明细
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findTradeLogByPage(int,
	 * int, java.lang.String)
	 */
	@Override
	public ResultVo findTradeLogByPage(int pageNo,int pageSize,String ste,String name,
			String storeName,String addTime,String endTime,String tradeNo,String payTradeNo,String tradeType,String tradeStatus) throws ParseException{
		return gjfTradeService.findTradeLogByPage(pageNo, pageSize, ste,name,storeName,addTime,endTime,tradeNo,payTradeNo,tradeType,tradeStatus);
	}
	
	/*
	 * 统计当前条件支付明细
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findTradeLogByPage(int,
	 * int, java.lang.String)
	 */
	@Override
	public ResultVo findCountTradeLog(String name,String storeName,String addTime,String endTime,String tradeNo,String payTradeNo,String tradeType,String tradeStatus){
		return gjfTradeService.findCountTradeLog(name,storeName,addTime,endTime,tradeNo,payTradeNo,tradeType,tradeStatus);
	}

	/*
	 * 充值授授信额度
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#addShouXin(java.lang.String,
	 * java.lang.Double)
	 */
	@Override
	public ResultVo addShouXin(String type, String account, Double tradeMoney,String shouType,String fileImage) {
		return gjfTradeService.addShouXin(type, account, tradeMoney,shouType,fileImage);
	}

	/*
	 * 修改充值授信额度的支付状态
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#updateShouXinPayStatus(java.
	 * lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ResultVo updateShouXinPayStatus(String tradeNo, String payTradeNo, String payStatus) {
		return gjfTradeService.updateShouXinPayStatus(tradeNo, payTradeNo, payStatus);
	}

	/*
	 * 获取所有授信记录
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#getAllShouXin(java.lang.
	 * Integer, java.lang.Integer)
	 */
	@Override
	public ResultVo getAllShouXin(Integer pageNo, Integer pageSize, String account) {
		return gjfTradeService.getShouXinRc(pageNo, pageSize, account);
	}

	/*
	 * 查询用户代理信息
	 * 
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findAgentInfo(java.lang.
	 * String)
	 */
	@Override
	public ResultVo findAgentInfo(String account) {
		return gjfTradeService.findAgentInfo(account);
	}

	/*
	 * 查询用户代理分红历史记录
	 * 
	 * @see
	 * cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findAgentHistory(java.lang.
	 * Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ResultVo findAgentHistory(Integer pageNo, Integer pageSize, String account) {
		return gjfTradeService.findAgentHistory(pageNo, pageSize, account);
	}
	
	/* 
	 * 查询我的下级代理
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findNextAgent(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ResultVo findNextAgent(String account, Integer pageNo, Integer pageSize) {
		return gjfTradeService.findNextAgent(account, pageNo, pageSize);
	}

	/**
	 * 查询提现详细信息
	 */
	@Override
	public ResultVo findMemberTradeDetail(Long id, String token) {
		return gjfTradeService.findMemberTradeDetail(id, token);
	}

	@Override
	public ResultVo findMemberSalesWelfare(String account, String tradeType) {
		return gjfTradeService.findMemberSalesWelfare(account, tradeType);
	}

	/**
	 * 我的钱包---福利权益
	 * @param account
	 * @return
	 */
	@Override
	public ResultVo findMemberParticipate(String account,String type) {
		
		return  gjfTradeService.findMemberParticipate(account,type);
	}

	/**
	 * 我的钱包---累积消费
	 * @param account
	 * @return
	 */
	@Override
	public ResultVo findMemberInterests(String account,String type) {
		
		return gjfTradeService.findMemberInterests(account,type);
	}

	@Override
	public ResultVo findMemberTradeBenefitById(Long id, String token) {
		return gjfTradeService.findMemberTradeBenefitById(id,token);
	}

	@Override
	public ResultVo findMemberWithdrawHistoryMoney(Long id, String token) {
		return gjfTradeService.findMemberWithdrawHistoryMoney(id, token);
	}

	@Override
	public ResultVo findBalanceMoneyHistoryByMemberId(int pageNo, int pageSize, Long id, String token,String tradeType) {
		return gjfTradeService.findBalanceMoneyHistoryByMemberId(pageNo,pageSize,id,token,tradeType);
		
		
	}

	@Override
	public ResultVo findDiviHistoryByMemberId(int pageNo, int pageSize, Long id) {
		return gjfTradeService.findDiviHistoryByMemberId(pageNo, pageSize, id);
	}

	@Override
	public ResultVo findAgentDiviByMemberId(int pageNo, int pageSize, Long id, String identity) {
		return gjfTradeService.findAgentDiviByMemberId(pageNo,pageSize,id,identity);
	}


	/**
	 * 获取微信信息
	 */
	@Override
	public ResultVo findWeiXinBaseInfo(String type) {

		return gjfTradeService.findWeiXinPayBaseInfo(type);
	}

	@Override
	public ResultVo findAllDrawCashHistory(int pageNo, int pageSize) {

		return null;
	}


	@Override
	public ResultVo findMemberO2OHistory(int pageNo, int pageSize, Long id, String token) {
		return gjfTradeService.findMemberO2OHistory(pageNo,pageSize,id,token);
	}

	/**
	 * 获取全部微信配置信息
	 */
	@Override
	public ResultVo findAllWeiXinInfo(Integer pageNo,Integer pageSize) {
		return gjfTradeService.findAllWeiXinInfo(pageNo, pageSize);
	}

	@Override
	public ResultVo addWeiXinInfo(GjfWeiXinPayInfo weiXinInfo) {

		return gjfTradeService.addWeiXinInfo(weiXinInfo);
	}

	@Override
	public ResultVo updateWeiXinInfo(GjfWeiXinPayInfo weiXinInfo) {

		return gjfTradeService.updateWeiXinInfo(weiXinInfo);
	}

	@Override
	public ResultVo updateWeiInfos(Long id) {

		return gjfTradeService.updateWeiInfos(id);
	}

	@Override
	public ResultVo findWeiXinInfoById(Long id) {
	
		return gjfTradeService.findWeiXinInfoById(id);
	}
    
	/*
	 * 获取全部分红权设置信息
	 */
	@Override
	public ResultVo findAllDividends(Integer pageNo, Integer pageSize) {
		
		return gjfBenefitInfoService.findAllDividends(pageNo,pageSize);
	}

	/**
	 * 根据id获取分红权信息
	 * @param divId
	 * @return
	 */
	@Override
	public ResultVo findDividendsById(Long divId) {
	
		return gjfBenefitInfoService.findDividendsById(divId);
	}

	@Override
	public ResultVo addDividensData(GjfSetDividends setDiv) {
		
		return gjfBenefitInfoService.addDividensData(setDiv);
	}

	@Override
	public ResultVo removeDividensData(Long divId) {
		
		return gjfBenefitInfoService.removeDividensData(divId);
	}

	@Override
	public ResultVo modifyDividensData(GjfSetDividends setDiv) {
		
		return gjfBenefitInfoService.modifyDividensData(setDiv);
	}

	/**
	 * 查询用户最近七天之内的用户数据
	 */
	@Override
	public ResultVo findBenefitByTime(String mobile) {
		
		return gjfBenefitInfoService.findBenefitByTime(mobile);
	}

	
	/**
	 * 用户确认让利提示信息
	 * @param tradeNo
	 * @return
	 */
	@Override
	public ResultVo modifyBenefitConfirmStatus(String tradeNo) {
		
		return gjfBenefitInfoService.modifyBenefitConfirmStatus(tradeNo);
	}

	/*
	 * 根据订单号获取提现信息
	 * @see cc.messcat.gjfeng.dubbo.core.GjfTradeDubbo#findMemberTradeByOrderSn(java.lang.String)
	 */
	@Override
	public ResultVo findMemberTradeByOrderSn(String orderSn) {
		
		return gjfTradeService.findMemberTradeByOrderSn(orderSn);
	}

	/**
	 * 获取代付充值列表
	 */
	@Override
	public ResultVo findRechargePaid(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return gjfTradeService.findRechargePaid(pageNo, pageSize);
	}

	/**
	 * 添加代付记录
	 */
	@Override
	public ResultVo addRechargeRecord(GjfRechargePaid rechargePaid) {
	
		return gjfTradeService.addRechargeRecod(rechargePaid);
	}

	/**
	 * 分红转移
	 */

	public ResultVo modifyTransferDividendsMoney(String account) {
		
		return gjfBenefitInfoService.modifyTransferMoney(account);
	}

	/**
	 * 转移分红权
	 */
	@Override
	public ResultVo updateMemberPointTransfer(String type, String memberMobile, String transferMemberMobile,
			BigDecimal transferMoney) {
		
		return gjfTradeService.updateMemberPointTransfer(type, memberMobile, transferMemberMobile, transferMoney);
	}
	
	/**
	 * 后台获取积分转移记录
	 */
	public ResultVo findMemberPointByPager(Integer pageNo,Integer pageSize,String memberName,String transferMemberName,String memberMobile,String transferMemberMobile,String type){
		return gjfTradeService.findAllMemberPointByPage(pageNo, pageSize, memberName, transferMemberName, memberMobile, transferMemberMobile,type);
	}
	
	/**
	 * 获取转移历史记录
	 */
	public ResultVo findTransferHistoryByPager(Integer pageNo,Integer pageSize,String account,String type){
		return gjfTradeService.findTransferHistoryByPager(pageNo, pageSize, account, type);
	}

	/**
	 * 后台获取用户合并详情信息
	 */
	@Override
	public ResultVo findMemberMergeDetailById(Long id) {
		
		return gjfTradeService.findMemberMergeDetailById(id);
	}

	/**
	 * 获取特殊发放人统计记录
	 */
	@Override
	public ResultVo findSpecialTotalHistory(Integer pageNo, Integer pageSize) {
		
		return gjfBenefitInfoService.findSpecialTotalHistory(pageNo, pageSize);
	}

	/**
	 * 查看特殊发放人交易记录
	 */
	@Override
	public ResultVo findSpecialMemTradeDiviHistory(Integer pageNo, Integer pageSize, String addTime, Long memId,String type) {
		
		return gjfBenefitInfoService.findSpecialTradeDiviHistory(pageNo, pageSize, addTime, memId,type);
	}

	/**
	 * 获取授信记录
	 */
	@Override
	public ResultVo findShouxinHistory(Integer pageNo, Integer pageSize, String shouxinType) {
		// TODO Auto-generated method stub
		return gjfTradeService.findShouxinHistory(pageNo, pageSize, shouxinType);
	}

	/**
	 * 获取授信详细信息
	 */
	@Override
	public ResultVo findShouxinById(Long id) {
		// TODO Auto-generated method stub
		return gjfTradeService.findShouxinById(id);
	}

	/**
	 * 审核线下授信
	 */
	@Override
	public ResultVo updateShouxinStatus(Long id, String acduitStatus) {
		// TODO Auto-generated method stub
		return gjfTradeService.updateShouxinStatus(id, acduitStatus);
	}

	/**
	 * 获取用户凤凰宝信息
	 */
	@Override
	public ResultVo findFhTreasureInfo(String account) {
		
		return gjfTradeService.addMemberFhTreasureInfo(account);
	}

	/**
	 * 把余额转到凤凰宝
	 */
	@Override
	public ResultVo addBalanceToFhTreasure(String account, Double money) {
		
		return gjfTradeService.addBalanceToFhTreasure(account, money);
	}

	/**
	 * 添加用户凤凰宝转移记录
	 * @param account
	 * @param TransferMobile
	 * @param tradeMoney
	 * @return
	 */
	@Override
	public ResultVo addTransferFhTreasureHistory(String account, String TransferMobile, double tradeMoney) {
		
		return gjfTradeService.addTransferFhTreasureHistory(account, TransferMobile, tradeMoney);
	}

	/**
	 * 凤凰宝提现
	 */
	@Override
	public ResultVo addFhTreasureDrawCash(String account, String remark, Long myBankId, Double money) {
	
		return gjfTradeService.addFhTreasureDrawCash(account, remark, myBankId, money);
	}

	/**
	 * 获取凤凰宝交易记录
	 */
	@Override
	public ResultVo findFhTreaureTradeHistory(Integer pageNo, Integer pageSize, String account) {
		
		return gjfTradeService.findFhTreaureTradeHistory(pageNo, pageSize, account);
	}

	/**
	 * 后台获取凤凰宝交易记录
	 */
	@Override
	public ResultVo findFhTreasureTradeHistoryByCondition(Integer pageNo, Integer pageSize, String tradeType,
			String memName, String memMobile) {
		
		return gjfTradeService.findFhTreasureTradeHistoryByCondition(pageNo, pageSize, tradeType, memName, memMobile);
	}

	/**
	 * 添加商家充值商家联盟记录
	 * @param account
	 * @param tradeMoney
	 * @param merchantType
	 * @param payType
	 * @return
	 */
	@Override
	public ResultVo addMerchantRechargeHistory(String account, double tradeMoney, String merchantType, String payType) {
		
		return gjfTradeService.addMerchantRechargeHistory(account, tradeMoney, merchantType, payType);
	}
	
	/**
	 * 商家充值支付回调
	 * @param status
	 * @param tradeNo
	 * @return
	 */
	public ResultVo updateRechargeHistoryStatus(String status, String tradeNo){
		
		return gjfTradeService.updateRechargeHistoryStatus(status, tradeNo);
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
	@Override
	public ResultVo addMemberVouchersHistory(String account, String mobile, Double amount, String payType) {
		
		return gjfTradeService.addMemberVouchersHistory(account, mobile, amount, payType);
	}

	/**
	 * 商家赠送代金券支付回调
	 * @param status
	 * @param tradeNo
	 * @return
	 */
	@Override
	public ResultVo updateVoucherHistoryStatus(String status, String tradeNo) {
		
		return gjfTradeService.updateVoucherHistoryStatus(status,tradeNo);
	}

	/***
	 * 后台设置商家联盟
	 * @param type
	 * @param account
	 * @param tradeMoney
	 * @return
	 */
	@Override
	public ResultVo addMerchantModelInBack(String type, String account, double tradeMoney,Long area,Long pri,Long city,Date startTime,Date endTime) {
		
		return gjfTradeService.addMerchantModelInBack(type, account, tradeMoney,area,pri,city,startTime,endTime);
	}

	/**
	 * 获取赠送商家列表
	 */
	@Override
	public ResultVo findGiveMerchantModelByType(String type, Long memberId) {
	
		return gjfTradeService.findGiveMerchantModelByType(type,memberId);
	}

	/**
	 * 添加商家赠送记录
	 * @param account
	 * @param giveMemberId
	 * @param type
	 * @return
	 */
	@Override
	public ResultVo addMerchantGiveHistory(String account, Long giveMemberId, String type) {
		
		return gjfTradeService.addMerchantGiveHistory(account, giveMemberId, type);
	}

	/**
	 * 添加会员升级赠送记录
	 * @param account
	 * @param tradeMoney
	 * @param merchantType
	 * @param payType
	 * @return
	 */
	@Override
	public ResultVo addMerchantRechargeToMemberHistory(String account, String mobile, double tradeMoney,
			String merchantType, String payType) {
		
		return gjfTradeService.addMerchantRechargeToMemberHistory(account, mobile, tradeMoney, merchantType, payType);
	}

	/**
	 * 会员升级赠送支付回调
	 * @param status
	 * @param tradeNo
	 * @return
	 */
	@Override
	public ResultVo updateMerchantRechargeToMemberHistory(String status, String tradeNo) {
		
		return gjfTradeService.updateMerchantRechargeToMemberHistory(status, tradeNo);
	}

	/**
	 * 获取会员升级赠送记录
	 * @param memberId
	 * @return
	 */
	@Override
	public ResultVo findMerchantRechargeToMemberHistory(Long memberId) {
		
		return gjfTradeService.findMerchantRechargeToMemberHistory(memberId);
	}

	/**
	 * 获取用户赠送代金券记录
	 */
	@Override
	public ResultVo findMemberVoucherHistory(Long memberId) {
		
		return gjfTradeService.findMemberVoucherHistory(memberId);
	}

	

	/**
	 * 获取用户推荐奖励历史记录
	 * @param memberId
	 * @return
	 */
	@Override
	public ResultVo findMemberDirectMemberMoney(Long memberId) {
		
		return gjfTradeService.findMemberDirectMemberMoney(memberId);
	}

	/**
	 * 商家联盟奖励转移到凤凰宝
	 */
	@Override
	public ResultVo addMerchantDirectMoneyToFhTreasure(String account, double money) {
	
		return gjfTradeService.addMerchantDirectMoneyToFhTreasure(account, money);
	}

	/**
	 * 后台获取代金券交易历史记录
	 */
	@Override
	public ResultVo findMemberVouchers(String memberName, String memberMobile, Integer pageNo, Integer pageSize) {
		
		return gjfTradeService.findMemberVouchers(memberName, memberMobile, pageNo, pageSize);
	}

	/**
	 * 获取联盟商家升级记录
	 * @param pageNo
	 * @param pageSize
	 * @param type
	 * @return
	 */
	@Override
	public ResultVo findMemberUpgradeHistory(Integer pageNo, Integer pageSize, String type,String account,String memberName) {
		
		return gjfTradeService.findMemberUpgradeHistory(pageNo,pageSize,type,account,memberName);
	}

	
	/**
	 * 获取用户升级vip推荐奖励
	 * @param pageNo
	 * @param pageSize
	 * @param account
	 * @return
	 */
	@Override
	public ResultVo findMemberUpgradeVipDirectHistory(Integer pageNo, Integer pageSize, String account) {
		
		return gjfTradeService.findMemberUpgradeVipDirectHistory(pageNo, pageSize, account);
	}

	/**
	 * 后台充值积分
	 * @param useName
	 * @param integral
	 * @return
	 */
	@Override
	public ResultVo addRechargeIntegralInBack(String useName, String account, double rechargeMoney) {
		
		return gjfTradeService.addRechargeIntegralInBack(useName, account, rechargeMoney);
	}

	/**
	 * 后台获取积分充值记录
	 * @param pageNo
	 * @param pageSize
	 * @param mobile
	 * @param memName
	 * @param type
	 * @return
	 */
	@Override
	public ResultVo findAllRechargeIntergral(Integer pageNo, Integer pageSize, String mobile, String memName,
			String type) {
		
		return gjfTradeService.findAllRechargeIntergral(pageNo, pageSize, mobile, memName, type);
	}
	
	
	
}
