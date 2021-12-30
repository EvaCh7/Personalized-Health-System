/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(pDateString);
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

    public  static boolean isTimeBetween(String time, String from_time, String to_time) {
        LocalTime from = LocalTime.parse(from_time);
        LocalTime to = LocalTime.parse(to_time);
        LocalTime _time = LocalTime.parse(time);
        if (_time.isAfter(from) && _time.isBefore(to)) {
            return true;

        }
        return false;

    }

}
