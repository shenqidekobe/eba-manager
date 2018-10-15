package com.microBusiness.manage.controller.ass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ass.AssShippingAddressService;
import com.microBusiness.manage.util.Code;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assShippingAddressController")
@RequestMapping("/ass/address")
public class AssShippingAddressController extends BaseController {

	@Resource
	private AssShippingAddressService assShippingAddressService;
	@Resource
	private AreaService areaService;
	
	@RequestMapping(value = "/getAddress", method = RequestMethod.GET)
	@ResponseBody
    public JsonEntity getAddress(String unionId, HttpServletRequest request, HttpServletResponse response) {
       AssChildMember assChildMember = this.getAssChildMember(unionId);
       
       List<AssShippingAddress> assShippingAddressL = assShippingAddressService.findByChildMember(assChildMember);
       if (assShippingAddressL == null) {
    	   return new JsonEntity().error(Code.code12000, Code.code12000.getDesc());
       }
       
       List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
       
       for (AssShippingAddress assShippingAddress : assShippingAddressL) {
    	   Map<String , Object> retMap = new HashMap<>();
    	   retMap.put("addressName", assShippingAddress.getAddressName());
           retMap.put("id", assShippingAddress.getId());
           retMap.put("name", assShippingAddress.getName());
           retMap.put("tel", assShippingAddress.getTel());
           retMap.put("address", assShippingAddress.getAddress());
           if (assShippingAddress.getArea() != null) {
        	   retMap.put("area", assShippingAddress.getArea().getFullName());
               retMap.put("areaId", assShippingAddress.getArea().getId());
           }
           retMap.put("defaults", assShippingAddress.getDefaults());
           reList.add(retMap);
       }
       
       return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), reList);
    }
	
	 @RequestMapping(value = "/save", method = RequestMethod.POST)
	 @ResponseBody
	 public JsonEntity save(String unionId, AssShippingAddress assShippingAddress ,Long areaId, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);

	    if(areaId == null) {
	    	return new JsonEntity("010502" , "参数错误");
		}
		try {
			List<AssShippingAddress> address = assShippingAddressService.findByChildMember(assChildMember);
			if(address.size() > 0) {
				assShippingAddress.setDefaults(AssShippingAddress.Defaults.noDefaults);
			}else {
				assShippingAddress.setDefaults(AssShippingAddress.Defaults.defaults);
			}
			assShippingAddress.setArea(areaService.find(areaId));
			assShippingAddress.setAssChildMember(assChildMember);
			assShippingAddressService.save(assShippingAddress);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code12002, Code.code12002.getDesc());
		}

	 }
	 
	 @RequestMapping(value = "/update", method = RequestMethod.POST)
	 @ResponseBody
	 public JsonEntity update(String unionId, AssShippingAddress assShippingAddress ,Long areaId, HttpServletRequest request, HttpServletResponse response) {
		 if(areaId == null) {
		    return new JsonEntity("010502" , "参数错误");
		 }
		 
		 try {
			assShippingAddress.setArea(areaService.find(areaId));
			assShippingAddressService.update(assShippingAddress);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code12003, Code.code12003.getDesc());
		}
	     
	}

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
	public JsonEntity delete(String unionId, Long id) {
    	AssChildMember assChildMember = this.getAssChildMember(unionId);
    	
    	try {
//			AssShippingAddress address = assShippingAddressService.findByChildMember(assChildMember);
//			address.setDeleted(true);
			
    		AssShippingAddress address = assShippingAddressService.find(id);
    		address.setDeleted(true);
    		
			assShippingAddressService.update(address);
			
			List<AssShippingAddress> defaultsAddresses = assShippingAddressService.findBYAddress(assChildMember, AssShippingAddress.Defaults.defaults);
			if(defaultsAddresses.size() < 1) {
				List<AssShippingAddress> addresses = assShippingAddressService.findByChildMember(assChildMember);
				if(addresses.size() > 0) {
					AssShippingAddress shippingAddress = addresses.get(0);
						shippingAddress.setDefaults(AssShippingAddress.Defaults.defaults);
						assShippingAddressService.update(shippingAddress);
				}
			}
			return JsonEntity.successMessage();
		} catch (Exception e) {
			return new JsonEntity().error(Code.code12004, Code.code12004.getDesc());
		}
	}

    @RequestMapping(value = "/getAddressById" , method = RequestMethod.GET)
    @ResponseBody
	public JsonEntity getAddressById(String unionId, Long id, HttpServletRequest request, HttpServletResponse response) {
    	AssChildMember assChildMember = this.getAssChildMember(unionId);
    	
    	try {
    		AssShippingAddress address = assShippingAddressService.find(id);
    		
    		Map<String , Object> retMap = new HashMap<>();
            retMap.put("id", address.getId());
            retMap.put("addressName", address.getAddressName());
            retMap.put("name", address.getName());
            retMap.put("tel", address.getTel());
            retMap.put("address", address.getAddress());
            retMap.put("defaults", address.getDefaults());
            if (address != null) {
            	retMap.put("area", address.getArea().getFullName());
                retMap.put("areaId", address.getArea().getId());
			}
    		
            return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), retMap);
		} catch (Exception e) {
			return new JsonEntity().error(Code.code12000, Code.code12000.getDesc());
		}
	}
    
    /**
     * 设置默认地址
     * @param unionId
     * @param id 地址id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setDefaultAddress" , method = RequestMethod.POST)
    public JsonEntity setDefaultAddress(String unionId, Long id) {
    	AssChildMember assChildMember = this.getAssChildMember(unionId);
    	if(null == id) {
    		return new JsonEntity(Code.code12005, Code.code12005.getDesc());
    	}
    	//获取默认地址并设置为非默认地址
    	List<AssShippingAddress> addressList = assShippingAddressService.findBYAddress(assChildMember, AssShippingAddress.Defaults.defaults);
    	for(AssShippingAddress asa : addressList) {
    		asa.setDefaults(AssShippingAddress.Defaults.noDefaults);
    		assShippingAddressService.update(asa);
    	}
    	AssShippingAddress address = assShippingAddressService.find(id);
    	if(null == address) {
    		return new JsonEntity(Code.code13003, Code.code13003.getDesc());
    	}
    	address.setDefaults(AssShippingAddress.Defaults.defaults);
    	AssShippingAddress pAddress = assShippingAddressService.update(address);
    	return JsonEntity.successMessage(pAddress.getDefaults());
    }
    
    /**
     * 保存并使用该地址
     * @param unionId
     * @param assShippingAddress
     * @param areaId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveAndUseAddress" , method = RequestMethod.POST)
    public JsonEntity saveAndUseAddress(String unionId, AssShippingAddress assShippingAddress ,Long areaId) {
    	AssChildMember assChildMember = this.getAssChildMember(unionId);
	    if(areaId == null) {
	    	return new JsonEntity("010502" , "参数错误");
		}
		try {
			List<AssShippingAddress> address = assShippingAddressService.findByChildMember(assChildMember);
			if(address.size() > 0) {
				assShippingAddress.setDefaults(AssShippingAddress.Defaults.noDefaults);
			}else {
				assShippingAddress.setDefaults(AssShippingAddress.Defaults.defaults);
			}
			assShippingAddress.setArea(areaService.find(areaId));
			assShippingAddress.setAssChildMember(assChildMember);
			AssShippingAddress pAddress = assShippingAddressService.save(assShippingAddress);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("addressName", pAddress.getAddressName());
	    	resultMap.put("id", pAddress.getId());
	    	resultMap.put("name", pAddress.getName());
	    	resultMap.put("tel", pAddress.getTel());
	    	resultMap.put("address", pAddress.getAddress());
	        if (pAddress.getArea() != null) {
	        	resultMap.put("area", pAddress.getArea().getFullName());
	        	resultMap.put("areaId", pAddress.getArea().getId());
	        }
	        resultMap.put("defaults", pAddress.getDefaults());
			return JsonEntity.successMessage(resultMap);
		} catch (Exception e) {
			return new JsonEntity(Code.code12002, Code.code12002.getDesc());
		}
    }
    
    /**
     * 选择地址
     * @param unionId
     * @param id 地址id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/chooseAddress" , method = RequestMethod.GET)
    public JsonEntity chooseAddress(String unionId , Long id) {
    	AssChildMember assChildMember = this.getAssChildMember(unionId);
    	if(null == id) {
    		return new JsonEntity(Code.code_asslist_11118, Code.code_asslist_11118.getDesc());
    	}
    	//获取地址
    	AssShippingAddress assShippingAddress = assShippingAddressService.find(id);
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("addressName", assShippingAddress.getAddressName());
    	resultMap.put("id", assShippingAddress.getId());
    	resultMap.put("name", assShippingAddress.getName());
    	resultMap.put("tel", assShippingAddress.getTel());
    	resultMap.put("address", assShippingAddress.getAddress());
        if (assShippingAddress.getArea() != null) {
        	resultMap.put("area", assShippingAddress.getArea().getFullName());
        	resultMap.put("areaId", assShippingAddress.getArea().getId());
        }
        resultMap.put("defaults", assShippingAddress.getDefaults());
    	return JsonEntity.successMessage(resultMap);
    }
}
