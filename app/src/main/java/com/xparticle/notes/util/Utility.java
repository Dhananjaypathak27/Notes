package com.xparticle.notes.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getCurrentTimestamp(){


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getMonthFromNumber(String monthNumber){
        switch (monthNumber){
            case "01":{
                return "jan";
            }
            case "02":{
                return "feb";
            }
            case "03":{
                return "mar";
            }
            case "04":{
                return "apr";
            }
            case "05":{
                return "may";
            }
            case "06":{
                return "jun";
            }
            case "07":{
                return "jul";
            }
            case "08":{
                return "aug";
            }
            case "09":{
                return "sep";
            }
            case "10":{
                return "oct";
            }
            case "11":{
                return "nov";
            }
            case "dec":{
                return "dec";
            }
            default:{
                return "error";
            }

        }
    }
}
