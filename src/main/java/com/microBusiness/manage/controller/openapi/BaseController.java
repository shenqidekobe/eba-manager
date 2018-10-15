package com.microBusiness.manage.controller.openapi;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.microBusiness.manage.entity.OutApiJsonEntity;
import com.microBusiness.manage.exception.OutApiException;
import com.microBusiness.manage.util.Code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/1/30 下午2:15
 * Describe:
 * Update:
 */
public class BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";
    
    @Resource(name = "validator")
	private Validator validator;
    
    /**
     * 这里进行全局异常捕获，可自定义异常进行处理
     * @param exception
     * @param response
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public OutApiJsonEntity exceptionHandler(Exception exception, HttpServletResponse response) {
    	
    	if (exception instanceof OutApiException) {
    		OutApiException outApiException = (OutApiException) exception;
    		return outApiException.getEntity();
		}
    	
        LOGGER.error("统一异常：", exception);

        return OutApiJsonEntity.error(Code.code100003) ;
    }
    
    protected boolean isValid(Object target, Class<?>... groups) {
		Assert.notNull(target);

		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		}
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
		return false;
	}

    protected boolean isValid(Collection<Object> targets, Class<?>... groups) {
		Assert.notEmpty(targets);

		for (Object target : targets) {
			if (!isValid(target, groups)) {
				return false;
			}
		}
		return true;
	}
}
