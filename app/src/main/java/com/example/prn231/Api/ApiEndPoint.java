package com.example.prn231.Api;

public class ApiEndPoint {
    // Địa chỉ cơ sở (base URL) của API
    private static final String BASE_URL = "http://103.162.14.116:5000/api/v1/";

    // Các endpoint API
    public static final String LOGIN_GOOGLE = BASE_URL + "auth/login_google";

    //
    public static final String CREATE_GROUP = BASE_URL + "groups";

}
