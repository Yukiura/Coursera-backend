package com.yukidoki.coursera.utils.wsUtils.signInUtils;

import com.yukidoki.coursera.ws.ChatEndpoint;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

public class CountDownJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Integer classId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get("classId");
        try {
            ChatEndpoint.countDown(classId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
