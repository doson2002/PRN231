package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Adapter.MentorOwnSchedules;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Fragment.Mentor.HomeMentorFragment;
import com.example.prn231.Model.Slot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MentorMeetingSchedule extends AppCompatActivity {
    private RecyclerView recyclerViewsSchedules;
    private MentorOwnSchedules scheduleAdapter;
    private Calendar currentCalendar;
    private String currentDate;
    private TextView dateNowSchedule, dateNowScheduleLeft, dateNowScheduleRight;
    private ImageButton nextWeekButton, backWeekButton, nextDayButton, backDayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_meeting_schedule);

        // Initialize views
        initializeViews();

        // Initialize calendar
        currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        // Format the current date for display
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        currentDate = formatter.format(currentCalendar.getTime());

        // Set up back navigation
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeMentorFragment.class);
            startActivity(intent);
            finish();
        });

        // Get authentication token
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", "");
        String authToken = "Bearer " + accessToken;
        String mentorId = sharedPreferences.getString("userId", "");

        // Initialize date display
        updateDateRanges();
        fetchSlotData(authToken, mentorId, currentDate);

        // Set up navigation button listeners
        setupNavigationListeners(authToken, mentorId);
    }

    private void initializeViews() {
        recyclerViewsSchedules = findViewById(R.id.rvSlots);
        dateNowSchedule = findViewById(R.id.dateNowDetail);
        dateNowScheduleLeft = findViewById(R.id.dateNowDetailLeft);
        dateNowScheduleRight = findViewById(R.id.dateNowDetailRight);
        nextWeekButton = findViewById(R.id.nextWeekButton);
        backWeekButton = findViewById(R.id.backWeekButton);
        nextDayButton = findViewById(R.id.nextDayButton);
        backDayButton = findViewById(R.id.backDayButton);
    }

    private void setupNavigationListeners(String authToken, String mentorId) {
        nextWeekButton.setOnClickListener(v -> {
            advanceWeek(true);
            fetchSlotData(authToken, mentorId, currentDate);
        });

        backWeekButton.setOnClickListener(v -> {
            advanceWeek(false);
            fetchSlotData(authToken, mentorId, currentDate);
        });

        nextDayButton.setOnClickListener(v -> {
            advanceDay(true);
            fetchSlotData(authToken, mentorId, currentDate);
        });

        backDayButton.setOnClickListener(v -> {
            advanceDay(false);
            fetchSlotData(authToken, mentorId, currentDate);
        });
    }

    private void advanceWeek(boolean isForward) {
        currentCalendar.add(Calendar.DAY_OF_MONTH, isForward ? 7 : -7);
        updateDateRanges();
    }

    private void advanceDay(boolean isForward) {
        currentCalendar.add(Calendar.DAY_OF_MONTH, isForward ? 1 : -1);
        updateDateRanges();
    }


    private void updateDateRanges() {
        // Clone the current calendar to avoid modifying the original instance
        Calendar tempCal = (Calendar) currentCalendar.clone();

        // Get the current day of the week (1 = Sunday, 7 = Saturday)
        int dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK);

        // Adjust dayOfWeek so that Monday = 1 and Sunday = 7
        int adjustedDayOfWeek = (dayOfWeek == Calendar.SUNDAY) ? 7 : dayOfWeek - 1;

        // Find the Monday of the current week
        tempCal.add(Calendar.DAY_OF_MONTH, -(adjustedDayOfWeek - 1));
        int weekStart = tempCal.get(Calendar.DAY_OF_MONTH);

        // Reset tempCal to today and find the Sunday of the current week
        tempCal = (Calendar) currentCalendar.clone();
        tempCal.add(Calendar.DAY_OF_MONTH, 7 - adjustedDayOfWeek);
        int weekEnd = tempCal.get(Calendar.DAY_OF_MONTH);

        // Handle month boundary cases for weekEnd
        String weekEndSuffix = "";
        if (weekEnd < tempCal.get(Calendar.DAY_OF_MONTH)) {
            tempCal.add(Calendar.MONTH, 1);
            tempCal.set(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
            weekEndSuffix = " (" + monthFormat.format(tempCal.getTime()) + ")";
        }

        // Get the current day of the month
        int dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);

        // Update the currentDate string with the full date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        currentDate = formatter.format(currentCalendar.getTime());

        // Update TextViews
        dateNowSchedule.setText(String.valueOf(dayOfMonth));
        dateNowScheduleLeft.setText(String.valueOf(weekStart));
        dateNowScheduleRight.setText(weekEnd + weekEndSuffix);
    }

    private void fetchSlotData(String authToken, String mentorId, String date) {
        String url = ApiEndPoint.GET_ALL_SLOTS + "?mentorId=" + mentorId + "&Date=" + date;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("isSuccess")) {
                                JSONArray slotsArray = response.getJSONArray("value");
                                List<Slot> slotList = new ArrayList<>();

                                for (int i = 0; i < slotsArray.length(); i++) {
                                    JSONObject slotObject = slotsArray.getJSONObject(i);
                                    Slot slot = new Slot();
                                    slot.setId(slotObject.getString("slotId"));
                                    slot.setStartTime(slotObject.getString("startTime"));
                                    slot.setEndTime(slotObject.getString("endTime"));
                                    slot.setDate(slotObject.getString("date"));
                                    slot.setOnline(slotObject.getBoolean("isOnline"));
                                    slot.setNote(slotObject.getString("note"));
                                    slotList.add(slot);
                                }

                                // Update RecyclerView
                                scheduleAdapter = new MentorOwnSchedules(MentorMeetingSchedule.this, slotList);
                                recyclerViewsSchedules.setLayoutManager(new LinearLayoutManager(MentorMeetingSchedule.this));
                                recyclerViewsSchedules.setAdapter(scheduleAdapter);
                            } else {
                                Toast.makeText(MentorMeetingSchedule.this, "Failed to retrieve slots", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MentorMeetingSchedule.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}