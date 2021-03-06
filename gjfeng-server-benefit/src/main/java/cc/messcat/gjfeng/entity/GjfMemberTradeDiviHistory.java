package cc.messcat.gjfeng.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * GjfMemberTradeDivi entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gjf_member_trade_divi_history")
public class GjfMemberTradeDiviHistory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_", length = 10)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="MEMBER_ID_")
	private GjfMemberInfo memberId;
	
	@Column(name = "TRADE_NO_", length = 20)
	private String tradeNo;
	
	@Column(name = "TRADE_MONEY_", precision=10, scale=2)
	private BigDecimal tradeMoney;
	
	@Column(name = "TRADE_DIVI_NUM_", precision=10, scale=2)
	private BigDecimal tradeDiviNum;
	
	@Column(name = "TRADE_UNIT_", precision=10, scale=2)
	private BigDecimal tradeUnit;
	
	@Column(name = "TRADE_REAL_UNIT_", precision=10, scale=2)
	private BigDecimal tradeRealUnit;
	
	@Column(name = "TRADE_RATIO_", precision=10, scale=2)
	private BigDecimal tradeRatio;
	
	@Column(name = "ADD_TIME_")
	private Date addTime;
	
	@Column(name = "TRADE_TIME_")
	private Date tradeTime;
	
	@Column(name = "TRADE_TYPE_", length = 1)
	private String tradeType;
	
	@Column(name = "TRADE_STATUS_", length = 1)
	private String tradeStatus;
	
	@Column(name = "TRADE_TRMO_", length = 100)
	private String tradeTrmo;
	
	@Column(name = "TOKEN_", length = 200)
	private String token;
	
	@Column(name = "IS_ACTIVITY_", length = 1)
	private String isActivity;
	
	@Column(name = "TRADE_SECOND_MONEY_", precision=20, scale=2)
	private BigDecimal tradeSecondMoney;
	
	@Column(name = "TRADE_THREE_MONEY_", precision=20, scale=2)
	private BigDecimal tradeThreeMoney;
	
	@Column(name = "TRADE_SECOND_UNIT_", precision=20, scale=6)
	private BigDecimal tradeSecondUnit;
	
	@Column(name = "TRADE_THREE_UNIT_", precision=20, scale=6)
	private BigDecimal tradeThreeUnit;
	
	@Column(name = "FIRST_CONSUMPTION_", precision=20, scale=2)
	private BigDecimal firstConsumption;
	
	@Column(name = "SECOND_CONSUMPTION_", precision=20, scale=2)
	private BigDecimal secondConsumption;
	
	@Column(name = "THREE_CONSUMPTION_", precision=20, scale=2)
	private BigDecimal threeConsumption;

	// Property accessors
	
	public GjfMemberTradeDiviHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GjfMemberTradeDiviHistory(Long id, GjfMemberInfo memberId, String tradeNo, BigDecimal tradeMoney,
		BigDecimal tradeDiviNum, BigDecimal tradeUnit, BigDecimal tradeRealUnit, BigDecimal tradeRatio, Date addTime,
		Date tradeTime, String tradeType, String tradeStatus, String tradeTrmo, String token) {
		super();
		this.id = id;
		this.memberId = memberId;
		this.tradeNo = tradeNo;
		this.tradeMoney = tradeMoney;
		this.tradeDiviNum = tradeDiviNum;
		this.tradeUnit = tradeUnit;
		this.tradeRealUnit = tradeRealUnit;
		this.tradeRatio = tradeRatio;
		this.addTime = addTime;
		this.tradeTime = tradeTime;
		this.tradeType = tradeType;
		this.tradeStatus = tradeStatus;
		this.tradeTrmo = tradeTrmo;
		this.token = token;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GjfMemberInfo getMemberId() {
		return this.memberId;
	}

	public void setMemberId(GjfMemberInfo memberId) {
		this.memberId = memberId;
	}

	public String getTradeNo() {
		return this.tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getTradeMoney() {
		return this.tradeMoney;
	}

	public void setTradeMoney(BigDecimal tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public BigDecimal getTradeRatio() {
		return this.tradeRatio;
	}

	public void setTradeRatio(BigDecimal tradeRatio) {
		this.tradeRatio = tradeRatio;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getTradeTime() {
		return this.tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeStatus() {
		return this.tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTradeTrmo() {
		return this.tradeTrmo;
	}

	public void setTradeTrmo(String tradeTrmo) {
		this.tradeTrmo = tradeTrmo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public BigDecimal getTradeDiviNum() {
		return tradeDiviNum;
	}

	public void setTradeDiviNum(BigDecimal tradeDiviNum) {
		this.tradeDiviNum = tradeDiviNum;
	}

	public BigDecimal getTradeUnit() {
		return tradeUnit;
	}

	public void setTradeUnit(BigDecimal tradeUnit) {
		this.tradeUnit = tradeUnit;
	}

	public BigDecimal getTradeRealUnit() {
		return tradeRealUnit;
	}

	public void setTradeRealUnit(BigDecimal tradeRealUnit) {
		this.tradeRealUnit = tradeRealUnit;
	}

	public BigDecimal getTradeSecondMoney() {
		return tradeSecondMoney;
	}

	public void setTradeSecondMoney(BigDecimal tradeSecondMoney) {
		this.tradeSecondMoney = tradeSecondMoney;
	}

	public BigDecimal getTradeThreeMoney() {
		return tradeThreeMoney;
	}

	public void setTradeThreeMoney(BigDecimal tradeThreeMoney) {
		this.tradeThreeMoney = tradeThreeMoney;
	}

	public BigDecimal getTradeSecondUnit() {
		return tradeSecondUnit;
	}

	public void setTradeSecondUnit(BigDecimal tradeSecondUnit) {
		this.tradeSecondUnit = tradeSecondUnit;
	}

	public BigDecimal getTradeThreeUnit() {
		return tradeThreeUnit;
	}

	public void setTradeThreeUnit(BigDecimal tradeThreeUnit) {
		this.tradeThreeUnit = tradeThreeUnit;
	}

	public BigDecimal getFirstConsumption() {
		return firstConsumption;
	}

	public void setFirstConsumption(BigDecimal firstConsumption) {
		this.firstConsumption = firstConsumption;
	}

	public BigDecimal getSecondConsumption() {
		return secondConsumption;
	}

	public void setSecondConsumption(BigDecimal secondConsumption) {
		this.secondConsumption = secondConsumption;
	}

	public BigDecimal getThreeConsumption() {
		return threeConsumption;
	}

	public void setThreeConsumption(BigDecimal threeConsumption) {
		this.threeConsumption = threeConsumption;
	}

	public String getIsActivity() {
		return isActivity;
	}

	public void setIsActivity(String isActivity) {
		this.isActivity = isActivity;
	}
	
	

}