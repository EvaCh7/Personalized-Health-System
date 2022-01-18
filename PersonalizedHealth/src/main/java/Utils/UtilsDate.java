/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Randevouz;

/**
 *
 * @author kokol
 */
public class UtilsDate {

    public static boolean isFutureDate(String pDateString) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pDateString);
            return new Date().before(date);
        } catch (ParseException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    

    public static String getDate(String date_time) {
        String[] str = date_time.split(" ");
        return str[0];

    }

    public static String getTime(String date_time) {
        String[] str = date_time.split(" ");
        return str[1];

    }

    public static boolean isTimeBetween(String time, String from_time, String to_time) {
        LocalTime from = LocalTime.parse(from_time);
        LocalTime to = LocalTime.parse(to_time);
        LocalTime _time = LocalTime.parse(time);
        if (_time.isAfter(from) && _time.isBefore(to)) {
            return true;

        }
        return false;

    }

    public static boolean isDateBetween(String curDate, String fromDate, String toDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date current_date = sdf.parse(curDate);
        Date from_date = sdf.parse(fromDate);
        Date to_date = sdf.parse(toDate);

        if (current_date.after(from_date) && current_date.before(to_date)) {
            return true;
        }
        return false;

    }

    public static boolean is4Hours(String time) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date_rand = formatter.parse(time);
        Date date_now = new Date(System.currentTimeMillis());

        long difference_In_Time = date_rand.getTime() - date_now.getTime();
        long hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
        long minutes = (difference_In_Time / (1000 * 60)) % 60;

        if ((hours == 4 && minutes == 0) || (hours < 4)) {
            return true;
        } else {
            return false;
        }
    }

}
