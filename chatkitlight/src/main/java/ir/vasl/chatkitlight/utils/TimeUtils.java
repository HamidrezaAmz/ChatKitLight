package ir.vasl.chatkitlight.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
            return "چند لحظه پیش";
        }
        //Minutes
        else if (minutes <= 60) {
            if (minutes == 1) {
                return "یک دقیقه پیش";
            } else {
                return minutes + " دقیقه قبل";
            }
        }
        //Hours
        else if (hours <= 24) {
            if (hours == 1) {
                return "یک ساعت پیش";
            } else {
                return hours + " ساعت قبل";
            }
        }
        //Days
        else if (days <= 7) {
            if (days == 1) {
                return "دیروز";
            } else {
                return days + " روز قبل";
            }
        }
        //Weeks
        else if (weeks <= 4.3) {
            if (weeks == 1) {
                return "یک هفته پیش";
            } else {
                return weeks + " هفته قبل";
            }
        }
        //Months
        else if (months <= 12) {
            if (months == 1) {
                return "یک ماه پیش";
            } else {
                return months + " ماه قبل";
            }
        }
        //Years
        else {
            if (years == 1) {
                return "یک سال پیش";
            } else {
                return years + " سال قبل";
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

    public static String getTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
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

    public static String convertDate(long timeStamp) {

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(timeStamp);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String formattedYear = String.format(Locale.US, "%02d", year);
        String formattedMonth = String.format(Locale.US, "%02d", month);
        String formattedDay = String.format(Locale.US, "%02d", day);

        return new StringBuilder().append(formattedYear).append("/").append(formattedMonth).append("/").append(formattedDay).toString();
    }

    public static String convertDateToTime(long timeStamp) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeStamp);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        String formattedHours = String.format(Locale.US, "%02d", hours);
        String formattedMinutes = String.format(Locale.US, "%02d", minutes);

        return new StringBuilder().append(formattedHours).append(":").append(formattedMinutes).toString();
    }

}
