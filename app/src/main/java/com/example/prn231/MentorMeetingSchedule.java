package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.Adapter.MentorOwnSchedules;
import com.example.prn231.Api.SlotApi;
import com.example.prn231.Fragment.Mentor.HomeMentorFragment;
import com.example.prn231.Model.ResponseSingelModel;
import com.example.prn231.Model.Schedule;
import com.example.prn231.Services.MentorSlotServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentorMeetingSchedule extends AppCompatActivity {
    SlotApi mentorSlotServices;


    Calendar currentCalendar;
    String now;

    RecyclerView recyclerViewsSchedules;
    MentorOwnSchedules scheduleAdapter;
    TextView dateNowSchedule, dateNowScheduleLeft, dateNowScheduleRight;
    ImageButton backDayButton, nextDayButton, nextWeekButton, backWeekButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_meeting_schedule);

        currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        dateNowSchedule = findViewById(R.id.dateNowDetail);
        dateNowScheduleLeft = findViewById(R.id.dateNowDetailLeft);
        dateNowScheduleRight = findViewById(R.id.dateNowDetailRight);
        recyclerViewsSchedules = findViewById(R.id.rvSlots);
        nextWeekButton = findViewById(R.id.nextWeekButton);
        backWeekButton = findViewById(R.id.backWeekButton);

        nextDayButton = findViewById(R.id.nextDayButton);
        backDayButton = findViewById(R.id.backDayButton);

        ImageView backArrow = findViewById(R.id.back_arrow);
        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent = new Intent(getApplicationContext(), HomeMentorFragment.class);
                startActivity(intent);
                finish(); // This will navigate back to the previous activity in the stack
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        String authToken = "Bearer " + accessToken;

        String mentorId = sharedPreferences.getString("userId", "");

        // Format the current date for display
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        now = formatter.format(currentCalendar.getTime());
        updateDateRanges();
        updateList(authToken, mentorId, now);

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceWeek(true);
                updateList(authToken, mentorId, now);
            }
        });

        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceDay(true);
                updateList(authToken, mentorId, now);
            }
        });

        backWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceWeek(false);
                updateList(authToken, mentorId, now);
            }
        });

        backDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceDay(false);
                updateList(authToken, mentorId, now);
            }
        });
    }

    private void advanceWeek(boolean isPlus) {
        // Add 7 days to the current date
        if(isPlus){
            currentCalendar.add(Calendar.DAY_OF_MONTH, 7);
        } else {
            currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
        }

        // Update the UI with new date ranges
        updateDateRanges();
    }

    private void advanceDay(boolean isPlus) {
        // Add 7 days to the current date
        if(isPlus){
            currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        // Update the UI with new date ranges
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
        tempCal = (Calendar) currentCalendar.clone(); // Reset calendar
        tempCal.add(Calendar.DAY_OF_MONTH, 7 - adjustedDayOfWeek);
        int weekEnd = tempCal.get(Calendar.DAY_OF_MONTH);

        // Handle month boundary cases for weekEnd
        String weekEndSuffix = "";
        if (weekEnd < tempCal.get(Calendar.DAY_OF_MONTH)) {
            // If week end exceeds the current month, adjust the month for the next month
            tempCal.add(Calendar.MONTH, 1); // Move to the next month
            tempCal.set(Calendar.DAY_OF_MONTH, 1); // Set to the first day of the next month
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
            weekEndSuffix = " (" + monthFormat.format(tempCal.getTime()) + ")";
        }

        // Get the current day of the month
        int dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);

        // Update the 'now' string with the full date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        now = formatter.format(currentCalendar.getTime());  // Update now with full date

        // Update TextViews
        dateNowSchedule.setText(Integer.toString(dayOfMonth)); // Current day of the month
        dateNowScheduleLeft.setText(Integer.toString(weekStart)); // Start of the week (Monday)
        dateNowScheduleRight.setText(weekEnd + weekEndSuffix); // End of the week (Sunday)
    }
    private void updateList(String authToken, String mentorId, String now){
        Log.d("Time", now);
        mentorSlotServices = MentorSlotServices.getMentorSlotApi();
        Call<ResponseSingelModel<List<Schedule>>> schedultCall = mentorSlotServices.getAllMentorSlot(authToken, mentorId, now);
        schedultCall.enqueue(new Callback<ResponseSingelModel<List<Schedule>>>() {
            @Override
            public void onResponse(Call<ResponseSingelModel<List<com.example.prn231.Model.Schedule>>> schedultCall, Response<ResponseSingelModel<List<Schedule>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.prn231.Model.Schedule> slotList = response.body().getValue();

                    if (slotList != null) {
                        runOnUiThread(() -> {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(MentorMeetingSchedule.this);
                            recyclerViewsSchedules.setLayoutManager(layoutManager);
                            scheduleAdapter = new MentorOwnSchedules(slotList, mentorId);
                            recyclerViewsSchedules.setAdapter(scheduleAdapter);
                            // If the adapter is already set, you may just need to update the data and notify the adapter
                            scheduleAdapter.notifyDataSetChanged();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MentorMeetingSchedule.this, "", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseSingelModel<List<com.example.prn231.Model.Schedule>>> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
            }
        });

    }
}