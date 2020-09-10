package com.example.demo.controller;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Result;
import com.example.demo.constant.GloabalConstant;
import com.example.demo.entity.QuartzJobModule;
import com.example.demo.qzComp.TaskJobDetail;
import com.example.demo.utils.CronUtil;
import com.example.demo.utils.QuartzJobComponent;

/**
 * @project: 郭鹏飞的博客(wwww.pengfeiguo.com)
 * @description: quartz controller 。
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-09-04 825338623@qq.com Create 1.0
 * @copyright ©2019-2020
 */
@RestController
@RequestMapping("/job")
public class JobController {
    private final static Logger LOGGER = LoggerFactory.getLogger(JobController.class);
    
    @Autowired
    private QuartzJobComponent quartzJobComponent;
    
    @PostMapping("/add")
    @ResponseBody
    public Result save() {
        LOGGER.info("新增任务");
        try {
            QuartzJobModule quartzJobModule = new QuartzJobModule();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2020);
            cal.set(Calendar.MONTH, 8);
            cal.set(Calendar.DATE, 9);
            cal.set(Calendar.HOUR_OF_DAY, 12);
            cal.set(Calendar.MINUTE, 30);
            cal.set(Calendar.SECOND, 00);
            Date startDate = cal.getTime();// 任务开始日期为2020年9月9日12点30分
            
            Calendar endCal = Calendar.getInstance();
            endCal.set(Calendar.YEAR, 2020);
            endCal.set(Calendar.MONTH, 8);
            endCal.set(Calendar.DATE, 12);
            endCal.set(Calendar.HOUR_OF_DAY, 12);
            endCal.set(Calendar.MINUTE, 30);
            endCal.set(Calendar.SECOND, 00);
            Date endDate = endCal.getTime();// 任务结束日期为2020年9月12日12点30分
            
            quartzJobModule.setStartTime(CronUtil.getStartDate(startDate));
            quartzJobModule.setEndTime(CronUtil.getEndDate(endDate));
            // 注意：在后面的任务中需要通过这个JobName来获取你要处理的数据，因此您可以讲这个设置为你要处理的数据的主键，比如id
            quartzJobModule.setJobName("testJobId");
            quartzJobModule.setTriggerName("tesTriggerNmae");
            quartzJobModule.setJobGroupName(GloabalConstant.QZ_JOB_GROUP_NAME);
            quartzJobModule.setTriggerGroupName(GloabalConstant.QZ_TRIGGER_GROUP_NAME);
            String weeks = "1,2,3,5";// 该处模拟每周1,2,3,5执行任务
            String cronExpression = CronUtil.convertCronExpression(startDate,
                    endDate, weeks.split(","));
            quartzJobModule.setCron(cronExpression);
            quartzJobModule.setJobClass(TaskJobDetail.class);
            quartzJobComponent.addJob(quartzJobModule);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.ok();
        }
        return Result.ok();
    }
    
    @PostMapping("/edit")
    @ResponseBody
    public Result edit() {
        LOGGER.info("编辑任务");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 12);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 00);
        Date startDate = cal.getTime();// 任务开始日期为2020年9月12日12点30分
        
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.YEAR, 2020);
        endCal.set(Calendar.MONTH, 8);
        endCal.set(Calendar.DATE, 24);
        endCal.set(Calendar.HOUR_OF_DAY, 12);
        endCal.set(Calendar.MINUTE, 30);
        endCal.set(Calendar.SECOND, 00);
        Date endDate = endCal.getTime();// 任务结束日期为2020年9月24日12点30分
        // "testJobId"为add方法添加的job的name
        quartzJobComponent.modifyJobTime("testJobId", "/10 *  * ? * *", startDate, endDate);
        return Result.ok();
    }
    
    @PostMapping("/pause")
    @ResponseBody
    public Result pause(String jobName, String jobGroup) {
        LOGGER.info("停止任务");
        quartzJobComponent.pauseJob("testJobId");
        return Result.ok();
    }
    
    @PostMapping("/resume")
    @ResponseBody
    public Result resume(String jobName, String jobGroup) {
        LOGGER.info("恢复任务");
        quartzJobComponent.removeJob("testJobId");
        return Result.ok();
        
    }
    
    @PostMapping("/remove")
    @ResponseBody
    public Result remove(String jobName, String jobGroup) {
        LOGGER.info("移除任务");
        quartzJobComponent.removeJob("testJobId");
        return Result.ok();
    }
}
