package com.microBusiness.manage.template.method;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.util.FreeMarkerUtils;
import com.microBusiness.manage.util.SystemUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 金额计算 
 * add  1
 * sub  2
 * mult  3
 * divi 4
 * */
@Component("currencyOperMethod")
public class CurrencyOperMethod implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		Integer oper = FreeMarkerUtils.getArgument(0, Integer.class, arguments);
		BigDecimal[] args = new BigDecimal[arguments.size() - 1];
		if (arguments.size() > 1) {
			for (int i = 1; i < arguments.size(); i++) {
				BigDecimal amount = FreeMarkerUtils.getArgument(i, BigDecimal.class, arguments);
				args[i-1]=amount;
			}
		} 
		BigDecimal result=BigDecimal.ZERO;
		if(oper==1) {
			for(BigDecimal ad:args) {
				if(ad==null)continue;
				result=result.add(ad);
			}
		}else if(oper==2) {
			
		}else if(oper==3) {
			
		}else if(oper==4) {
			for(BigDecimal ad:args) {
				if(ad==null)continue;
				result=result.multiply(ad);
			}
		}
		Setting setting = SystemUtils.getSetting();
		String price = setting.setScale(result).toString();
		return new SimpleScalar(price);
	}

}