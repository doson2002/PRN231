package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MentorMeetingSchedule extends AppCompatActivity {
    private RecyclerView recyclerViewsSchedules;
    private MentorOwnSchedules scheduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_meeting_schedule);

        recyclerViewsSchedules = findViewById(R.id.rvSlots);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeMentorFragment.class);
            startActivity(intent);
            finish();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", "");
        String authToken = "Bearer " + accessToken;
        String mentorId = sharedPreferences.getString("userId", "");

        fetchSlotData(authToken, mentorId);
    }

    private void fetchSlotData(String authToken, String mentorId) {
        String url = ApiEndPoint.GET_ALL_SLOTS + "?mentorId=" + mentorId;

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