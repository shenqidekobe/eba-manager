package com.microBusiness.manage.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.microBusiness.manage.DateEditor;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.exception.UserException;
import com.microBusiness.manage.exception.ValidExeption;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.MemberService;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.Constant;
import com.microBusiness.manage.util.CookieUtil;
import com.microBusiness.manage.util.DateUtils;

/**
 */
@Controller("apiBaseController")
@RequestMapping("/api/small/base")
public class BaseController {
	
    public final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";

    @Resource
    private MemberService memberService ;

    @Resource
    private ChildMemberService childMemberService ;
    @Resource(name = "validator")
	private Validator validator;
    
    @InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}
    
    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public Member getUserInfo(HttpServletRequest request) {

        Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_UNION_ID_NAME);
        String tokenValue = null;
        Member userInfo = null;
        if (null == cookie || null == cookie.getValue() || cookie.getValue().isEmpty()) {
            throw new UserException();
        }
        tokenValue = cookie.getValue();
        //tokenValue = "o_Enes0PBxMCQlTykTOuHwGetIjc";
        ChildMember childMember = childMemberService.findByUnionId(tokenValue) ;
        userInfo = childMember.getMember() ;

        if (null == userInfo) {
            throw new UserException();
        }
        return userInfo;

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

    public boolean needCheckMobile(String backUrl) {
        if (StringUtils.isNotEmpty(backUrl) && (StringUtils.contains(backUrl, "supplier.html") || StringUtils.contains(backUrl, "supplier.html"))) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前用户的子账号
     * @param request
     * @return
     */
    public ChildMember getCurrChildMem(HttpServletRequest request) {

        Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_UNION_ID_NAME);
        String tokenValue = null;
        if (null == cookie || null == cookie.getValue() || cookie.getValue().isEmpty()) {
            throw new UserException();
        }
        tokenValue = cookie.getValue();

        ChildMember childMember = childMemberService.findByUnionId(tokenValue) ;

        if (null == childMember) {
            throw new UserException();
        }
        return childMember;

    }


    public String getOpenId(HttpServletRequest request){
        Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_OPEN_ID_NAME);
        String tokenValue = null;
        if (null == cookie || null == cookie.getValue() || cookie.getValue().isEmpty()) {
            return null ;
        }
        return tokenValue ;
    }

    public String getUnionId(HttpServletRequest request){
        Cookie cookie = CookieUtil.getCookieByName(request, Constant.COOKIE_UNION_ID_NAME);
        String tokenValue = null;
        if (null == cookie || null == cookie.getValue() || cookie.getValue().isEmpty()) {
            return null ;
        }
        return tokenValue ;
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

    public Map<String, Object> dealPage(int currPage , int totalPage ,  List<Map<String , Object>> result) {

        Map<String, Object> map = new HashMap<>();
        map.put("list", result);
        map.put("pageNumber", currPage);
        map.put("totalPages", totalPage);

        return map ;
    }
    
    
    @RequestMapping(value = "/getShowDate", method = RequestMethod.GET)
	@ResponseBody
	public JsonEntity getShowDate(String smOpenId, Long proxyUserId, String ts, Date startDate, Date endDate) {
		if(null != ts) {
			if(ts.equalsIgnoreCase("thisWeek")) {
				startDate = DateUtils.startThisWeek();
				endDate = DateUtils.endOfTheWeek();
			};
			if(ts.equalsIgnoreCase("lastWeek")) {
				startDate = DateUtils.lastWeekStartTime();
				endDate = DateUtils.lastWeekEndTime();
			};
			if(ts.equalsIgnoreCase("lastMonth")) {
				startDate = DateUtils.lastMonthStartTime();
				endDate = DateUtils.lastMonthEndTime();
			};
			if(ts.equalsIgnoreCase("thisMonth")) {
				startDate = DateUtils.startThisMonth();
				endDate = DateUtils.theEndOfTheMonth();
			};
		}
		if(null == startDate) {
			startDate = DateUtils.startThisWeek();
		}
		if(null == endDate) {
			endDate = DateUtils.endOfTheWeek();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return JsonEntity.successMessage(map);
	}
}
