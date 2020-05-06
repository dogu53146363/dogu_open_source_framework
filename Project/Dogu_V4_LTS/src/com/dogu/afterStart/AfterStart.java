package com.dogu.afterStart;

import com.dogu.timmer.QuartzManager;

public class AfterStart {
	public AfterStart() {
		//加载定时任务数据库配置
        QuartzManager qm = new QuartzManager();
        try {
			qm.initJob();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
