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

import com.android.volley.AuthFailureError;
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
            startActivity(new Intent(LoginActivity.this, NavBottomStudentActivity.class));
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
                        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                        boolean isFailure = jsonResponse.getBoolean("isFailure");

                        if (isSuccess && !isFailure) {
                            // Lấy dữ liệu từ JSON response
                            JSONObject value = jsonResponse.getJSONObject("value");
                            String accessToken = value.getString("accessToken");
                            String refreshToken = value.getString("refreshToken");
                            String refreshTokenExpiryTime = value.getString("refreshTokenExpiryTime");

                            // Xác thực thành công, có thể lưu accessToken và refreshToken
                            // Lưu hoặc xử lý token theo nhu cầu của bạn
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Log.d("Login", "Access Token: " + accessToken);
                            Log.d("Login", "Refresh Token: " + refreshToken);
                            Log.d("Login", "Refresh Token Expiry: " + refreshTokenExpiryTime);
                        } else {
                            // Xác thực thất bại
                            JSONObject error = jsonResponse.getJSONObject("error");
                            String errorMessage = error.getString("message");
                            Toast.makeText(LoginActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Xử lý lỗi
            Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Tạo JSON object để gửi
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("googleToken", idToken);  // Truyền IdToken vào key "googleToken"

                return new JSONObject(jsonBody).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Thêm yêu cầu vào hàng đợi
        queue.add(stringRequest);
    }

}

