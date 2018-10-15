package com.microBusiness.manage.tag;

import org.apache.shiro.web.tags.HasPermissionTag;

/**
 * Created by mingbai on 2017/8/23.
 * 功能描述：
 * 修改记录：
 */
public class MyHasPermissionTag extends HasPermissionTag {

    private static final String OR_OPERATOR = " or ";

    @Override
    protected boolean showTagBody(String permission) {
        if(permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for(String orPermission : permissions) {
                if(isPermitted(orPermission)) {
                    return true;
                }
            }
            return false;
        }
        return super.showTagBody(permission);
    }
}
