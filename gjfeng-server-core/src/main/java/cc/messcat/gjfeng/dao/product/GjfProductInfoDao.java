package cc.messcat.gjfeng.dao.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cc.messcat.gjfeng.common.bean.Pager;
import cc.messcat.gjfeng.common.vo.app.ProductInfoVo;
import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.dao.BaseHibernateDao;
import cc.messcat.gjfeng.entity.GjfEnterpriseColumn;

public interface GjfProductInfoDao extends BaseHibernateDao<Serializable> {
	
	/**
	 * @描述 查询O2O商品的猜你喜欢
	 * @author Karhs
	 * @date 2017年1月18日
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<ProductInfoVo> findO2OIndexPro(int pageNo,int pageSize, Double longitude, Double latitude,Long cityId);

	/**
	 * @描述 根据商品栏目查询商品信息
	 * @author Karhs
	 * @date 2017年1月4日
	 * @param columnId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<ProductInfoVo> findProByColumnId(Long columnId, int pageNo, int pageSize, String shopType,String orderType, String likeValue,
		Double longitude, Double latitude,Long cityId);

	/**
	 * @描述 查询父类栏目下的所有商品
	 * @author Karhs
	 * @date 2017年1月14日
	 * @param columnId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<ProductInfoVo> findProByFatherColumnId(Long columnId, int pageNo, int pageSize, String shopType, String orderType, String likeValue,
		Double longitude, Double latitude,Long cityId);
	
	/**
	 * @描述 查询本店热销商品
	 * @author Karhs
	 * @date 2017年1月20日
	 * @param storeId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<ProductInfoVo> findHotProByStoreId(Long goodsId, int pageNo, int pageSize);
	
	/**
	 * @描述 查询o2o商品信息
	 * @author Karhs
	 * @date 2017年1月19日
	 * @param id
	 * @return
	 */
	public ProductInfoVo findO2OProductById(Long id,Double longitude, Double latitude);

	/**
	 * @描述 下架商品
	 * @author Karhs
	 * @date 2017年1月10日
	 * @param ids
	 * @param storeId
	 * @return
	 */
	public int delProduct(Long[] ids, Long storeId);
	
	/**
	 * 查询用户购物车信息
	 * @param memberId
	 * @return
	 */
	public List findAllCartMember(Long memberId);

	/**
	 * 查询热销产品
	 * @param param
	 * @return
	 */
	public List<ProductInfoVo> findHotProductsByStoreId(Long storeId, Map<String, Object> param);
	
	/**
	 * 删除商品栏目信息
	 * @param productId
	 * @param columnId
	 * @return
	 */
	public int deleteProductColumn(Long productId, Long columnId);
	
	/**
	 * 获取商户采购商品
	 * @param likeName
	 * @return
	 */
	public List<ProductInfoVo> findMerchantProcurementProduct(Long columnId,String likeName,Integer pageNo,Integer pageSize,String discount);
	
	/**
	 * 首页获取栏目商品信息
	 * @param goodsType
	 * @param pageTypeId
	 * @return
	 */
	public List<GjfEnterpriseColumn> findIndexPro(String goodsType, Long pageTypeId);
	
	/**
	 * pc获取商家采购商品
	 * @param columnId
	 * @param likeName
	 * @param pageNo
	 * @param pageSize
	 * @param discount
	 * @return
	 */
	public Pager findMerchantProcurementProductInPc(Long columnId,String likeName,Integer pageNo,Integer pageSize,String discount);
}
