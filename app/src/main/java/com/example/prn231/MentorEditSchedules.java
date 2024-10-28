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
import com.example.prn231.Fragment.Mentor.HomeMentorFragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MentorEditSchedules extends AppCompatActivity {

    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvDate;
    private Switch isOnline;
    private EditText note;
    private Button btn;
    private RequestQueue requestQueue;
    private static final String API_URL = "http://mbs.pro.vn:4000/api/v1/slots";
    private String slotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_edit_schedules);

        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvDate = findViewById(R.id.tvDate);
        isOnline = findViewById(R.id.switchIsOnline);
        note = findViewById(R.id.etNote);
        btn = findViewById(R.id.bookingButton);

        requestQueue = Volley.newRequestQueue(this);

        // Retrieve data from Intent
        if (getIntent() != null) {
            slotId = getIntent().getStringExtra("slotId");
            String slotStart = getIntent().getStringExtra("slotStart");
            String slotEnd = getIntent().getStringExtra("slotEnd");
            String slotDate = getIntent().getStringExtra("slotDate");
            String slotNote = getIntent().getStringExtra("slotNote");
            String slotType = getIntent().getStringExtra("slotType");

            // Set the hints for EditText fields
            tvStartTime.setText(slotStart);
            tvEndTime.setText(slotEnd);
            tvDate.setText(slotDate);
            note.setText(slotNote);
            isOnline.setChecked("Online".equals(slotType));
        }

        tvStartTime.setOnClickListener(v -> showTimePicker(true));
        tvEndTime.setOnClickListener(v -> showTimePicker(false));
        tvDate.setOnClickListener(v -> showDatePicker());

        btn.setOnClickListener(v -> updateSlot());

        ImageView backBtn = findViewById(R.id.back_arrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MentorMeetingSchedule.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String getJwtToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
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
                    String date = String.format("%02d/%02d/%d", selectedMonth + 1, selectedDay, selectedYear);
                    tvDate.setText(date);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void updateSlot() {
        try {
            String startTime = tvStartTime.getText().toString();
            String endTime = tvEndTime.getText().toString();
            String date = formatDate(tvDate.getText().toString()); // Format date here
            String noteText = note.getText().toString();

            // Validate input
            if (startTime.isEmpty() || endTime.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all date and time fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validateTimeSlot(startTime, endTime)) {
                Toast.makeText(this, "Time slot must be between 30 minutes and 1 hour", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject requestBody = new JSONObject();
            JSONObject slotModel = new JSONObject();
            slotModel.put("id", slotId);
            slotModel.put("startTime", startTime);
            slotModel.put("endTime", endTime);
            slotModel.put("date", date); // Use formatted date
            slotModel.put("isOnline", isOnline.isChecked());
            slotModel.put("note", noteText);

            requestBody.put("slotModel", slotModel);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    API_URL,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MentorEditSchedules.this, "Slot updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MentorEditSchedules.this, HomeMentorFragment.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = error.getMessage();
                            if (errorMessage == null) {
                                errorMessage = "An error occurred. Please try again.";
                            }
                            Toast.makeText(MentorEditSchedules.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String jwtToken = getJwtToken();
                    if (!jwtToken.isEmpty()) {
                        headers.put("Authorization", "Bearer " + jwtToken);
                    }
                    return headers;
                }
            };

            // Add request to queue
            requestQueue.add(jsonRequest);

        } catch (Exception e) {
            Toast.makeText(this, "Error creating request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(String inputDate) {
        try {
            String[] parts = inputDate.split("/");
            int day = Integer.parseInt(parts[1]); // Day
            int month = Integer.parseInt(parts[0]); // Month
            int year = Integer.parseInt(parts[2]); // Year

            // Return in MM/dd/yyyy format
            return String.format("%02d/%02d/%d", month, day, year);
        } catch (Exception e) {
            return inputDate; // Return original string if parsing fails
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}