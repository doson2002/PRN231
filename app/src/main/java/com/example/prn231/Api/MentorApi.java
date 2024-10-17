package com.example.prn231.Api;

import com.example.prn231.Model.Mentor;
import com.example.prn231.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MentorApi {
    String MENTOR = "mentors";

    @GET(MENTOR)
    Call<ResponseModel<Mentor>> getAllMentor(
            @Header("Authorization") String authToken,
            @Query("serchTerm") String serchTerm,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );
}
