package com.example.navigation_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
        LocationManager locationManager;
        LocationListener locationListener;
        TextView longitudeText;
        TextView latitudeText;
        TextView accuracyText;
        TextView altitudeText;
        TextView addressText;
        int lo,la;
    String latitude, longitude, accuracy, altitude;

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }

            }
        }

        public void updateLocation(Location location) {
            latitude = Double.toString(location.getLatitude());
            la= (int) location.getLatitude();
            longitude = Double.toString(location.getLongitude());
            lo=(int)location.getLongitude();
            accuracy = Double.toString(location.getAccuracy());
            altitude = Double.toString(location.getAltitude());
            latitudeText.setText("Latitude:" + latitude);
            longitudeText.setText("Longitude:" + longitude);
            accuracyText.setText("Accuracy:" + accuracy);
            altitudeText.setText("Altitude:" + altitude);
            addressText.setText("Sorry no data available :(");
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addressList != null) {
                    String address = "";
                    if (addressList.get(0).getThoroughfare() != null) {
                        address += addressList.get(0).getThoroughfare() + " ";

                    }
                    if (addressList.get(0).getPostalCode() != null) {
                        address += addressList.get(0).getPostalCode() + " ";

                    }
                    if (addressList.get(0).getAdminArea() != null) {
                        address += addressList.get(0).getAdminArea() + " ";
                    }
                    addressText.setText("Address:"+address);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            longitudeText = findViewById(R.id.longitudetext);
            latitudeText = findViewById(R.id.lattext);
            accuracyText = findViewById(R.id.acctext);
            altitudeText = findViewById(R.id.alttext);
            addressText = findViewById(R.id.addresstext);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
            bottomNavigationView.setSelectedItemId(R.id.location_stats);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.location_stats:
                            return true;
                        case R.id.map:
                            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                            intent.putExtra("latitude",la);
                            intent.putExtra("longitude",lo);
                             startActivity(intent);
                            overridePendingTransition(0, 0);
                            return true;

                    }
                    return false;
                }
            });


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.i("Location", location.toString());
                    updateLocation(location);

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    updateLocation(lastLocation);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }
