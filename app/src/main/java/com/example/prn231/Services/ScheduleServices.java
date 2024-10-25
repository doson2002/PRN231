package com.example.prn231.Services;

import com.example.prn231.Api.ApiClient;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Api.MentorApi;
import com.example.prn231.Api.ScheduleApi;

public class ScheduleServices {
    public static ScheduleApi getScheduleApi(){
        ApiClient client =  new ApiClient(ApiEndPoint.BASE_URL_COMMAND);
        return client.getClient().create(ScheduleApi.class);
    }
}
