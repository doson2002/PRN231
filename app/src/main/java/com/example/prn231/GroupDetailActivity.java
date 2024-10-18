package com.example.prn231;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Adapter.MemberAdapter;
import com.example.prn231.Adapter.MemberSearchAdapter;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.DTO.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailActivity extends AppCompatActivity {

    private TextView textViewAddMember;
    private TextView groupName, groupDescription;
    private RecyclerView recyclerViewMembers, recyclerViewMembersDialog;
    private MemberAdapter memberAdapter;
    private MemberSearchAdapter memberAdapterSearch;
    private List<Member> memberList = new ArrayList<>();
    private List<Member> membersListSearch = new ArrayList<>();
    private ImageView back_button;
    private CardView cardViewMember;
    private LinearLayout selectedMembersLayout ;
    private Member selectedMember;
    private TextView textViewSelectedMember;
    private ImageView imageViewRemoveMember;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khi người dùng nhấn vào TextView, hiển thị dialog thêm thành viên
               onBackPressed();
            }
        });
        // Ánh xạ các thành phần giao diện
        groupName = findViewById(R.id.groupName);
        groupDescription = findViewById(R.id.groupDescription);
        recyclerViewMembers = findViewById(R.id.recyclerViewMembers);
        // Khởi tạo RecyclerView
        memberList = new ArrayList<>();
        memberAdapter = new MemberAdapter(memberList);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMembers.setAdapter(memberAdapter);

        // Lấy groupId từ Intent
        String groupId = getIntent().getStringExtra("groupId");

        // Gọi API để lấy chi tiết nhóm
        loadGroupDetail(groupId, accessToken);

        // Khởi tạo TextView và thiết lập sự kiện click
        textViewAddMember = findViewById(R.id.textViewAddMember);
        textViewAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khi người dùng nhấn vào TextView, hiển thị dialog thêm thành viên
                showAddMemberDialog(accessToken, groupId);
            }
        });
    }
    private void loadGroupDetail(String groupId, String accessToken) {
        String url = ApiEndPoint.GET_GROUP_DETAIL + "/" + groupId;  // URL của API lấy chi tiết nhóm

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("isSuccess")) {
                                JSONObject groupObject = response.getJSONObject("value");

                                // Hiển thị thông tin nhóm
                                groupName.setText(groupObject.getString("name"));
                                groupDescription.setText(groupObject.getString("stack"));

                                // Lấy danh sách thành viên
                                JSONArray membersArray = groupObject.getJSONArray("members");
                                for (int i = 0; i < membersArray.length(); i++) {
                                    JSONObject memberObj = membersArray.getJSONObject(i);
                                    String email = memberObj.getString("email");
                                    String fullName = memberObj.getString("fullName");
                                    memberList.add(new Member(email, fullName));
                                }

                                // Cập nhật RecyclerView
                                memberAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(GroupDetailActivity.this, "Failed to load group details", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GroupDetailActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(GroupDetailActivity.this, "API request failed", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void showAddMemberDialog(String accessToken, String groupId) {
        // Tạo dialog mới
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Tắt tiêu đề của dialog
        dialog.setContentView(R.layout.dialog_add_member);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo các thành phần trong dialog
        EditText editTextSearchMember = dialog.findViewById(R.id.editTextSearchMember);
        Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        ImageView closeButton = dialog.findViewById(R.id.close_button);
        selectedMembersLayout = dialog.findViewById(R.id.selectedMembersLayout);
        textViewSelectedMember = dialog.findViewById(R.id.textViewSelectedEmail); // TextView để hiển thị member đã chọn
        imageViewRemoveMember = dialog.findViewById(R.id.closeButton); // Dấu X để xóa chọn
        // Xử lý sự kiện cho nút xóa chọn thành viên
        imageViewRemoveMember.setOnClickListener(v -> {
            selectedMember = null; // Xóa chọn thành viên
            textViewSelectedMember.setText("No member selected"); // Cập nhật thông báo
            cardViewMember.setVisibility(View.GONE); // Ẩn CardView nếu không có thành viên nào
            selectedMembersLayout.setVisibility(View.GONE);
        });
        // Khởi tạo RecyclerView
        // Khởi tạo CardView và RecyclerView
         cardViewMember = dialog.findViewById(R.id.cardViewMember);

        recyclerViewMembersDialog = dialog.findViewById(R.id.recyclerViewMembers);
        recyclerViewMembersDialog.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và adapter
        membersListSearch = new ArrayList<>();
        memberAdapterSearch = new MemberSearchAdapter(membersListSearch, member -> {
            // Gọi hàm để hiển thị thành viên đã chọn
            selectMember(member);
        });
        recyclerViewMembersDialog.setAdapter(memberAdapterSearch);

        // Ẩn CardView khi bắt đầu
        cardViewMember.setVisibility(View.GONE);

        // TextWatcher để theo dõi thay đổi trong EditText và gọi API searchMember
        editTextSearchMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Gọi API searchMember khi người dùng nhập
                if (s.length() > 0) {
                    fetchUsersByEmail(5,s.toString(),accessToken);  // Gọi hàm API searchMember
                }else {
                    // Nếu không có chữ nào trong ô tìm kiếm, ẩn CardView
                    cardViewMember.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý ở đây
            }
        });

        // Xử lý nút Hủy
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Xử lý nút Xác nhận
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động thêm thành viên ở đây
                callApiAddMemberToGroup(groupId,accessToken);

            }
        });

        // Đóng dialog khi bấm nút close
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        dialog.show();
    }
    // Hàm chọn thành viên
    private void selectMember(Member member) {
        selectedMember = member;
        displaySelectedMember();
    }

    // Hàm hiển thị thành viên đã chọn
    private void displaySelectedMember() {
        if (selectedMember != null) {
            cardViewMember.setVisibility(View.GONE); // Hiện CardView nếu có thành viên đã chọn
            selectedMembersLayout.setVisibility(View.VISIBLE);
            // Hiển thị thông tin thành viên
            textViewSelectedMember.setText(selectedMember.getEmail());
        }
    }
    

