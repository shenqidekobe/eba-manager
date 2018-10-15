//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang.StringUtils;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.microBusiness.manage.Filter;
//import com.microBusiness.manage.Filter.Operator;
//import com.microBusiness.manage.Page;
//import com.microBusiness.manage.Pageable;
//import com.microBusiness.manage.entity.Admin;
//import com.microBusiness.manage.entity.CustomerRelation;
//import com.microBusiness.manage.entity.HostingShop;
//import com.microBusiness.manage.entity.JsonEntity;
//import com.microBusiness.manage.entity.Member;
//import com.microBusiness.manage.entity.MemberMember;
//import com.microBusiness.manage.entity.Need;
//import com.microBusiness.manage.entity.NeedProduct;
//import com.microBusiness.manage.entity.NeedShopProduct;
//import com.microBusiness.manage.entity.Order;
//import com.microBusiness.manage.entity.Product;
//import com.microBusiness.manage.entity.Shop;
//import com.microBusiness.manage.entity.ShopType;
//import com.microBusiness.manage.entity.SourceType;
//import com.microBusiness.manage.entity.Supplier;
//import com.microBusiness.manage.entity.SupplierAssignRelation;
//import com.microBusiness.manage.entity.SupplierNeedProduct;
//import com.microBusiness.manage.entity.SupplierProduct;
//import com.microBusiness.manage.entity.SupplierSupplier;
//import com.microBusiness.manage.entity.SupplierType;
//import com.microBusiness.manage.entity.SupplyNeed;
//import com.microBusiness.manage.entity.SupplyNeed.AssignedModel;
//import com.microBusiness.manage.entity.SupplyType;
//import com.microBusiness.manage.service.AdminService;
//import com.microBusiness.manage.service.CartItemService;
//import com.microBusiness.manage.service.CustomerRelationService;
//import com.microBusiness.manage.service.GoodsService;
//import com.microBusiness.manage.service.HostingShopService;
//import com.microBusiness.manage.service.MemberMemberService;
//import com.microBusiness.manage.service.MemberService;
//import com.microBusiness.manage.service.NeedProductService;
//import com.microBusiness.manage.service.NeedService;
//import com.microBusiness.manage.service.NeedShopProductService;
//import com.microBusiness.manage.service.OrderItemService;
//import com.microBusiness.manage.service.OrderLogService;
//import com.microBusiness.manage.service.OrderService;
//import com.microBusiness.manage.service.ProductService;
//import com.microBusiness.manage.service.ShopService;
//import com.microBusiness.manage.service.SupplierAssignRelationService;
//import com.microBusiness.manage.service.SupplierNeedProductService;
//import com.microBusiness.manage.service.SupplierProductService;
//import com.microBusiness.manage.service.SupplierService;
//import com.microBusiness.manage.service.SupplierSupplierService;
//import com.microBusiness.manage.service.SupplyNeedService;
//import com.microBusiness.manage.service.SystemSettingService;
//import com.microBusiness.manage.util.Code;
//import com.microBusiness.manage.util.DateUtils;
//
//public class MigrateShop extends BaseTest {
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
//	@Resource
//	private AdminService adminService;
//	@Resource
//	private NeedShopProductService needShopProductService;
//	@Resource
//	private HostingShopService hostingShopService;
//	@Resource
//	private CustomerRelationService customerRelationService;
//	@Resource
//	private ShopService shopService;
//	@Resource
//	private MemberMemberService memberMemberService;
//	
//	@Transactional
//	@Test
//	public void doShopAndShopCompany(){
//		Pageable pageable = new Pageable();
//		pageable.setPageSize(1000);
//		Supplier supplier = supplierService.find(12L);
//		Set<Need> needSet = supplier.getNeeds();
//		int i = 0;
//		for (Need need : needSet) {
//			if(StringUtils.isNotBlank(need.getClientNum())){
//				
//				i++;
//				//System.out.println(i + ":" + need.getClientNum());
//				//获取电话   托管员手机号 用来注册企业
//				Member byMember=null;   //托管员
//				Shop shop =null;		//门店
//				HostingShop hostingShop=null;
//				if (need.getShops() != null ) {
//					if (need.getShops().size()>1) {
//						System.out.println("need id is :"+need.getId()+"  ，有多个shop。");
//						continue;
//					}else {
//						shop = need.getShops().get(0);
//						List<HostingShop> hostingShops=hostingShopService.findListByShop(shop);
//						if (hostingShops.size() == 1) {
//							hostingShop=hostingShops.get(0);
//							byMember=hostingShop.getByMember();
//						}else {
//							System.out.println("shop id is :"+shop.getId()+"  ，有多个托管员或者没有托管员。");
//							continue;
//						}
//					}
//				}
//				//注册企业
//				String tel = byMember.getMobile();
//				//判断手机号是否已被占用
//				Admin admin=adminService.findBybindPhoneNum(tel);
//				if (admin != null){
//					System.out.println("need id is :"+need.getId()+" ，手机号:"+tel+"已被占用。");
//					continue;
//				}
//				
//				//判断用户名是否重复
//				String userName="dh"+tel;
//				if (adminService.findByUsername(userName) != null) {
//					System.out.println("need id is :"+need.getId()+" ，用户名:"+userName+"已被占用。");
//					continue;
//				}
//				
//				Supplier supplierSelf = new Supplier();
//				supplierSelf.setName(need.getName());
//				supplierSelf.setTel(tel);
//				supplierSelf.setUserName(userName);
//				supplierSelf.setArea(need.getArea());
//				supplierSelf.setAddress(need.getAddress());
//				
//				admin=new Admin();
//				admin.setBindPhoneNum(tel);
//				admin.setUsername(userName);
//				admin.setPassword("dh1234");
//				
//				adminService.register(supplierSelf, admin, byMember);
//				
//				//建立企业客户
//				CustomerRelation customerRelation=new CustomerRelation();
//				customerRelation.setAddress(supplierSelf.getAddress());
//				customerRelation.setClientName(supplierSelf.getName());
//				customerRelation.setClientType(CustomerRelation.ClientType.enterprise);
//				customerRelation.setInviteCode(supplierSelf.getInviteCode());
//				customerRelation.setSupplierAddress(supplierSelf.getAddress());
//				customerRelation.setSupplierName(supplierSelf.getName());
//				customerRelation.setSupplierTel(tel);
//				customerRelation.setSupplierUserName(userName);
//				customerRelation.setArea(supplierSelf.getArea());
//				customerRelation.setSourceType(SourceType.pc);
//				customerRelation.setBySupplier(supplierSelf);
//				customerRelation.setSupplier(supplier);
//				customerRelation.setClientNum(need.getClientNum());
//				customerRelation=customerRelationService.save(customerRelation);
//				
//				//建立企业供应关系
//				SupplierSupplier supplierSupplier=new SupplierSupplier();
//				supplierSupplier.setStatus(SupplierSupplier.Status.inTheSupply);
//				supplierSupplier.setSupplier(supplier);
//				supplierSupplier.setBySupplier(supplierSelf);
//				try {
//					Date startDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-03-01 00:00:00");
//					Date endDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2028-03-01 23:59:59");
//					supplierSupplier.setStartDate(startDate);
//					supplierSupplier.setEndDate(endDate);
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				supplierSupplier.setOpenNotice(false);
//				supplierSupplier.setSupplyBatch(1);
//				supplierSupplier=supplierSupplierService.save(supplierSupplier);
//				
//				//供应商品
//				SupplyNeed supplyNeed=new SupplyNeed();
//				supplyNeed.setSupplier(supplier);
//				supplyNeed.setNeed(need);
//				supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
//				supplyNeed=supplyNeedService.findSupplyNeed(supplyNeed);
//				if (supplyNeed != null) {
//					Set<NeedProduct> needProducts=supplyNeed.getNeedProducts();
//					//分配关系
//					SupplierAssignRelation supplierAssignRelation=new SupplierAssignRelation();
//					supplierAssignRelation.setNeed(need);
//					supplierAssignRelation.setSupplyRelation(supplierSupplier);
//					List<SupplierNeedProduct> supplierNeedProducts=new ArrayList<>();
//					
//					for (NeedProduct needProduct : needProducts) {
//						SupplierProduct supplierProduct=new SupplierProduct();
//						supplierProduct.setSupplyRelation(supplierSupplier);
//		            	supplierProduct.setProducts(needProduct.getProducts());
//		            	supplierProduct.setMinOrderQuantity(needProduct.getMinOrderQuantity());
//		            	supplierProduct.setSupplyPrice(needProduct.getSupplyPrice());
//		            	supplierProductService.save(supplierProduct);
//		            	
//		            	SupplierNeedProduct newSupplierNeedProduct=new SupplierNeedProduct();
//				       	newSupplierNeedProduct.setNeed(need);
//				       	newSupplierNeedProduct.setSupplyPrice(needProduct.getSupplyPrice());
//				       	newSupplierNeedProduct.setProducts(needProduct.getProducts());
//				       	newSupplierNeedProduct.setSupplyRelation(supplierSupplier);
//				       	newSupplierNeedProduct.setAssignRelation(supplierAssignRelation);
//				       	supplierNeedProducts.add(newSupplierNeedProduct);
//					}
//					supplierAssignRelation.setSupplierNeedProducts(supplierNeedProducts);
//					supplierAssignRelationService.save(supplierAssignRelation);
//					
//					//删除分配
//					supplyNeedService.deleted(supplyNeed);
//				}
//				
//				//改变need参数
//				need.setSupplier(supplierSelf);
//				need.setTel(tel);
//				need.setMember(byMember);
//				needService.update(need);
//				
//				//改变shop参数
//				shop.setMember(byMember);
//				shopService.update(shop);
//				
//				MemberMember memberMember=hostingShop.getMemberMember();
//				//删除托管员
//				hostingShopService.delete(hostingShop);
//				
//				//删除店员
//				memberMemberService.delete(memberMember);
//				
//				
//				if (supplyNeed != null) {
//					//处理订单
//					List<Filter> list = new ArrayList<Filter>();
//					Filter filter = new Filter();
//					filter.setProperty("need");
//					filter.setValue(need);
//					filter.setOperator(Operator.eq);
//					list.add(filter);
//					
//					Filter filter2 = new Filter();
//					filter2.setProperty("supplyNeed");
//					filter2.setValue(supplyNeed);
//					filter2.setOperator(Operator.eq);
//					list.add(filter2);
//					pageable.setFilters(list);
//					Page<Order> pageList = orderService.findPage(pageable);
//					System.out.println("need name : " + need.getName() + "==订单数:" + pageList.getContent().size());
//					for (Order order : pageList.getContent()) {
//						System.out.println(i + ":" + order.getId()  + ":"  + order.getNeed().getClientNum());
//						order.setType(Order.Type.formal);
//						order.setSupplier(supplier);
//						order.setToSupplier(supplierSelf);
//						order.setSupplyNeed(null);
//						order.setSupplierSupplier(supplierSupplier);
//						order.setShop(shop);
//						order.setSupplierType(SupplierType.TWO);
//						orderService.update(order);
//					}
//				}
//			}
//		}
//	
//		
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
//	
//	
//
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
//	
//	
//}
