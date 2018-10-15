package com.microBusiness.manage.controller.ass;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.exception.UserException;
import com.microBusiness.manage.exception.ValidExeption;
import com.microBusiness.manage.service.ass.AssChildMemberService;
import com.microBusiness.manage.util.Code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Created by afei.
 * User: afei
 * Date: 2016/5/26 10:05
 * Describe: 订单助手 base 控制层
 * Update:
 */
public class BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";

    @Resource
    private AssChildMemberService assChildMemberService ;
    @Resource(name = "validator")
	private Validator validator;
    
    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public Member getUserInfo(HttpServletRequest request) {
        return null ;
    }

    /**
     * 获取助手用户子账号实体
     *
     * @param request
     * @return
     */
    public AssChildMember getAssChildMember(String unionId) {
    	AssChildMember assChildMember = assChildMemberService.findByUnionId(unionId);
    	
    	if (assChildMember == null) {
    		 throw new UserException();
		}
    	return assChildMember;
    }

    /**
     * 统一异常捕获
     *
     * @param exception
     * @param response
     * @return JsonEntity
     */
    @ExceptionHandler
    @ResponseBody
    public JsonEntity exceptionHandler(Exception exception, HttpServletResponse response) {
        logger.error("统一异常：", exception);
        if (exception instanceof UserException) {
            return new JsonEntity(Code.code010101, Code.code010101.getDesc());
        }else if(exception instanceof ConversionFailedException){
            return new JsonEntity(Code.code019998, Code.code019998.getDesc());
        }else if(exception instanceof ValidExeption){
            return new JsonEntity(Code.code019997, exception.getMessage());
        } else {
            return new JsonEntity(Code.code019999, Code.code019999.getDesc());
        }
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

}
