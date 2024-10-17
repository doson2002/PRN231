package com.example.prn231;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prn231.Fragment.Student.AccountFragment;
import com.example.prn231.Fragment.Student.GroupFragment;
import com.example.prn231.Fragment.Student.HomeStudentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavBottomStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nav_bottom_student);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set Fragment mặc định khi khởi tạo Activity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                    new HomeStudentFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        replaceFragment(new HomeStudentFragment());
                        break;
                    case R.id.addGroup:
                        replaceFragment(new GroupFragment());
                        break;
                    case R.id.account:
                        replaceFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}