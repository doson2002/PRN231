package com.example.prn231.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.MentorDetail;
import com.example.prn231.MentorDetailBooking;
import com.example.prn231.Model.Schedule;
import com.example.prn231.R;

import java.util.List;

public class GetMentorDetailSlotsAdapter extends RecyclerView.Adapter<GetMentorDetailSlotsAdapter.ItemViewHolder>{
    private List<Schedule> scheduleList;
    private String mentorId;
    private String mentorName;

    public GetMentorDetailSlotsAdapter(List<Schedule> scheduleList, String mentorId, String mentorName) {
        this.scheduleList = scheduleList;
        this.mentorId = mentorId;
        this.mentorName = mentorName;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the skill item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_mentor_details, parent, false);

        return new GetMentorDetailSlotsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.slotTime.setText(schedule.getStartTime() != null && schedule.getEndTime() != null
                ? schedule.getStartTime() + "-" + schedule.getEndTime() : "N/A");

        holder.slotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the MentorDetailActivity
                Intent intent = new Intent(v.getContext(), MentorDetailBooking.class);

                // Pass the mentor details to the activity
                intent.putExtra("mentorId", mentorId);
                intent.putExtra("mentorName", mentorName);
                intent.putExtra("slotId", schedule.getId());
                intent.putExtra("slotStart", schedule.getStartTime());
                intent.putExtra("slotEnd", schedule.getEndTime());
                intent.putExtra("slotDate", schedule.getDate());
                intent.putExtra("slotType", schedule.getOnline() ? "Online" : "Offline");
                intent.putExtra("slotNote", schedule.getNote());

                // Start the activity
                v.getContext().startActivity(intent);
            }
        });

        if(schedule.getOnline()) {
            holder.slotType.setText("Online");
        } else {
            holder.slotType.setText("Offline");
        }

        if (schedule.getBooked()) {
            holder.slotStatusDot.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.booked_color));
            holder.slotStatusContent.setText("Booked");
        } else {
            holder.slotStatusDot.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.idle_color));
            holder.slotStatusContent.setText("Idle");
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList != null ? scheduleList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declare the view components as per the new layout
        TextView slotTime, slotType, slotStatusDot, slotStatusContent;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by their IDs as per the new layout
            slotTime = itemView.findViewById(R.id.tvMentorSLot);
            slotType = itemView.findViewById(R.id.tvMentorSLotType);
            slotStatusDot = itemView.findViewById(R.id.tvMentorSLotStatusDot);
            slotStatusContent = itemView.findViewById(R.id.tvMentorSLotStatusText);
        }
    }
}
