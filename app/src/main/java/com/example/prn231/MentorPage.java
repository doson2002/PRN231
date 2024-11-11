package com.example.prn231;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 10;
    private EditText searchEditText;
    private String currentSearchTerm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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


        searchEditText = findViewById(R.id.search_edit_text);
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

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Trigger search when the text changes
                currentSearchTerm = charSequence.toString();
                currentPage = 1; // Reset to first page
                itemList.clear(); // Clear current items
                loadNextPage(); // Load results based on search term
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void loadNextPage() {
        isLoading = true; // Set loading to true to prevent multiple calls

        SharedPreferences sharedPreferences = getSharedPreferences("PRN231", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken","");
        String authToken = "Bearer " + accessToken;

        Call<ResponseModel<Mentor>> call = mentorServices.getAllMentor(authToken, currentSearchTerm, currentPage, PAGE_SIZE);
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