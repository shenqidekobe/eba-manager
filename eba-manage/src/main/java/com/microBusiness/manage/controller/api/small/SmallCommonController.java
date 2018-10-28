package com.microBusiness.manage.controller.api.small;

import com.microBusiness.manage.controller.api.BaseController;
import com.microBusiness.manage.entity.Ad;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.JsonEntity;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.service.AreaService;
import com.microBusiness.manage.service.ChildMemberService;
import com.microBusiness.manage.service.SnService;
import com.microBusiness.manage.service.WeChatService;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;
import com.microBusiness.manage.util.ApiSmallUtils;
import com.microBusiness.manage.util.Code;
import com.microBusiness.manage.util.WebUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2017/11/2 下午6:19
 * Describe:
 * Update:
 */
@Controller
@RequestMapping("/api/small/common")
public class SmallCommonController extends BaseController {

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
    
    @Resource
	private WeChatService weChatService;
    
    @Resource
	private AssCustomerRelationService assRelationService;
    
    @Resource
    private SnService snService;
    
    @Resource
    private ChildMemberService childMemberService ;
    
    private static Logger LOGGER = LoggerFactory.getLogger(SmallCommonController.class);

    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public @ResponseBody
    JsonEntity area(Long parentId, Integer level) {

        if(null == level){
            return JsonEntity.error(Code.code019998) ;
        }

        Map<String, Map<String, Object>> result = new HashMap();

        this.getAreas(parentId , level , result) ;

        return JsonEntity.successMessage(result) ;

    }


    @RequestMapping(value = "/initArea", method = RequestMethod.GET)
    public @ResponseBody
    JsonEntity initArea(Long areaId) {

        Area selectArea = areaService.find(areaId) ;

        if(null == selectArea){
            return JsonEntity.error(Code.code019998) ;
        }

        Map<String, Map<String, Object>> result = new HashMap();

        this.getAreas(selectArea , selectArea.getParent() , result) ;


        return JsonEntity.successMessage(result) ;

    }



    private void getAreas(Area currentArea , Area parent  , Map<String, Map<String, Object>> result ){

        List<Area> childs;

        if(null == parent){
            childs = areaService.findRoots() ;
        }else{
            childs = new ArrayList<>(parent.getChildren());
        }

        int level = currentArea.getGrade() + 1 ;

        int levelIndex = 0;
        int levelShow = 0;

        final List<Map<String, Object>> hiddenData = new ArrayList<>();

        final List<String> showData = new ArrayList<>();

        for (Area area : childs) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", area.getName());
            item.put("id", area.getId());

            hiddenData.add(item);
            showData.add(area.getName());

            if (area.getId().compareTo(currentArea.getId()) == 0) {
                levelShow = levelIndex;
            }

            levelIndex++;

        }
        final int finalLevelShow = levelShow;
        result.put("level" + level, new HashMap<String, Object>() {{
            this.put("hiddenData", hiddenData);
            this.put("showData", showData);
            this.put("showIndex", finalLevelShow);
        }});

        Area tempParent = currentArea.getParent();
        if(null == tempParent){
            return ;
        }

        this.getAreas(tempParent, tempParent.getParent(), result);


    }

    private void getAreas(Long parentId , int level , Map<String, Map<String, Object>> result){
        List<Area> childs ;

        if(null == parentId){
            childs = areaService.findRoots() ;

        }else{
            Area parent = areaService.find(parentId) ;
            childs = new ArrayList<>(parent.getChildren());
        }


        if(CollectionUtils.isEmpty(childs)){
            return ;
        }

        final List<Map<String, Object>> hiddenData = new ArrayList<>();

        final List<String> showData = new ArrayList<>();

        int levelShow = 0 ;

        for (Area area : childs) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", area.getName());
            item.put("id", area.getId());

            hiddenData.add(item);
            showData.add(area.getName());

        }

        final int finalLevelShow = levelShow;
        level ++ ;
        result.put("level" +level  , new HashMap<String, Object>() {{
            this.put("hiddenData", hiddenData);
            this.put("showData", showData);
            this.put("showIndex" , finalLevelShow) ;
        }});

        Long tempParentId = childs.get(0).getId() ;

        this.getAreas(tempParentId , level , result) ;
    }

}