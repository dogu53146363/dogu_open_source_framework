package com.dogu.route;

import com.dogu.action.ConfigManageAction;
import com.dogu.action.IndexAction;
import com.dogu.action.LogManageAction;
import com.dogu.action.MainAction;
import com.dogu.action.MenuManageAction;
import com.dogu.action.OrgManageAction;
import com.dogu.action.OrgRightsManageAction;
import com.dogu.action.RoleManageAction;
import com.dogu.action.RoleRightsManageAction;
import com.dogu.action.TimmerManageAction;
import com.dogu.action.UserManageAction;
import com.dogu.action.UserRightsManageAction;
import com.jfinal.config.Routes;

/**
 * 系统路由配置
 * @author Dogu
 *
 */
public class SystemRoute {
	
    public SystemRoute(Routes me) {
    	me.add("/", IndexAction.class);                            // 全局登录路由
    	me.add("/main", MainAction.class);                         // 主页路由
    	me.add("/usermanage", UserManageAction.class);             // 用户管理路由
    	me.add("/rolemanage", RoleManageAction.class);             // 角色管理路由
    	me.add("/orgmanage", OrgManageAction.class);               // 组织机构管理路由
    	me.add("/menumanage", MenuManageAction.class);             // 菜单管理路由
    	me.add("/timmermanage", TimmerManageAction.class);         // 定时任务管理路由
    	me.add("/logmanage", LogManageAction.class);               // 日志管理路由
    	me.add("/configmanage", ConfigManageAction.class);         // 系统配置管理路由
    	me.add("/userightsmanage", UserRightsManageAction.class);  // 用户行权限路由
    	me.add("/rolerightsmanage", RoleRightsManageAction.class); // 角色行权限路由
    	me.add("/orgrightsmanage", OrgRightsManageAction.class);   // 组织机构行权限路由
	}
}