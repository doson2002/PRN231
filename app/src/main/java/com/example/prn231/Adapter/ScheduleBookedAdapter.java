package com.example.prn231.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        holder.tvAddFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(holder.itemView.getContext());
            }
        });
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
    private void showFeedbackDialog(Context context) {
        // Tạo Dialog
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_feedback); // Sử dụng layout mà bạn đã cung cấp

        // Tìm các view bên trong dialog
        EditText editTextFeedback = dialog.findViewById(R.id.editTextProjectName);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBarInput);
        Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        ImageView closeButton = dialog.findViewById(R.id.close_button);

        // Đóng dialog khi nhấn vào nút "Hủy" hoặc biểu tượng "X"
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Xử lý khi người dùng nhấn nút "Tạo"
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = editTextFeedback.getText().toString();
                float rating = ratingBar.getRating();

                // Thực hiện hành động sau khi người dùng nhập feedback và đánh giá
                // Ví dụ: gửi feedback lên server hoặc cập nhật giao diện

                dialog.dismiss(); // Đóng dialog sau khi xác nhận
            }
        });

        // Hiển thị dialog
        dialog.show();
    }
}