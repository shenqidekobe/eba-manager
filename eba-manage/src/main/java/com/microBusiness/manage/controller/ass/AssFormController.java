package com.microBusiness.manage.controller.ass;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssForm;
import com.microBusiness.manage.service.ass.AssFormService;
import com.microBusiness.manage.util.Code;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("assFormController")
@RequestMapping("/ass/form")
public class AssFormController extends BaseController {

	@Resource
	private AssFormService assFormService;
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody JsonEntity add(String unionId, AssForm assForm, HttpServletRequest request, HttpServletResponse response) {
		AssChildMember assChildMember = this.getAssChildMember(unionId);

		if (assForm == null) {
			return new JsonEntity("010502" , "参数错误");
		}

		try {
			assForm.setAssChildMember(assChildMember);
			assFormService.save(assForm);
			return JsonEntity.successMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonEntity().error(Code.code14000, Code.code14000.getDesc());
		}
	}

}