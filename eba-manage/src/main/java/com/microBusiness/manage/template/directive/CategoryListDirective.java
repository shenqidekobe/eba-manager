package com.microBusiness.manage.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.service.CategoryService;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("categoryListDirective")
public class CategoryListDirective extends BaseDirective {

	private static final String VARIABLE_NAME = "categorys";
	
	@Resource(name = "CategoryServiceImpl")
	private CategoryService categoryService;
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {

		// 获取类别列表
		List<Category> categorys = categoryService.findRoots();

		setLocalVariable(VARIABLE_NAME, categorys, env, body);
	}
	
}
