package com.example.hw2;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<Record> records;
    private GoogleMap mMap;
    private LatLng[] locations;
    private ListView maps_LSTV_list;
    private Button maps_BT_back;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RecordManager recordManager = new RecordManager(MapsActivity.this);
        records = recordManager.getRecords();


        maps_LSTV_list = findViewById(R.id.maps_LSTV_list);
        maps_BT_back = findViewById(R.id.maps_BT_back);
        maps_BT_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locations = new LatLng[records.size()];
        fillPlacesList();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (records.size() == 0)
            return;
        mMap = googleMap;
        Record r;
        for (int i = 0; i < locations.length; i++) {
            r = records.get(i);
            locations[i] = new LatLng(r.getLatitude(), r.getLongitude());
            mMap.addMarker(new MarkerOptions().position(locations[i]).title("" + (i + 1) + ": " + r.toString()).icon(BitmapDescriptorFactory.defaultMarker()));

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 18.0f));

    }


    private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[position], 18.0f));

        }
    };

    private void fillPlacesList() {
        // Set up an ArrayAdapter to convert likely places into TextViews to populate the ListView
        ArrayAdapter<Record> placesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);
        maps_LSTV_list.setAdapter(placesAdapter);
        maps_LSTV_list.setOnItemClickListener(listClickedHandler);
    }
}
