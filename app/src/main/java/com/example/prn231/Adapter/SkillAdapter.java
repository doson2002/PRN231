package com.example.prn231.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.DTO.Group;
import com.example.prn231.DTO.SkillMentor;
import com.example.prn231.GroupDetailActivity;
import com.example.prn231.Model.Skill;
import com.example.prn231.R;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyViewHolder> {

    private List<SkillMentor> skillList;
    private Context context;

    public SkillAdapter(List<SkillMentor> skillList, Context context) {
        this.skillList = skillList;
        this.context = context;
    }

    @NonNull
    @Override
    public SkillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill_mentor, parent, false);
        return new SkillAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillAdapter.MyViewHolder holder, int position) {
        SkillMentor skill = skillList.get(position);
        holder.textSkillName.setText(skill.getSkillName());
        holder.textViewType.setText("Type: "+ skill.getSkillCategoryType());
        holder.textViewDescription.setText("Description: " + skill.getSkillDescription());



    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textSkillName, textViewType, textViewDescription;
        ImageView imgAddSkill;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textSkillName = itemView.findViewById(R.id.textSkillName);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
