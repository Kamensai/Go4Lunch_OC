package com.khamvongsa.victor.go4lunch.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khamvongsa.victor.go4lunch.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by <Victor Khamvongsa> on <18/11/2021>
 */
public class MapViewFragment extends Fragment {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng grenoble = new LatLng(45.188529, 5.724524);
                mMap.addMarker(new MarkerOptions()
                        .position(grenoble)
                        .title("Marker in Grenoble"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(grenoble, 16));
            }
        });
        // Inflate the layout for this fragment

        return view;
    }

}
