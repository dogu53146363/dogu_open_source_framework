package com.dogu.config;

import com.dogu.constants.Constant;
import com.jfinal.config.Constants;
import com.jfinal.core.Const;
import com.jfinal.render.ViewType;

/**
 * ConstantConfig配置
 * @author Dogu
 *
 */
public class ConstantConfig {
	
	public ConstantConfig(Constants me) {
		me.setDevMode(false);// 是否启用调试模式,打开的时候改成true就可以了
		me.setViewType(ViewType.JSP); // 设置视图类型为Jsp，否则默认为FreeMarker
		me.setError404View(Constant.PAGE404PATH);// 设置404页面
		me.setMaxPostSize(Const.DEFAULT_MAX_POST_SIZE);// 设置文件大小,默认为10MB
		me.setI18nDefaultLocale("zh_CN");
	}
}
