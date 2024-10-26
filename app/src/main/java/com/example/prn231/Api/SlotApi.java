package com.example.prn231.Api;

import com.example.prn231.Model.Mentor;
import com.example.prn231.Model.MentorDetail;
import com.example.prn231.Model.ResponseModel;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SlotApi {
    String MENTOR = "slots";

    @GET(MENTOR)
    Call<ResponseSingelModel<List<com.example.prn231.Model.Schedule>>> getAllMentorSlot(
            @Header("Authorization") String authToken,
            @Query("mentorId") String mentorId,
            @Query("Date") String date
    );
}
