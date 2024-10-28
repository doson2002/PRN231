package com.example.prn231.Api;

public class ApiEndPoint {
    // Địa chỉ cơ sở (base URL) của API
    public static final String BASE_URL_LOGIN = "http://103.162.14.116:5000/api/v1/";
    public static final String BASE_URL_COMMAND = "http://103.162.14.116:4000/api/v1/";
    public static final String BASE_URL_QUERY = "http://103.162.14.116:3000/api/v1/";

    // Các endpoint API
    public static final String LOGIN_GOOGLE = BASE_URL_LOGIN + "auth/login_google";
    //
    public static final String CREATE_GROUP = BASE_URL_COMMAND + "groups";
    public static final String GET_ALL_GROUP = BASE_URL_QUERY + "groups";
    public static final String GET_GROUP_DETAIL = BASE_URL_QUERY + "groups";
    public static final String ADD_MEMBER_TO_GROUP = BASE_URL_COMMAND + "groups/member";
    public static final String ADD_MENTOR_TO_GROUP = BASE_URL_COMMAND + "groups/mentor";



    //User
    public static final String GET_USER_BY_EMAIL = BASE_URL_QUERY + "user";
    public static final String GET_USER_BY_ID = BASE_URL_QUERY + "user";
    //project
    public static final String CREATE_PROJECT = BASE_URL_COMMAND + "projects";

    //schedule
    public static final String GET_SCHEDULE_BOOKED = BASE_URL_QUERY + "schedules";





    //Feedback
    public static final String CREATE_FEEDBACK = BASE_URL_COMMAND + "feedbacks";

    //Slots
    public static final String GET_ALL_SLOTS = BASE_URL_QUERY + "slots";
}