private void callApiAddMemberToGroup(String groupId, String accessToken) {
    String url = ApiEndPoint.ADD_MEMBER_TO_GROUP; // URL của API

    // Tạo dữ liệu JSON cần gửi
    JSONObject jsonBody = new JSONObject();
    try {
        jsonBody.put("groupId", groupId);
        jsonBody.put("memberId", selectedMember.getUserId());
    } catch (JSONException e) {
        e.printStackTrace();
    }

    // Tạo một request mới
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST, // POST method
            url,
            jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Xử lý response thành công từ API
                    memberList.add(selectedMember); // Thêm thành viên vào danh sách
                    Toast.makeText(GroupDetailActivity.this, "Thành viên đã được thêm vào nhóm!", Toast.LENGTH_SHORT).show();

                    // Cập nhật danh sách thành viên trong adapter và đóng dialog
                    memberAdapter.updateMemberList(memberList);
                    dialog.dismiss(); // Đóng dialog sau khi thêm thành viên thành công
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // Kiểm tra xem lỗi có phải là phản hồi từ server với mã 422 không
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        try {
                            // Chuyển đổi phản hồi lỗi thành chuỗi
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            String errorMessage = data.getString("detail");

                            // Hiển thị lỗi chi tiết từ API
                            if (errorMessage.equals("Member already joined group")) {
                                Toast.makeText(GroupDetailActivity.this, "Thành viên đã tham gia nhóm trước đó!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GroupDetailActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Các lỗi khác
                        Toast.makeText(GroupDetailActivity.this, "Lỗi khi thêm thành viên!", Toast.LENGTH_SHORT).show();
                    }
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

    // Tạo request queue và thêm request vào hàng đợi
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(jsonObjectRequest);
}

    // Giả lập hàm gọi API searchMember
    private void fetchUsersByEmail(int memberCount, String emailKeyword, String accessToken) {
        // Tạo URL với memberCount và emailKeyword
        String url = ApiEndPoint.GET_USER_BY_EMAIL +"/"+ memberCount + "/" + emailKeyword;

        // Tạo request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Tạo request GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Kiểm tra nếu API trả về thành công
                            boolean isSuccess = response.getBoolean("isSuccess");
                            if (isSuccess) {
                                // Lấy danh sách member
                                JSONArray members = response.getJSONArray("value");
                                // Clear and update the member list
                                List<Member> fetchedMembers = new ArrayList<>();
                                for (int i = 0; i < members.length(); i++) {
                                    JSONObject memberJson = members.getJSONObject(i);
                                    String userId = memberJson.getString("userId");
                                    String email = memberJson.getString("email");
                                    String fullName = memberJson.getString("fullName");
                                    Member member = new Member(email, fullName);
                                    member.setUserId(userId);
                                    // Thêm member vào danh sách
                                    fetchedMembers.add(member);
                                    // Xử lý thông tin member (hiển thị lên UI, ...)
                                    Toast.makeText(GroupDetailActivity.this, "Email: " + email + "\nFull Name: " + fullName, Toast.LENGTH_SHORT).show();
                                }
                                // Cập nhật adapter với danh sách member mới
                                memberAdapterSearch.updateMemberList(fetchedMembers);
                                membersListSearch = fetchedMembers;
                                // Kiểm tra xem danh sách có rỗng không
                                if (fetchedMembers.isEmpty()) {
                                    cardViewMember.setVisibility(View.GONE); // Ẩn CardView
                                } else {
                                    cardViewMember.setVisibility(View.VISIBLE); // Hiện CardView
                                }
                            } else {
                                // Xử lý khi API không thành công
                                Toast.makeText(GroupDetailActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GroupDetailActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi khi gọi API
                        Toast.makeText(GroupDetailActivity.this, "API Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        // Thêm request vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }


}
