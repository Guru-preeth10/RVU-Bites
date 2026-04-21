package com.example.rvubites.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rvubites.R;
import com.example.rvubites.databinding.ActivityTrackOrderBinding;
import com.example.rvubites.models.Order;
import com.google.firebase.firestore.FirebaseFirestore;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.List;

public class TrackOrderActivity extends AppCompatActivity {

    private ActivityTrackOrderBinding binding;
    private Order order;
    private Marker deliveryMarker;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int step = 0;
    private final int TOTAL_STEPS = 5; 
    private FirebaseFirestore db;

    // Simulated coordinates (Example: RVU Campus area)
    private final GeoPoint RESTAURANT_POS = new GeoPoint(12.9237, 77.5844);
    private final GeoPoint USER_POS = new GeoPoint(12.9347, 77.6101);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        
        binding = ActivityTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        order = (Order) getIntent().getSerializableExtra("order");

        setupMap();
        startTrackingSimulation();

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void setupMap() {
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK);
        binding.mapView.setMultiTouchControls(true);

        IMapController mapController = binding.mapView.getController();
        mapController.setZoom(16.0);
        mapController.setCenter(RESTAURANT_POS);

        Marker restMarker = new Marker(binding.mapView);
        restMarker.setPosition(RESTAURANT_POS);
        restMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        restMarker.setTitle("Restaurant");
        binding.mapView.getOverlays().add(restMarker);

        Marker userMarker = new Marker(binding.mapView);
        userMarker.setPosition(USER_POS);
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        userMarker.setTitle("You");
        binding.mapView.getOverlays().add(userMarker);

        deliveryMarker = new Marker(binding.mapView);
        deliveryMarker.setPosition(RESTAURANT_POS);
        deliveryMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        deliveryMarker.setTitle("Delivery Partner");
        binding.mapView.getOverlays().add(deliveryMarker);

        Polyline line = new Polyline();
        List<GeoPoint> pts = new ArrayList<>();
        pts.add(RESTAURANT_POS);
        pts.add(USER_POS);
        line.setPoints(pts);
        binding.mapView.getOverlays().add(line);
    }

    private void startTrackingSimulation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (step <= TOTAL_STEPS) {
                    double lat = RESTAURANT_POS.getLatitude() + (USER_POS.getLatitude() - RESTAURANT_POS.getLatitude()) * (double)step / TOTAL_STEPS;
                    double lon = RESTAURANT_POS.getLongitude() + (USER_POS.getLongitude() - RESTAURANT_POS.getLongitude()) * (double)step / TOTAL_STEPS;
                    
                    GeoPoint nextPos = new GeoPoint(lat, lon);
                    deliveryMarker.setPosition(nextPos);
                    binding.mapView.invalidate();

                    updateStatus(step);
                    
                    step++;
                    handler.postDelayed(this, 2000); 
                }
            }
        }, 1000);
    }

    private void updateStatus(int currentStep) {
        if (currentStep < 2) {
            binding.tvStatus.setText("Preparing your order...");
            binding.tvStatus.setTextColor(getResources().getColor(R.color.primary));
        } else if (currentStep < TOTAL_STEPS) {
            binding.tvStatus.setText("Delivery Partner is on the way");
            binding.tvStatus.setTextColor(getResources().getColor(R.color.primary));
        } else {
            // Success/Delivered state
            binding.tvStatus.setText("✓ Order Delivered Successfully!");
            binding.tvStatus.setTextColor(getResources().getColor(R.color.success));
            binding.tvTime.setText("Just Now");
            binding.progressIndicator.setIndeterminate(false);
            binding.progressIndicator.setProgress(100);
            binding.progressIndicator.setIndicatorColor(getResources().getColor(R.color.success));
            
            updateOrderInFirestore();
            Toast.makeText(this, "Enjoy your meal!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateOrderInFirestore() {
        if (order != null && order.getOrderId() != null) {
            db.collection("orders").document(order.getOrderId())
                    .update("status", "Delivered")
                    .addOnSuccessListener(aVoid -> {
                        // Status updated in background
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }
}