import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.service.GoodsService;
import com.microBusiness.manage.service.NeedProductService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ProductService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.util.DateUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-test.xml"})
public class ExcelImport {

	@Resource
	private SupplierService supplierService;
	
	@Resource
	private SupplyNeedService supplyNeedService;
	@Resource
	private NeedService needService;
	@Resource
	private ProductService productService;
	@Resource
	private NeedProductService needProductService;
	@Resource
	private GoodsService goodsService;
	
	/**
	 * 批量创建个体供应
	 */
	@Test
	public void test() {
		try {
			Workbook workbook = new XSSFWorkbook(new File("E://quanshuju.xlsx"));
			Sheet sheet = workbook.getSheetAt(0);
			int rowLen = sheet.getPhysicalNumberOfRows();
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
			Row row;
			for(int i = 1 ; i < rowLen ; i++) {
				row = sheet.getRow(i) ;
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				//客户编号
				Cell clientNumCell = row.getCell(0);
				String clientNum = clientNumCell.getStringCellValue();
				
				//商品编号
				Cell snCell = row.getCell(1);
				String sn = "";
				try {
					sn = snCell.getStringCellValue();
				} catch (Exception e) {
					sn = snCell.getNumericCellValue()+"";
					if(sn.indexOf(".")>-1){
						sn=sn.substring(0, sn.indexOf("."));
					}
					
				}
				
                //商品价格
                Cell priceCell = row.getCell(2);
                BigDecimal price;
               try {
            	   Double pc = priceCell.getNumericCellValue();
            	   price = new BigDecimal(pc).setScale(2,   BigDecimal.ROUND_HALF_UP);
				} catch (Exception e) {
					String str = priceCell.getStringCellValue();
					price = new BigDecimal(str);
				}
                map.put("clientNum", clientNum.trim());
                map.put("sn", sn.trim());
                map.put("price", price);
                resultList.add(map);
			
                System.out.println("aaaaaaa："+i+ "客户编号:" + clientNum + "商品编号:" + sn + "价格:"+price);
			}
			
			Map<String, List<Map<String, Object>>> eMap = new HashMap<String, List<Map<String,Object>>>();
			for(Map<String, Object> map : resultList) {
				String clientNum = (String) map.get("clientNum");
				String sn = (String) map.get("sn");
				BigDecimal price = (BigDecimal) map.get("price");
				if(eMap.containsKey(clientNum)) {
					List<Map<String, Object>> list = eMap.get(clientNum);
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("clientNum", clientNum);
					map2.put("sn", sn);
					map2.put("price", price);
					list.add(map2);
					eMap.put(clientNum, list);
				}else {
					List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("clientNum", clientNum);
					map2.put("sn", sn);
					map2.put("price", price);
					list.add(map2);
					eMap.put(clientNum, list);
				}
			}
			
			Set<String> keyset = eMap.keySet();
			Supplier supplier = supplierService.find(12L);
			for(String key : keyset) {
				Need need = needService.find(key,supplier);
				if(null != need) {
					Date startDate = new Date();
					Calendar calendar = Calendar.getInstance();
		            calendar.set(2018, 10, 15);
					Date endDate = calendar.getTime();
					
					List<SupplyNeed.Status> status=new ArrayList<>();
					status.add(SupplyNeed.Status.SUPPLY);
					status.add(SupplyNeed.Status.WILLSUPPLY);
					List<SupplyNeed> listw=supplyNeedService.findByDateList(supplier,need);
					if(listw.size() > 0) {
						SupplyNeed supplyNeed = listw.get(0);
						
						List<NeedProduct> needProducts = needProductService.findList(supplyNeed);
						
						List<Map<String, Object>> list = eMap.get(key);
						//过滤重复的商品
			            Map<String, Map<String, Object>> moap = new HashMap<String, Map<String,Object>>();
			            for(Map<String, Object> map : list) {
			            	String sn = (String) map.get("sn");
			            	BigDecimal price = (BigDecimal) map.get("price");
			            	if(!moap.containsKey(sn)) {
			            		Map<String, Object> map2 = new HashMap<String, Object>();
			            		map2.put("sn", sn);
			            		map2.put("price", price);
			            		moap.put(sn, map2);
			            	}
			            }
			            
			            for(NeedProduct needProduct : needProducts) {
			            	Product product = needProduct.getProducts();
			            	Product product2 = productService.find(product.getId());
			            	Goods goods = product2.getGoods();
			            	String pSn = goods.getSn();
			            	Map<String, Object> map = moap.get(pSn);
			            	if(map != null) {
			            		moap.remove(pSn);
			            	}
			            }
			            
			            Set<String> newKey = moap.keySet();
			            for(String keys : newKey) {
			            	Map<String, Object> map = moap.get(keys);
			            	String sn = (String) map.get("sn");
			            	BigDecimal price = (BigDecimal) map.get("price");
			            	
			            	List<Product> products = productService.findList(sn, supplier);
			            	for(Product product : products) {
			            		NeedProduct needProduct = new NeedProduct();
				            	needProduct.setProducts(product);
				            	needProduct.setMinOrderQuantity(1);
				            	needProduct.setSupplyPrice(price);
				            	needProduct.setSupplyNeed(supplyNeed);
				            	needProductService.save(needProduct);
			            	}
			            }
						
						
						
					} else {
						SupplyNeed supplyNeed=new SupplyNeed();
			            supplyNeed.setSupplier(supplier);
			            supplyNeed.setNeed(need);
			            supplyNeed.setStartDate(DateUtils.specifyDateZero(startDate));
			            
			            supplyNeed.setEndDate(DateUtils.specifyDatetWentyour(endDate));
			            supplyNeed.setAssignedModel(SupplyNeed.AssignedModel.STRAIGHT);
			            supplyNeed.setOpenNotice(false);
			            supplyNeed.setNoticeDay(5);
			            
			            List<NeedProduct> needProductList = new ArrayList<NeedProduct>();
			            
			            List<Map<String, Object>> list = eMap.get(key);
			            
			            //过滤重复的商品
			            Map<String, Map<String, Object>> moap = new HashMap<String, Map<String,Object>>();
			            for(Map<String, Object> map : list) {
			            	String sn = (String) map.get("sn");
			            	BigDecimal price = (BigDecimal) map.get("price");
			            	if(!moap.containsKey(sn)) {
			            		Map<String, Object> map2 = new HashMap<String, Object>();
			            		map2.put("sn", sn);
			            		map2.put("price", price);
			            		moap.put(sn, map2);
			            	}
			            }
			            
			            Set<String> set = moap.keySet();
			            for(String keys : set) {
			            	Map<String, Object> map = moap.get(keys);
			            	String sn = (String) map.get("sn");
			            	BigDecimal price = (BigDecimal) map.get("price");
			            	
			            	List<Product> products = productService.findList(sn, supplier);
			            	for(Product product : products) {
			            		NeedProduct needProduct = new NeedProduct();
				            	needProduct.setProducts(product);
				            	needProduct.setMinOrderQuantity(1);
				            	needProduct.setSupplyPrice(price);
				            	needProductList.add(needProduct);
			            	}
			            }
			            if(needProductList.size() > 0) {
			            	supplyNeedService.save(supplyNeed, needProductList);
			            }
					
					}
					
					System.out.println("结束。。。。。。。。。。。。。。。。。。。。。。");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
