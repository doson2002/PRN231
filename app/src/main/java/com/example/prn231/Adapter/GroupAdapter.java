package com.example.prn231.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.Group;
import com.example.prn231.GroupDetailActivity;
import com.example.prn231.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groupList;
    private Context context;

    public GroupAdapter(List<Group> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.textGroupName.setText(group.getName());
        holder.textViewProjectName.setText("Project: "+ group.getProjectName());
        holder.textViewMentorName.setText("Mentor: " + group.getMentorName());
        holder.textViewLeaderName.setText("Leader: " + group.getLeaderName());

        // Xử lý sự kiện click vào từng item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupDetailActivity.class);
                intent.putExtra("groupId", group.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProjectName, textViewMentorName, textViewLeaderName, textViewStack,textGroupName;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            textGroupName = itemView.findViewById(R.id.textGroupName);
            textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
            textViewMentorName = itemView.findViewById(R.id.textViewMentorName);
            textViewLeaderName = itemView.findViewById(R.id.textViewLeaderName);
        }
    }
}
