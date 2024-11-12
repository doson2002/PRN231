package com.example.prn231;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.DTO.SkillMentor;
import com.example.prn231.Model.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private FloatingActionButton fabAddGroup;
    private List<Subject> subjectList;
    private ArrayAdapter<Subject> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group);
         fabAddGroup = findViewById(R.id.fabAddGroup);
        fabAddGroup.setOnClickListener(v -> {
            // Create a new dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_create_group);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            // Get references to the views in the dialog
            EditText editTextGroupName = dialog.findViewById(R.id.editTextGroupName);
            EditText editTextStack = dialog.findViewById(R.id.editTextStack);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
            ImageView closeButton = dialog.findViewById(R.id.close_button);
            Spinner spinnerSubject = dialog.findViewById(R.id.spinnerSubject);
            subjectList = new ArrayList<>();  // Tạo list chứa dữ liệu cho Spinner
            adapter = new ArrayAdapter<Subject>(this, android.R.layout.simple_spinner_item, subjectList) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    // Đặt màu chữ cho item đã chọn
                    ((TextView) view).setTextColor(Color.BLACK); // Màu chữ khi chưa click
                    return view;
                }
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    // Tùy chỉnh hiển thị item trong dropdown nếu cần
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubject.setAdapter(adapter);

            // Gọi API để tải dữ liệu cho Spinner
            fetchSubjects();
            // Set listeners for buttons
            buttonCancel.setOnClickListener(view -> dialog.dismiss());
            closeButton.setOnClickListener(view -> dialog.dismiss());

            buttonConfirm.setOnClickListener(view -> {
                // Get the input values
                String groupName = editTextGroupName.getText().toString().trim();
                String stackValue = editTextStack.getText().toString().trim();
                // Lấy ra đối tượng Subject đã chọn từ Spinner
                Subject selectedSubject = (Subject) spinnerSubject.getSelectedItem();
                String subjectId = selectedSubject.getGroupId(); // Lấy subjectId từ Subject đã chọn

                // Validate input fields
                if (groupName.isEmpty() || stackValue.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create the JSON object to send in the request body
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("name", groupName);
                    jsonBody.put("stacks", stackValue);
                    jsonBody.put("subjectId", subjectId);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to create JSON body", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Define the API endpoint URL
                String url = ApiEndPoint.CREATE_GROUP;
                SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("accessToken","");
                // Create a new StringRequest
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        response -> {
                            // Handle successful response
                            Toast.makeText(this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        },
                        error -> {
                            // Handle error response
                            Toast.makeText(this, "Failed to create group: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        // Add any required headers, e.g., authentication token
                         headers.put("Authorization", "Bearer " + accessToken);
                        return headers;
                    }
                };

                // Set the request timeout policy if needed
                request.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                // Add the request to the RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
                dialog.dismiss();
            });

            // Show the dialog
            dialog.show();
        });

    }
    private void fetchSubjects() {
        String url = ApiEndPoint.GET_ALL_SUBJECT; // URL của API

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray itemsArray = response.getJSONArray("value");

                            // Duyệt qua các phần tử trong mảng items và thêm vào skillList
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject skillObject = itemsArray.getJSONObject(i);
                                String id = skillObject.getString("id");
                                String name = skillObject.getString("name");
                                Subject subject = new Subject();
                                subject.setGroupId(id);
                                subject.setName(name);
                                subjectList.add(subject);
                            }

                            // Cập nhật adapter
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); // Xử lý lỗi
            }
        });

        // Thêm request vào hàng đợi của Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}