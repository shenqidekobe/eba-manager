package com.microBusiness.manage.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dto.ShopAssistantDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.MemberMember;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedImportInfo;
import com.microBusiness.manage.entity.NeedImportLog;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.SourceType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.service.DepartmentService;
import com.microBusiness.manage.service.HostingShopService;
import com.microBusiness.manage.service.MemberMemberService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.NeedImportInfoService;
import com.microBusiness.manage.service.NeedImportLogService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.service.ReceiverService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.service.SupplyNeedService;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.service.ass.AssUpdateTipsService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;





import com.microBusiness.manage.util.Constant;


/**
 * Created by mingbai on 2017/1/23.
 * 功能描述：
 * 修改记录：
 */
@Controller
@RequestMapping("/admin/need")
public class NeedController extends BaseController {

    @Resource
    private NeedService needService ;

    @Resource
    private SupplyNeedService supplyNeedService;

    @Resource
    private AreaService areaService ;

    @Resource
    private AdminService adminService ;

    @Resource
    private SupplierService supplierService ;

    @Resource
    private NeedImportLogService needImportLogService ;

    @Resource
    private NeedImportInfoService needImportInfoService ;

    @Resource
    private MemberService memberService ;
    @Resource
    private ReceiverService receiverService;
    @Resource
	private DepartmentService departmentService ;
    @Resource
    private AssChildMemberService assChildMemberService;
    @Resource
    private AssUpdateTipsService assUpdateTipsService;

    @Resource
    private HostingShopService hostingShopService;

