package cc.messcat.gjfeng.common.vo.app;

import java.io.Serializable;
import java.math.BigDecimal;

import cc.messcat.gjfeng.entity.GjfAddressArea;
import cc.messcat.gjfeng.entity.GjfAddressCity;
import cc.messcat.gjfeng.entity.GjfAddressProvince;

public class StoreInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	
	private Long id;
	
	private String storeName;
	
	private String sellerName;
	
	private String sellerMobile;
	
	private String sellerEmail;
	
	private String storeNum;
	
    private GjfAddressProvince provinceId;
	
	private GjfAddressCity cityId;
	
	private GjfAddressArea areaId;
	
	private String storeCitys;
	
	private String addressDetail;
	
	private String storeLogoUrl;
	
	private String storeBanner;
	
	private String storeKeywords;
	
	private String storeDescription;
	
	private Long storeCreditScore;
	
	private BigDecimal storeDesccreditScore;
	
	private BigDecimal storeServiceCreditScore;
	
	private BigDecimal storeDeliveryCreditScore;
	
	private BigDecimal storeSaleTotalMoney;
	
	private BigDecimal storeMonthSaleTotalMoney;
	
	private String storeBefCustomer;
	
	private String storeAftCustomer;
	
	private String storeWorkingTime;
	
	private BigDecimal storeFreePrice;
	
	private String storeIsOwnShop;
	
	private BigDecimal storeRealIncomeRatio;
	
	private BigDecimal storeRealGiftRatio;
	
	private String storePro;
	
	private String storeType;
	
	private String storeStatus;
	
    private String payMchId;
	
	private String payKey;
	
	private Double distance;
	
	private String isActivityStore;
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerMobile() {
		return sellerMobile;
	}

	public void setSellerMobile(String sellerMobile) {
		this.sellerMobile = sellerMobile;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getStoreLogoUrl() {
		return storeLogoUrl;
	}

	public void setStoreLogoUrl(String storeLogoUrl) {
		this.storeLogoUrl = storeLogoUrl;
	}

	public String getStoreBanner() {
		return storeBanner;
	}

	public void setStoreBanner(String storeBanner) {
		this.storeBanner = storeBanner;
	}

	public String getStoreKeywords() {
		return storeKeywords;
	}

	public void setStoreKeywords(String storeKeywords) {
		this.storeKeywords = storeKeywords;
	}

	public String getStoreDescription() {
		return storeDescription;
	}

	public void setStoreDescription(String storeDescription) {
		this.storeDescription = storeDescription;
	}

	public String getStoreBefCustomer() {
		return storeBefCustomer;
	}

	public void setStoreBefCustomer(String storeBefCustomer) {
		this.storeBefCustomer = storeBefCustomer;
	}

	public String getStoreAftCustomer() {
		return storeAftCustomer;
	}

	public void setStoreAftCustomer(String storeAftCustomer) {
		this.storeAftCustomer = storeAftCustomer;
	}

	public String getStoreWorkingTime() {
		return storeWorkingTime;
	}

	public void setStoreWorkingTime(String storeWorkingTime) {
		this.storeWorkingTime = storeWorkingTime;
	}

	public BigDecimal getStoreFreePrice() {
		return storeFreePrice;
	}

	public void setStoreFreePrice(BigDecimal storeFreePrice) {
		this.storeFreePrice = storeFreePrice;
	}

	public String getStoreIsOwnShop() {
		return storeIsOwnShop;
	}

	public void setStoreIsOwnShop(String storeIsOwnShop) {
		this.storeIsOwnShop = storeIsOwnShop;
	}

	public BigDecimal getStoreRealIncomeRatio() {
		return storeRealIncomeRatio;
	}

	public void setStoreRealIncomeRatio(BigDecimal storeRealIncomeRatio) {
		this.storeRealIncomeRatio = storeRealIncomeRatio;
	}

	public BigDecimal getStoreRealGiftRatio() {
		return storeRealGiftRatio;
	}

	public void setStoreRealGiftRatio(BigDecimal storeRealGiftRatio) {
		this.storeRealGiftRatio = storeRealGiftRatio;
	}

	public String getStorePro() {
		return storePro;
	}

	public void setStorePro(String storePro) {
		this.storePro = storePro;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreCitys() {
		return storeCitys;
	}

	public void setStoreCitys(String storeCitys) {
		this.storeCitys = storeCitys;
	}

	public Long getStoreCreditScore() {
		return storeCreditScore;
	}

	public void setStoreCreditScore(Long storeCreditScore) {
		this.storeCreditScore = storeCreditScore;
	}

	public BigDecimal getStoreDesccreditScore() {
		return storeDesccreditScore;
	}

	public void setStoreDesccreditScore(BigDecimal storeDesccreditScore) {
		this.storeDesccreditScore = storeDesccreditScore;
	}

	public BigDecimal getStoreServiceCreditScore() {
		return storeServiceCreditScore;
	}

	public void setStoreServiceCreditScore(BigDecimal storeServiceCreditScore) {
		this.storeServiceCreditScore = storeServiceCreditScore;
	}

	public BigDecimal getStoreDeliveryCreditScore() {
		return storeDeliveryCreditScore;
	}

	public void setStoreDeliveryCreditScore(BigDecimal storeDeliveryCreditScore) {
		this.storeDeliveryCreditScore = storeDeliveryCreditScore;
	}

	public GjfAddressProvince getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(GjfAddressProvince provinceId) {
		this.provinceId = provinceId;
	}

	public GjfAddressCity getCityId() {
		return cityId;
	}

	public void setCityId(GjfAddressCity cityId) {
		this.cityId = cityId;
	}

	public GjfAddressArea getAreaId() {
		return areaId;
	}

	public void setAreaId(GjfAddressArea areaId) {
		this.areaId = areaId;
	}

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public BigDecimal getStoreSaleTotalMoney() {
		return storeSaleTotalMoney;
	}

	public void setStoreSaleTotalMoney(BigDecimal storeSaleTotalMoney) {
		this.storeSaleTotalMoney = storeSaleTotalMoney;
	}

	public BigDecimal getStoreMonthSaleTotalMoney() {
		return storeMonthSaleTotalMoney;
	}

	public void setStoreMonthSaleTotalMoney(BigDecimal storeMonthSaleTotalMoney) {
		this.storeMonthSaleTotalMoney = storeMonthSaleTotalMoney;
	}

	public String getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(String storeNum) {
		this.storeNum = storeNum;
	}

	public String getPayMchId() {
		return payMchId;
	}

	public void setPayMchId(String payMchId) {
		this.payMchId = payMchId;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}


	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getIsActivityStore() {
		return isActivityStore;
	}

	public void setIsActivityStore(String isActivityStore) {
		this.isActivityStore = isActivityStore;
	}

	
	
}