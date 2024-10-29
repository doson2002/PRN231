package com.example.prn231.Services;

import com.example.prn231.Api.ApiClient;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Api.GroupApi;
import com.example.prn231.Api.SlotApi;

public class GroupServices {
    public static GroupApi getGroupApi(){
        ApiClient client =  new ApiClient(ApiEndPoint.BASE_URL_QUERY);
        return client.getClient().create(GroupApi.class);
    }
}
