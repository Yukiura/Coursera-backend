package com.yukidoki.coursera.utils.wsUtils.signInUtils;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class SignInCounter {
    private final Scheduler scheduler;

    public SignInCounter(Integer classId, int duration) throws SchedulerException {
        Date now = new Date();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        this.scheduler = schedulerFactory.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(CountDownJob.class)
                .withIdentity("Job" + classId + now, "COUNTDOWN_JOB")
                .usingJobData("classId", classId)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("Trigger" + classId + now, "COUNTDOWN_TRIGGER")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(duration * 60 - 1))
                .build();
        this.scheduler.scheduleJob(jobDetail, trigger);
    }

    public void execute() throws SchedulerException {
        scheduler.start();
    }

    public void stop() throws SchedulerException {
        scheduler.shutdown();
    }
}
