package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.Adapter.GetMentorDetailSkillAdapter;
import com.example.prn231.Adapter.GetMentorDetailSlotsAdapter;
import com.example.prn231.Api.MentorApi;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;
import com.example.prn231.Services.MentorServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentorDetail extends AppCompatActivity {
    MentorApi mentorServices;
    RecyclerView recyclerViewSkills;
    GetMentorDetailSkillAdapter skillAdapter;

    List<Schedule> scheduleList = new ArrayList<>();
    RecyclerView recyclerViewsSchedules;
    GetMentorDetailSlotsAdapter scheduleAdapter;

    TextView pointTextView, dateJoinTextView, nameTextView, emailTextView, mentorSkillTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_detail);

        // Get the mentor ID and other details from the Intent
        String mentorId = getIntent().getStringExtra("mentorId");

        nameTextView = findViewById(R.id.tvMentorName);
        emailTextView = findViewById(R.id.tvMentorEmail);
        pointTextView = findViewById(R.id.tvMentorPoints);
        dateJoinTextView = findViewById(R.id.tvMentorDateJoin);
        mentorSkillTitle = findViewById(R.id.tvMentorSkillsTitle);
        recyclerViewSkills = findViewById(R.id.rvSkills);

        ImageView backArrow = findViewById(R.id.back_arrow);
        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent = new Intent(getApplicationContext(), MentorPage.class);
                startActivity(intent);
                finish(); // This will navigate back to the previous activity in the stack
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        //String authToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJzdHJpbmciLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiIwIiwiUm9sZSI6IjAiLCJVc2VySWQiOiIzNDFhODI3NC0wYWMyLTQxMmEtOTgyNC1lMzA3YThhMzEwZDAiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic3RyaW5nIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmVkIjoiMTAvMTgvMjAyNCAwMzowNTozOCIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWVpZGVudGlmaWVyIjoiMzQxYTgyNzQtMGFjMi00MTJhLTk4MjQtZTMwN2E4YTMxMGQwIiwiZXhwIjoxNzI5MjI0MDM4LCJpc3MiOiJodHRwOi8vMTAzLjE2Mi4xNC4xMTY6ODA4MCIsImF1ZCI6Imh0dHA6Ly8xMDMuMTYyLjE0LjExNjo4MDgwIn0.Cf4X090k-UtHrh3d1-ieACpdus3JZ5HFEGmUNzoTvCY" ; // Replace with actual token

        String authToken = "Bearer " + accessToken;

        mentorServices = MentorServices.getMentorApi();
        Call<ResponseSingelModel<com.example.prn231.Model.MentorDetail>> call = mentorServices.getMentorDetail(mentorId, authToken);
        call.enqueue(new Callback<ResponseSingelModel<com.example.prn231.Model.MentorDetail>>() {
            @Override
            public void onResponse(Call<ResponseSingelModel<com.example.prn231.Model.MentorDetail>> call, Response<ResponseSingelModel<com.example.prn231.Model.MentorDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.prn231.Model.MentorDetail mentor = response.body().getValue();

                    if (mentor != null) {
                        runOnUiThread(() -> {
                            // Update points
                            nameTextView.setText(mentor.getFullName());
                            emailTextView.setText(mentor.getEmail());
                            Integer points = mentor.getPoint(); // Giả sử getPoint() trả về Integer
                            pointTextView.setText(points.toString()); // Chuyển đổi thành chuỗi
                            dateJoinTextView.setText(mentor.getCreatedOnUtc());

                            if (mentor.getSkills().size() == 0) {
                                mentorSkillTitle.setVisibility(View.GONE);  // Hides the view and removes it from the layout
                            } else {
                                mentorSkillTitle.setVisibility(View.VISIBLE);  // Shows the view
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(MentorDetail.this);
                            recyclerViewSkills.setLayoutManager(layoutManager);
                            skillAdapter = new GetMentorDetailSkillAdapter(mentor.getSkills());
                            recyclerViewSkills.setAdapter(skillAdapter);
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MentorDetail.this, "", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseSingelModel<com.example.prn231.Model.MentorDetail>> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
            }
        });

        //scheduleList
        scheduleList.add(new Schedule("1", "09:00", "11:00", 0, true));
        scheduleList.add(new Schedule("2", "09:00", "11:00", 1, false));
        scheduleList.add(new Schedule("3", "09:00", "11:00", 1, true));
        scheduleList.add(new Schedule("4", "09:00", "11:00", 0, false));
        scheduleList.add(new Schedule("5", "09:00", "11:00", 0, false));
        scheduleList.add(new Schedule("6", "09:00", "11:00", 1, true));

        recyclerViewsSchedules = findViewById(R.id.rvSlots);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MentorDetail.this);
        recyclerViewsSchedules.setLayoutManager(layoutManager);
        scheduleAdapter = new GetMentorDetailSlotsAdapter(scheduleList);
        recyclerViewsSchedules.setAdapter(scheduleAdapter);
    }
}