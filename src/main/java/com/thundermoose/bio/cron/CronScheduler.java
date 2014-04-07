package com.thundermoose.bio.cron;

import com.google.common.collect.ImmutableMap;
import com.thundermoose.bio.managers.NcbiManager;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class CronScheduler {
  private static final Logger log = org.apache.log4j.Logger.getLogger(CronScheduler.class);

  @Value("${cron.ncbiRefresh}")
  String cronTab;
  @Autowired
  NcbiManager ncbiManager;

  Scheduler scheduler;

  public void init() throws SchedulerException {
    scheduler = new StdSchedulerFactory().getScheduler();
    if (!scheduler.isStarted()) {
      scheduler.start();
      scheduleNcbiJob();
    }
  }

  public void scheduleNcbiJob() throws SchedulerException {
    log.debug("NCBI cron: [" + cronTab + "]");
    JobDataMap data = new JobDataMap(ImmutableMap.of("manager", ncbiManager));

    JobDetail job = JobBuilder.newJob(NcbiCron.class)
            .withIdentity("ncbiCron").build();
    Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("ncbi")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronTab))
            .usingJobData(data)
            .forJob(job)
            .build();
    scheduler.scheduleJob(job, trigger);
    scheduler.triggerJob(job.getKey(), data);
  }
}
