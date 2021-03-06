package com.microBusiness.manage.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.microBusiness.manage.FileType;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.service.FileService;
import com.microBusiness.manage.util.Code;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller("fileController")
@RequestMapping("/api/file")
public class FileController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	/**
	 * 文件上传
	 * @param fileType
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/upload")
	public @ResponseBody JsonEntity upload(FileType fileType, MultipartFile file) {
		Map<String, Object> uploadMap = new HashMap<String, Object>();
		if(fileType == null || file == null || file.isEmpty()) {
			return JsonEntity.error(Code.code_upload_011901, Code.code_upload_011901.getDesc());
		}
		if (!fileService.isValid(fileType, file)) {
			return JsonEntity.error(Code.code_upload_011902, Code.code_upload_011902.getDesc());
		}
		String url = fileService.upload(fileType, file, false , true);
		if(StringUtils.isEmpty(url)) {
			return JsonEntity.error(Code.code_upload_011903, Code.code_upload_011903.getDesc());
		}
		uploadMap.put("message", "操作成功！");
		uploadMap.put("url", url);
		return JsonEntity.successMessage(uploadMap);
	}
}
