package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.DaySlot;
import com.example.prn231.R;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private List<DaySlot> daySlotList;

    public CalendarAdapter(List<DaySlot> daySlotList) {
        this.daySlotList = daySlotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DaySlot daySlot = daySlotList.get(position);

        // Set ngày cho item
        holder.dateTextView.setText(daySlot.getDate());

        // Khởi tạo SlotAdapter cho từng ngày
        DaySlotAdapter slotAdapter = new DaySlotAdapter(daySlot.getSlotList());
        holder.recyclerViewSlots.setAdapter(slotAdapter);
        holder.recyclerViewSlots.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return daySlotList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        RecyclerView recyclerViewSlots;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.text_date);
            recyclerViewSlots = itemView.findViewById(R.id.recycler_view_slots);
        }
    }
}
