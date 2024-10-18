package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.Member;
import com.example.prn231.R;

import java.util.List;

public class MemberSearchAdapter extends RecyclerView.Adapter<MemberSearchAdapter.MemberViewHolder> {

    private List<Member> memberList;
    private OnMemberClickListener listener; // Khai báo listener

    public MemberSearchAdapter(List<Member> memberList,  OnMemberClickListener listener) {
        this.memberList = memberList;
        this.listener = listener; // Khởi tạo listener
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        // Hiển thị tên và email theo định dạng "memberName - memberEmail"
        holder.memberInfo.setText(member.getFullName() + " - " + member.getEmail());
        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            // Gọi phương thức callback
            listener.onMemberClick(member);
        });
    }
    // Cập nhật danh sách members
    public void updateMemberList(List<Member> newMembers) {
        this.memberList.clear();
        this.memberList.addAll(newMembers);
        notifyDataSetChanged();  // Thông báo cho RecyclerView rằng dữ liệu đã thay đổi
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView memberInfo;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            memberInfo = itemView.findViewById(R.id.memberInfo);
        }
    }

    public interface OnMemberClickListener {
        void onMemberClick(Member member); // Phương thức callback
    }

}