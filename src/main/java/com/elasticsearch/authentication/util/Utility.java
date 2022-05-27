package com.elasticsearch.authentication.util;



import com.elasticsearch.authentication.dto.AgentResponse;
import com.elasticsearch.authentication.exception.LoggingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Stream;

public class Utility {

    public static String getStringJoinerObj(String delimiter, String... values) {
        return String.join(delimiter, values).toLowerCase();
    }

    public static String getJoiningString(String... values) {
        StringJoiner joiner = new StringJoiner(Constant.DELIMITER, "[", "]");
        Stream.of(values).forEach(joiner::add);
        return joiner.toString();
    }


    public static AgentResponse createResponseObject(String status, String message, Object data) {
        AgentResponse response = new AgentResponse();
        response.setStatus(status);
        response.setData(data);
        response.setMessage(message);
        return response;
    }

//    public static DashboardResponse createResponse(String status, String statusCode, String message, Object response){
//        DashboardResponse dashboardResponse=new DashboardResponse();
//        dashboardResponse.setStatus(status);
//        dashboardResponse.setStatusCode(statusCode);
//        dashboardResponse.setMessage(message);
//        dashboardResponse.setResponse(response);
//        return dashboardResponse;
//    }

    /*public static Function<Instant, String> UTC_TO_IST_TIMESTAMP = (instant) -> {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(sdf.parse(instant.toString()));
        } catch (Exception e) {
            return LocalDateTime.now().toString();
        }
    };*/

    /*public static Function<String, String> UTC_STRING_TO_IST_STRING = (utcTime) -> {

        return LocalDateTime.ofInstant(Instant.parse(utcTime), ZoneId.of("Asia/Kolkata")).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(sdf.parse(utcTime));
        } catch (Exception e) {
            return LocalDateTime.now().toString();
        }
    };*/

    public static Function<String, String> UTC_STRING_TO_IST_STRING = (utcTime) ->
    {
        try {
            return LocalDateTime.ofInstant(Instant.parse(utcTime), ZoneId.of("Asia/Kolkata")).toString();
        } catch (Exception ex) {
            return LocalDateTime.now().toString();
        }
    };

    public static Function<Instant, String> UTC_TO_IST_TIMESTAMP = (instant) -> UTC_STRING_TO_IST_STRING.apply(instant.toString());

    public static Function<String, String> IST_STRING_TO_UTC_STRING = (istTime) -> {

        if (istTime == null)
            return LocalDateTime.now().toString();

        String format = "yyyy-MM-dd'T'hh:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date parse = simpleDateFormat.parse(istTime);

            return dateToString(parse, format, "UTC") + "Z";

        } catch (ParseException e) {
            return istTime + "Z";
        }
    };

    public static String dateToString(Date date, String format, String timeZone) {

        // create SimpleDateFormat object
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        // default system timezone if passed null or empty
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {

            timeZone = Calendar.getInstance().getTimeZone().getID();

        }

        // set timezone to SimpleDateFormat
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        // return Date in required format with timezone as String
        return sdf.format(date);

    }

    public static String getHourFromTime(String keyAsString) {
        String istTimestamp = UTC_STRING_TO_IST_STRING.apply(keyAsString);
        return LocalDateTime.parse(istTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME).getHour() + ":00";
    }


    public static void isTrue(boolean expression, String message) throws LoggingException {

        if (expression) {
            throw new LoggingException(Constant.STATUS_FAILURE, message);
        }
    }
}
