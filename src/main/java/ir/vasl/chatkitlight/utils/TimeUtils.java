package ir.vasl.chatkitlight.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final String TAG = "TimeUtils";

    public static String timeToHumanTime(long timeAgo) {

        long cur_time = (Calendar.getInstance().getTimeInMillis());
        long time_elapsed = cur_time - timeAgo;

        //noinspection UnnecessaryLocalVariable
        long seconds = time_elapsed;
        int minutes = Math.round((float) time_elapsed / 60);
        int hours = Math.round((float) time_elapsed / 3600);
        int days = Math.round((float) time_elapsed / 86400);
        int weeks = Math.round((float) time_elapsed / 604800);
        int months = Math.round((float) time_elapsed / 2600640);
        int years = Math.round((float) time_elapsed / 31207680);

        // Seconds
        if (seconds <= 60) {
            return "just now";
        }
        //Minutes
        else if (minutes <= 60) {
            if (minutes == 1) {
                return "one minute ago";
            } else {
                return minutes + " minutes ago";
            }
        }
        //Hours
        else if (hours <= 24) {
            if (hours == 1) {
                return "an hour ago";
            } else {
                return hours + " hrs ago";
            }
        }
        //Days
        else if (days <= 7) {
            if (days == 1) {
                return "yesterday";
            } else {
                return days + " days ago";
            }
        }
        //Weeks
        else if (weeks <= 4.3) {
            if (weeks == 1) {
                return "a week ago";
            } else {
                return weeks + " weeks ago";
            }
        }
        //Months
        else if (months <= 12) {
            if (months == 1) {
                return "a month ago";
            } else {
                return months + " months ago";
            }
        }
        //Years
        else {
            if (years == 1) {
                return "one year ago";
            } else {
                return years + " years ago";
            }
        }
    }

    public static long getCurrTimeStamp() {
        return System.currentTimeMillis();
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getReadableHumanTime(String inputTime) {

        if (!isNumeric(inputTime))
            return inputTime;

        long inputTimeLong = Long.parseLong(inputTime);

        return timeToHumanTime(inputTimeLong);

    }

    public static String getCurrTime() {

        try {
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
            Date date = new Date(currentTime);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "--:--";
        }
    }

}
