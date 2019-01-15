package com.microBusiness.manage.controller.api;

import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Verification;
import com.microBusiness.manage.service.VerificationService;
import com.microBusiness.manage.util.Code;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/ver")
public class VerController {

    @Resource(name = "verificationServiceImpl")
    private VerificationService verService;


    @RequestMapping(value = "/verification",method = RequestMethod.GET)
    @ResponseBody
    public JsonEntity ver(String tag, HttpServletRequest request, HttpServletResponse response) {
        Verification ver = verService.findByTag(tag);

        if (ver != null) {
            if (ver.getProofTime() == null) {
                //首次次验证
                ver.setProofTime(new Date());
                verService.update(ver);
            }

            Map<String, Object> hasMap = new HashMap<>();
            hasMap.put("proofTime", ver.getProofTime());
            return JsonEntity.successMessage(hasMap);
        } else {
            return JsonEntity.error(Code.code998, "验证失败");
        }

    }
}
