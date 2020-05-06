package com.dogu.beforeStop;

import com.dogu.timmer.QuartzManager;

public class BeforeStop {
	public BeforeStop() {
		//停止定时任务
		QuartzManager qm = new QuartzManager();
        qm.shutdown();
	}
}
