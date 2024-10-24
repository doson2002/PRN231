package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.Member;
import com.example.prn231.DTO.ScheduleBooked;
import com.example.prn231.R;

import java.util.List;

public class ScheduleBookedAdapter extends RecyclerView.Adapter<ScheduleBookedAdapter.MyViewHolder> {

    private List<ScheduleBooked> scheduleBookedList;

    public ScheduleBookedAdapter(List<ScheduleBooked> scheduleBookedList) {
        this.scheduleBookedList = scheduleBookedList;
    }

    @NonNull
    @Override
    public ScheduleBookedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_booked, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleBookedAdapter.MyViewHolder holder, int position) {
        ScheduleBooked scheduleBooked = scheduleBookedList.get(position);
        // Hiển thị tên và email theo định dạng "memberName - memberEmail"
        holder.tvDate.setText(scheduleBooked.getDate());
        holder.tvBookingTime.setText(scheduleBooked.getStartTime()+" - " +scheduleBooked.getEndTime());
        holder.tvGroupName.setText(scheduleBooked.getGroupName());
        if(scheduleBooked.isFeedBack()){
            holder.tvFeedBackStatus.setText("Đã feedback");
        }else {
            holder.tvFeedBackStatus.setText("Chưa feedback");
            holder.tvAddFeedback.setVisibility(View.VISIBLE);
        }
    }
    // Cập nhật danh sách members
    public void updateScheduleList(ScheduleBooked scheduleBooked) {
        this.scheduleBookedList.add(scheduleBooked);
        notifyDataSetChanged();  // Thông báo cho RecyclerView rằng dữ liệu đã thay đổi
    }

    @Override
    public int getItemCount() {
        return scheduleBookedList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingTime,tvFeedBackStatus, tvGroupName, tvDate,tvAddFeedback;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvFeedBackStatus = itemView.findViewById(R.id.tvFeedBackStatus);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAddFeedback = itemView.findViewById(R.id.tvAddFeedback);

        }
    }
}