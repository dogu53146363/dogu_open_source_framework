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

public class MenuTreeUtil_BACK {

	public static Map<String, Object> mapArray = new LinkedHashMap<String, Object>();
	public List<Tree> menuCommon;
	public List<Object> list = new ArrayList<Object>();

	public List<Object> menuList(List<Tree> menu) {
		this.menuCommon = menu;
		for (Tree x : menu) {
			Map<String, Object> mapArr = new LinkedHashMap<String, Object>();
			if ("ROOT".equals(x.getpId())) {
				mapArr.put("id", x.getId());
				mapArr.put("pid", x.getpId());
				mapArr.put("name", x.getName());
				mapArr.put("url", x.getUrl());
				mapArr.put("icon", x.getIcon());
				mapArr.put("subMenus", menuChild(x.getId()));
				list.add(mapArr);
			}
		}
		return list;
	}

	public List<?> menuChild(String id) {
		List<Object> lists = new ArrayList<Object>();
		for (Tree a : menuCommon) {
			Map<String, Object> childArray = new LinkedHashMap<String, Object>();
			if (id.equals(a.getpId())) {
				childArray.put("id", a.getId());
				childArray.put("pid", a.getpId());
				childArray.put("name", a.getName());
				childArray.put("url", a.getUrl());
				childArray.put("icon", a.getIcon());
				childArray.put("subMenus", menuChild(a.getId()));
				lists.add(childArray);
			}
		}
		return lists;
	}
}