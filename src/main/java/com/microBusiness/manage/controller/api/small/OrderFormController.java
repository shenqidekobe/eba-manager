package com.microBusiness.manage.controller.api.small;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.OrderForm;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.OrderFormService;
import com.microBusiness.manage.util.Code;

@Controller
@RequestMapping("/api/small/orderForm")
public class OrderFormController extends BaseController {

	@Resource
	private OrderFormService orderFormService;
	@Resource
	private ChildMemberService childMemberService;
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody JsonEntity add(String smOpenId, String formId, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isEmpty(formId)||StringUtils.isEmpty(smOpenId)) {
			return new JsonEntity("010502" , "参数错误");
		}
		ChildMember childMember = childMemberService.findBySmOpenId(smOpenId);

		OrderForm orderForm=new OrderForm();
		orderForm.setFormId(formId);
		try {
			orderForm.setChildMember(childMember);
			orderFormService.save(orderForm);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code14000, Code.code14000.getDesc());
		}
	}
}
