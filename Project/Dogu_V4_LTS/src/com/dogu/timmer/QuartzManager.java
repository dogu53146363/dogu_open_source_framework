package com.dogu.timmer;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.dogu.constants.Constant;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class QuartzManager {

	private Scheduler scheduler = null;

	public QuartzManager() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			System.out.println("Init Scheduler Success");
		} catch (SchedulerException ex) {
			System.err.println("Init Scheduler Error:" + ex);
		}
	}

	// 初始化启动任务
	public void initJob() {
		try {
			List<Record> list = Db.find("SELECT * FROM SYS_TIMMER WHERE F_STATUS = ?",Constant.CAPITAL_STR_Y);
			for (Record rec : list) {
				String className = rec.getStr("F_CLZ");
				Class<? extends Job> jobClazz = null;
				try {
					jobClazz = Class.forName(className).asSubclass(Job.class);
				} catch (Exception e) {
					System.err.println(className + "Not Implements Class:Job:" + e);
					continue;
				}
				String name = rec.getStr("F_TIMMERID");
				String Identity = rec.getStr("F_TIMMERID");
				String cronExpression = rec.getStr("F_CRON");
				this.addJob(name, Identity, jobClazz, cronExpression);
			}
			this.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// 添加任务
	public void addJob(String name, String Identity, Class<? extends Job> clazz, String cronExpression)
			throws ParseException {
		try {
			// 构造任务
			JobDetail job = newJob(clazz).withIdentity(name, Identity).build();
			// 构造任务触发器
			Trigger trg = newTrigger().withIdentity(name, Identity).withSchedule(cronSchedule(cronExpression)).build();
			// 将作业添加到调度器
			scheduler.scheduleJob(job, trg);
			System.out.println("Create Timed Task [" + name + "] In Identity [" + Identity + "] Success");
		} catch (SchedulerException e) {
			e.printStackTrace();
			System.err.println("Create Timed Task [" + name + "] In Identity [" + Identity + "] Error");
		}
	}

	// 移除任务
	public void removeJob(String name, String Identity) {
		try {
			TriggerKey tk = TriggerKey.triggerKey(name, Identity);
			scheduler.pauseTrigger(tk);// 停止触发器
			scheduler.unscheduleJob(tk);// 移除触发器
			JobKey jobKey = JobKey.jobKey(name, Identity);
			scheduler.deleteJob(jobKey);// 删除作业
			System.out.println("Remove Timed Task [" + Identity + "] In Identity [" + Identity + "] Success");
		} catch (SchedulerException e) {
			e.printStackTrace();
			System.err.println("Remove Timed Task [" + Identity + "] In Identity [" + Identity + "] Error");
		}
	}

	public void start() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			scheduler.shutdown();
			System.out.println("Stop Scheduler Success");
		} catch (SchedulerException e) {
			e.printStackTrace();
			System.out.println("Stop Scheduler Error");
		}
	}
}
