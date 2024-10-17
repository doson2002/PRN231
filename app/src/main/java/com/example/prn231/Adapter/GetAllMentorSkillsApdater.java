package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.R;

import java.util.List;

public class GetAllMentorSkillsApdater extends RecyclerView.Adapter<GetAllMentorSkillsApdater.ItemViewHolder>{
    private List<String> skillsList;

    public GetAllMentorSkillsApdater(List<String> skillsList) {
        this.skillsList = skillsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the skill item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skill, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Bind the skill name to the TextView
        String skill = skillsList.get(position);
        holder.skillText.setText(skill);
    }

    @Override
    public int getItemCount() {
        return skillsList != null ? skillsList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declare the view components as per the new layout
        TextView skillText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by their IDs as per the new layout
            skillText = itemView.findViewById(R.id.skill_text);
        }
    }
}
