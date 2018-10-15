import javax.annotation.Resource;

import org.junit.Test;

import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.CompanyGoodsService;
import com.microBusiness.manage.service.SupplierService;

public class CompanyGoodsServiceTest extends OtherTest {
	
	@Resource
	private CompanyGoodsService companyGoodsService;
	
	@Resource
	private SupplierService supplierService;

	@Test
	public void testSaveFromDhm(){
		CompanyGoods companyGoods = new CompanyGoods();
		Supplier supplier = supplierService.find(1l);
		companyGoods.setSupplier(supplier);
		Long[] goodsIds = new Long[]{1l,2l};
		Long[] categoryIds = new Long[]{1l,2l};
		//companyGoodsService.saveFromDhm(companyGoods, goodsIds, categoryIds);
	}
	
}
