package com.example.prn231;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.loginButton);

        // Cấu hình Google Sign-In để lấy mã thông báo ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id)) // Lấy ID token từ resources
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (googleSignInAccount != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }

        // Đăng ký kết quả Activity
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Lấy tài khoản đã đăng nhập sau khi người dùng chọn tài khoản từ hộp thoại
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInTask(task);
                });

        // Xử lý sự kiện click cho nút đăng nhập
        loginButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });
    }

    private void handleSignInTask(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String idToken = account.getIdToken(); // Lấy ID token
            sendTokenToBackend(idToken); // Gửi ID token đến backend
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Login failed with error code: " + e.getStatusCode() +
                    "\nMessage: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendTokenToBackend(String idToken) {
        // URL của backend nơi bạn xử lý xác thực
        String url = ApiEndPoint.LOGIN_GOOGLE;

        // Tạo RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Tạo yêu cầu POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Xử lý phản hồi từ server
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            // Xác thực thành công
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xác thực thất bại
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Xử lý lỗi
            Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Thêm mã token vào request body
                Map<String, String> params = new HashMap<>();
                params.put("googleToken", idToken); // Đặt tên trường là googleToken
                return params;
            }
        };

        // Thêm yêu cầu vào hàng đợi
        queue.add(stringRequest);
    }
}

