package com.example.prn231;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private FloatingActionButton fabAddGroup;
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

            // Set listeners for buttons
            buttonCancel.setOnClickListener(view -> dialog.dismiss());
            closeButton.setOnClickListener(view -> dialog.dismiss());

            buttonConfirm.setOnClickListener(view -> {
                // Get the input values
                String groupName = editTextGroupName.getText().toString().trim();
                String stackValue = editTextStack.getText().toString().trim();

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
}