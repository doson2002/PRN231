package com.example.prn231.Fragment.Student;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment  extends Fragment {
    private TextView tvUserName,tvEmail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_student, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        String userId = sharedPreferences.getString("userId","");
        tvUserName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvEmail);
        callApiGetUserById(userId, accessToken);

        return view;
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
                                // Cập nhật UI: Tên, Email và Điểm
                                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PRN231", MODE_PRIVATE);
                                String role = sharedPreferences.getString("role", "");
                                String nameText = "";
                                if(role.equals("0")) {
                                    nameText = "Student " + fullName;
                                }
                                if(role.equals("1")) {
                                    nameText = "Mentor " + fullName;
                                }
                                tvUserName.setText(nameText);
                                tvEmail.setText(email);
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
}
