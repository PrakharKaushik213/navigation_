package com.example.navigation_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class savePlace_activity extends AppCompatActivity {
    static ArrayList<String>places=new ArrayList<String>();
    static  ArrayList<LatLng> location=new ArrayList<LatLng>();
     static ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_place_activity);
        Intent lalo=getIntent();
        places.clear();
        location.clear();

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.navigation_demo", Context.MODE_PRIVATE);
        ArrayList<String>lat=new ArrayList<>();
        ArrayList<String>lon=new ArrayList<>();
        lat.clear();
        lon.clear();
        try{
            places=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            lat=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            lon=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons",ObjectSerializer.serialize(new ArrayList<String>())));

        }catch(Exception e){
            e.printStackTrace();
        }
        if(places.size()>0 &&lat.size()>0 && lon.size()>0){
            if(places.size()==lat.size() && places.size()==lon.size()){
               for(int i=0;i<lat.size();i++){
                   location.add(new LatLng(Double.parseDouble(lat.get(i)),Double.parseDouble(lon.get(i))));
               }
            }


        }
         else {
            places.add("Add new places");
            location.add(new LatLng(0, 0));
        }

        final int latitude=lalo.getIntExtra("latitude",0);
        final int longitude=lalo.getIntExtra("longitude",0);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setSelectedItemId(R.id.savePlace);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.location_stats:
                        Intent inten =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(inten);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.savePlace:
                        return true;

                    case R.id.map:
                        Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                        intent.putExtra("latitude",latitude);
                        intent.putExtra("longitude",longitude);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });


        ListView list=findViewById(R.id.saved_places);

         arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,places);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MapsActivity2.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("index",position);
                startActivity(intent);

            }
        });



    }
}
