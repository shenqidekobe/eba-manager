package com.microBusiness.manage.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.dto.SupplierDto;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.Code;

@Controller
@RequestMapping("/api/supplier")
public class supplierController extends BaseController{

	@Resource
	private SupplierService supplierService ;
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/formal")
    @ResponseBody
    public JsonEntity selectSupplier(HttpServletRequest request, HttpServletResponse response){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
    	try {
    		Member member = getUserInfo(request);
    		
    		List<SupplierDto> supplyList = supplierService.findSupplierListByMember(member,null);

			for (SupplierDto dto : supplyList) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", dto.getSupplier().getId());
				map.put("name", dto.getSupplier().getName());
				map.put("address", dto.getSupplier().getAddress());
				map.put("tel", dto.getSupplier().getTel());
				map.put("userName", dto.getSupplier().getUserName());
				map.put("imagelogo", dto.getSupplier().getImagelogo());
				resultList.add(map);
			}

//    		Set<SupplyNeed> SupplyNeeds = need.getNeedSuppliers();
//    		for (SupplyNeed supplyNeed : SupplyNeeds) {
//    			Supplier supplier = supplyNeed.getSupplier();
//    			
//    			Map<String,Object> map = new HashMap<String,Object>();
//    			map.put("id", supplier.getId());
//    			map.put("name", supplier.getName());
//    			map.put("address", supplier.getAddress());
//    			map.put("tel", supplier.getTel());
//    			map.put("userName", supplier.getUserName());
//    			map.put("imagelogo", supplier.getImagelogo());
//            	resultList.add(map);
//			}
			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(), resultList);
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code010401, Code.code010401.getDesc());
		}
    }

	/**
	 * 获取正式供应商列表
	 * @param request
	 * @param response
	 * @return
	 */
//	@RequestMapping(value = "/formal")
//	@ResponseBody
//	public JsonEntity formalSupplier(HttpServletRequest request, HttpServletResponse response , Pageable pageable){
//		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//
//		try {
//			//获取当前收货点用户
//			Member member = getUserInfo(request);
//			Need need = member.getNeed();
//
//			List<Supplier> suppliers = supplierService.findFormal(need , pageable) ;
//			List<Map<String , Object>> retSuppliers = new ArrayList<>();
//			for(final Supplier supplier : suppliers){
//				retSuppliers.add(new HashMap<String , Object>(){{
//					this.put("id", supplier.getId());
//					this.put("name", supplier.getName());
//					this.put("address", supplier.getAddress());
//					this.put("tel", supplier.getTel());
//					this.put("userName", supplier.getUserName());
//					this.put("imagelogo", supplier.getImagelogo());
//				}});
//			}
//
//			return new JsonEntity(Code.code0,"",request.getRequestURL().toString(),retSuppliers);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new JsonEntity().error(Code.code010401, Code.code010401.getDesc());
//		}
//	}
	
}
