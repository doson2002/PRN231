package com.example.prn231;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prn231.Adapter.GetAllMentorAdapter;
import com.example.prn231.Api.MentorApi;
import com.example.prn231.Model.Mentor;
import com.example.prn231.Model.ResponseModel;
import com.example.prn231.Services.MentorServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentorPage extends AppCompatActivity {

    MentorApi mentorServices;
    RecyclerView recyclerViewMentors;
    private GetAllMentorAdapter mentorAdapter;
    private List<Mentor> itemList = new ArrayList<>();
    //=======

//    private CategoryAdapter categoryAdapter;
//    RecyclerView recyclerViewSkills;
//    private List<String> categoryList = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_page);

        ImageView backArrow = findViewById(R.id.back_arrow);

        // Set an OnClickListener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                Intent intent = new Intent(getApplicationContext(), NavBottomStudentActivity.class);
                startActivity(intent);
                finish(); // This will navigate back to the previous activity in the stack
            }
        });

        mentorServices = MentorServices.getMentorApi();
        recyclerViewMentors = findViewById(R.id.recyclerViewMentors);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMentors.setLayoutManager(layoutManager);
        mentorAdapter = new GetAllMentorAdapter(itemList);
        recyclerViewMentors.setAdapter(mentorAdapter);

        recyclerViewMentors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadNextPage(); // Load more data when reaching the end
                    }
                }
            }
        });

        // Load the first page of data
        loadNextPage();
    }

    private void loadNextPage() {
        isLoading = true; // Set loading to true to prevent multiple calls

        String authToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJzdHJpbmciLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiIwIiwiUm9sZSI6IjAiLCJVc2VySWQiOiIzNDFhODI3NC0wYWMyLTQxMmEtOTgyNC1lMzA3YThhMzEwZDAiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic3RyaW5nIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9leHBpcmVkIjoiMTAvMTcvMjAyNCAyMDoxNDoyMCIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWVpZGVudGlmaWVyIjoiMzQxYTgyNzQtMGFjMi00MTJhLTk4MjQtZTMwN2E4YTMxMGQwIiwiZXhwIjoxNzI5MTk5MzYwLCJpc3MiOiJodHRwOi8vMTAzLjE2Mi4xNC4xMTY6ODA4MCIsImF1ZCI6Imh0dHA6Ly8xMDMuMTYyLjE0LjExNjo4MDgwIn0.7f3XP9Iows4EgO7cwcipY1GB6T_uTvq4FFKd2bZ0KMU"; // Replace with actual token
        Call<ResponseModel<Mentor>> call = mentorServices.getAllMentor(authToken, "", currentPage, PAGE_SIZE);
        call.enqueue(new Callback<ResponseModel<Mentor>>() {
            @Override
            public void onResponse(Call<ResponseModel<Mentor>> call, Response<ResponseModel<Mentor>> response) {
                List<Mentor> products = response.body().getValue().getItems();
                if (products == null || products.isEmpty()) {
                    isLastPage = true; // No more pages if there are no products
                    return;
                }

                runOnUiThread(() -> {
                    itemList.addAll(products);
                    mentorAdapter.notifyDataSetChanged();
                });
                currentPage++; // Increment the current page

                // Check if this is the last page
                if (products.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
                isLoading = false; // Reset loading state
            }

            @Override
            public void onFailure(Call<ResponseModel<Mentor>> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
                isLoading = false; // Reset loading state on failure
            }
        });
    }
}