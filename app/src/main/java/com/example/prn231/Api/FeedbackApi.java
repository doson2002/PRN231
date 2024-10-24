package com.example.prn231.Api;

import com.example.prn231.Model.Feedback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FeedbackApi {
    @POST("feedbacks")
    Call<Void> submitFeedback(
            @Header("Authorization") String token,
            @Body Feedback feedback
    );
}