package com.example.prn231.Services;

import com.example.prn231.Api.ApiClient;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Api.GroupApi;
import com.example.prn231.Api.SubjectApi;

public class SubjectServices {
    public static SubjectApi SubjectServices(){
        ApiClient client =  new ApiClient(ApiEndPoint.BASE_URL_QUERY);
        return client.getClient().create(SubjectApi.class);
    }
}
