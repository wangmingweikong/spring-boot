package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * @project: 郭鹏飞的博客(wwww.pengfeiguo.com)
 * @description: 日期工具类
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-09-04 825338623@qq.com Create 1.0
 * @copyright ©2019-2020
 */
public class DateUtils {
    
    /**
     * 模式 :yyyyMMddHHmmss
     */
    private static final String YYYYMMDD_HHMMSS = "yyyyMMddHHmmss";
    /**
     * 模式 :yyyyMMdd
     */
    private static final String YYYYMMDD        = "yyyyMMdd";
    
    /**
     * 方法说明：日期类型按照指定格式转成字符串.
     *
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return
     */
    public static String date2String(Date date, String pattern) {
        if (null == date) {
            date = new Date();
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            return getDateFormat(pattern).format(date);
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * 方法说明：获取指定模式pattern的SimpleDateFormat对象.
     *
     * @param pattern
     *            日期格式
     * @return
     */
    private static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }
    
    /**
     * 方法说明：获取默认模式"yyyyMMdd"的SimpleDateFormat对象.
     *
     * @return
     */
    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(YYYYMMDD);
    }
    
    /**
     * 日期转换
     *
     * @param srcStr
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     * @throws ParseException
     */
    public static String stringFormat(String srcStr, String pattern) throws ParseException {
        Date date = string2Date(srcStr);
        return date2String(date, pattern);
    }
    
    /**
     * 方法说明：日期类型转成yyyyMMdd格式字符串.
     *
     * @param date
     *            日期
     * @return
     */
    public static String date2String(Date date) {
        return date2String(date, YYYYMMDD);
    }
    
    /**
     * 方法说明：字符串转日期类型.
     *
     * @param date
     *            日期字符串
     * @return
     * @throws ParseException
     * @throws Exception
     */
    public static Date string2Date(String date) throws ParseException {
        if (date.length() != 16) {
            return getDateFormat().parse(date);
        }
        else {
            return getDateFormat(YYYYMMDD_HHMMSS).parse(date);
        }
    }
    
    /**
     * 按照转换规则将日期字符串转换为Date类型的时间
     *
     * @param dateString
     *            要转换的日期字符串
     * @param format
     *            转换的格式，例如：YYYYMMDD
     * @return 转换后的Date类型的日期
     * @throws ParseException
     * @throws BusinessException
     *             异常
     */
    public static Date string2Date(String dateString, String format) throws ParseException {
        SimpleDateFormat sd1 = new SimpleDateFormat(format);
        Date date = sd1.parse(dateString);
        return date;
    }
    
}
