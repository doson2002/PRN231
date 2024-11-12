package com.example.prn231.Api;

public class ApiEndPoint {
    // Địa chỉ cơ sở (base URL) của API
    public static final String BASE_URL_LOGIN = "http://103.162.14.116:8080/auth-api/v1/";
    public static final String BASE_URL_COMMAND = "http://103.162.14.116:8080/command-api/v1/";
    public static final String BASE_URL_QUERY = "http://103.162.14.116:8080/query-api/v1/";

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
    public static final String GET_SCHEDULE_BOOKED_FOR_MENTOR = BASE_URL_QUERY + "schedules/mentor";

    public static final String CHANGE_STATUS_SCHEDULE = BASE_URL_COMMAND + "schedules/change-stats";






    //Feedback
    public static final String CREATE_FEEDBACK = BASE_URL_COMMAND + "feedbacks";

    //Skill for mentor
    public static final String GET_MENTOR_DETAIL = BASE_URL_QUERY + "mentors";
    public static final String GET_ALL_SKILL = BASE_URL_QUERY + "skills";
    public static final String CREATE_MENTOR_SKILL = BASE_URL_COMMAND + "mentorSkills";


// subject
public static final String GET_ALL_SUBJECT = BASE_URL_QUERY + "subjects";




    //Slots
    public static final String GET_ALL_SLOTS = BASE_URL_QUERY + "slots";


    //login user name password
    public static final String LOGIN_EMAIL_PASSWORD = BASE_URL_LOGIN + "auth/login";
}
