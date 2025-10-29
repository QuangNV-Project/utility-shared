package com.quangnv.service.utility_shared.util;

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

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
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
