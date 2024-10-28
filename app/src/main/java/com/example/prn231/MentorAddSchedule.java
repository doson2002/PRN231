package com.example.prn231;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.Fragment.Mentor.HomeMentorFragment;
import com.example.prn231.Model.Slot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MentorAddSchedule extends AppCompatActivity {

    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvDate;
    private ImageButton btnStartTime;
    private ImageButton btnEndTime;
    private ImageButton btnDate;
    private Switch isOnline;
    private EditText note;
    private Button btn;
    private RequestQueue requestQueue;
    private static final String API_URL = ApiEndPoint.BASE_URL_COMMAND + "slots";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_add_schedule);

        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvDate = findViewById(R.id.tvDate);
        btnStartTime = findViewById(R.id.btnStartTime);
        btnEndTime = findViewById(R.id.btnEndTime);
        btnDate = findViewById(R.id.btnDate);
        isOnline = findViewById(R.id.switchIsOnline);
        note = findViewById(R.id.etNote);
        btn = findViewById(R.id.bookingButton);

        requestQueue = Volley.newRequestQueue(this);

        btnStartTime.setOnClickListener(v -> showTimePicker(true));
        btnEndTime.setOnClickListener(v -> showTimePicker(false));
        btnDate.setOnClickListener(v -> showDatePicker());

        tvStartTime.setOnClickListener(v -> showTimePicker(true));
        tvEndTime.setOnClickListener(v -> showTimePicker(false));
        tvDate.setOnClickListener(v -> showDatePicker());

        btn.setOnClickListener(v -> createSlot());

        ImageView backBtn = findViewById(R.id.back_arrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeMentorFragment.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, selectedMinute) -> {
                    String time = String.format("%02d:%02d", hourOfDay, selectedMinute);
                    if (isStartTime) {
                        tvStartTime.setText(time);
                    } else {
                        tvEndTime.setText(time);
                    }
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    tvDate.setText(date);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private String getJwtToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        String authToken = "Bearer " + accessToken;
        return authToken;
    }

    private boolean validateTimeSlot(String startTime, String endTime) {
        try {
            String[] startParts = startTime.split(":");
            String[] endParts = endTime.split(":");

            int startHour = Integer.parseInt(startParts[0]);
            int startMinute = Integer.parseInt(startParts[1]);
            int endHour = Integer.parseInt(endParts[0]);
            int endMinute = Integer.parseInt(endParts[1]);

            // Convert to minutes for easier calculation
            int startTotalMinutes = startHour * 60 + startMinute;
            int endTotalMinutes = endHour * 60 + endMinute;

            int duration = endTotalMinutes - startTotalMinutes;

            return duration >= 30 && duration <= 60;
        } catch (Exception e) {
            return false;
        }
    }

    private String formatDate(String inputDate) {
        try {
            String[] parts = inputDate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Return in MM/dd/yyyy format
            return String.format("%02d/%02d/%d", month, day, year);
        } catch (Exception e) {
            return inputDate;
        }
    }

    private void createSlot() {
        try {
            // Create slot model
            Slot slot = new Slot();
            slot.setStartTime(tvStartTime.getText().toString());
            slot.setEndTime(tvEndTime.getText().toString());
            slot.setDate(tvDate.getText().toString());
            slot.setOnline(isOnline.isChecked());
            slot.setNote(note.getText().toString());

            if (slot.getStartTime().isEmpty() || slot.getEndTime().isEmpty() || slot.getDate().isEmpty()) {
                Toast.makeText(this, "Please fill in all date and time fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validateTimeSlot(slot.getStartTime(), slot.getEndTime())) {
                Toast.makeText(this, "Time slot must be between 30 minutes and 1 hour", Toast.LENGTH_SHORT).show();
                return;
            }

            String formattedDate = formatDate(slot.getDate());

            JSONObject requestBody = new JSONObject();
            JSONArray slotModelsArray = new JSONArray();

            // Create the slot model object
            JSONObject slotModel = new JSONObject();
            slotModel.put("startTime", slot.getStartTime());
            slotModel.put("endTime", slot.getEndTime());
            slotModel.put("date", formattedDate);
            slotModel.put("isOnline", slot.isOnline());
            slotModel.put("note", slot.getNote());

            slotModelsArray.put(slotModel);
            requestBody.put("slotModels", slotModelsArray);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MentorAddSchedule.this, "Slot created successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = error.getMessage();
                            if (errorMessage == null) {
                                errorMessage = "Please choose date in the first week of semester";
                            }
                            Toast.makeText(MentorAddSchedule.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String jwtToken = getJwtToken();
                    if (!jwtToken.isEmpty()) {
                        headers.put("Authorization", jwtToken);
                    }
                    return headers;
                }
            };

            // Add request to queue
            requestQueue.add(jsonRequest);

        } catch (JSONException e) {
            Toast.makeText(this, "Error creating request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}