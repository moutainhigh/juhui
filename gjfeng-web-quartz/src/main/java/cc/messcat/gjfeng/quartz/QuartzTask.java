package cc.messcat.gjfeng.quartz;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cc.messcat.gjfeng.dubbo.benefit.GjfBenefitInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.CountInfoDubbo;
import cc.messcat.gjfeng.dubbo.core.GjfOrderInfoDubbo;
import cc.messcat.gjfeng.dubbo.sms.SmsDubbo;

@Component
public class QuartzTask {

	@Autowired
	@Qualifier("benefitInfoDubbo")
	private GjfBenefitInfoDubbo benefitInfoDubbo;

	@Autowired
	@Qualifier("countInfoDubbo")
	private CountInfoDubbo countInfoDubbo;

	@Autowired
	@Qualifier("orderInfoDubbo")
	private GjfOrderInfoDubbo orderInfoDubbo;

	@Autowired
	@Qualifier("smsDubbo")
	private SmsDubbo smsDubbo;

	/**
	 * @描述 定时分红
	 * @author Karhs
	 * @date 2017年2月9日
	 */
	public void updateBenefit() {
		System.out.println("定时会员和商家分红");
		try {
			// benefitInfoDubbo.updateBenefit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @描述 定时统计分红
	 * @author Karhs
	 * @date 2017年2月9日
	 */
	public void updateActBenefit() {
		System.out.println("定时统计分红");
		try {
			benefitInfoDubbo.updateMemberBenefitNotify();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @描述 定时代理分红
	 * @author Karhs
	 * @date 2017年2月9日
	 */
	public void updateAgentBenefit() {
		System.out.println("定时代理分红");
		try {
			benefitInfoDubbo.updateAgentBenefitByDayNotify();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/********* 统计报表 ************/
	/**
	 * @描述 统计总报表
	 * @author Karhs
	 * @date 2017年2月17日
	 * @return
	 */
	public void updateSummaryReport() {
		countInfoDubbo.updateSummaryReport();
	}

	/**
	 * @描述 统计让利日报表
	 * @author Karhs
	 * @date 2017年2月17日
	 * @return
	 */
	public void updateBenefitReport() {
		countInfoDubbo.updateBenefitReport();
	}

	/**
	 * @描述 统计福利产出日报表
	 * @author Karhs
	 * @date 2017年2月17日
	 * @return
	 */
	public void updateWealOutputReport() {
		countInfoDubbo.updateWealOutputReport();
	}

	/**
	 * @描述 统计福利派发报表
	 * @author Karhs
	 * @date 2017年2月17日
	 * @return
	 */
	public void updateWealPayoutReport() {
		countInfoDubbo.updateWealPayoutReport();
	}

	/**
	 * @描述 统计提现日报表
	 * @author Karhs
	 * @date 2017年2月17日
	 * @return
	 */
	public void updateWithdrawReport() {
		countInfoDubbo.updateWithdrawReport();
	}

	/**
	 * @描述 统计资金池表
	 * @author Karhs
	 * @date 2017年2月17日
	 * @return
	 */
	public void updatePoolReport() {
		countInfoDubbo.updatePoolReport();
	}

	/**
	 * @描述 修改总报表和福利派发报表的福利派发为实际派发额
	 * @author Karhs
	 * @date 2017年3月10日
	 */
	public void updateSummaryAndPayoutReport() {
		countInfoDubbo.updateSummaryAndPayoutReport();
	}

	/**
	 * 添加报表统计数据
	 */
	public void addTotalReport() {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = "";
		try {
			Date beginDate = new Date();
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
			endDate = dft.format(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		countInfoDubbo.addTotalReport(endDate);
	}

	/**
	 * 更新短信状态
	 */
	public void updateMnsSmsStatus() {
		// System.out.println("updateMnsSmsStatus");
		smsDubbo.updateMnsSmsStatus();
	}

	/**
	 * 调整用户每月是否参与分红
	 * 
	 * @return
	 */
	public void updateMemberIsDiviStatus() {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(new Date());
		final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		String lastTime = sdf.format(lastDate);
		if (time.equals(lastTime)) {
			countInfoDubbo.modifyAcountConsumption();
		}
	}

	/**
	 * 领回20%分红权减半50%再减半不变
	 * 
	 * @return
	 */
	public void updateMemberDedcteDivi() {
		countInfoDubbo.modifyMemberDeductDivi();
	}

	/**
	 * 结算天猫订单积分
	 */
	public void updateOrderBenefit() {
		orderInfoDubbo.updateOrderBenefit();
	}

	/**
	 * 统计用户是否为活跃用户
	 */
	public void updateMemberIsActivity() {
		Date bdate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(bdate);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			countInfoDubbo.modifyMemberIsActivity();	
		}
		
	}
	/**
	 * 发放活跃会员分红
	 */
	public void updateActivityMemberDivi(){
		benefitInfoDubbo.updateBenefit("0","1");
	}
	/**
	 * 发放非活跃会员分红
	 */
	public void updateNoActivityMemberDivi(){
		benefitInfoDubbo.updateBenefit("0","0");
	}
	
	/**
	 * 发放活跃商户分红
	 */
	public void updateActivityMerchantDivi(){
		benefitInfoDubbo.updateBenefit("1","1");
	}
	
	/**
	 * 发放非活跃商户分红
	 */
	public void updateNoActivityMerchantDivi(){
		benefitInfoDubbo.updateBenefit("1","0");
	}
}
