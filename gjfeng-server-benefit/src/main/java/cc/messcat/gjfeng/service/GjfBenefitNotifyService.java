package cc.messcat.gjfeng.service;

import java.sql.SQLException;

import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.entity.GjfErrorInfo;

public interface GjfBenefitNotifyService {
	
	/**
	 * @描述 每天24点计算返回所有用户所获得的分红，已供于手动会自动发放
	 * @author Karhs
	 * @date 2017年2月7日
	 * @param membersMobile
	 * @param merchantsMobile
	 * @param totalBenefit
	 * @param consumptionMoney
	 * @return
	 */
	public ResultVo updateMemberBenefitNotify() throws NumberFormatException, SQLException;
	
	/**
	 * @描述 每天定时24点计算当天代理所获得的分红
	 * @author Karhs
	 * @date 2016年12月28日
	 * @return
	 */
	public ResultVo updateAgentBenefitByDayNotify() throws NumberFormatException, SQLException;
	
	/**
	 * @描述 手动或自动发放会员或商家分红
	 * @author Karhs
	 * @date 2017年2月16日
	 * @return
	 */
	public ResultVo updateBenefit(String account,String activityType);
	
	/**
	 * @描述 手动结算发放代理分红
	 * @author Karhs
	 * @date 2017年2月20日
	 * @return
	 */
	public ResultVo updateAgentBenefit(Long memberId,String token);
	
	/**
	 * @描述 保存错误信息
	 * @author Karhs
	 * @date 2017年2月23日
	 * @param errorInfo
	 * @return
	 */
	public ResultVo saveErrMsg(GjfErrorInfo errorInfo);
	
	/**
	 * 添加用户扣减历史
	 * @return
	 */
	public ResultVo addMemberDeductHistory();
	
	/**
	 * 发放积分
	 * @param account
	 * @param activityType
	 * @return
	 */
	public ResultVo updateIntegralDirectMoney(String ratioType,String activityType);
}
