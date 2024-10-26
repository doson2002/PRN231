package com.example.prn231.Services;

import com.example.prn231.Api.ApiClient;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Api.MentorApi;
import com.example.prn231.Api.SlotApi;

public class MentorSlotServices {
    public static SlotApi getMentorSlotApi(){
        ApiClient client =  new ApiClient(ApiEndPoint.BASE_URL_QUERY);
        return client.getClient().create(SlotApi.class);
    }
}
