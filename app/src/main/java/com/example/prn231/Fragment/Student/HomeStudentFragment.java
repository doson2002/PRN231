package com.example.prn231.Fragment.Student;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prn231.LoginActivity;
import com.example.prn231.R;
import com.google.android.material.navigation.NavigationView;


public class HomeStudentFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_student, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);

        // Setup Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            // Add the hamburger menu icon and set up the toggle to open the drawer
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        // Get the header view of the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Find the logout button in the header
        ImageView logoutButton = headerView.findViewById(R.id.logout_button);
        // Set onClick listener for the logout button
        logoutButton.setOnClickListener(v -> {
            // Handle logout logic here (e.g., clearing user session, navigating to login screen)
            Toast.makeText(getContext(), "Logout clicked", Toast.LENGTH_SHORT).show();

            // Example: clear the user's session and navigate to login activity
            performLogout();

            // Close the drawer after logout
            drawerLayout.closeDrawer(GravityCompat.START);
        });

    }

    private void performLogout() {
        // Clear session data (this could be shared preferences, token, etc.)
        // Navigate to login screen or close the app
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void signUserOut(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PRN231", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

// Xóa trạng thái đăng nhập
        editor.putBoolean("isLoggedIn", false);
        editor.remove("accessToken");  // Xóa token nếu có
        editor.remove("email");
        editor.remove("role");
        editor.apply();

        // Return User Back To Home:
        Intent goToHome = new Intent(requireContext(), LoginActivity.class);
        startActivity(goToHome);
    }
}