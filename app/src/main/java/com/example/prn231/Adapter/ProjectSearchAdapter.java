package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.Member;
import com.example.prn231.DTO.Project;
import com.example.prn231.R;

import java.util.List;

public class ProjectSearchAdapter extends RecyclerView.Adapter<ProjectSearchAdapter.MyViewHolder> {

    private List<Project> projectList;
    private ProjectSearchAdapter.OnProjectClickListener listener; // Khai báo listener

    public ProjectSearchAdapter(List<Project> projectList,  ProjectSearchAdapter.OnProjectClickListener listener) {
        this.projectList = projectList;
        this.listener = listener; // Khởi tạo listener
    }

    @NonNull
    @Override
    public ProjectSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ProjectSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectSearchAdapter.MyViewHolder holder, int position) {
        Project project = projectList.get(position);
        // Hiển thị tên và email theo định dạng "memberName - memberEmail"
        holder.projectInfo.setText(project.getName() + " - " + project.getDescription());
        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            // Gọi phương thức callback
            listener.onProjectClick(project);
        });
    }
    // Cập nhật danh sách members
    public void updateProjectList(List<Project> newProject) {
        this.projectList.clear();
        this.projectList.addAll(newProject);
        notifyDataSetChanged();  // Thông báo cho RecyclerView rằng dữ liệu đã thay đổi
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView projectInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            projectInfo = itemView.findViewById(R.id.memberInfo);
        }
    }

    public interface OnProjectClickListener {
        void onProjectClick(Project project); // Phương thức callback
    }

}