package cc.messcat.gjfeng.dubbo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo;
import cc.messcat.gjfeng.entity.GjfBenefitInfo;
import cc.messcat.gjfeng.entity.GjfBenefitMqLog;
import cc.messcat.gjfeng.service.GjfBenefitInfoService;
import cc.messcat.gjfeng.service.GjfBenefitNotifyService;

public class GjfBenefitInfoDubboImpl implements GjfBenefitInfoDubbo {
	
	@Autowired
	@Qualifier("gjfBenefitInfoService")
	private GjfBenefitInfoService gjfBenefitInfoService;
	
	@Autowired
	@Qualifier("gjfBenefitNotifyService")
	private GjfBenefitNotifyService gjfBenefitNotifyService;

	/* 
	 * 修改让利系统的比例配置
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#updateBenefitInfo(java.util.List)
	 */
	@Override
	public ResultVo updateBenefitInfo(List<GjfBenefitInfo> benefitInfos) {
		return gjfBenefitInfoService.updateBenefitInfo(benefitInfos);
	}

	/* 
	 * 根据类型查找占比配置
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#findByType(java.lang.String)
	 */
	@Override
	public GjfBenefitInfo findByType(String type) {
		return gjfBenefitInfoService.findByType(type);
	}

	/* 
	 * 查询所有的占比配置
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#findAlls()
	 */
	@Override
	public List<GjfBenefitInfo> findAlls() {
		return gjfBenefitInfoService.findAlls();
	}

	/* 
	 * 每3分钟计算返回所有用户所获得的分红
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#updateMemberBenefitNotify()
	 */
	@Override
	public ResultVo updateMemberBenefitNotify() throws NumberFormatException, SQLException {
		return gjfBenefitNotifyService.updateMemberBenefitNotify();
	}

	/* 
	 * 每天定时计算当天代理所获得的分红
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#updateAgentBenefitByDayNotify()
	 */
	@Override
	public ResultVo updateAgentBenefitByDayNotify() throws NumberFormatException, SQLException {
		return gjfBenefitNotifyService.updateAgentBenefitByDayNotify();
	}

	/* 
	 * 手动或自动发放会员或商家分红
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#updateBenefit()
	 */
	@Override
	public ResultVo updateBenefit(String account,String activityType) {
		//return gjfBenefitNotifyService.updateBenefit(account,activityType);
		return gjfBenefitNotifyService.updateIntegralDirectMoney(account,activityType);
	}

	/* 
	 * 手动结算发放代理分红
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#updateAgentBenefit()
	 */
	@Override
	public ResultVo updateAgentBenefit(Long memberId,String token) {
		return gjfBenefitNotifyService.updateAgentBenefit(memberId, token);
	}


	@Override
	public ResultVo findBenefitDayByPage(int pageNo, int pageSize) {
		return gjfBenefitInfoService.findBenefitDayByPage(pageNo, pageSize);
	}

	@Override
	public ResultVo findBenefitHistoryByTime(String addTime) throws Exception{
		return gjfBenefitInfoService.findBenefitHistoryByTime(addTime);
	}

	/* 
	 * 保存mq调用记录
	 * @see cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo#saveBenefitMqLog(cc.messcat.gjfeng.entity.GjfBenefitMqLog)
	 */
	@Override
	public ResultVo saveBenefitMqLog(GjfBenefitMqLog benefitMqLog) {
		return gjfBenefitInfoService.saveBenefitMqLog(benefitMqLog);
	}

	@Override
	public ResultVo findTodayRealTimePool(String type) {
		return gjfBenefitInfoService.findTodayRealTimePool(type);
	}

	/**
	 * 测试云南扣减分红权
	 */
	@Override
	public ResultVo testMemberDedectDiviNum() {
		// TODO Auto-generated method stub
		return gjfBenefitNotifyService.addMemberDeductHistory();
	}

}
