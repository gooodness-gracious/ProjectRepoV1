package com.example.cufacultyfacilityfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton facultyFinder;
    ImageButton facilityFinder;
    ImageButton classFinder;
    ImageButton adminLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facultyFinder = (ImageButton) findViewById(R.id.facultyfinder);
        facultyFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), teacher_finder.class));
            }
        });

        classFinder = (ImageButton) findViewById(R.id.classfinder);
        classFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), classroom_finder.class));
            }
        });

        facilityFinder = (ImageButton) findViewById(R.id.facilityfinder);
        facilityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), facility_finder.class));
            }
        });

        adminLogin=(ImageButton) findViewById(R.id.adminButton);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminPage.class));
            }
        });
    }
}