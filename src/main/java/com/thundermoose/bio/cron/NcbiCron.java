package com.thundermoose.bio.cron;

import com.thundermoose.bio.managers.NcbiManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;


/**
 * Created by Thundermoose on 4/6/2014.
 */
public class NcbiCron implements Job {
  private static final Logger log = org.apache.log4j.Logger.getLogger(NcbiCron.class);

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    NcbiManager manager = (NcbiManager) jobExecutionContext.getMergedJobDataMap().get("manager");

    try {
      log.info("Starting refresh of NCBI data");
      long time = System.currentTimeMillis();
      manager.refresh();
      log.info("Completed refresh of NCBI data in " + (System.currentTimeMillis() - time) + "ms");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
