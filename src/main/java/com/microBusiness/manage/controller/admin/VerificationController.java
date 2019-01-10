package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Verification;
import com.microBusiness.manage.service.VerificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * 验证管理
 */
@Controller("verificationController")
@RequestMapping("/admin/ver")
public class VerificationController {
    protected static final Message ERROR_MESSAGE = Message.error("admin.message.error");

    protected static final Message SUCCESS_MESSAGE = Message.success("admin.message.success");

    @Resource(name = "verificationServiceImpl")
    private VerificationService verService;

    /**
     * 创建验证
     *
     * @param batchNo
     * @param num
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public Message create(String batchNo, int num) {
        batchNo = "batchNo" + batchNo;
        if (batchNo == null || num <= 0) {
            return ERROR_MESSAGE;
        }
//        String url = "?tag=";

        //保存数据库
        for (int i = 0; i < num; i++) {
            //二维码对应url

            //生成验证tag
            String u = UUID.randomUUID().toString().replace("-", "");

            StringBuffer tag = new StringBuffer();
            tag.append(batchNo);
            tag.append("-");
            tag.append(u);

            //保存数据
            Verification ver = new Verification();
            ver.setBatchNo(batchNo);
            ver.setTag(tag.toString());

            verService.save(ver);
        }

        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.put("page", verService.findPage(pageable));
        return "/admin/ver/list";
    }


}
