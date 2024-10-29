package com.example.prn231;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Adapter.ScheduleForMentorAdapter;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.DTO.ScheduleBooked;
import com.example.prn231.Fragment.Mentor.HomeMentorFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleBookedForMentorActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ScheduleForMentorAdapter scheduleAdapter;
    private List<ScheduleBooked> scheduleList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_booked_for_mentor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView backBtn = findViewById(R.id.back_arrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        String accessToken = sharedPreferences.getString("accessToken","");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleAdapter = new ScheduleForMentorAdapter(this,scheduleList, this::showAcceptRejectDialog);
        recyclerView.setAdapter(scheduleAdapter);

        // Gọi hàm để lấy dữ liệu từ API
        fetchSchedulesFromApi(accessToken);
    }
    private void fetchSchedulesFromApi(String accessToken) {
        String url = ApiEndPoint.GET_SCHEDULE_BOOKED_FOR_MENTOR; // Thay URL của bạn tại đây

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        scheduleList.clear(); // Xóa dữ liệu cũ trước khi thêm dữ liệu mới
                        JSONArray jsonArray = response.getJSONArray("value");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String groupId = jsonObject.getString("groupId");
                            String groupName = jsonObject.getString("groupName");
                            String startTime = jsonObject.getString("startTime");
                            String endTime = jsonObject.getString("endTime");
                            String date = jsonObject.getString("date");
                            boolean isFeedback = jsonObject.getBoolean("isFeedback");
                            int isAccepted = jsonObject.getInt("isAccepted");

                            // Tạo đối tượng Schedule và thêm vào danh sách
                            ScheduleBooked schedule = new ScheduleBooked(id, groupName, startTime, endTime, date, isFeedback, isAccepted);
                            schedule.setGroupId(groupId);
                            scheduleList.add(schedule);
                        }
                        scheduleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error fetching schedules", Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                // Add bất kỳ header nào cần thiết, ví dụ như access token
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Set the request timeout policy if needed
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void showAcceptRejectDialog(ScheduleBooked schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Status");
        builder.setMessage("Do you want to accept or reject this schedule?");

        builder.setPositiveButton("Accept", (dialog, which) -> {
            updateScheduleStatus(schedule.getId(), 1);
            dialog.dismiss();
        });

        builder.setNegativeButton("Reject", (dialog, which) -> {
            updateScheduleStatus(schedule.getId(), 2);
            dialog.dismiss();
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateScheduleStatus(String scheduleId, int status) {
        String url = ApiEndPoint.CHANGE_STATUS_SCHEDULE; // Thay bằng URL API để cập nhật trạng thái
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        JSONObject params = new JSONObject();
        try {
            params.put("scheduleId", scheduleId);
            params.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    Toast.makeText(this, status == 1 ? "Schedule Accepted" : "Schedule Rejected", Toast.LENGTH_SHORT).show();
                    fetchSchedulesFromApi(accessToken); // Refresh the list after update
                },
                error -> {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                // Add bất kỳ header nào cần thiết, ví dụ như access token
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Set the request timeout policy if needed
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);
    }
}