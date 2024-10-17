package com.example.prn231.Api;

public class ApiEndPoint {
    // Địa chỉ cơ sở (base URL) của API
    private static final String BASE_URL_LOGIN = "http://103.162.14.116:5000/api/v1/";
    private static final String BASE_URL_COMMAND = "http://103.162.14.116:4000/api/v1/";
    private static final String BASE_URL_QUERY = "http://103.162.14.116:3000/api/v1/";

    // Các endpoint API
    public static final String LOGIN_GOOGLE = BASE_URL_LOGIN + "auth/login_google";
    //
    public static final String CREATE_GROUP = BASE_URL_COMMAND + "groups";
    public static final String GET_ALL_GROUP = BASE_URL_QUERY + "groups";


}
