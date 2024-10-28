package com.example.prn231.Api;

import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SubjectApi {
    String SUBJECT = "subjects";

    @GET(SUBJECT)
    Call<ResponseSingelModel<List<Subject>>> getSubjects(
            @Header("Authorization") String authToken
    );
}
