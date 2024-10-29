package com.example.prn231.Fragment.Mentor;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Adapter.SkillAdapter;
import com.example.prn231.Api.ApiEndPoint;

import com.example.prn231.Api.ApiService;
import com.example.prn231.DTO.SkillMentor;
import com.example.prn231.DTO.VolleyMultipartRequest;
import com.example.prn231.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SkillFragment extends Fragment {
    private FloatingActionButton fabAddSkill;
    private RecyclerView recyclerViewList;
    private SkillAdapter skillAdapter;
    private List<SkillMentor> skillList;
    private List<SkillMentor> allSkillList;
    private ArrayAdapter<SkillMentor> adapter;
    private RequestQueue requestQueue;
    private  ImageView selectedImageView;
    private static final int PICK_IMAGE_MULTIPLE = 1;
    private List<Uri> selectedImagesUris; // Danh sách chứa URI của các ảnh đã chọn


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skill, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        String mentorId = sharedPreferences.getString("userId","");

        recyclerViewList = view.findViewById(R.id.recyclerViewList);

        // Set up RecyclerView
        recyclerViewList.setLayoutManager(new LinearLayoutManager(getContext()));
        skillList = new ArrayList<>();
        skillAdapter = new SkillAdapter(skillList, requireContext());
        recyclerViewList.setAdapter(skillAdapter);
// Khởi tạo RequestQueue của Volley
        requestQueue = Volley.newRequestQueue(requireContext());
        // Gọi hàm để load dữ liệu từ API
        loadSkillDataFromAPI(accessToken, mentorId);

        fabAddSkill = view.findViewById(R.id.fabAddSkill);
        fabAddSkill.setOnClickListener(v -> {
            // Create a new dialog
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_create_skill);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);


            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
            Spinner spinnerSkill = dialog.findViewById(R.id.spinnerSkill);
             selectedImageView = dialog.findViewById(R.id.selectedImageView);
            Button buttonChooseImage = dialog.findViewById(R.id.buttonChooseImage);
            // Khởi tạo danh sách ảnh đã chọn
            selectedImagesUris = new ArrayList<>();

            buttonChooseImage.setOnClickListener(view1 -> {
                // Sử dụng ImagePicker để chọn ảnh
                ImagePicker.with(this)
                        .galleryOnly() // Chỉ cho phép chọn từ thư viện
                        .maxResultSize(1080, 1080) // Kích thước tối đa
                        .compress(1024) // Kích thước nén
                        .start();
            });
            // Thiết lập Spinner với adapter
            allSkillList = new ArrayList<>();  // Tạo list chứa dữ liệu cho Spinner
            adapter = new ArrayAdapter<SkillMentor>(requireContext(), android.R.layout.simple_spinner_item, allSkillList) {

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
            spinnerSkill.setAdapter(adapter);

            // Gọi API để tải dữ liệu cho Spinner
            fetchSkills();
            // Set listeners for buttons
            buttonCancel.setOnClickListener(view1 -> dialog.dismiss());

            buttonConfirm.setOnClickListener(view1 -> {
                // Get the input values
                int selectedSkillPosition = spinnerSkill.getSelectedItemPosition();
                SkillMentor selectedSkill = allSkillList.get(selectedSkillPosition); // Lấy skill đã chọn

                // Gọi hàm upload với skillId và danh sách ảnh
                uploadSkillWithRetrofit(selectedSkill.getId(), selectedImagesUris, accessToken);
                dialog.dismiss();
            });

            // Show the dialog
            dialog.show();
        });

        return view;
    }
    public void uploadSkillWithRetrofit(String skillId, List<Uri> selectedImagesUris,String accessToken) {
        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEndPoint.BASE_URL_COMMAND) // Thay "YOUR_BASE_URL" bằng URL của server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Tạo body cho skillId
        RequestBody skillIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(skillId));

        // Chuyển danh sách hình ảnh thành List<MultipartBody.Part>
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : selectedImagesUris) {
            File file = new File(uri.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("productImages[]", file.getName(), requestFile);
            imageParts.add(imagePart);
        }

        // Tạo yêu cầu upload
        Call<ResponseBody> call = apiService.uploadSkill("Bearer " + accessToken, skillIdBody, imageParts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Skill created successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(), "Failed to create skill: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchSkills() {
        String url = ApiEndPoint.GET_ALL_SKILL; // URL của API

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray itemsArray = response.getJSONObject("value").getJSONArray("items");

                            // Duyệt qua các phần tử trong mảng items và thêm vào skillList
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject skillObject = itemsArray.getJSONObject(i);
                                String id = skillObject.getString("id");
                                String name = skillObject.getString("name");
                                SkillMentor skillMentor = new SkillMentor();
                                skillMentor.setId(id);
                                skillMentor.setSkillName(name);
                                allSkillList.add(skillMentor);
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
    private void loadSkillDataFromAPI(String accessToken, String mentorId) {
        String url = ApiEndPoint.GET_MENTOR_DETAIL + "/"+mentorId;  // Đổi thành URL API của bạn
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Kiểm tra nếu yêu cầu thành công
                            if (response.getBoolean("isSuccess")) {
                                // Lấy object "value" từ response
                                JSONObject valueObj = response.getJSONObject("value");

                                // Lấy mảng "skills" từ object "value"
                                JSONArray skillsArray = valueObj.getJSONArray("skills");

                                // Lặp qua từng object trong mảng "skills"
                                for (int i = 0; i < skillsArray.length(); i++) {
                                    JSONObject skillObj = skillsArray.getJSONObject(i);

                                    // Parse các trường từ từng skill object
                                    String skillName = skillObj.getString("skillName");
                                    String skillDescription = skillObj.getString("skillDesciption");
                                    String skillCategoryType = skillObj.getString("skillCategoryType");

                                    // Tạo đối tượng SkillMentor và gán giá trị
                                    SkillMentor skillMentor = new SkillMentor();
                                    skillMentor.setSkillName(skillName);
                                    skillMentor.setSkillDescription(skillDescription);
                                    skillMentor.setSkillCategoryType(skillCategoryType);

                                    // Thêm skill vào danh sách
                                    skillList.add(skillMentor);
                                }

                                // Thông báo cho adapter dữ liệu đã thay đổi
                                skillAdapter.notifyDataSetChanged();
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
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    // Xử lý kết quả từ ImagePicker
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            // Lấy Uri của ảnh đã chọn
            Uri imageUri = data.getData();
            if (imageUri != null) {
                selectedImagesUris.add(imageUri);
                // Cập nhật UI để hiển thị ảnh đã chọn
                selectedImageView.setImageURI(imageUri);
            }
        }
    }


}