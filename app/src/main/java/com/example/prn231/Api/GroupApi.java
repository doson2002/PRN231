package com.example.prn231.Api;

import com.example.prn231.Model.Group;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GroupApi {
    String GROUP = "groups";

    @GET(GROUP)
    Call<ResponseSingelModel<List<Group>>> getGroups(
            @Header("Authorization") String authToken
    );
}
