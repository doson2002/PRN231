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

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Member> memberList;

    public MemberAdapter(List<Member> memberList) {
        this.memberList = memberList;
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
}