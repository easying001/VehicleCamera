package com.easying.vehiclecamera.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by think on 2016/11/5.
 */

public class DateHelper {
    public static final String SECOND = "ss";
    public static final String MINUTE = "mm";
    public static final String HOUR = "HH";
    public static final String DAY = "dd";
    public static final String MONTH = "MM";
    public static final String YEAR = "yyyy";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String MM_DD = "MM-dd";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String MM_SS = "mm:ss";
    public static final String HH_MM = "HH:mm";
    public static final int CALENDAR_SECOND = Calendar.SECOND;
    public static final int CALENDAR_MINUTE = Calendar.MINUTE;
    public static final int CALENDAR_HOUR = Calendar.HOUR_OF_DAY;
    public static final int CALENDAR_DAY = Calendar.DAY_OF_MONTH;
    public static final int CALENDAR_MONTH = Calendar.MONTH;
    public static final int CALENDAR_YEAR = Calendar.YEAR;
    // 秒数常亮
    private static final int S_YEAR = 365 * 24 * 60 * 60;// 年
    private static final int S_MONTH = 30 * 24 * 60 * 60;// 月
    private static final int S_DAY = 24 * 60 * 60;// 天
    private static final int S_HOUR = 60 * 60;// 小时
    private static final int S_MINUTE = 60;// 分钟
    // 星期
    private static String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四",
            "星期五", "星期六" };

    // --------------------------------------------当天
    // 工具-------------------------------------------------
    /**
     * 得到当前年
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.YEAR);
    }

    /**
     * 得到当前月
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当月第几号(当天)
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到当前日期的字符串
     *
     * @return
     */
    public static String getCurrentStringTime(String format) {
        return getStringTime(format, System.currentTimeMillis());
    }

    /**
     * 得到当前Calendar
     *
     * @return
     */
    public static Calendar getCurrentCalendar() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return c;
    }

    /**
     * 得到时分秒
     *
     * @return int[]=3个: hms
     */
    public static int[] getCurrentHourMinuteSecond() {
        Calendar c = getCurrentCalendar();
        int[] hms = { changeTimeTo12(c.get(Calendar.HOUR_OF_DAY)),
                c.get(Calendar.MINUTE), c.get(Calendar.SECOND) };
        return hms;
    }

    /**
     * 当前秒
     *
     * @return
     */
    public static int getCurrentSecond() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.SECOND);
    }

    /**
     * 当前分
     *
     * @return
     */
    public static int getCurrentMinute() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.MINUTE);

    }

    /**
     * 当前时-12小时制
     *
     * @return
     */
    public static int getCurrentHourFor12() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.HOUR);
    }

    /**
     * 当前时-24小时制
     *
     * @return
     */
    public static int getCurrentHourFor24() {
        Calendar c = getCurrentCalendar();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 当前是否上午
     *
     * @return
     */
    public static boolean isAM() {
        return getCurrentCalendar().get(Calendar.AM_PM) == Calendar.AM;
    }

    /**
     * 当前是否下午
     *
     * @return
     */
    public static boolean isPM() {
        return getCurrentCalendar().get(Calendar.AM_PM) == Calendar.PM;
    }

    // ----------------------当天 工具----------------------

    // ----------------------日期格式化 工具----------------------
    /**
     * 格式化日期时间
     *
     * @param pattern
     *            格式yyyy-MM-dd HH:mm:SS...
     * @return
     */
    public static String getStringTime(String format, long timeMillis) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(new Date(
                timeMillis));
    }

    public static String formatToTen(int num) {
        return String.format("%02d", num);
    }

    public static String format(int year, int month, int day) {
        return String.format("%02d-%02d-%02d", year, month, day);
    }

    // ----------------------日期格式化 工具----------------------
    // ----------------------日期计算 工具----------------------

    /**
     * 按calendarType递增 add 次
     *
     * @param timeInMillis
     * @param calendarType
     *            (Calendar.YEAR Calendar.DAY子类的)
     * @param add
     * @return
     */
    public static Calendar next(long timeInMillis, int calendarType, int add) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.add(calendarType, Math.abs(add));
        return c;
    }

    /**
     * 按calendarType减 remove 次
     *
     * @param timeInMillis
     * @param remove
     * @return
     */
    public static Calendar last(long timeInMillis, int calendarType, int remove) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.add(calendarType, -Math.abs(remove));
        return c;
    }

    /**
     * 得到24份,每小时
     *
     * @param data
     *            (1440分钟,24小时数据)
     */
    public static int[] getMinuteSumFor24Copies(byte[] data) {
        data = Arrays.copyOf(data, data.length + 24);

        List<Integer> bytesFor24 = new ArrayList<Integer>();
        int step = 0;
        int length = data.length;
        for (int i = 0; i < length; i++) {
            step += data[i] & 0xff;
            if (i != 0 && i % 60 == 0) {
                bytesFor24.add(step);
                step = 0;
            }
        }
        int[] d = new int[bytesFor24.size()];
        for (int i = 0; i < bytesFor24.size(); i++) {
            d[i] = bytesFor24.get(i);
        }

        return d;
    }

    /**
     * 得到24份,每小时
     *
     * @param data
     *            (1440分钟,24小时数据)
     */
    public static List<Integer> get2MinuteSumFor24Copies(byte[] data) {
        List<Integer> bytesFor24 = new ArrayList<Integer>();
        int step = 0;
        int length = data.length;
        for (int i = 0; i < length; i++) {
            step += data[i] & 0xff;
            if (i != 0 && i % 59 == 0) {
                bytesFor24.add(step);
                step = 0;
            }
        }
        return bytesFor24;
    }

    /**
     * 某年某月有几天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getdays(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year - 1900, month, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 某年某月1号是星期几
     *
     * @param year
     * @param month
     * @return
     */
    public static int getWeek(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 0);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 得到某天星期几
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getDayOfWeek(int year, int month, int day) {
        Calendar time = Calendar.getInstance();
        time.set(year, month - 1, day);
        return time.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 得到某天星期几(中文)
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getDayOfWeekString(int year, int month, int day) {
        Calendar time = Calendar.getInstance();
        time.set(year, month - 1, day);
        return weekDays[time.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 得到某天星期几(自定义)
     *
     * @param year
     * @param month
     * @param day
     * @param weekDays
     *            (星期日~星期六)
     * @return
     */
    public static String getDayOfWeekString(int year, int month, int day,
                                            String[] weekDays) {
        Calendar time = Calendar.getInstance();
        time.set(year, month - 1, day);
        return weekDays[time.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 24小时制转换12小时制
     *
     * @param h_24
     * @return
     */
    public static int changeTimeTo12(int h_24) {
        if (h_24 == 12) {
            return h_24;
        } else if (h_24 == 0) {
            return 12;
        }
        Date d = null;
        try {
            d = (Date) new SimpleDateFormat("HH")
                    .parseObject(formatToTen(h_24));
        } catch (ParseException e) {
            e.printStackTrace();
            return h_24;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.HOUR);
    }

    /**
     * 12小时制转换24小时制
     *
     * @param h_12
     *            传入12小时制
     * @param isAM
     *            是上午?
     * @return
     */
    public static int changeTimeTo24(int h_12, boolean isAM) {
        if (!isAM) {
            h_12 = 12 + h_12;
        }

        Date d = null;
        try {
            d = (Date) new SimpleDateFormat("HH")
                    .parseObject(formatToTen(h_12));
        } catch (ParseException e) {
            e.printStackTrace();
            return h_12;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    // ----------------------日期计算 工具----------------------
    // ----------------------日期类型转换 工具----------------------

    /**
     * 类型转换为String类型
     *
     * @param data
     *            Date类型的时间
     * @param formatType
     *            格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * long类型转换为String类型
     *
     * @param currentTime
     *            要转换的long类型的时间
     * @param formatType
     *            要转换的string类型的时间格式
     * @return
     */
    public static String longToString(long currentTime, String formatType) {
        String strTime = "";
        Date date = longToDate(currentTime, formatType);// long类型转成Date类型
        strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * string类型转换为date类型
     *
     * @param strTime
     *            要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd
     *            HH:mm:ss//yyyy年MM月dd日HH时mm分ss秒，
     * @param formatType
     *            的时间格式必须要与formatType的时间格式相同
     * @return
     */
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * long转换为Date类型
     *
     * @param currentTime
     *            要转换的long类型的时间
     * @param formatType
     *            要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * string类型转换为long类型
     *
     * @param strTime
     *            要转换的String类型的时间, strTime的时间格式和formatType的时间格式必须相同
     * @param formatType
     *            时间格式
     * @return
     */
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型
     *
     * @param date
     *            要转换的date类型的时间
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp
     *            时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr = null;
        if (timeGap > S_YEAR) {
            timeStr = timeGap / S_YEAR + "年前";
        } else if (timeGap > S_MONTH) {
            timeStr = timeGap / S_MONTH + "个月前";
        } else if (timeGap > S_DAY) {// 1天以上
            timeStr = timeGap / S_DAY + "天前";
        } else if (timeGap > S_HOUR) {// 1小时-24小时
            timeStr = timeGap / S_HOUR + "小时前";
        } else if (timeGap > S_MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / S_MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 歌曲long时间转换
     * @param duration
     * @return
     */
    public static String getMusicTime(long duration) {
        int secondAll = (int) (duration / 1000);
        int minute = secondAll / 60;
        int second = secondAll % 60;
        return String.format("%02d:%02d", minute, second);
    }
}
