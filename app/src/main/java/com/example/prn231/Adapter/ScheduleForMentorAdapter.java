package com.example.prn231.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prn231.Api.ApiEndPoint;
import com.example.prn231.DTO.ScheduleBooked;
import com.example.prn231.Model.Schedule;
import com.example.prn231.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleForMentorAdapter extends RecyclerView.Adapter<ScheduleForMentorAdapter.ViewHolder> {
    private Context context;
    private List<ScheduleBooked> scheduleList;
    private final OnScheduleClickListener listener;


    public ScheduleForMentorAdapter(Context context,List<ScheduleBooked> scheduleList,OnScheduleClickListener listener) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule_booked_mentor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleBooked schedule = scheduleList.get(position);

        // Set date, group name, and booking time
        holder.tvDate.setText(schedule.getDate());
        holder.tvGroupName.setText(schedule.getGroupName());
        holder.tvBookingTime.setText(schedule.getStartTime() + " - " + schedule.getEndTime());

        // Set feedback status
        if (schedule.isFeedBack()) {
            holder.tvAddFeedback.setVisibility(View.GONE);
            holder.tvFeedBackStatus.setText("Already feedback");
        } else {
            holder.tvAddFeedback.setVisibility(View.VISIBLE);
            holder.tvFeedBackStatus.setText("");
        }

        // Set acceptance status
        switch (schedule.getIsAccepted()) {
            case 0:
                holder.tvStatus.setText("Pending - ");
                holder.tvChangeStatus.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.tvStatus.setText("Accepted");
                holder.tvChangeStatus.setVisibility(View.GONE);
                break;
            case 2:
                holder.tvStatus.setText("Rejected");
                holder.tvChangeStatus.setVisibility(View.GONE);
                break;
        }

        // Set onClickListener for change status
        holder.tvChangeStatus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStatusChangeClick(schedule);
            }
        });
        holder.tvAddFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(holder.itemView.getContext(),schedule);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvGroupName, tvBookingTime, tvStatus, tvAddFeedback, tvFeedBackStatus, tvChangeStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddFeedback = itemView.findViewById(R.id.tvAddFeedback);
            tvFeedBackStatus = itemView.findViewById(R.id.tvFeedBackStatus);
            tvChangeStatus = itemView.findViewById(R.id.tvChangeStatus);
        }
    }
    private void showFeedbackDialog(Context context, ScheduleBooked scheduleBooked) {
        // Tạo Dialog
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_feedback); // Sử dụng layout mà bạn đã cung cấp
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                int rating = (int) ratingBar.getRating();
                String scheduleId = scheduleBooked.getId();
                String groupId = scheduleBooked.getGroupId();
                sendFeedback(feedback,rating,scheduleId,groupId);

                // Thực hiện hành động sau khi người dùng nhập feedback và đánh giá
                // Ví dụ: gửi feedback lên server hoặc cập nhật giao diện

                dialog.dismiss(); // Đóng dialog sau khi xác nhận
            }
        });

        // Hiển thị dialog
        dialog.show();
    }
    // Hàm gửi feedback lên server
    private void sendFeedback(String feedback, float rating, String scheduleId,String  groupId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("groupId", groupId);
            requestBody.put("content", feedback);
            requestBody.put("scheduleId", scheduleId);
            requestBody.put("rating", rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ApiEndPoint.CREATE_FEEDBACK;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                response -> {
                    Toast.makeText(context, "Gửi feedback thành công!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(context, "Lỗi khi gửi feedback!", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (accessToken != null) {
                    headers.put("Authorization", "Bearer " + accessToken);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public interface OnScheduleClickListener {
        void onStatusChangeClick(ScheduleBooked schedule);
    }
}