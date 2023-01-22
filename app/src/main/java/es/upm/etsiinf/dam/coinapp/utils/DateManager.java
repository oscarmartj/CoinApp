package es.upm.etsiinf.dam.coinapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    public static String changeFormatDate(String dateString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = inputFormat.parse(dateString);
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = outputFormat.format(date);
        formattedDate = formattedDate.substring(0,1).toUpperCase() + formattedDate.substring(1);

        long currentTime = System.currentTimeMillis();
        long diffInMilliseconds = currentTime - date.getTime();
        long diffInSeconds = diffInMilliseconds / 1000;
        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;
        long diffInDays = diffInHours / 24;
        long diffInYears = diffInDays / 365;

        String aboutTime;
        if(diffInYears == 1) {
            aboutTime = "about " + diffInYears + " year";
        }else{
            aboutTime = "about " + diffInYears + " years";
        }
        return formattedDate + " (" + aboutTime + ")";
    }




}
