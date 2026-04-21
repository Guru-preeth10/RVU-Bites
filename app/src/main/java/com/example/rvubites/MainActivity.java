package com.example.rvubites;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rvubites.activities.SplashActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Redirect to SplashActivity which is the actual entry point
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}