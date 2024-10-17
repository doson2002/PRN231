package com.example.prn231.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.MentorDetail;
import com.example.prn231.Model.Mentor;
import com.example.prn231.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class GetAllMentorAdapter extends RecyclerView.Adapter<GetAllMentorAdapter.ItemViewHolder>  {
    private List<Mentor> mentorList;

    public GetAllMentorAdapter(List<Mentor> mentorList) {
        this.mentorList = mentorList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Mentor mentor = mentorList.get(position); // Assuming you have a Mentor list

        // Set mentor image, name, and email
        holder.mentorName.setText(mentor.getFullName());
        holder.mentorEmail.setText(mentor.getEmail());

        holder.mentorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the MentorDetailActivity
                Intent intent = new Intent(v.getContext(), MentorDetail.class);

                // Pass the mentor details to the activity
                intent.putExtra("mentorId", mentor.getId());
                intent.putExtra("mentorName", mentor.getFullName());
                intent.putExtra("mentorEmail", mentor.getEmail());

                // Start the activity
                v.getContext().startActivity(intent);
            }
        });

        // If you're loading an image dynamically, use libraries like Glide or Picasso
        // Glide.with(context).load(mentor.getImageUrl()).into(holder.mentorImage);

        // Add skills to FlexboxLayout
        holder.mentorSkillsFlexbox.removeAllViews(); // Clear previous views to prevent duplication
        for (String skill : mentor.getSkills()) {
            TextView skillView = (TextView) LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_skill, holder.mentorSkillsFlexbox, false);
            skillView.setText(skill);
            holder.mentorSkillsFlexbox.addView(skillView);
        }
    }

    @Override
    public int getItemCount() {
        return mentorList != null ? mentorList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declare the view components as per the new layout
        ImageView mentorImage;
        TextView mentorName, mentorEmail, skillsLabel;
        FlexboxLayout mentorSkillsFlexbox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by their IDs as per the new layout
            mentorImage = itemView.findViewById(R.id.mentor_image);
            mentorName = itemView.findViewById(R.id.mentor_name);
            mentorEmail = itemView.findViewById(R.id.mentor_email);
            skillsLabel = itemView.findViewById(R.id.skills_label);
            mentorSkillsFlexbox = itemView.findViewById(R.id.mentor_skills_flexbox);
        }
    }
}
