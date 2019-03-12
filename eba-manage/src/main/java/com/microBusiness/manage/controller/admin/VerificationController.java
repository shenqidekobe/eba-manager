package com.microBusiness.manage.controller.admin;

import com.microBusiness.manage.Message;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.Verification;
import com.microBusiness.manage.service.VerificationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
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
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Message create(int batchNo, int num) {
        String batchNoStr = batchNo + "";
        if (batchNo <= 0 || num <= 0) {
            return ERROR_MESSAGE;
        }

        //保存数据库
        for (int i = 0; i < num; i++) {

            //生成验证tag
            String u = UUID.randomUUID().toString().replace("-", "");

            StringBuffer tag = new StringBuffer();
            tag.append(batchNoStr);
            tag.append("-");
            tag.append(u);

            //保存数据
            Verification ver = new Verification();
            ver.setBatchNo(batchNoStr);
            ver.setTag(tag.toString());

            verService.save(ver);
        }

        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/impl", method = RequestMethod.GET)
    public ResponseEntity<File> implFile(HttpServletResponse response)  throws IOException{
        String url = "https://huayi.tripyi.com/ver/";
        List<Verification> all = verService.findAll();

        File f = new File("v.txt");

        try {
            if (!f.exists()) {
                f.createNewFile();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/x-plain");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + f.getName());// 设置文件名

                FileWriter writer = new FileWriter(f);
                BufferedWriter bw = new BufferedWriter(writer);

                for (Verification v : all) {
                    bw.write(url + v.getTag() + "\r\n");
                }

                bw.flush();

                writer.close();
                bw.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return buildResponseEntity(f);
    }

    @RequestMapping(value = "/impl2", method = RequestMethod.GET)
    public ResponseEntity<File> implFile2(String batchNo,HttpServletResponse response)  throws IOException{
        String url = "https://huayi.tripyi.com/ver/";
        List<Verification> list = verService.findByBatchNo(batchNo);
        File f = new File(batchNo+"_ver.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/x-plain");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + f.getName());// 设置文件名

                FileWriter writer = new FileWriter(f);
                BufferedWriter bw = new BufferedWriter(writer);

                for (Verification v : list) {
                    bw.write(url + v.getTag() + "\r\n");
                }

                bw.flush();
                writer.close();
                bw.close();
            }
        } catch (IOException e) {
        }
        return buildResponseEntity(f);
    }

    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "/admin/ver/add";
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.put("page", verService.findPage(pageable));
        return "/admin/ver/list";
    }

    public ResponseEntity<File> buildResponseEntity(File file) throws IOException {
//        byte[] body = null;
//        //获取文件
//        InputStream is = new FileInputStream(file);
//        body = new byte[is.available()];
//        is.read(body);
        HttpHeaders headers = new HttpHeaders();
        //设置文件类型
        headers.add("Content-Disposition", "attchement;filename=" + file.getName());
        //设置Http状态码

//　　　　HttpStatus statusCode = HttpStatus.OK;
        //返回数据
//　　　　ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        return new ResponseEntity<File>(file,headers, HttpStatus.OK);
    }

}
