package com.example.prn231.Services;

import com.example.prn231.Api.ApiClient;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Api.MentorApi;

public class MentorServices {
    public static MentorApi getMentorApi(){
        ApiClient client =  new ApiClient(ApiEndPoint.BASE_URL_QUERY);
        return client.getClient().create(MentorApi.class);
    }
}
