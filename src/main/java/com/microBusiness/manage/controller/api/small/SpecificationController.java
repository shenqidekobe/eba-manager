package com.microBusiness.manage.controller.api.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Specification;
import com.microBusiness.manage.entity.Types;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.service.ProductCategoryService;
import com.microBusiness.manage.service.SpecificationService;
import com.microBusiness.manage.util.Code;
/**
 * 订货没小程序规格
 * @author yuezhiwei
 *
 */
@Controller
@RequestMapping(value = "/api/small/specification")
public class SpecificationController extends BaseController {
	
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource
	private MemberService memberService;
	@Resource
    private ChildMemberService childMemberService;
	
	/**
	 * 
	 * @Title: getQueryCategory
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午2:21:55
	 * @Description: 查询分类
	 * @return: JsonEntity
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/getQueryCategory" , method = RequestMethod.GET)
	public JsonEntity getQueryCategory(String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		List<ProductCategory> productCategories = productCategoryService.findTree(member, null);
		List<Map<String, Object>> resultMap = new ArrayList<Map<String,Object>>();
		for(final ProductCategory productCategory : productCategories) {
			resultMap.add(new HashMap<String, Object>(){{
				this.put("id", productCategory.getId());
				this.put("name", productCategory.getName());
				this.put("grade", productCategory.getGrade());
			}});
		}
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: list
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午2:49:30
	 * @Description: 规格列表
	 * @return: JsonEntity
	 */
	@SuppressWarnings("serial")
	@ResponseBody
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public JsonEntity list(String unionId , Pageable pageable) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		Page<Specification> page = specificationService.findPage(pageable, member);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> specList = new ArrayList<Map<String,Object>>();
		for(final Specification specification : page.getContent()) {
			specList.add(new HashMap<String, Object>(){{
				this.put("id", specification.getId());
				this.put("name", specification.getName());
				this.put("categoryName", specification.getProductCategory() == null ? null : specification.getProductCategory().getName());
				this.put("options", specification.getOptions());
			}});
		}
		resultMap.put("specList", specList);
		resultMap.put("totalPages", page.getTotalPages());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: save
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午3:05:18
	 * @Description: 添加规格
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/save" , method = RequestMethod.POST)
	public JsonEntity save(Specification specification, Long productCategoryId , String unionId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		specification.setProductCategory(productCategory);
		if(null == specification.getName()) {
			return JsonEntity.error(Code.code_small_specification_100001, Code.code_small_specification_100001.getDesc());
		}
		//验证名称不能重复
		List<Specification> specifications = specificationService.existName(member, Types.local, specification.getName(), productCategory);
		if(specifications.size() > 0) {
			return JsonEntity.error(Code.code_small_specification_100003, Code.code_small_specification_100003.getDesc());
		}
		specification.setMember(member);
		specification.setTypes(Types.local);
		specificationService.save(specification);
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: edit
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午3:17:25
	 * @Description: TODO
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/edit" , method = RequestMethod.GET)
	public JsonEntity edit(Long id , String unionId) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		if(null == id) {
			return JsonEntity.error(Code.code_small_specification_100002, Code.code_small_specification_100002.getDesc());
		}
		Specification specification = specificationService.find(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("id", specification.getId());
		resultMap.put("name", specification.getName());
		resultMap.put("options", specification.getOptions());
		ProductCategory productCategory = specification.getProductCategory();
		resultMap.put("productCategoryId", productCategory.getId());
		return JsonEntity.successMessage(resultMap);
	}
	
	/**
	 * 
	 * @Title: update
	 * @author: yuezhiwei
	 * @date: 2018年3月7日下午3:21:44
	 * @Description: 更新规格
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/update" , method = RequestMethod.POST)
	public JsonEntity update(Specification specification , String unionId , String oldName, Long productCategoryId) {
		Member member = childMemberService.findByUnionId(unionId).getMember();
		CollectionUtils.filter(specification.getOptions(), new AndPredicate(new UniquePredicate(), new Predicate() {
			public boolean evaluate(Object object) {
				String option = (String) object;
				return StringUtils.isNotEmpty(option);
			}
		}));
		if(null == specification.getName()) {
			return JsonEntity.error(Code.code_small_specification_100001, Code.code_small_specification_100001.getDesc());
		}
		// specification2 = specificationService.find(specification.getId());
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		//验证名称不能重复
		if(!oldName.equals(specification.getName())) {
			List<Specification> specifications = specificationService.existName(member, Types.local, specification.getName(),productCategory);
			if(specifications.size() > 0) {
				return JsonEntity.error(Code.code_small_specification_100003, Code.code_small_specification_100003.getDesc());
			}
		}
		specificationService.update(specification, "productCategory" , "member" , "types");
		return JsonEntity.successMessage();
	}
	
	/**
	 * 
	 * @Title: delete
	 * @author: yuezhiwei
	 * @date: 2018年3月13日下午7:13:19
	 * @Description: 删除
	 * @return: JsonEntity
	 */
	@ResponseBody
	@RequestMapping(value = "/delete" , method = RequestMethod.POST)
	public JsonEntity delete(Long id , String unionId) {
		//Member member = childMemberService.findByUnionId(unionId).getMember();
		if(null == id) {
			return JsonEntity.error(Code.code13003, Code.code13003.getDesc());
		}
		specificationService.delete(id);
		return JsonEntity.successMessage();
	}
}
