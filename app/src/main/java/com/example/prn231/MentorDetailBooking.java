package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.prn231.Adapter.GetMentorDetailSlotsAdapter;
import com.example.prn231.Api.GroupApi;
import com.example.prn231.Api.ScheduleApi;
import com.example.prn231.Api.SlotApi;
import com.example.prn231.Api.SubjectApi;
import com.example.prn231.Model.Group;
import com.example.prn231.Model.MentorSlotRequestBody;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;
import com.example.prn231.Model.Subject;
import com.example.prn231.Services.GroupServices;
import com.example.prn231.Services.MentorServices;
import com.example.prn231.Services.ScheduleServices;
import com.example.prn231.Services.SubjectServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentorDetailBooking extends AppCompatActivity {
    ScheduleApi scheduleServices;
    GroupApi groupServices;
    SubjectApi subServices;
    TextView tvmentorName, tvmentorSlot, tvslotType, tvslotNote, tvMentorDate;
    EditText edStart, edEnd;
    Button btnBooking;
    Spinner spinner;
    private String groupIdChoice;
    Spinner spinnerSub;
    private String subIdChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_detail_booking);

        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        String authToken = "Bearer " + accessToken;

        tvmentorName = findViewById(R.id.tvMentorName);
        tvmentorSlot = findViewById(R.id.tvMentorSLot);
        tvslotType = findViewById(R.id.tvMentorSLotType);
        tvslotNote = findViewById(R.id.tvMentorNote);
        tvMentorDate = findViewById(R.id.tvMentorSLotDate);
        btnBooking = findViewById(R.id.bookingButton);
        edStart = findViewById(R.id.etStartSLot);
        edEnd = findViewById(R.id.etEndSLot);
        spinner = findViewById(R.id.spinnerGroupSelection);
        spinnerSub = findViewById(R.id.spinnerSubSelection);

        groupServices = GroupServices.getGroupApi();
        subServices = SubjectServices.SubjectServices();

        String mentorId = getIntent().getStringExtra("mentorId");
        String slotId = getIntent().getStringExtra("slotId");
        String mentorName = getIntent().getStringExtra("mentorName");
        String slotStart = getIntent().getStringExtra("slotStart");
        String slotEnd = getIntent().getStringExtra("slotEnd");
        String slotDate = getIntent().getStringExtra("slotDate");
        String slotType = getIntent().getStringExtra("slotType");
        String slotNote = getIntent().getStringExtra("slotNote");
        String from = getIntent().getStringExtra("from");
        String groupId = getIntent().getStringExtra("groupId");

        Log.d("slotId", slotId);

        if(groupId != null && !groupId.isEmpty()){
            findViewById(R.id.spinGroupText).setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
        }

        groupIdChoice = groupId;

        Call<ResponseSingelModel<List<Group>>> slotCall = groupServices.getGroups(authToken);
        slotCall.enqueue(new Callback<ResponseSingelModel<List<Group>>>() {
            @Override
            public void onResponse(Call<ResponseSingelModel<List<Group>>> call, Response<ResponseSingelModel<List<Group>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Group> groups = response.body().getValue();

                    if (groups != null) {
                        runOnUiThread(() -> {
                            // Extract group names for displaying and IDs for tracking
                            List<String> groupNames = new ArrayList<>();
                            List<String> groupIds = new ArrayList<>();

                            for (Group group : groups) {
                                groupNames.add(group.getName());
                                groupIds.add(group.getGroupId());
                            }

                            // Create and set adapter
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MentorDetailBooking.this, R.layout.spinner_item, groupNames);
                            adapter.setDropDownViewResource(R.layout.spinner_item);
                            spinner.setAdapter(adapter);

                            // Handle selection to get ID of chosen group
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedGroupId = groupIds.get(position);
                                    groupIdChoice = selectedGroupId; // Store the chosen group ID
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // Optional: handle no selection case
                                }
                            });
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MentorDetailBooking.this, "Failed to load groups.", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseSingelModel<List<Group>>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(MentorDetailBooking.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        Call<ResponseSingelModel<List<Subject>>> subCall = subServices.getSubjects(authToken);
        subCall.enqueue(new Callback<ResponseSingelModel<List<Subject>>>() {
            @Override
            public void onResponse(Call<ResponseSingelModel<List<Subject>>> call, Response<ResponseSingelModel<List<Subject>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Subject> groups = response.body().getValue();

                    if (groups != null) {
                        runOnUiThread(() -> {
                            // Extract group names for displaying and IDs for tracking
                            List<String> groupNames = new ArrayList<>();
                            List<String> groupIds = new ArrayList<>();

                            for (Subject group : groups) {
                                groupNames.add(group.getName());
                                groupIds.add(group.getGroupId());
                            }

                            // Create and set adapter
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MentorDetailBooking.this, R.layout.spinner_item, groupNames);
                            adapter.setDropDownViewResource(R.layout.spinner_item);
                            spinnerSub.setAdapter(adapter);

                            // Handle selection to get ID of chosen group
                            spinnerSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedGroupId = groupIds.get(position);
                                    subIdChoice = selectedGroupId; // Store the chosen group ID
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // Optional: handle no selection case
                                }
                            });
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MentorDetailBooking.this, "Failed to load groups.", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseSingelModel<List<Subject>>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(MentorDetailBooking.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        scheduleServices = ScheduleServices.getScheduleApi();

        ImageView backArrow = findViewById(R.id.back_arrow);
        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent;
                if(from.equals("mentor")){
                    intent = new Intent(getApplicationContext(), MentorDetail.class);
                    intent.putExtra("mentorId", mentorId);
                } else {
                    intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
                    intent.putExtra("groupId", groupId);
                }
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
                Log.d("slotId", slotId);
                MentorSlotRequestBody requestBody = new MentorSlotRequestBody(
                        slotId,
                        subIdChoice,
                        groupIdChoice,
                        edStart.getText().toString(), edEnd.getText().toString());

                Log.d("123123", authToken);

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