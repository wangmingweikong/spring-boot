package com.example.demo.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @project: 郭鹏飞的博客(wwww.pengfeiguo.com)
 * @description: cron生成工具类
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-09-04 825338623@qq.com Create 1.0
 * @copyright ©2019-2020
 */
public class CronUtil {
    
    /**
     * @param batchScheduleModel
     * @return
     * @Desc 转化cron表达式
     */
    public static String convertCronExpression(Date startDate, Date endDate, String[] weeks) {
        StringBuffer sb = new StringBuffer();
        sb.append(convertSeconds()).append(" ").append(convertMinutes(startDate)).append(" ")
            .append(convertHours(startDate, endDate)).append(" ").append(convertDay(startDate, endDate)).append(" ")
            .append(convertMonth(startDate, endDate)).append(" ").append(convertWeek(weeks));
        return sb.toString();
    }
    
    /**
     * 获取定时任务开始时间
     * 
     * @param batchScheduleModel
     * @return
     * @throws ParseException
     */
    public static Date getStartDate(Date startDate) throws ParseException {
        String yyyyMMddS = DateUtils.date2String(startDate, "yyyyMMdd");
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
                                                           // 12小时制，calendar.HOUR_OF_DAY 24小时）
        int startMin = startCal.get(Calendar.MINUTE);// 分
        String startTime = "2359";
        if (startHour < Integer.parseInt(startTime.substring(0, 2))) {
            startTime = startHour + "" + startMin;
        }
        return DateUtils.string2Date(yyyyMMddS + startTime + "00", "yyyyMMddHHmmss");
    }
    
    /**
     * 获取定时任务结束时间
     * 
     * @param batchScheduleModel
     * @return
     * @throws ParseException
     */
    public static Date getEndDate(Date endDate) throws ParseException {
        String yyyyMMddE = DateUtils.date2String(endDate, "yyyyMMdd");
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(endDate);
        int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
                                                           // 12小时制，calendar.HOUR_OF_DAY 24小时）
        int startMin = startCal.get(Calendar.MINUTE);// 分
        String endTime = "0000";
        if (startHour < Integer.parseInt(endTime.substring(0, 2))) {
            endTime = startHour + "" + startMin;
        }
        return DateUtils.string2Date(yyyyMMddE + endTime + "00", "yyyyMMddHHmmss");
    }
    
    /**
     * 判断当前时间是否在规则时间范围内
     * 
     * @param timesEntityList
     * @return
     */
    public static boolean isInRuleTimes(Date startDate, Date endDate) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
                                                 // 12小时制，calendar.HOUR_OF_DAY 24小时）
        int minute = cal.get(Calendar.MINUTE);// 分
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(date);
        int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
        // 12小时制，calendar.HOUR_OF_DAY 24小时）
        int startMinute = startCal.get(Calendar.MINUTE);// 分
        
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(date);
        int endHour = endCal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
                                                       // 12小时制，calendar.HOUR_OF_DAY 24小时）
        int endMinute = cal.get(Calendar.MINUTE);// 分
        if (startHour < hour && hour < endHour) {
            return true;
        }
        if (startHour == hour && hour == endHour && startMinute < minute && minute < endMinute) {
            return true;
        }
        if (startHour == hour && startMinute < minute) {
            return true;
        }
        if (endHour == hour && minute < endMinute) {
            return true;
        }
        return false;
    }
    
    /**
     * 抽取cron中的hour
     * 
     * @param batchRuleTimeEntityList
     * @return
     */
    public static String convertHours(Date startDay, Date endDay) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDay);
        int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
                                                           // 12小时制，calendar.HOUR_OF_DAY 24小时）
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDay);
        int endHour = endCal.get(Calendar.HOUR_OF_DAY);// 小时（calendar.HOUR
                                                       // 12小时制，calendar.HOUR_OF_DAY 24小时）
        StringBuffer sb = new StringBuffer();
        sb.append(startHour).append("-").append(endHour);
        return sb.toString();
    }
    
    /**
     * 抽取cron中的minute
     * 
     * @param batchRuleTimeEntityList
     * @return
     */
    public static String convertMinutes(Date startDay) {
        return "* ";
        /*
         * StringBuffer sb = new StringBuffer();
         * batchRuleTimeEntityList.forEach((b)->{
         * String start = b.getStartTime();
         * String end = b.getEndTime();
         * String minS = start.substring(3,5);
         * String minE = end.substring(3,5);
         * sb.append(minS).append("-").append(minE).append(",");
         * });
         * return sb.deleteCharAt(sb.length()-1).toString();
         */
    }
    
    /**
     * 抽取cron中的seconds
     * 
     * @return
     */
    public static String convertSeconds() {
        return "1";
    }
    
    /**
     * 抽取cron中的day, 在这个项目里直接返回？
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static String convertDay(Date startDate, Date endDate) {
        return "?";
        /*
         * String start = DateUtil.formatDate(DateUtil.YYYYMMDD, startDate);
         * String end = DateUtil.formatDate(DateUtil.YYYYMMDD, endDate);
         * String dayS = start.substring(6,8);
         * String dayE = end.substring(6,8);
         * return dayS + "-" + dayE;
         */
    }
    
    /**
     * 抽取cron中的month
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static String convertMonth(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        // 获取月份（因为在格里高利历和罗马儒略历一年中第一个月为JANUARY，它为0，最后一个月取决于一年中的月份数，所以这个值初始为0，所以需要加1）
        int startMonth = startCal.get(Calendar.MONTH) + 1;
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        // 获取月份（因为在格里高利历和罗马儒略历一年中第一个月为JANUARY，它为0，最后一个月取决于一年中的月份数，所以这个值初始为0，所以需要加1）
        int endMonth = endCal.get(Calendar.MONTH) + 1;
        return startMonth + "-" + endMonth;
    }
    
    /**
     * 抽取cron中的week
     * 
     * @param dayOfWeeks
     * @return
     */
    public static String convertWeek(String[] dayOfWeeks) {
        StringBuffer sb = new StringBuffer();
        for (String dayOfWeek : dayOfWeeks) {
            sb.append(dayOfWeek).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * 抽取cron中的week
     * 
     * @param dayOfWeeks
     * @return
     */
    public static String convertWeek(String dayOfWeeks) {
        StringBuffer sb = new StringBuffer();
        String[] split = dayOfWeeks.split(",");
        for (String str : split) {
            sb.append(str).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
}
