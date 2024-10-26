package com.example.prn231.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.prn231.DTO.Member;
import com.example.prn231.DTO.ScheduleBooked;
import com.example.prn231.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleBookedAdapter extends RecyclerView.Adapter<ScheduleBookedAdapter.MyViewHolder> {

    private List<ScheduleBooked> scheduleBookedList;
    private Context context;

    public ScheduleBookedAdapter(List<ScheduleBooked> scheduleBookedList, Context context) {
        this.scheduleBookedList = scheduleBookedList;
        this.context = context;
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
                showFeedbackDialog(holder.itemView.getContext(),scheduleBooked);
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
}