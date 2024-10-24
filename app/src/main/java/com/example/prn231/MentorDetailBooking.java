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

public class MentorDetailBooking extends AppCompatActivity {
    TextView tvmentorName, tvmentorSlot, tvslotType, tvslotNote, tvMentorDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_detail_booking);

        String mentorId = getIntent().getStringExtra("mentorId");
        String slotId = getIntent().getStringExtra("slotId");
        String mentorName = getIntent().getStringExtra("mentorName");
        String slotStart = getIntent().getStringExtra("slotStart");
        String slotEnd = getIntent().getStringExtra("slotEnd");
        String slotDate = getIntent().getStringExtra("slotDate");
        String slotType = getIntent().getStringExtra("slotType");
        String slotNote = getIntent().getStringExtra("slotNote");

        ImageView backArrow = findViewById(R.id.back_arrow);
        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent = new Intent(getApplicationContext(), MentorDetail.class);
                intent.putExtra("mentorId", mentorId);
                startActivity(intent);
                finish(); // This will navigate back to the previous activity in the stack
            }
        });



        tvmentorName = findViewById(R.id.tvMentorName);
        tvmentorSlot = findViewById(R.id.tvMentorSLot);
        tvslotType = findViewById(R.id.tvMentorSLotType);
        tvslotNote = findViewById(R.id.tvMentorNote);
        tvMentorDate = findViewById(R.id.tvMentorSLotDate);


        tvmentorName.setText(mentorName);
        tvmentorSlot.setText(slotStart + "-" + slotEnd);
        tvMentorDate.setText(slotDate);
        tvslotType.setText(slotType);
        tvslotNote.setText(slotNote);

    }
}