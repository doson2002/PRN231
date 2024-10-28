package com.example.prn231.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.MentorDetailBooking;
import com.example.prn231.MentorEditSchedules;
import com.example.prn231.Model.Slot; // Import the Slot model
import com.example.prn231.R;

import java.util.List;

public class MentorOwnSchedules extends RecyclerView.Adapter<MentorOwnSchedules.ItemViewHolder> {
    private List<Slot> slotList; // Change type to Slot
    private Context context;

    public MentorOwnSchedules(Context context, List<Slot> slotList) {
        this.context = context;
        this.slotList = slotList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_mentor_without_book, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Slot slot = slotList.get(position);
        holder.slotTime.setText(slot.getStartTime() + " - " + slot.getEndTime());
        holder.slotType.setText(slot.isOnline() ? "Online" : "Offline");
        holder.slotDate.setText(slot.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MentorEditSchedules.class);
            intent.putExtra("slotId", slot.getId());
            intent.putExtra("slotStart", slot.getStartTime());
            intent.putExtra("slotEnd", slot.getEndTime());
            intent.putExtra("slotDate", slot.getDate());
            intent.putExtra("slotType", slot.isOnline() ? "Online" : "Offline");
            intent.putExtra("slotNote", slot.getNote());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView slotTime;
        TextView slotType;
        TextView slotDate; // New reference for the date

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            slotTime = itemView.findViewById(R.id.tvMentorSlot);
            slotType = itemView.findViewById(R.id.tvMentorSlotType); // Ensure this is defined
            slotDate = itemView.findViewById(R.id.tvSlotDate); // Initialize the new TextView
        }
    }
}