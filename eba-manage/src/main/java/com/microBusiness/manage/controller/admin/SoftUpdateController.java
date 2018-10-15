package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedShopProductDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.service.*;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/20 下午6:31
 * Describe:
 * Update:
 */
//@Controller
//@RequestMapping(value = "/admin/soft")
public class SoftUpdateController extends BaseController {

    private static final String PATH_PRE = "/usr/b2b/lock/" ;

    private static final String LOCK_FILE_NAME = "fileLock2_7_0.lock";

    private static final Logger LOOGER = LoggerFactory.getLogger(SoftUpdateController.class) ;

    @Resource
    private NeedService needService ;
    @Resource
    private ShopService shopService ;
    @Resource
    private MemberService memberService;
    @Resource
    private MemberMemberService memberMemberService;
    @Resource
    private HostingShopService hostingShopService;
    @Resource
    private NeedShopProductService needShopProductService;
    @Resource
    private SupplyNeedService supplyNeedService;
    @Resource
    private NeedProductService needProductService;
    @Resource
    private OrderService orderService;
    @Resource
    private SupplierSupplierService supplierSupplierService;
    @Resource
    private AdminService adminService;
    @Resource
	private StockGoodsService stockGoodsService;
    
    @RequestMapping(value = "/needShop", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity needShop(HttpServletRequest request){
    	//简单校验，判断用户是否为超级管理员
        Admin admin = super.getCurrentAdmin(request) ;
        boolean isSystem=false;
        Set<Role> roles=admin.getRoles();
        for (Role role:roles) {
            if (role.getIsSystem()){
                isSystem=true;
            }
        }
        if (!isSystem){
            return new JsonEntity("101010" , "你不是超级管理员");
        }
    	List<NeedShopProduct> a = needShopProductService.getList();
    	for (NeedShopProduct needShopProduct : a) {
    		SupplyNeed supplyNeed=needShopProduct.getSupplyNeed();
    		if (supplyNeed != null && supplyNeed.getStatus() == SupplyNeed.Status.SUPPLY) {
    			Product product=needShopProduct.getProducts();
    			if (!product.getGoods().isDeleted()) {
    				List<Product> products = new ArrayList<>();
    				products.add(needShopProduct.getProducts());
    				stockGoodsService.save(needShopProduct.getShop(), products);
				}
			}
		}
    	return JsonEntity.successMessage();
    }
    
    /**
     * 2.7.0 版本升级
     * @param request
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity update2_7_0(HttpServletRequest request){
        //简单校验，判断用户是否为超级管理员
        Admin admin = super.getCurrentAdmin(request) ;
        boolean isSystem=false;
        Set<Role> roles=admin.getRoles();
        for (Role role:roles) {
            if (role.getIsSystem()){
                isSystem=true;
            }
        }
        if (!isSystem){
            return new JsonEntity("101010" , "你不是超级管理员");
        }

        String fileUri = PATH_PRE + LOCK_FILE_NAME ;
        //判断lock文件是否存在，如果已经存在了，则表示已经更新了
        File file = new File(fileUri) ;
        if(file.exists()){
            //文件已经存在了，则表示已经更新过了，不更新
            return new JsonEntity("101011" , "本版本已更新过数据");
        }

        List<Admin> admins=adminService.getListByDepartment(null, null);
        for (Admin admin2 : admins) {
        	//用户没有绑定手机号  随机生成手机号
    		if (admin2.getBindPhoneNum() == null){
    			String tel="";
    			do {
    				String opt = RandomStringUtils.randomNumeric(10);
    				tel="2" + opt;
				} while (adminService.findBybindPhoneNum(tel) != null);
    			
    			admin2.setBindPhoneNum(tel);
    			adminService.refreshAdmin(admin2);
    		}
		}
        
        //将个体客户进行升级 , 应该需要根据企业做不同处理

        long count = needService.count(new Filter(){{
            this.setProperty("type");
            this.setValue(Need.Type.general);
            this.setOperator(Operator.eq);
        }});
        //每次处理200条
        int every = 200 ;

        int pages = (int) Math.ceil((double) count / (double) every );

        for(int i=0 ; i< pages ; i++){
            Page<Need> result = needService.findPage(new Pageable(i , every) , null , Need.Type.general , null,null) ;

            List<Need> needs = result.getContent() ;

            for(Need need : needs){
            	//金茶王企业id:69   小杨生煎:23  上海又乐网络科技有限公司:12
            	Supplier supplier=need.getSupplier();
        		//member和后台管理员关联
        		Admin needAdmin=supplier.getSystemAdmin();
                if (supplier.getId() == 69 || supplier.getId()==23 || supplier.getId()==12 || supplier.getId()==2){
                    Member member=memberService.findByMobile(needAdmin.getBindPhoneNum());
                    //如果没有member  新建
                    if (member == null){
                        member=memberService.addMember(needAdmin.getBindPhoneNum());
                    }
                    member.setAdmin(needAdmin);
                    memberService.refreshMember(member);

                    String name=need.getUserName();   //店员名称
                    String tel=need.getTel();         //店员手机号

                    need.setTel(member.getMobile());
                    need.setMember(member);
                    need.setShopType(ShopType.direct);
                    needService.refreshNeed(need);

                    Shop tempShop = new Shop() ;
                    tempShop.setAddress(need.getAddress());
                    tempShop.setArea(need.getArea());
                    tempShop.setName(need.getName()) ;
                    tempShop.setShopType(ShopType.direct);
                    tempShop.setMember(member);
                    tempShop.setUserName(need.getUserName());
                    tempShop.getNeeds().add(need);

                    shopService.save(tempShop);

                    //老数据need手机号不与主账号的手机号相同则加为托管员
                    if (!tel.equals(member.getMobile())){
                        MemberMember memberMember=memberMemberService.verifyTelExists(tel,member,null);
                        if(memberMember == null){
                        	memberMember = memberMemberService.save(member, tel, name);
                        }
                        HostingShop hostingShop=new HostingShop();
                        hostingShop.setMemberMember(memberMember);
                        hostingShop.setMember(member);
                        hostingShop.setShop(tempShop);
                        hostingShop.setByMember(memberMember.getByMember());
                        hostingShopService.save(hostingShop);
                    }

                    //处理分配信息
                    SupplyNeed supplyNeed=new SupplyNeed();
                    supplyNeed.setSupplier(supplier);
                    supplyNeed.setNeed(need);
                    supplyNeed=supplyNeedService.findSupplyNeedOnSupply(supplyNeed);

                    if (supplyNeed != null){
                        NeedProduct find=new NeedProduct();
                        find.setSupplyNeed(supplyNeed);
                        List<NeedProduct> needProducts=needProductService.findByParams(find);
                        for (NeedProduct needProduct:needProducts) {
                            Product product=needProduct.getProducts();
                            NeedShopProduct needShopProduct=new NeedShopProduct();
                            needShopProduct.setShop(tempShop);
                            needShopProduct.setProducts(product);
                            needShopProduct.setSupplyNeed(needProduct.getSupplyNeed());
                            needShopProduct.setMinOrderQuantity(product.getMinOrderQuantity());
                            needShopProduct.setSupplyPrice(product.getSupplyPrice());
                            needShopProductService.save(needShopProduct);
                        }
                    }

                    //处理订单信息
                    Set<Order> orders=supplier.getOrders();
                    for (Order order:orders) {
                        if (order.getNeed().equals(need)) {
                            order.setShop(tempShop);
                            orderService.update(order);
                        }
                    }
                }else {
                    //其他账号改为加盟店
                    need.setShopType(ShopType.affiliate);
                    needService.refreshNeed(need);
                }

            }

        }




        //企业供应处理
        List<SupplierSupplier> list=supplierSupplierService.findAll();
        Map<String,Integer> map=new HashMap<>();
        //待确认map
        Map<String,SupplierSupplier> toBeConfirmedMap=new HashMap<>();
        //已确认map
        Map<String,SupplierSupplier> inTheSupplyMap=new HashMap<>();
        for (int i=list.size()-1;i>=0;i--){
            SupplierSupplier supplierSupplier=list.get(i);
            //使用 供应商+被供应企业id作为key
            String key=supplierSupplier.getSupplier().getId()+""+supplierSupplier.getBySupplier().getId();
            if (map.get(key) == null) {
                map.put(key,1);
            }else {
                map.put(key,map.get(key)+1);
            }
            //设置批次
            supplierSupplier.setSupplyBatch(map.get(key));
            //把未开始的状态改为已拒绝
            if (supplierSupplier.getStatus() == SupplierSupplier.Status.willSupply){
                supplierSupplier.setStatus(SupplierSupplier.Status.rejected);
            }
            //判断待确认是否存在多个
            if (supplierSupplier.getStatus() == SupplierSupplier.Status.toBeConfirmed){
                if (toBeConfirmedMap.get(key) == null) {
                    toBeConfirmedMap.put(key,supplierSupplier);
                }else {
                    SupplierSupplier supplierSupplier1=toBeConfirmedMap.get(key);
                    if (supplierSupplier.getCreateDate().before(supplierSupplier1.getCreateDate())){
                        toBeConfirmedMap.put(key,supplierSupplier);
                        supplierSupplier1.setStatus(SupplierSupplier.Status.expired);
                        supplierSupplier1.setDeleted(true);
                        supplierSupplierService.refreshSupplierSupplier(supplierSupplier1);
                    }else {
                        supplierSupplier.setStatus(SupplierSupplier.Status.expired);
                        supplierSupplier.setDeleted(true);
                    }
                }
            }
            //判断已确认是否存在多个
            if (supplierSupplier.getStatus() == SupplierSupplier.Status.inTheSupply){
                if (inTheSupplyMap.get(key) == null) {
                    inTheSupplyMap.put(key,supplierSupplier);
                }else {
                    SupplierSupplier supplierSupplier1=inTheSupplyMap.get(key);
                    if (supplierSupplier.getCreateDate().before(supplierSupplier1.getCreateDate())){
                        inTheSupplyMap.put(key,supplierSupplier);
                        supplierSupplier1.setStatus(SupplierSupplier.Status.expired);
                        supplierSupplier1.setDeleted(true);
                        supplierSupplierService.refreshSupplierSupplier(supplierSupplier1);
                    }else {
                        supplierSupplier.setStatus(SupplierSupplier.Status.expired);
                        supplierSupplier.setDeleted(true);
                    }
                }
            }
            supplierSupplierService.refreshSupplierSupplier(supplierSupplier);
        }



        //门店供应处理
        List<SupplyNeed> supplyNeeds=supplyNeedService.findAll();
        Map<String,SupplyNeed> supplyNeedMap=new HashMap<>();
        for (SupplyNeed supplyNeed:supplyNeeds) {
            //删除过的不处理
            if (supplyNeed.isDeleted()){
                continue;
            }
            //过期的删除
            if (supplyNeed.getStatus() == SupplyNeed.Status.EXPIRED){
                supplyNeed.setDeleted(true);
                supplyNeedService.refreshSupplyNeed(supplyNeed);
                continue;
            }
            if (supplyNeed.getStatus() == SupplyNeed.Status.SUPPLY || supplyNeed.getStatus() == SupplyNeed.Status.WILLSUPPLY){
                //使用 供应商+被供应企业id作为key
                String key=supplyNeed.getSupplier().getId()+""+supplyNeed.getNeed().getId();
                if (supplyNeedMap.get(key) == null){
                    supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
                    supplyNeedMap.put(key,supplyNeed);
                }else {
                    SupplyNeed supplyNeed1=supplyNeedMap.get(key);
                    if (supplyNeed.getCreateDate().before(supplyNeed1.getCreateDate())){
                        supplyNeed.setStatus(SupplyNeed.Status.SUPPLY);
                        supplyNeedMap.put(key,supplyNeed);

                        supplyNeed1.setStatus(SupplyNeed.Status.EXPIRED);
                        supplyNeed1.setDeleted(true);
                        supplyNeedService.refreshSupplyNeed(supplyNeed1);
                    }
                }
                supplyNeedService.refreshSupplyNeed(supplyNeed);
            }

        }


        //更行成功后 ， 创建lock文件

        try {
            file.createNewFile() ;
        } catch (IOException e) {
            e.printStackTrace();
            LOOGER.error("create file error" , e);
        }

        return JsonEntity.successMessage() ;

    }

}
