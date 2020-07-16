package com.example.navigation_demo;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        if (index == 0) {
            int latitude = intent.getIntExtra("latitude", 0);
            int longitude = intent.getIntExtra("longitude", 0);

            LatLng sydney = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(sydney).title("Yor are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    String address = "";

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {

                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            if (addressList.get(0).getThoroughfare() != null) {
                                address = addressList.get(0).getThoroughfare();
                            }
                            if (addressList.get(0).getSubThoroughfare() != null) {
                                address += addressList.get(0).getSubThoroughfare();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (address.equals("")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");
                        address += sdf.format(new Date());
                    }
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                    savePlace_activity.places.add(address);
                    savePlace_activity.location.add(latLng);
                    savePlace_activity.arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(MapsActivity2.this, "Location Registered", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.navigation_demo", Context.MODE_PRIVATE);
                    try{
                        ArrayList<String>latitude=new ArrayList<>();
                        ArrayList<String>longitude=new ArrayList<>();
                        for(LatLng cord:savePlace_activity.location){
                            latitude.add(Double.toString(cord.latitude));
                            longitude.add(Double.toString(cord.longitude));
                        }

                        sharedPreferences.edit().putString("places",ObjectSerializer.serialize(savePlace_activity.places)).apply();
                        sharedPreferences.edit().putString("lats",ObjectSerializer.serialize(latitude)).apply();
                        sharedPreferences.edit().putString("lons",ObjectSerializer.serialize(longitude)).apply();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Location visitedPlaces = new Location(LocationManager.GPS_PROVIDER);
            visitedPlaces.setLongitude(savePlace_activity.location.get(index).longitude);
            visitedPlaces.setLatitude(savePlace_activity.location.get(index).latitude);
            LatLng sydney = new LatLng(visitedPlaces.getLatitude(), visitedPlaces.getLongitude());

            mMap.addMarker(new MarkerOptions().position(sydney).title("you are heree"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }
}
