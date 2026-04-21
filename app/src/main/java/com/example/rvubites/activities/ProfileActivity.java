package com.example.rvubites.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rvubites.databinding.ActivityProfileBinding;
import com.example.rvubites.utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        refreshProfile();

        binding.btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            preferenceManager.clear();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
        binding.btnSavedAddresses.setOnClickListener(v -> showAddressDialog());
        
        binding.btnOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, OrdersActivity.class));
        });

        binding.btnAbout.setOnClickListener(v -> {
            Toast.makeText(this, "RVU Bites v1.0.0\nDeveloped for RVU Students", Toast.LENGTH_LONG).show();
        });
    }

    private void refreshProfile() {
        binding.tvProfileName.setText(preferenceManager.getUserName());
        if (mAuth.getCurrentUser() != null) {
            binding.tvProfileEmail.setText(mAuth.getCurrentUser().getEmail());
        }
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(preferenceManager.getUserName());
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                updateProfileInFirestore(newName);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Address");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setText(preferenceManager.getUserAddress());
        
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String address = input.getText().toString().trim();
            preferenceManager.setUserAddress(address);
            
            if (mAuth.getCurrentUser() != null) {
                db.collection("users").document(mAuth.getCurrentUser().getUid())
                    .update("address", address);
            }
            Toast.makeText(this, "Address updated", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateProfileInFirestore(String name) {
        if (mAuth.getCurrentUser() == null) return;

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .update(user)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.setUserName(name);
                    refreshProfile();
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}