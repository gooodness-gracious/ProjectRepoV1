package com.example.cufacultyfacilityfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {

    Button addFaculty;
    Button modifyFaculty;
    Button deleteFaculty;
    Button addFacility;
    Button modifyFacility;
    Button deleteFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        addFaculty = (Button) findViewById(R.id.addButtonFaculty);
        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddFaculty.class));
            }
        });

        modifyFaculty = (Button) findViewById(R.id.modifyButtonFaculty);
        modifyFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ModifyFaculty.class));
            }
        });

        deleteFaculty = (Button) findViewById(R.id.deleteButtonFaculty);
        deleteFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteFaculty.class));
            }
        });

        addFacility = (Button) findViewById(R.id.addButtonFacility);
        addFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddFacility.class));
            }
        });

        modifyFacility = (Button) findViewById(R.id.modifyButtonFacility);
        modifyFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ModifyFacility.class));
            }
        });

        deleteFacility = (Button) findViewById(R.id.deleteButtonFacility);
        deleteFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteFacility.class));
            }
        });
    }
}