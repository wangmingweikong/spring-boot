package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.constant.GloabalConstant;
import com.example.demo.entity.QuartzJobModule;

import lombok.extern.slf4j.Slf4j;

/**
 * @project: 郭鹏飞的博客(wwww.pengfeiguo.com)
 * @description: qz工具类
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-09-04 825338623@qq.com Create 1.0
 * @copyright ©2019-2020 。
 */
@Slf4j
@Component
public class QuartzJobComponent {
    
    @Autowired
    private Scheduler scheduler;
    
    /**
     * @Description: 添加一个定时任务
     * @param quartzModel
     */
    public void addJob(QuartzJobModule quartzModel) {
        if (quartzModel.verify()) {
            try {
                JobDetail job = JobBuilder.newJob(quartzModel.getJobClass())
                    .withIdentity(quartzModel.getJobName(), quartzModel.getJobGroupName())
                    .setJobData(quartzModel.getJobDataMap()).build();
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzModel.getCron());
                // 按新的cronExpression表达式构建一个新的trigger
                Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzModel.getTriggerName(), quartzModel.getTriggerGroupName())
                    .startAt(quartzModel.getStartTime()).endAt(quartzModel.getEndTime()).withSchedule(
                        scheduleBuilder)
                    .build();
                scheduler.scheduleJob(job, trigger);
                // 启动
                if (!scheduler.isShutdown()) {
                    scheduler.start();
                }
            }
            catch (SchedulerException e) {
                log.error("Add quartz job error, jobName = {}", quartzModel.getJobName());
            }
            
        }
        else {
            log.error("QuartzModel is invalid!");
        }
    }
    
    /**
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     * @param cron
     */
    public void modifyJobTime(String jobName, String cron, Date startDate, Date endDate) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, GloabalConstant.QZ_TRIGGER_GROUP_NAME);
        
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .startAt(startDate)
                    .endAt(endDate).withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @Description:修改任务，（可以修改任务名，任务类，触发时间）
     *                                      原理：移除原来的任务，添加新的任务
     * @param oldJobName
     *            ：原任务名
     * @param jobName
     * @param jobclass
     * @param cron
     */
    public void modifyJob(String oldJobName, String jobName, Class jobclass, String cron) {
        /*
         * removeJob(oldJobName);
         * addJob(jobName, jobclass, cron);
         * System.err.println("修改任务"+oldJobName);
         */
        TriggerKey triggerKey = TriggerKey.triggerKey(oldJobName, GloabalConstant.QZ_TRIGGER_GROUP_NAME);
        JobKey jobKey = JobKey.jobKey(oldJobName, GloabalConstant.QZ_JOB_GROUP_NAME);
        try {
            Trigger trigger = (Trigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
            System.err.println("移除任务:" + oldJobName);
            
            JobDetail job = JobBuilder.newJob(jobclass).withIdentity(jobName,
                GloabalConstant.QZ_JOB_GROUP_NAME)
                .build();
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            // 按新的cronExpression表达式构建一个新的trigger
            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(jobName,
                GloabalConstant.QZ_TRIGGER_GROUP_NAME)
                .withSchedule(scheduleBuilder).build();
            
            // 交给scheduler去调度
            scheduler.scheduleJob(job, newTrigger);
            
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
                System.err.println("添加新任务:" + jobName);
            }
            System.err.println("修改任务【" + oldJobName + "】为:" + jobName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    /**
     * @Description: 修改一个任务的触发时间
     * @param triggerName
     * @param triggerGroupName
     * @param cron
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // trigger已存在，则更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.resumeTrigger(triggerKey);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @Description 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     */
    public void removeJob(String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, GloabalConstant.QZ_TRIGGER_GROUP_NAME);
        JobKey jobKey = JobKey.jobKey(jobName, GloabalConstant.QZ_JOB_GROUP_NAME);
        try {
            Trigger trigger = (Trigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
            System.err.println("移除任务:" + jobName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @Description: 移除一个任务
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @Description:暂停一个任务(使用默认组名)
     * @param jobName
     */
    public void pauseJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, GloabalConstant.QZ_JOB_GROUP_NAME);
        try {
            scheduler.pauseJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description:暂停一个任务
     * @param jobName
     * @param jobGroupName
     */
    public void pauseJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.pauseJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description:恢复一个任务(使用默认组名)
     * @param jobName
     */
    public void resumeJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, GloabalConstant.QZ_JOB_GROUP_NAME);
        try {
            scheduler.resumeJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description:恢复一个任务
     * @param jobName
     * @param jobGroupName
     */
    public void resumeJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.resumeJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description:启动所有定时任务
     */
    public void startJobs() {
        try {
            scheduler.start();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @Description 关闭所有定时任务
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @Description: 立即运行任务，这里的立即运行，只会运行一次，方便测试时用。
     * @param jobName
     */
    public void triggerJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, GloabalConstant.QZ_JOB_GROUP_NAME);
        try {
            scheduler.triggerJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description: 立即运行任务，这里的立即运行，只会运行一次，方便测试时用。
     * @param jobName
     * @param jobGroupName
     */
    public void triggerJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.triggerJob(jobKey);
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @Description: 获取任务状态
     * @param jobName
     *            触发器名
     */
    public String getTriggerState(String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, GloabalConstant.QZ_TRIGGER_GROUP_NAME);
        String name = null;
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            name = triggerState.name();
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
        return name;
    }
    
    /**
     * @Description:获取最近5次执行时间
     * @param cron
     */
    public List<String> getRecentTriggerTime(String cron) {
        List<String> list = new ArrayList<String>();
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 5);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            for (Date date : dates) {
                list.add(dateFormat.format(date));
            }
        }
        catch (ParseException e) {
            log.error("GetRecentTriggerTime error, cron = {}", cron, e);
        }
        return list;
    }
    
}
