package example.timmer;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 第二个定时任务
 * @author Dogu
 *
 */
public class FirstTimmer implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Methord();
	}

	public void Methord() {
		System.out.println("第一个示例定时任务执行的时间是："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
}