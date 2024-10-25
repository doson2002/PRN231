package com.example.prn231.Api;



import com.example.prn231.Model.MentorSlotRequestBody;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ScheduleApi {
    String SCHEDULE = "schedules";

    @POST(SCHEDULE)
    Call<ResponseSingelModel<String>> bookMentorSlot(
            @Header("Authorization") String authToken,
            @Body MentorSlotRequestBody mentorSlotRequestBody
    );
}


