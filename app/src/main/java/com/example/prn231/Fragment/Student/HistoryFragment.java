package com.example.prn231.Fragment.Student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Adapter.ScheduleBookedAdapter;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.DTO.ScheduleBooked;
import com.example.prn231.Decorations.ItemDecorationDividerForActivity;
import com.example.prn231.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleBookedAdapter adapter;
    private List<ScheduleBooked> scheduleBookedList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_booked_history, container, false);

        View rootView = view.findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                // Áp dụng padding để tránh bị thanh hệ thống che
                v.setPadding(insets.getSystemWindowInsetLeft(), 0,
                        insets.getSystemWindowInsetRight(), 0);
                return insets.consumeSystemWindowInsets();
            }
        });
        scheduleBookedList = new ArrayList<>();
        adapter = new ScheduleBookedAdapter(scheduleBookedList, requireContext());

        // Call the API to fetch orders
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PRN231", getActivity().MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");

        fetchScheduleFromApi(accessToken);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

//        int marginStart = 50; // Ví dụ: bạn có thể đặt giá trị này phù hợp với yêu cầu
//        int marginEnd = 50; // Ví dụ: bạn có thể đặt giá trị này phù hợp với yêu cầu
//        recyclerView.addItemDecoration(new ItemDecorationDividerForActivity(getActivity(), R.drawable.divider, marginStart, marginEnd));
//


        return  view;


    }

    private void fetchScheduleFromApi(String jwtToken) {
        String url = ApiEndPoint.GET_SCHEDULE_BOOKED;

        // Initialize a request queue
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Create a JsonObjectRequest to handle the new structure
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // Kiểm tra nếu phản hồi thành công
                        if (response.getBoolean("isSuccess")) {
                            // Lấy mảng "value"
                            JSONArray valueArray = response.getJSONArray("value");
                            // Loop through the array
                            for (int i = 0; i < valueArray.length(); i++) {
                                JSONObject jsonObject = valueArray.getJSONObject(i);
                                // Parse the JSON object
                                ScheduleBooked scheduleBooked = new ScheduleBooked();
                                scheduleBooked.setId(jsonObject.getString("id"));
                                scheduleBooked.setGroupId(jsonObject.getString("groupId"));
                                scheduleBooked.setDate(jsonObject.getString("date"));
                                scheduleBooked.setGroupName(jsonObject.getString("groupName"));
                                scheduleBooked.setStartTime(jsonObject.getString("startTime"));
                                scheduleBooked.setEndTime(jsonObject.getString("endTime"));
                                scheduleBooked.setFeedBack(jsonObject.getBoolean("isFeedback"));
                                // Add to the list
                                scheduleBookedList.add(scheduleBooked);
                            }
                            // Notify the adapter that data has changed
                            adapter.notifyDataSetChanged();
                        } else {
                            // Xử lý trường hợp không thành công
                            String errorMessage = response.getJSONObject("error").getString("message");
                            Log.e("API Error", errorMessage);
                            Toast.makeText(getActivity(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (jwtToken != null) {
                    headers.put("Authorization", "Bearer " + jwtToken);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }




}
