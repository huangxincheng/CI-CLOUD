package com.husen.ci.framework.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/***
 @Author:MrHuang
 @Date: 2019/6/28 10:44
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class DateUtils {

    private static final String FORMAT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String FORMAT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Date转换为LocalDateTime
     * @param date date
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime localDateTime
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * date转换localDate
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date) {
        return date2LocalDateTime(date).toLocalDate();
    }

    /**
     * localdae转换date
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * localDateTime转时间字符串
     * @param localDateTime
     * @return
     */
    public static String localDateTime2TimeString(LocalDateTime localDateTime) {
       return localDateTime2String(localDateTime, FORMAT_TIME_PATTERN);
    }

    /**
     * localDateTime转日期字符串
     * @param localDateTime
     * @return
     */
    public static String localDateTime2DateString(LocalDateTime localDateTime) {
        return localDateTime2String(localDateTime, FORMAT_DATE_PATTERN);
    }

    /**
     * localDateTime 转制定字符串
     * @param localDateTime
     * @param formatPattern
     * @return
     */
    public static String localDateTime2String(LocalDateTime localDateTime, String formatPattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatPattern);
        return dtf.format(localDateTime);
    }

    /**
     * localDate转字符串
     * @param localDate
     * @return
     */
    public static String localDate2String(LocalDate localDate) {
        return localDate.toString();
    }


    /**
     * 获取秒
     * @param localDateTime
     * @return
     */
    public static long getSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取毫秒
     * @param localDateTime
     * @return
     */
    public static long getMilllis(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static void main(String[] args) {
        System.out.println(getSecond(LocalDateTime.now()));
        System.out.println(getMilllis(LocalDateTime.now()));
    }
}
