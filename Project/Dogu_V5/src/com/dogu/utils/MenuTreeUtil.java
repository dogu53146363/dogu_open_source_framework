package com.dogu.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类名称：MenuTreeUtil
 * 类描述：递归构造树型结构
 */

public class MenuTreeUtil {

	public List<Tree> menuCommon;
	public List<Object> list = new ArrayList<Object>();
	
	/**
	 * 先查询第一层的
	 * @param menu
	 * @return
	 */
	public List<Object> menuList(List<Tree> menu) {
		this.menuCommon = menu;
		Map<String, Object> mapArr = new LinkedHashMap<String, Object>();
		for (int i = 0; i < menu.size(); i++) {
			if ("-1".equals(menu.get(i).getpId())) {
				mapArr = new LinkedHashMap<String, Object>();
				mapArr.put("id", menu.get(i).getId());
				mapArr.put("pid", menu.get(i).getpId());
				mapArr.put("name", menu.get(i).getName());
				mapArr.put("url", menu.get(i).getUrl());
				mapArr.put("icon", menu.get(i).getIcon());
				mapArr.put("subMenus", menuChild(menu.get(i).getId()));
				list.add(mapArr);
			}
		}
		return list;
	}
	
	/**
	 * 然后做递归调用
	 * @param id
	 * @return
	 */
	public List<?> menuChild(String id) {
		List<Object> lists = new ArrayList<Object>();
		for (int i=0;i<menuCommon.size();i++) {
			Map<String, Object> childArray = new LinkedHashMap<String, Object>();
			if (id.equals(menuCommon.get(i).getpId())) {
				childArray.put("id", menuCommon.get(i).getId());
				childArray.put("pid", menuCommon.get(i).getpId());
				childArray.put("name", menuCommon.get(i).getName());
				childArray.put("url", menuCommon.get(i).getUrl());
				childArray.put("icon", menuCommon.get(i).getIcon());
				childArray.put("subMenus", menuChild(menuCommon.get(i).getId()));
				lists.add(childArray);
			}
		}
		return lists;
	}
}