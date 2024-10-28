package com.example.prn231;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prn231.Api.ApiClient;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Api.FeedbackApi;
import com.example.prn231.Model.Feedback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackActivity extends AppCompatActivity {

    private EditText reviewText;
    private RatingBar rating;
    private Button btn;
    private String TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);

        // Initialize views
        reviewText = findViewById(R.id.editReviewText);
        rating = findViewById(R.id.ratingBar);
        btn = findViewById(R.id.submitReviewButton);

        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");

        TOKEN = "Bearer " + accessToken;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set button click listener
        btn.setOnClickListener(this::submitFeedback);
    }

    private void submitFeedback(View view) {
        String content = reviewText.getText().toString();
        int ratingValue = (int) rating.getRating();
        //change with intent
        String groupId = "A5D93D45-C4BE-4692-41D3-08DCF387EF10";
        //change with intent
        String scheduleId = "BC977A43-425B-4C34-9963-1319BDA17AD8";

        Feedback feedback = new Feedback(groupId, content, scheduleId, ratingValue);

        // Create Retrofit instance using ApiClient
        ApiClient apiClient = new ApiClient(ApiEndPoint.BASE_URL_COMMAND);
        FeedbackApi feedbackApi = apiClient.getClient().create(FeedbackApi.class);

        Call<Void> call = feedbackApi.submitFeedback(TOKEN, feedback);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    //change later after success if want redirecct
                    Toast.makeText(FeedbackActivity.this, "Feedback successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    //change later
                    Toast.makeText(FeedbackActivity.this, "Failed to feedback." + response.message() + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FeedbackActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}