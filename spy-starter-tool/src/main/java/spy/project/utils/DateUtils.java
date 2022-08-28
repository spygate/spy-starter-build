package spy.project.utils;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class DateUtils {

    public static final String Date_Format_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String Date_Format_YMD = "yyyy-MM-dd";
    public static final String Date_Format_HMS = "HH:mm:ss";
    public static final String Date_Format_YMD2 = "yyMMdd";
    public static final String Date_INT_YMD = "yyyyMMdd";
    public static final String Date_INT_HMS = "HHmmss";

    public static CurrentDateTime currentDateTime;

    public static CurrentDateTime getCurrent() {
        currentDateTime = new CurrentDateTime();
        return currentDateTime;
    }

    public static String format(String pattern) {
        return currentDateTime.format(pattern);
    }

    public static Integer formatIntValue(String pattern) {
        return currentDateTime.formatIntValue(pattern);
    }


    @Data
    public static class CurrentDateTime {

        public LocalDateTime now;

        public CurrentDateTime() {
            this.now = LocalDateTime.now();
        }

        public String format(String pattern) {
            return DateTimeFormatter.ofPattern(pattern).format(now);
        }

        public Integer formatIntValue(String intPattern) {
            return Integer.valueOf(DateTimeFormatter.ofPattern(intPattern).format(now));
        }

    }

    /**
     * 获取当天都秒数
     */
    public static String secondDay(LocalDateTime start) {
        String seconds = String.valueOf(start.getSecond() + start.getMinute() * 60 + start.getHour() * 3600);
        seconds = String.join("", Collections.nCopies(5 - seconds.length(), "0")) + seconds;
        return seconds;
    }


}
