package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginEmailPasswordActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    private String role;
    private String userId;
    private String email;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_email_password);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        backBtn = findViewById(R.id.imageView);

        loginButton.setOnClickListener(v -> {
            String emailText = emailInput.getText().toString().trim();
            String passwordText = passwordInput.getText().toString().trim();
            if (!emailText.isEmpty() && !passwordText.isEmpty()) {
                loginWithEmailPassword(emailText, passwordText);
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void loginWithEmailPassword(String email, String password) {
        // Create the request body
        Map<String, String> params = new HashMap<>();
        params.put("emailOrUserName", email);
        params.put("password", password);

        // Make the API call using Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiEndPoint.LOGIN_EMAIL_PASSWORD,
                new JSONObject(params),
                response -> {
                    try {
                        boolean isSuccess = response.getBoolean("isSuccess");
                        boolean isFailure = response.getBoolean("isFailure");

                        if (isSuccess && !isFailure) {
                            JSONObject value = response.getJSONObject("value");
                            String accessToken = value.getString("accessToken");
                            String refreshToken = value.getString("refreshToken");
                            String refreshTokenExpiryTime = value.getString("refreshTokenExpiryTime");

                            // Decode the access token to get the role, email, and user ID
                            decodeAccessToken(accessToken);

                            // Save the user data in shared preferences
                            saveUserData(accessToken, refreshToken, refreshTokenExpiryTime);

                            // Redirect the user based on their role
                            redirectBasedOnRole(role);
                        } else {
                            JSONObject error = response.getJSONObject("error");
                            String errorMessage = error.getString("message");
                            Toast.makeText(this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "An error occurred during login. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "An error occurred during login. Please try again later.", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }

    private void decodeAccessToken(String accessToken) {
        String[] tokenParts = accessToken.split("\\.");
        String payload = new String(Base64.decode(tokenParts[1], Base64.DEFAULT));

        try {
            JSONObject payloadObject = new JSONObject(payload);
            role = payloadObject.getString("Role");
            userId = payloadObject.getString("UserId");
            email = payloadObject.getString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred during login. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(String accessToken, String refreshToken, String refreshTokenExpiryTime) {
        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.putString("refreshTokenExpiryTime", refreshTokenExpiryTime);
        editor.putString("role", role);
        editor.putString("userId", userId);
        editor.putString("email", email);
        editor.apply();
    }

    private void redirectBasedOnRole(String role) {
        Intent intent;
        if (role.equals("0")) {
            intent = new Intent(LoginEmailPasswordActivity.this, NavBottomStudentActivity.class);
        } else if (role.equals("1")) {
            intent = new Intent(LoginEmailPasswordActivity.this, NavBottomMentorActivity.class);
        } else {
            // Handle other roles or default case
            Toast.makeText(this, "Invalid user role. Please contact support.", Toast.LENGTH_SHORT).show();
            intent = new Intent(LoginEmailPasswordActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}