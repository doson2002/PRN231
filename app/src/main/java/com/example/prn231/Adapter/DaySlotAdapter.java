package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.Slot;
import com.example.prn231.R;

import java.util.List;

public class DaySlotAdapter extends RecyclerView.Adapter<DaySlotAdapter.ViewHolder> {

    private List<Slot> slotList; // Danh sách các Slot của một ngày

    public DaySlotAdapter(List<Slot> slotList) {
        this.slotList = slotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Slot slot = slotList.get(position);

        // Set thời gian bắt đầu và kết thúc
        holder.tvStartTime.setText(slot.getStartTime());
        holder.tvEndTime.setText(slot.getEndTime());

        // Set tên mentor (giả sử thông tin mentor đã có từ dữ liệu)
        holder.textMentor.setText("Mentor 1");

        // Kiểm tra trạng thái đặt chỗ
        if (slot.isBook()) {
            holder.bookButton.setEnabled(false); // Nếu đã đặt thì nút bị vô hiệu
            holder.bookButton.setText("Booked");
        } else {
            holder.bookButton.setEnabled(true);
            holder.bookButton.setText("Book");
        }

        // Sự kiện click vào nút Book
        holder.bookButton.setOnClickListener(v -> {
            // Thực hiện logic đặt chỗ tại đây (ví dụ gọi API hoặc thay đổi trạng thái)
            // Sau khi đặt chỗ, cập nhật giao diện
            holder.bookButton.setText("Booked");
            holder.bookButton.setEnabled(false);
        });
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStartTime, tvEndTime, textMentor;
        Button bookButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            textMentor = itemView.findViewById(R.id.text_mentor);
            bookButton = itemView.findViewById(R.id.button_book);
        }
    }
}
