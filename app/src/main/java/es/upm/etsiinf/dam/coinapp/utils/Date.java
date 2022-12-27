package es.upm.etsiinf.dam.coinapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date {
    private final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    SimpleDateFormat simpleDateFormat;

    public Date () {
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    public java.util.Date parseDateString(String dateString) throws ParseException {
        return simpleDateFormat.parse(dateString);
    }
    public String formatDate(Date date) {
        return simpleDateFormat.format(date);
    }




}
