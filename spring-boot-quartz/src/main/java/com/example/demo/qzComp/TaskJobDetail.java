package com.example.demo.qzComp;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

/**
 * @project: 郭鹏飞的博客(wwww.pengfeiguo.com)
 * @description: quartz job 业务处理类
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-09-04 825338623@qq.com Create 1.0
 * @copyright ©2019-2020
 */
@DisallowConcurrentExecution
@Slf4j
public class TaskJobDetail extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("begin delwith batch task >>>>>>>>>>>>>>>>>>>>>>>");
        String batchId = context.getJobDetail().getKey().getName();
        log.info("执行的任务id为：[{}]", batchId);
    }
    
}
