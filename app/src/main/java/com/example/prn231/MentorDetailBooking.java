package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prn231.Api.ScheduleApi;
import com.example.prn231.Model.MentorSlotRequestBody;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;
import com.example.prn231.Services.MentorServices;
import com.example.prn231.Services.ScheduleServices;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentorDetailBooking extends AppCompatActivity {
    ScheduleApi scheduleServices;
    TextView tvmentorName, tvmentorSlot, tvslotType, tvslotNote, tvMentorDate;
    Button btnBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_detail_booking);

        tvmentorName = findViewById(R.id.tvMentorName);
        tvmentorSlot = findViewById(R.id.tvMentorSLot);
        tvslotType = findViewById(R.id.tvMentorSLotType);
        tvslotNote = findViewById(R.id.tvMentorNote);
        tvMentorDate = findViewById(R.id.tvMentorSLotDate);
        btnBooking = findViewById(R.id.bookingButton);

        scheduleServices = ScheduleServices.getScheduleApi();

        String mentorId = getIntent().getStringExtra("mentorId");
        String slotId = getIntent().getStringExtra("slotId");
        String mentorName = getIntent().getStringExtra("mentorName");
        String slotStart = getIntent().getStringExtra("slotStart");
        String slotEnd = getIntent().getStringExtra("slotEnd");
        String slotDate = getIntent().getStringExtra("slotDate");
        String slotType = getIntent().getStringExtra("slotType");
        String slotNote = getIntent().getStringExtra("slotNote");

        ImageView backArrow = findViewById(R.id.back_arrow);
        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent = new Intent(getApplicationContext(), MentorDetail.class);
                intent.putExtra("mentorId", mentorId);
                startActivity(intent);
                finish(); // This will navigate back to the previous activity in the stack
            }
        });

        tvmentorName.setText(mentorName);
        tvmentorSlot.setText(slotStart + "-" + slotEnd);
        tvMentorDate.setText(slotDate);
        tvslotType.setText(slotType);
        tvslotNote.setText(slotNote);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MentorSlotRequestBody requestBody = new MentorSlotRequestBody(
                        slotId,
                        "db101383-862c-4966-9674-9d5cda0670d1",
                        "0272f656-2fe3-4ec0-4667-08dcf5d7a659",
                        slotStart, slotEnd);

                SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("accessToken","");
                String authToken = "Bearer " + accessToken;

                Call<ResponseSingelModel<String>> call = scheduleServices.bookMentorSlot(authToken, requestBody);
                call.enqueue(new Callback<ResponseSingelModel<String>>() {
                    @Override
                    public void onResponse(Call<ResponseSingelModel<String>> call, Response<ResponseSingelModel<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String message = response.body().getValue();
                            Toast.makeText(MentorDetailBooking.this, message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MentorDetail.class);
                            intent.putExtra("mentorId", mentorId);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("API Error", "Code: " + response.code() + " Body: " + errorBody);
                                Toast.makeText(MentorDetailBooking.this,
                                        "Error: " + response.code() + " - " + errorBody,
                                        Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseSingelModel<String>> call, Throwable t) {
                        Log.e("API Error", "Network failure", t);
                        Toast.makeText(MentorDetailBooking.this,
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}