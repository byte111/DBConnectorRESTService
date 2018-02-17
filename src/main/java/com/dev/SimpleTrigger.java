package com.dev;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleTrigger {

	public static void main(String[] args) {
	/*	JobDetail job  = JobBuilder.newJob(TestJob.class)
									.withIdentity("job1","grp1")
									.build();
		Trigger trigger = TriggerBuilder.newTrigger()
										.withIdentity("Trigger1","grp1")
										//.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
										.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
										.build();
		
		try {
			Scheduler scheduler = (new  StdSchedulerFactory()).getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
