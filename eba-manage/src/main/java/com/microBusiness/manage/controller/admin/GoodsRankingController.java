/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.controller.admin;

import javax.annotation.Resource;

import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.service.GoodsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminGoodsRankingController")
@RequestMapping("/admin/goods_ranking")
public class GoodsRankingController extends BaseController {

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Goods.RankingType rankingType, Pageable pageable, Model model) {
		if (rankingType == null) {
			rankingType = Goods.RankingType.sales;
		}
		model.addAttribute("rankingTypes", Goods.RankingType.values());
		model.addAttribute("rankingType", rankingType);
		model.addAttribute("page", goodsService.findPage(rankingType, pageable));
		return "/admin/goods_ranking/list";
	}

}