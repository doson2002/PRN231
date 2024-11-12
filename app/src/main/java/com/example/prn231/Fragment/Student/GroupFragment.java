package com.example.prn231.Fragment.Student;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Adapter.GroupAdapter;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.DTO.Group;
import com.example.prn231.DTO.Member;
import com.example.prn231.Model.Subject;
import com.example.prn231.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupFragment extends Fragment {
    private FloatingActionButton fabAddGroup;
    private RecyclerView recyclerViewList;
    private GroupAdapter groupAdapter;
    private List<Group> groupList;
    private RequestQueue requestQueue;
    private List<Subject> subjectList;
    private ArrayAdapter<Subject> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_group, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        recyclerViewList = view.findViewById(R.id.recyclerViewList);

        // Set up RecyclerView
        recyclerViewList.setLayoutManager(new LinearLayoutManager(getContext()));
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList, requireContext());
        recyclerViewList.setAdapter(groupAdapter);
// Khởi tạo RequestQueue của Volley
        requestQueue = Volley.newRequestQueue(getContext());
        // Gọi hàm để load dữ liệu từ API
        loadGroupDataFromAPI(accessToken);


        fabAddGroup = view.findViewById(R.id.fabAddGroup);
        fabAddGroup.setOnClickListener(v -> {
            // Create a new dialog
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_create_group);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            // Get references to the views in the dialog
            EditText editTextGroupName = dialog.findViewById(R.id.editTextGroupName);
            EditText editTextStack = dialog.findViewById(R.id.editTextStack);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
            ImageView closeButton = dialog.findViewById(R.id.close_button);
            Spinner spinnerSubject = dialog.findViewById(R.id.spinnerSubject);
            subjectList = new ArrayList<>();  // Tạo list chứa dữ liệu cho Spinner
            adapter = new ArrayAdapter<Subject>(requireContext(), android.R.layout.simple_spinner_item, subjectList) {

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
            buttonCancel.setOnClickListener(view1 -> dialog.dismiss());
            closeButton.setOnClickListener(view1 -> dialog.dismiss());

            buttonConfirm.setOnClickListener(view1 -> {
                // Get the input values
                String groupName = editTextGroupName.getText().toString().trim();
                String stackValue = editTextStack.getText().toString().trim();
                // Lấy ra đối tượng Subject đã chọn từ Spinner
                Subject selectedSubject = (Subject) spinnerSubject.getSelectedItem();
                String subjectId = selectedSubject.getGroupId(); // Lấy subjectId từ Subject đã chọn
                // Validate input fields
                if (groupName.isEmpty() || stackValue.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(requireContext(), "Failed to create JSON body", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Define the API endpoint URL
                String url = ApiEndPoint.CREATE_GROUP;

                // Create a new JsonObjectRequest
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        response -> {
                            // Handle successful response
                            Toast.makeText(requireContext(), "Group created successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadGroupDataFromAPI(accessToken);
                        },
                        error -> {
                            // Handle error response
                            Toast.makeText(requireContext(), "Failed to create group: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                requestQueue.add(request);
            });

            // Show the dialog
            dialog.show();
        });

        return view;
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
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);
    }
    private void loadGroupDataFromAPI(String accessToken) {
        String url = ApiEndPoint.GET_ALL_GROUP;  // Đổi thành URL API của bạn

        // Sử dụng JsonObjectRequest vì response trả về là một đối tượng JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            groupList.clear();
                            // Kiểm tra nếu yêu cầu thành công
                            if (response.getBoolean("isSuccess")) {
                                // Lấy mảng "value" từ đối tượng JSON
                                JSONArray valueArray = response.getJSONArray("value");

                                // Lặp qua từng object trong mảng "value"
                                for (int i = 0; i < valueArray.length(); i++) {
                                    JSONObject groupObj = valueArray.getJSONObject(i);

                                    // Parse các trường từ từng object trong mảng
                                    String groupId = groupObj.getString("groupId");
                                    String name = groupObj.getString("name");
                                    String mentorName = groupObj.optString("mentorName", "N/A");  // Nếu mentorName là null, trả về "N/A"
                                    String leaderName = groupObj.getString("leaderName");
                                    String projectName = groupObj.optString("projectName", "Has No Project Yet");


                                    // Tạo object Group và thêm vào danh sách
                                    Group group = new Group(groupId, name, mentorName, leaderName, projectName, new ArrayList<Member>());
                                    groupList.add(group);
                                }

                                // Thông báo cho adapter dữ liệu đã thay đổi
                                groupAdapter.notifyDataSetChanged();
                            } else {
                                // Xử lý nếu API trả về isSuccess = false
                                Toast.makeText(getContext(), "API call was not successful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "API call failed", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Thêm request vào hàng đợi của Volley
        requestQueue.add(jsonObjectRequest);
    }


}