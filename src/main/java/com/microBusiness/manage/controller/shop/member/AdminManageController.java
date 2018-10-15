package com.microBusiness.manage.controller.shop.member;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.controller.admin.BaseController;
import com.microBusiness.manage.entity.NewMessageCompamy;
import com.microBusiness.manage.service.AdminService;
import com.microBusiness.manage.service.FavorCompanyGoodsService;
import com.microBusiness.manage.service.FavorCompanyService;
import com.microBusiness.manage.service.NewMessageCompamyService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 用户管理Controller
 * 
 * @author 吴战波
 *
 */
@Controller("adminManageController")
@RequestMapping("/shop/member/adminManage")
public class AdminManageController extends BaseController {
	
	@Resource
	private AdminService adminService;
	
	@Resource
	private FavorCompanyService favorCompanyService;
	
	@Resource
	private FavorCompanyGoodsService favorCompanyGoodsService;
	
	@Resource
	private NewMessageCompamyService newMessageCompamyService;

	/**
	 * 收藏企业
	 * 
	 * @param supplierId
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/favorCompany",method = RequestMethod.POST)
    public @ResponseBody Message favorCompany(Long supplierId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	if (supplierId == null) {
    		return ERROR_MESSAGE;
		}

    	favorCompanyService.favorCompany(this.getCurrentAdmin(request), supplierId);
    	addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
    	return SUCCESS_MESSAGE;
    }

	/**
	 * 收藏商品
	 * 
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/favorCompanyGoods", method = RequestMethod.POST)
	public @ResponseBody Message favorCompanyGoods(Long supplierId, HttpServletRequest request) {
		if (supplierId == null) {
			return ERROR_MESSAGE;
		}

		favorCompanyGoodsService.favorCompanyGoods(this.getCurrentAdmin(request), supplierId);
		return SUCCESS_MESSAGE;
	}

}
