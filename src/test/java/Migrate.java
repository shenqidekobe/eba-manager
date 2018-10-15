//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.microBusiness.manage.Filter;
//import com.microBusiness.manage.entity.CartItem;
//import com.microBusiness.manage.entity.LogType;
//import com.microBusiness.manage.entity.Member;
//import com.microBusiness.manage.entity.Need;
//import com.microBusiness.manage.entity.NeedProduct;
//import com.microBusiness.manage.entity.Order;
//import com.microBusiness.manage.entity.OrderItem;
//import com.microBusiness.manage.entity.OrderLog;
//import com.microBusiness.manage.entity.Product;
//import com.microBusiness.manage.entity.Supplier;
//import com.microBusiness.manage.entity.SupplierAssignRelation;
//import com.microBusiness.manage.entity.SupplierNeedProduct;
//import com.microBusiness.manage.entity.SupplierProduct;
//import com.microBusiness.manage.entity.SupplierSupplier;
//import com.microBusiness.manage.entity.SupplyNeed;
//import com.microBusiness.manage.entity.SupplyNeed.AssignedModel;
//import com.microBusiness.manage.entity.SupplyType;
//import com.microBusiness.manage.entity.SystemSetting;
//import com.microBusiness.manage.service.CartItemService;
//import com.microBusiness.manage.service.GoodsService;
//import com.microBusiness.manage.service.MemberService;
//import com.microBusiness.manage.service.NeedProductService;
//import com.microBusiness.manage.service.NeedService;
//import com.microBusiness.manage.service.OrderItemService;
//import com.microBusiness.manage.service.OrderLogService;
//import com.microBusiness.manage.service.OrderService;
//import com.microBusiness.manage.service.ProductService;
//import com.microBusiness.manage.service.SupplierAssignRelationService;
//import com.microBusiness.manage.service.SupplierNeedProductService;
//import com.microBusiness.manage.service.SupplierProductService;
//import com.microBusiness.manage.service.SupplierService;
//import com.microBusiness.manage.service.SupplierSupplierService;
//import com.microBusiness.manage.service.SupplyNeedService;
//import com.microBusiness.manage.service.SystemSettingService;
//
//public class Migrate extends OtherTest {
//	
//	@Resource
//	private OrderService orderService;
//	
//	@Resource
//	private SupplierService supplierService;
//	
//	@Resource
//	private NeedService needService;
//	@Resource
//	private MemberService memberService;
//	@Resource
//	private SupplierSupplierService supplierSupplierService;
//	@Resource
//	private SupplierAssignRelationService supplierAssignRelationService;
//	@Resource
//	private SupplyNeedService supplyNeedService;
//	@Resource
//	private GoodsService goodsService;
//	@Resource
//	private ProductService productService;
//	@Resource
//	private SupplierNeedProductService supplierNeedProductService;
//	@Resource
//	private SupplierProductService supplierProductService;
//	@Resource
//	private NeedProductService needProductService;
//	@Resource
//	private SystemSettingService systemSettingService;
//	@Resource
//	private OrderLogService orderLogService;
//	@Resource
//	private CartItemService cartItemService;
//	@Resource
//	private OrderItemService orderItemService;
//	
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	
//	@Test
//	public void doNeed(){
//		List<Need> needList = needService.findAll();
//		if(needList != null){
//			for (Need need : needList) {
//				System.out.println(need.getTel());
//				Member member = memberService.findByMobile(need.getTel());
//				if(member != null){
//					System.out.println(member.getId());
//					need.setMember(member);
//					needService.update(need);
//					System.out.println(need.getMember().getNickname());
//				}
//			}
//			
//		}
//	}
//	
//	/**@Test
//	@Transactional
//	public void doSupplyNeed(){
//		List<SupplierNeedProduct> list = supplierNeedProductService.findAll();
//		Set<Long> supplierSupplierSet = new HashSet<Long>();
//		Map<String, SupplierAssignRelation> supplyNeedMap = new HashMap<String, SupplierAssignRelation>();
//		Map<String, Long> assignMap = new HashMap<String, Long>();
//		if(list != null){
//			for (SupplierNeedProduct supplierNeedProduct : list) {
//				SupplierAssignRelation supplierAssignRelation = supplierNeedProduct.getAssignRelation();
//				if(assignMap.get(String.valueOf(supplierAssignRelation.getId())) != null){
//					continue;
//				}
//				assignMap.put(String.valueOf(supplierAssignRelation.getId()), supplierAssignRelation.getId());
//				Need need = supplierNeedProduct.getNeed();
//				Supplier bySupplier = need.getSupplier();
//				
//				
//				SupplyNeed supplyNeedParam = new SupplyNeed();
//				supplyNeedParam.setNeed(need);
//				supplyNeedParam.setSupplier(bySupplier);
//				supplyNeedParam.setStatus(SupplyNeed.Status.SUPPLY);
//				SupplyNeed supplyNeed = supplyNeedService.findSupplyNeed(supplyNeedParam);
//				if(supplyNeed == null){
//					supplyNeed = new SupplyNeed();
//					supplyNeed.setSupplier(bySupplier);
//					supplyNeed.setNeed(need);
//					SupplierSupplier supplierSupplier = supplierAssignRelation.getSupplyRelation();
//					supplierSupplierSet.add(supplierSupplier.getId());
//					
//					supplyNeed.setStartDate(supplierSupplier.getStartDate());
//					supplyNeed.setEndDate(supplierSupplier.getEndDate());
//					supplyNeed.setAssignedModel(AssignedModel.BRANCH);
//					
//					if(supplierSupplier.getStatus().equals(SupplierSupplier.Status.inTheSupply)){
//						supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
//					}else if(supplierSupplier.getStatus().equals(SupplierSupplier.Status.expired)){
//						supplyNeed.setStatus(SupplyNeed.Status.EXPIRED);
//					}else if(supplierSupplier.getStatus().equals(SupplierSupplier.Status.willSupply)){
//						supplyNeed.setStatus(SupplyNeed.Status.WILLSUPPLY);
//					}else if(supplierSupplier.getStatus().equals(SupplierSupplier.Status.suspendSupply)){
//						supplyNeed.setStatus(SupplyNeed.Status.STOP);
//					}
//					
//					supplyNeedService.save(supplyNeed);
//					supplyNeedMap.put(String.valueOf(supplyNeed.getId()), supplierAssignRelation);
//					
//					logger.error("supplierAssignRelation id : " + supplierAssignRelation.getId()
//					+ "====supplyNeed id : " + supplyNeed.getId() + "===" + 
//					supplyNeed.getStartDate() + "====" + supplyNeed.getEndDate() + "=== need" + supplyNeed.getNeed().getId());
//				}
//				
//			}
//			for (Long id : supplierSupplierSet) {
//				SupplierSupplier supplierSupplier = supplierSupplierService.find(id);
//				Supplier supplier = supplierSupplier.getSupplier();
//				Supplier bySupplier = supplierSupplier.getBySupplier();
//				
//				Set<SupplierProduct> productList = supplierSupplier.getSupplierProducts();
//				List<Long> goodsIdList = new ArrayList<Long>();
//				if(productList != null){
//					for (SupplierProduct supplierProduct : productList) {
//						Goods goods = supplierProduct.getProducts().getGoods();
//						goodsIdList.add(goods.getId());
//					}
//					List<Product> newProductList = goodsService.copySupplierGoods(supplierSupplier, goodsIdList, 
//							bySupplier);
//					if(newProductList == null){
//						continue;
//					}
//					System.out.println("supplier Id :" + supplier.getId() + "====== bySupplierId:" + bySupplier.getId()+ 
//							"======product size: " + newProductList.size() + " ======== supplierNeed size :" +  bySupplier.getSupplyNeeds().size());
//					for (Product product : newProductList) {
//						for (SupplyNeed supplyNeed : bySupplier.getSupplyNeeds()) {
//							NeedProduct needProduct = new NeedProduct();
//							needProduct.setProducts(product);
//							needProduct.setStatus(NeedProduct.Status.CHECKED);
//							needProduct.setSupplyNeed(supplyNeed);
//							
//							
//							Product sourceProduct = product.getSource();
//							if(sourceProduct == null){
//								continue;
//							}
//							Need need = supplyNeed.getNeed();
//							SupplierAssignRelation supplierAssignRelation = supplyNeedMap.get(String.valueOf(supplyNeed.getId()));
//							
//							
//							SupplierNeedProduct supplierNeedProduct = supplierNeedProductService
//									.findSupplierNeedProduct(supplierAssignRelation, need, sourceProduct);
//							if(supplierNeedProduct != null){
//								needProduct.setSupplyPrice(supplierNeedProduct.getSupplyPrice());
//								SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(supplierNeedProduct.getSupplyRelation(), supplierNeedProduct.getProducts());
//								if(supplierProduct != null){
//									needProduct.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
//								}
//							}
//							
//							needProductService.save(needProduct);
//						}
//						
//					}
//				}
//				
//			}
//			
//		}
//		
//	}**/
//	
//	
//	@Test
//	@Transactional
//	public void doOrder(){
//		List<Order> list = orderService.findAll();
//		if(list != null){
//			for (Order order : list) {
//				Member member = order.getMember();
//				Need need = member.getNeed();
//				if(need != null && order.getToSupplier() == null){
//					order.setToSupplier(need.getSupplier());
//				}
//				if(order.getType().equals(Order.Type.general) &&
//						order.getBuyType() == null){
//					order.setBuyType(Order.BuyType.general);
//				}else if(order.getType().equals(Order.Type.billGeneral) && 
//						order.getBuyType() == null){
//					if(need.getType().equals(Need.Type.turnover)){
//						order.setBuyType(Order.BuyType.waterSubstitute);
//					}else{
//						order.setBuyType(Order.BuyType.substitute);
//					}
//				}
//				if(order.getType().equals(Order.Type.general) ||
//						order.getType().equals(Order.Type.billGeneral)){//正式数据代下单
//					order.setType(Order.Type.formal);
//				}
//				order.setSupplyType(SupplyType.formal);
//				
//				order.setNeed(need);
//				if(need != null){
//					List<SupplierSupplier.Status> sus = new ArrayList<SupplierSupplier.Status>();
//					sus.add(SupplierSupplier.Status.inTheSupply);
//					sus.add(SupplierSupplier.Status.suspendSupply);
//					SupplierSupplier supplierSupplier = supplierSupplierService.getSupplierSupplier(need.getSupplier(), 
//							order.getSupplier(), order.getCreateDate(), sus);
//					order.setSupplierSupplier(supplierSupplier);
//					if(supplierSupplier != null){
//						List<OrderItem> itemList = order.getOrderItems();
//						if(itemList != null){
//							for (OrderItem orderItem : itemList) {
//								SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(supplierSupplier, orderItem.getProduct());
//								if(supplierProduct != null){
//									orderItem.setPriceUnit(supplierProduct.getSupplyPrice());
//									orderItem.setPrice(supplierProduct.getSupplyPrice()
//											.multiply(new BigDecimal(orderItem.getQuantity())));
//									orderItem.setPriceUnitB(supplierProduct.getSupplyPrice());
//									orderItem.setPriceB(supplierProduct.getSupplyPrice()
//											.multiply(new BigDecimal(orderItem.getQuantity())));
//									orderItemService.update(orderItem);
//								}
//										
//							}
//						}
//					}
//				}
//				orderService.update(order);
//			}
//		}
//		List<OrderLog> orderLogList = orderLogService.findAll();
//		if(orderLogList != null){
//			for (OrderLog orderLog : orderLogList) {
//				if(orderLog.getLogType() == null){
//					orderLog.setLogType(LogType.member);
//					orderLogService.update(orderLog);
//				}
//			}
//		}
//	}
//	
//	
//	
//	@Test
//	@Transactional
//	public void do正式模拟关系2分销关系直销模式_main(){
//		do正式模拟关系2分销关系直销模式(62l, 61l);//银乐迪
//		do正式模拟关系2分销关系直销模式(56l, 55l);//成都啦滋
//		do正式模拟关系2分销关系直销模式(53l, 54l);//七叶和茶
//		do正式模拟关系2分销关系直销模式(58l, 57l);//重庆鸡公煲
//		
//	}
//	
//	public void do正式模拟关系2分销关系直销模式(Long supplierId, Long bySupplierId){
//		Supplier supplier = supplierService.find(supplierId);//正式模拟的供应商 改为 分销商，建立个体客户关系t_need
//		Supplier bySupplier = supplierService.find(bySupplierId);//采购商 删除
//		
//		
//		//生成 企业和客户供应关系 SupplyNeed NeedProduct
//		List<SupplierSupplier> supplierSupplierList = supplierSupplierService.getSupplierSupplierList(bySupplier, supplier);
//		if(supplierSupplierList != null){
//			for (SupplierSupplier supplierSupplier2 : supplierSupplierList) {
//				Set<SupplierAssignRelation> set = supplierSupplier2.getSupplierAssignRelations();
//				if(set != null){
//					for (SupplierAssignRelation supplierAssignRelation : set) {
//						Need need = supplierAssignRelation.getNeed();
//						SupplyNeed supplyNeed = new SupplyNeed();
//						supplyNeed.setSupplier(supplier);
//						supplyNeed.setNeed(need);
//						
//						supplyNeed.setStartDate(supplierSupplier2.getStartDate());
//						supplyNeed.setEndDate(supplierSupplier2.getEndDate());
//						supplyNeed.setAssignedModel(AssignedModel.STRAIGHT);
//						
//						if(supplierSupplier2.getStatus().equals(SupplierSupplier.Status.inTheSupply)){
//							supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
//						}else if(supplierSupplier2.getStatus().equals(SupplierSupplier.Status.expired)){
//							supplyNeed.setStatus(SupplyNeed.Status.EXPIRED);
//						}else if(supplierSupplier2.getStatus().equals(SupplierSupplier.Status.willSupply)){
//							supplyNeed.setStatus(SupplyNeed.Status.WILLSUPPLY);
//						}else if(supplierSupplier2.getStatus().equals(SupplierSupplier.Status.suspendSupply)){
//							supplyNeed.setStatus(SupplyNeed.Status.STOP);
//						}
//						supplyNeedService.save(supplyNeed);
//						
//						List<SupplierNeedProduct> spList = supplierAssignRelation.getSupplierNeedProducts();
//						if(spList != null){
//							for (SupplierNeedProduct supplierNeedProduct : spList) {
//								Product product = supplierNeedProduct.getProducts();
//								NeedProduct needProduct = new NeedProduct();
//								needProduct.setProducts(product);
//								needProduct.setStatus(NeedProduct.Status.CHECKED);
//								needProduct.setSupplyNeed(supplyNeed);
//								
//								if(supplierNeedProduct != null){
//									needProduct.setSupplyPrice(supplierNeedProduct.getSupplyPrice());
//									SupplierProduct supplierProduct = supplierProductService.getSupplierProduct(supplierNeedProduct.getSupplyRelation(), supplierNeedProduct.getProducts());
//									if(supplierProduct != null){
//										needProduct.setMinOrderQuantity(supplierProduct.getMinOrderQuantity());
//									}
//								}
//								needProductService.save(needProduct);
//							}
//						}
//					}
//				}
//				
//				List<Filter> filters = new ArrayList<Filter>();
//				Filter filter = new Filter();
//				filter.setProperty("supplierSupplier");
//				filter.setOperator(Filter.Operator.eq);
//				filter.setValue(supplierSupplier2);
//				filters.add(filter);
//				List<Order> orderList = orderService.findList(10000, filters, null);
//				if(orderList != null){
//					for (Order order : orderList) {
//						SupplyNeed params = new SupplyNeed();
//						params.setSupplier(supplier);
//						params.setNeed(order.getNeed());
//						SupplyNeed supplyNeed = supplyNeedService.findSupplyNeedOnSupply(params);
//						order.setSupplyNeed(supplyNeed);
//						order.setSupplierSupplier(null);
//						order.setToSupplier(null);
//						order.setType(Order.Type.general);
//						order.setSupplyType(SupplyType.temporary);
//						orderService.update(order);
//					}
//				}
//				
//				
//			}
//		}
//				
//		
//		
//		
//		//删除正式模拟的企业的供应关系
//		if(supplierSupplierList != null){
//			for (SupplierSupplier sup : supplierSupplierList) {
//				supplierSupplierService.delete(sup);
//				//级别删除
//				//正式模拟的企业分配商品关系 SupplierAssignRelation
//				//正式模拟的企业分配的商品 SupplierProduct
//				//正式模拟的企业分配给收货点的商品 SupplierNeedProduct
//			}
//		}
//		
//		Set<Need> needSet = bySupplier.getNeeds();
//		if(needSet != null){
//			for (Need need : needSet) {
//				need.setSupplier(supplier);
//				needService.update(need);
//			}
//		}
//		
//		
//	}
//	
//	
//	@Test
//	public void doSystem(){
//		List<Supplier> supplierList = supplierService.findAll();
//		if(supplierList != null){
//			for (Supplier supplier : supplierList) {
//				SystemSetting systemSetting = new SystemSetting();
//				systemSetting.setIsDistributionModel(false);
//				systemSetting.setSupplier(supplier);
//				systemSettingService.save(systemSetting);
//			}
//		}
//	}
//	
//}
