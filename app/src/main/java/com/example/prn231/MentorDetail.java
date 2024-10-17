package com.example.prn231;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MentorDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_detail);

        // Get the mentor ID and other details from the Intent
        String mentorId = getIntent().getStringExtra("mentorId");
        String mentorName = getIntent().getStringExtra("mentorName");
        String mentorEmail = getIntent().getStringExtra("mentorEmail");

        TextView nameTextView = findViewById(R.id.mentor_detail_name);
        TextView emailTextView = findViewById(R.id.mentor_detail_email);

        nameTextView.setText(mentorName);
        emailTextView.setText(mentorEmail);


        ImageView backArrow = findViewById(R.id.back_arrow);
        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent = new Intent(getApplicationContext(), MentorPage.class);
                startActivity(intent);
                finish(); // This will navigate back to the previous activity in the stack
            }
        });
    }
}