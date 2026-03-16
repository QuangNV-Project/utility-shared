package com.quangnv.service.utility_shared.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.TSFBuilder;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {
    public static final String FORMAT_YEAR_MONTH = "yyyyMM";
    public static final String FORMAT_YEAR_MONTH_DAY = "yyyyMMdd";
    public static final String FORMAT_YEAR_MONTH_DAY_HOUR = "yyyyMMddHH";
    public static final String FORMAT_YEAR_MONTH_DAY_MINUTE = "yyyyMMddHHmm";
    public static final String FORMAT_YEAR_MONTH_DAY_SEC = "yyyyMMddHHmmss";
    public static final String FORMAT_YEAR_MONTH_DAY_SEC_HYPHEN = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_TO_STRING = "yyyy-MM-dd_HH-mm-ss";
    public static final String FORMAT_YEAR_MONTH_DAY_HYPHEN = "yyyy-MM-dd";
    public static final String FORMAT_MINUTE_DAY_MONTH_YEAR_SLASH = "HH:mm dd/MM/yyyy";
    public static final String FORMAT_DAY_MONTH_YEAR_SEC_SLASH = "dd/MM/yyyy HH:mm:ss";

    public static final String FORMAT_DAY_MONTH_YEAR_SLASH = "dd/MM/yyyy";
    public static final String FORMAT_MONTH_YEAR_SLASH = "MM/yyyy";
    public static final String FORMAT_DAY_MONTH_YEAR_SLASH_HOUR_MINUTE = "dd/MM/yyyy HH:mm";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_HOUR_MINUTE = "HH:mm";
    public static final String FORMAT_DAY_MONTH_YEAR_SLASH_STRING = "dd-MM-yyyy_HH-mm-ss";

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Get the start time of the month for the given date
     * @param date
     * @return
     */
    public static Date getStartOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get the end time of the month for the given date
     * @param date
     * @return
     */
    public static Date getEndOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // set ngày về ngày cuối cùng của tháng
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     *
     * @param date
     * @param format
     * @return
     */
    public static String toString(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date toDate(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static LocalTime convertDateToLocalTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null!");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

}
