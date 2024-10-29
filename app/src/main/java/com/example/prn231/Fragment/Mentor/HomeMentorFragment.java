package com.example.prn231.Fragment.Mentor;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.LoginActivity;
import com.example.prn231.MentorMeetingSchedule;
import com.example.prn231.MentorPage;
import com.example.prn231.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeMentorFragment extends Fragment {
    private NavigationView navigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_mentor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PRN231", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        String accessToken = sharedPreferences.getString("accessToken","");

        // Find views
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        navigationView = view.findViewById(R.id.navigation_view);

        // Setup Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            // Add the hamburger menu icon and set up the toggle to open the drawer
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        // Get the header view of the NavigationView
        View headerView = navigationView.getHeaderView(0);
        callApiGetUserById(userId, accessToken);
        // Find the logout button in the header
        ImageView logoutButton = headerView.findViewById(R.id.logout_button);
        // Set onClick listener for the logout button
        logoutButton.setOnClickListener(v -> {
            // Handle logout logic here (e.g., clearing user session, navigating to login screen)
            Toast.makeText(getContext(), "Logout clicked", Toast.LENGTH_SHORT).show();

            // Example: clear the user's session and navigate to login activity
            performLogout();

            // Close the drawer after logout
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Set up the click listener for the ImageView
        LinearLayout searchMentorButton = view.findViewById(R.id.btn_meeting_schedule);
        searchMentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click action here
                Toast.makeText(getContext(), "Button clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MentorMeetingSchedule.class);
                startActivity(intent);
            }
        });
    }

    private void callApiGetUserById(String userId, String accessToken) {
        String url = ApiEndPoint.GET_USER_BY_ID +"/"+ userId;  // URL của API

        // Tạo một request mới
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, // GET method
                url,
                null,  // Không cần body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Kiểm tra nếu API trả về thành công
                            boolean isSuccess = response.getBoolean("isSuccess");
                            if (isSuccess) {
                                // Lấy object value từ response
                                JSONObject value = response.getJSONObject("value");

                                // Lấy tên và email của người dùng
                                String fullName = value.getString("fullName");
                                String email = value.getString("email");
                                int points = value.getInt("points");

                                // Cập nhật UI: Tên, Email và Điểm
                                updateUserInfo(fullName, email, points);
                            } else {
                                // Xử lý khi response không thành công
                                Toast.makeText(requireActivity(), "Không thể lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireActivity(), "Lỗi JSON!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi từ API
                        Toast.makeText(requireActivity(), "Lỗi khi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
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

        // Tạo request queue và thêm request vào hàng đợi
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);
    }
    private void updateUserInfo(String fullName, String email, int points) {
        // Lấy View header của NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Lấy các TextView trong header
        TextView userNameTextView = headerView.findViewById(R.id.user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.user_email);

        // Cập nhật thông tin
        userNameTextView.setText(fullName);
        userEmailTextView.setText(email);

    }
    private void performLogout() {
        // Clear session data (this could be shared preferences, token, etc.)
        // Navigate to login screen or close the app
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}