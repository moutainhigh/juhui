package cc.messcat.gjfeng.service.benefit;

import java.math.BigDecimal;

import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.entity.GjfSetDividends;

public interface GjfBenefitInfoService {

	/**
	 * @描述 用户每消费一笔，就会计算一次其所获得的分红权
	 * @author Karhs
	 * @date 2016年12月29日
	 * @param membersMobile
	 *            用户账号
	 * @param merchantsMobile
	 *            商家账号
	 * @param totalBenefit
	 *            让利金额
	 * @param consumptionMoney
	 *            消费金额
	 * @return
	 */
	public ResultVo updateMemberDividendsNumNotify(String membersMobile, String merchantsMobile, Double consumptionMoney,
		String tradeNo,String merchartType);

	/**
	 * @描述 用户每消费一笔，就会计算一次其所获得的分红权
	 * @author Karhs
	 * @date 2016年12月28日
	 * @param membersMobile
	 *            账号（手机号）
	 * @param consumptionMoney
	 *            消费金额
	 * @param memberType
	 *            0:普通用户 1:商家
	 * @return
	 */
	public BigDecimal[] updateMemberBenefitNum(String membersMobile, String memberType, Double consumptionMoney,
		Double totalBenefit, String tradeNo, BigDecimal subSysPla);
		
	/**
	 * @描述 计算用户每笔消费所产生的分红权额度，并返还给客户和商家
	 * @author Karhs
	 * @date 2016年12月28日
	 * @param totalBenefit
	 * @param membersMobile
	 * @param memberType
	 * @param actType
	 *            计算分红类型 0：直推 1：会员或商家分红
	 * @return
	 */
	public BigDecimal updateBenefit(BigDecimal totalBenefit, String membersMobile, String memberType, String actType,
		String tradeNo);
	
	
	/**
	 * @描述 用户每消费一笔，就会计算一次其所获得的分红权(可在后台设置)
	 * @author Karhs
	 * @date 2016年12月28日
	 * @param membersMobile
	 *            账号（手机号）
	 * @param consumptionMoney
	 *            消费金额
	 * @param memberType
	 *            0:普通用户 1:商家
	 * @return
	 */
	public BigDecimal[] updateMemberBenefitNumCanSetInBack(String membersMobile, String memberType, Double consumptionMoney,
		Double totalBenefit, String tradeNo, BigDecimal subSysPla,String merchantGrade);
	
	/**
	 * 获取全部分红权设置信息
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public ResultVo findAllDividends(Integer pageNo,Integer pageSize);
	
	/**
	 * 根据id获取分红权设置信息
	 * @param divId
	 * @return
	 */
	public ResultVo findDividendsById(Long divId);
	
	/**
	 * 添加分红权设置信息
	 * @param setDiv
	 * @return
	 */
	public ResultVo addDividensData(GjfSetDividends setDiv);
	
	/**
	 *  删除分红权设置信息
	 * @param divId
	 * @return
	 */
	public ResultVo removeDividensData(Long divId);
	
	/**
	 *  编辑分红权设置信息
	 * @param divId
	 * @return
	 */
	public ResultVo modifyDividensData(GjfSetDividends setDiv);
	
	/**
	 * 查询今天最近7天之内的让利数据
	 * @param mobile
	 * @return
	 */
	public ResultVo findBenefitByTime(String mobile);
	
	/**
	 * 用户确认让利提示信息
	 * @param tradeNo
	 * @return
	 */
	public ResultVo modifyBenefitConfirmStatus(String tradeNo);
	
	/**
	 * 转移分红金额
	 * @param account
	 * @return
	 */
	public ResultVo modifyTransferMoney(String account);
	
	/**
	 * 调整用户每月是否参与分红
	 * @return
	 */
	public ResultVo modifyMemberDiviByMonth();
	
	/**
	 *领回20%分红权减半50%再减半不变
	 * @return
	 */
	public ResultVo modifyMemberDeductDivi();
	
	/**
	 * 添加让利给运营中心
	 * @return
	 */
	public ResultVo addOperationCenterBenefit(String merchantsMobile, String tradeNo, double buyMoney, double benefitMoney);
	
	/**
	 * 查看特殊发放人统计记录
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public ResultVo findSpecialTotalHistory(Integer pageNo,Integer pageSize);
	
	/**
	 * 获取特殊发放人记录
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public ResultVo findSpecialTradeDiviHistory(Integer pageNo,Integer pageSize,String addTime,Long memId,String type);
	
		
}
