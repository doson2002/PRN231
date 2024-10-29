package com.example.prn231.Api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("mentorSkills") // Thay "YOUR_API_ENDPOINT" bằng đường dẫn API
    Call<ResponseBody> uploadSkill(
            @Header("Authorization") String accessToken,
            @Part("skillId") RequestBody skillId,
            @Part List<MultipartBody.Part> productImages
    );
}