    @Resource
    private BindPhoneSmsService bindPhoneSmsService;
    @Resource
    private MemberMemberService memberMemberService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model , Need need , String searchName, Date startDate, Date endDate) {
        Admin admin = adminService.getCurrent();
        // TODO: 2017/2/5 添加所属供应商
        need.setSupplier(admin.getSupplier());
        need.setType(Need.Type.general);
        model.addAttribute("searchName", searchName);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
        model.addAttribute("page", needService.findByPage(pageable , need , searchName, startDate, endDate));
        Supplier supplier = super.getCurrentSupplier();
        model.addAttribute("isDistributionModel", supplier.getSystemSetting().getIsDistributionModel());
        return "/admin/need/list";
    }

    /**
     * 微信端后台收货点商品列表
     * @param pageable
     * @param model
     * @param need
     * @param searchName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/asynclist", method = RequestMethod.GET)
    public JsonEntity asynclist(Pageable pageable, ModelMap model , Need need , String searchName) {
        Admin admin = adminService.getCurrent();
        // TODO: 2017/2/5 添加所属供应商
        need.setSupplier(admin.getSupplier());
        need.setType(Need.Type.general);
        Page<Need> page = needService.findPage(pageable , need , searchName, null, null);
        List<Need> list = page.getContent();
        List<Object> needList = new ArrayList<Object>();
        for(Need needs : list) {
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("id", needs.getId());
        	map.put("name", needs.getName());
        	map.put("userName", needs.getUserName());
        	map.put("tel", needs.getTel());
        	map.put("needStatus", needs.getNeedStatus());
        	needList.add(map);
        }
        Map<String, Object> map=new HashMap<>();
		map.put("list", needList);
		map.put("pageNumber", page.getPageNumber());
		map.put("pageSize", page.getPageSize());
		map.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(map);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
    	model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
    	model.addAttribute("tel",getCurrentSupplier().getSystemAdmin().getBindPhoneNum());
        return "/admin/need/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ModelMap model , Need need ,Long areaId,Long adminId, ShopType shopType,String shopAssistantName,String shopAssistantTel, RedirectAttributes redirectAttributes) {
    	Supplier supplier = super.getCurrentSupplier();
        if(need == null || areaId == null || supplier == null) {
			addFlashMessage(redirectAttributes, Message.error("参数错误"));
			return "redirect:add.jhtml";
		}
        if(needService.nameExists(need.getName(), supplier)) {
        	addFlashMessage(redirectAttributes, Message.error("门店名称不能重复"));
			return "redirect:add.jhtml";
        }
		need.setAdmin(adminService.find(adminId));
		need.setArea(areaService.find(areaId));
		need.setSourceType(SourceType.pc);
        need.setSupplier(supplier);
        need.setShopType(shopType);
        if(shopType.equals(ShopType.affiliate)) {
        	//手机号为空，则自动生成一个虚拟手机号，号码规则：
            if(StringUtils.isEmpty(need.getTel())){
            	Need needEntity = null;
            	do {
            		String opt = RandomStringUtils.randomNumeric(10);
    	        	String tel = "2" + opt;
    	        	need.setTel(tel);
    	        	needEntity = needService.findByTel(tel);
    			} while (needEntity != null);
            }
    		/*if(needService.telExists(need.getTel(), supplier)) {
    			addFlashMessage(redirectAttributes, Message.error("不能重复添加同一个体"));
    			return "redirect:add.jhtml";
    		}*/
    		
        	boolean bool = needService.existTel(supplier, need.getTel(), ShopType.direct);
        	if(bool) {
            	addFlashMessage(redirectAttributes, Message.error("该手机号已经添加了直营店，不能再添加加盟店"));
    			return "redirect:add.jhtml";
            }
        	needService.save(need);
        }else {
        	String tel = getCurrentSupplier().getSystemAdmin().getBindPhoneNum();
        	need.setTel(tel);
            if(StringUtils.isNotEmpty(shopAssistantTel)) {
            	if(shopAssistantTel.equals(tel)) {
            		addFlashMessage(redirectAttributes, Message.error("店员手机号不能是当前企业手机号"));
        			return "redirect:add.jhtml";
            	}
            }
            boolean bool = needService.existTel(supplier, tel, ShopType.affiliate);
            if(bool) {
            	addFlashMessage(redirectAttributes, Message.error("该手机号已经添加了加盟店，不能再添加直营店"));
    			return "redirect:add.jhtml";
            }
        	needService.saveDirectStore(need, shopAssistantName, shopAssistantTel);
        }
        
        if(need.getTel().matches("1[3|4|5|7|8]\\d{9}")){
        	String msg = "您已成功开通微商小管理账号，可在微信中搜索“微商小管理”小程序，使用此手机号登录进行订货。";
        	bindPhoneSmsService.sendSms(need.getTel(), msg);
        }
        
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/getNeeds", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long , Object> getNeeds(Long supplierId){

        Supplier supplier = supplierService.find(supplierId);
        Set<Need> needs = supplier.getGeneralNeeds();
        Map<Long , Object> results = new HashMap<>();
        Supplier currSupplier = super.getCurrentSupplier() ;
        Set<SupplyNeed> supplyNeeds=new HashSet<SupplyNeed>();
        if (currSupplier != null) {
        	supplyNeeds = currSupplier.getSupplyNeeds();
		}
        Set<Need> tempNeeds = new HashSet<>();
        for (SupplyNeed suppplyNeed : supplyNeeds) {
            tempNeeds.add(suppplyNeed.getNeed());
        }
        for(final Need tempNeed:needs){
            // TODO: 2017/2/9 判断收货点是否添加过
            if(tempNeeds.contains(tempNeed)){
                continue ;
            }
            results.put(tempNeed.getId() , new HashMap(){{
                this.put("id" , tempNeed.getId());
                this.put("name" , tempNeed.getName());
                this.put("tel" , tempNeed.getTel());
                this.put("userName" , tempNeed.getUserName());
            }});
        }

        return results ;

    }

    @RequestMapping(value = "/checkTel", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkTel(String tel, String oldTel){
    	Supplier supplier = super.getCurrentSupplier();

        if (StringUtils.isEmpty(tel)) {
            return false;
        }
        Need need = needService.findByTelSupplier(tel, supplier);
        if(need != null) {
        	if(need.getTel().equals(oldTel)) {
        		return true;
        	}
        	if(!need.getTel().equals(oldTel)) {
        		return false;
        	}
		}
        return true;

    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(ModelMap model , Long id, String oldTel) {
    	Need need = needService.find(id);
        model.addAttribute("need" , need);
        if(need.getShopType().equals(ShopType.direct)) {
        	List<HostingShop> hostingShops = hostingShopService.findListByShop(need);
        	model.addAttribute("hostingShops", hostingShops);
        }
        model.addAttribute("departmentTree", departmentService.findTree(super.getCurrentSupplier() , null));
        model.addAttribute("oldTel" , oldTel);
        return "/admin/need/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(ModelMap model, Need need, Long adminId, Long areaId, ShopAssistantDtoListForm shopAssistantDtoListForm
    		,String shopAssistantName,String shopAssistantTel, RedirectAttributes redirectAttributes) {
    	Need pNeed = needService.find(need.getId());
    	//Need needEntity = needService.find(need.getId());
    	
    	if(needService.nameExists(need.getName(), pNeed.getSupplier(),pNeed)) {
        	addFlashMessage(redirectAttributes, Message.error("门店名称不能重复"));
			return ERROR_VIEW;
        }
        String oldTel = pNeed.getTel();
        
    	need.setArea(areaService.find(areaId));
       
        Admin admin=adminService.find(adminId);
        if(pNeed.getShopType().equals(ShopType.affiliate)) {
        	needService.updateNeed(need,admin);
        }else {
        	List<HostingShop> hostingShops = hostingShopService.findListByShop(need);
        	
        	List<ShopAssistantDto> list = new ArrayList<ShopAssistantDto>();
        	if(hostingShops.size() > 0) {
        		List<ShopAssistantDto> shopAssistantDtos = shopAssistantDtoListForm.getShopAssistantDtoList();
            	for(ShopAssistantDto dto : shopAssistantDtos) {
            		String tel = dto.getShopAssistantTel();
            		String name = dto.getShopAssistantName();
            		
            		if(StringUtils.isNotEmpty(tel) || StringUtils.isNotEmpty(name)) {
            			/*if(!tel.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
                			return ERROR_VIEW;
            			}*/
            			ShopAssistantDto shopAssistantDto = new ShopAssistantDto();
            			shopAssistantDto.setShopAssistantName(name);
            			shopAssistantDto.setShopAssistantTel(tel);
            			list.add(shopAssistantDto);
            		}
            	}
            	
        	}else {
        		if(StringUtils.isNotEmpty(shopAssistantName) && StringUtils.isNotEmpty(shopAssistantTel)) {
        			ShopAssistantDto shopAssistantDto = new ShopAssistantDto();
        			shopAssistantDto.setShopAssistantName(shopAssistantName);
        			shopAssistantDto.setShopAssistantTel(shopAssistantTel);
        			list.add(shopAssistantDto);
        		}else if(StringUtils.isNotEmpty(shopAssistantName) && StringUtils.isEmpty(shopAssistantTel)) {
        			MemberMember memberb = null;
                	do {
                		String opt = RandomStringUtils.randomNumeric(10);
                		shopAssistantTel = "2" + opt;
                		memberb = memberMemberService.findByTel(pNeed.getMember(), shopAssistantTel);
        			} while (memberb != null);
                	
                	ShopAssistantDto shopAssistantDto = new ShopAssistantDto();
        			shopAssistantDto.setShopAssistantName(shopAssistantName);
        			shopAssistantDto.setShopAssistantTel(shopAssistantTel);
        			list.add(shopAssistantDto);
        		}
        	}
        	needService.updateDirectNeed(need, admin, list);

        }
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        
        if(oldTel.startsWith(Constant.tel_pre_num2) && need.getTel().matches("1[3|4|5|7|8]\\d{9}")){
        	String msg = "您已成功开通微商小管理账号，可在微信中搜索“微商小管理”小程序，使用此手机号登录进行订货。";
        	bindPhoneSmsService.sendSms(need.getTel(), msg);
        }
        
        return "redirect:list.jhtml";
    }


    @RequestMapping(value = "/viewMore" , method = RequestMethod.POST)
    @ResponseBody
    public JsonEntity viewMore(ModelMap model , MultipartFile multipartFile) {

        NeedImportLog needImportLog = needService.dealImportMore(multipartFile , adminService.getCurrent() , super.getCurrentSupplier()) ;
        if(null == needImportLog || null == needImportLog.getId()){
            return new JsonEntity("15001" , "导入失败");
        }
        return JsonEntity.successMessage(needImportLog.getId()) ;
    }


    @RequestMapping(value = "/importMore" , method = RequestMethod.GET)
    public String importMore(ModelMap model) {

        return "/admin/need/import";

    }

    @RequestMapping(value = "/importList", method = RequestMethod.GET)
    public String importList(Pageable pageable, ModelMap model , Long logId) {

        if(null == logId){
            return ERROR_VIEW ;
        }

        NeedImportLog needImportLog = needImportLogService.find(logId) ;

        Page<NeedImportInfo> page = needImportInfoService.findPage(pageable , needImportLog) ;

        model.put("page" , page);
        model.put("supplierTel" , adminService.getCurrent().getBindPhoneNum());
        model.put("needImportLog" , needImportLog) ;

        if (needImportLog.getShopType() == ShopType.affiliate) {
        	 return "/admin/need/importList";
		}else {
			 return "/admin/need/importList_direct";
		}
    }


    @RequestMapping(value = "/saveMore", method = RequestMethod.POST)
    public String saveMore(ModelMap model , Long logId , RedirectAttributes redirectAttributes) {

        if(null == logId){
            return ERROR_VIEW ;
        }

        NeedImportLog needImportLog = needImportLogService.find(logId) ;

        if(null == needImportLog){
            return ERROR_VIEW ;
        }

        boolean isOk = needService.saveMore(needImportLog, adminService.getCurrent());
        if(isOk){
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        }else {
            addFlashMessage(redirectAttributes, ERROR_MESSAGE);
        }
        return "redirect:list.jhtml";
    }
    
    /**
     * 修改收货点时验证手机号是否存在
     * @param tel 店长手机号
     * @param id 收货点id
     * @return
     */
    @RequestMapping(value = "/modifyCheckTel", method = RequestMethod.GET)
    @ResponseBody
    public boolean modifyCheckTel(String tel , Long id){
        if (StringUtils.isEmpty(tel)) {
            return false;
        }
        if(id == null) {
        	return false;
        }
        Need need = needService.modifyCheckTel(tel , id);
        return null == need? true : false ;

    }

    /**
     * 修改收货点状态
     * @param need
     * @return
     */
    @RequestMapping(value = "/updateneedStatus", method = RequestMethod.GET)
    public @ResponseBody
    Message updateneedStatus(Need need) {
    	needService.updateneedStatus(need);
        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		if(ids == null) {
			return ERROR_MESSAGE;
		}
		for(Long id : ids) {
			List<SupplyNeed> supplyNeed = supplyNeedService.findByNeedSupplier(id, super.getCurrentSupplier());
			for (SupplyNeed supplyNeedNew : supplyNeed) {
				if(supplyNeedNew.getStatus() == SupplyNeed.Status.WILLSUPPLY || supplyNeedNew.getStatus() == SupplyNeed.Status.SUPPLY) {
					return Message.warn("此"+supplyNeedNew.getNeed().getName()+"个体客户存在供应关系不可删除！");
				}
			}
		}
		needService.delete(ids);
		return SUCCESS_MESSAGE;
	}
    
    @RequestMapping(value = "/searchMobile" , method = RequestMethod.GET)
	public String searchMobile() {
		return "/admin/need/search";
	}
    
    /**
     * 
     * @Title: checkTel
     * @author: yuezhiwei
     * @date: 2018年3月12日下午8:11:59
     * @Description: 验证手机号
     * @return: boolean
     */
    @RequestMapping(value = "/verifyTel", method = RequestMethod.GET)
    @ResponseBody
    public boolean verifyTel(String tel){
    	Supplier supplier = super.getCurrentSupplier();
    	if (StringUtils.isEmpty(tel)) {
            return false;
        }
    	boolean bool = needService.existTel(supplier, tel, ShopType.direct);
        return !bool;

    }
    
    
    
    public static class ShopAssistantDtoListForm {
    	
    	private List<ShopAssistantDto> shopAssistantDtoList;

		public List<ShopAssistantDto> getShopAssistantDtoList() {
			return shopAssistantDtoList;
		}

		public void setShopAssistantDtoList(List<ShopAssistantDto> shopAssistantDtoList) {
			this.shopAssistantDtoList = shopAssistantDtoList;
		}
    	
    	
    }
    
}
