package com.example.prn231.Api;

import com.example.prn231.Model.Mentor;
import com.example.prn231.Model.MentorDetail;
import com.example.prn231.Model.ResponseModel;
import com.example.prn231.Model.ResponseSingelModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
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

    @GET("mentors/{mentorId}") // Đường dẫn với tham số đường dẫn
    Call<ResponseSingelModel<MentorDetail>> getMentorDetail(
            @Path("mentorId") String mentorId, // Tham số đường dẫn
            @Header("Authorization") String authToken
    );

}
