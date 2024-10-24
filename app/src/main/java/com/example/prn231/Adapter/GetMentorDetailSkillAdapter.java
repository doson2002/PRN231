package com.example.prn231.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prn231.Model.Certificate;
import com.example.prn231.Model.Skill;
import com.example.prn231.R;

import java.util.List;

public class GetMentorDetailSkillAdapter extends RecyclerView.Adapter<GetMentorDetailSkillAdapter.ItemViewHolder>{
    private List<Skill> skillsList;

    public GetMentorDetailSkillAdapter(List<Skill> skillsList) {
        this.skillsList = skillsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the skill item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skill_mentor_details, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Bind the skill name to the TextView
        Skill skill = skillsList.get(position);
        holder.skillName.setText(skill.getSkillName() != null ? skill.getSkillName() : "N/A");
        holder.skillDescription.setText(skill.getSkillDesciption() != null ? "- " + skill.getSkillDesciption() : "N/A");
        holder.skillCategory.setText(skill.getSkillCategoryType() != null ? skill.getSkillCategoryType() : "N/A");

        List<Certificate> cerList = skill.getCetificates();

        for (Certificate cer : cerList) {
            // Create a new ImageView
            ImageView certificateImageView = new ImageView(holder.itemView.getContext());

            // Set layout parameters for the ImageView
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(8, 0, 8, 0); // Add margins between images if needed
            certificateImageView.setLayoutParams(layoutParams);

            // Set the image resource

            Glide.with(holder.itemView.getContext())
                    .load(cer.getCetificateImageUrl())
                    .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                    .error(R.drawable.ic_launcher_background) // Optional error image
                    .into(certificateImageView);

            // Optionally, adjust the scale and size of the ImageView
            certificateImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            certificateImageView.setAdjustViewBounds(true);
            certificateImageView.setMaxWidth(200); // Example width
            certificateImageView.setMaxHeight(200); // Example height

            // Add the ImageView to the certificates container
            holder.certificatesContainer.addView(certificateImageView);
        }
    }

    @Override
    public int getItemCount() {
        return skillsList != null ? skillsList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declare the view components as per the new layout
        TextView skillName, skillDescription, skillCategory;
        LinearLayout certificatesContainer;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by their IDs as per the new layout
            skillName = itemView.findViewById(R.id.tvSkillItemName);
            skillDescription = itemView.findViewById(R.id.tvSkillDescription);
            skillCategory = itemView.findViewById(R.id.tvSkillCategory);
            certificatesContainer = itemView.findViewById(R.id.certificatesContainer);
        }
    }
}