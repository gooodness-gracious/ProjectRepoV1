package com.example.cufacultyfacilityfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddFaculty extends AppCompatActivity {

    private static final String TAG = "AddFaculty";

    EditText edName;
    Button fsSubmit;
    Spinner spDept;
    Spinner spPost;
    Spinner spStart;
    Spinner spEnd;
    EditText edPhone;
    EditText edEmail;
    EditText edCabin;
    EditText edRoom;
    EditText edFloor;
    Spinner spBuilding;

    private static final String KEY_NAME="Name";
    private static final String KEY_DEPT="Department";
    private static final String KEY_POST="Position";
    private static final String KEY_START="Start Time";
    private static final String KEY_END="End Time";
    private static final String KEY_PHONE="Phone Number";
    private static final String KEY_EMAIL="Email Address";
    private static final String KEY_STAFFROOM_LOCATION="Staffroom Location";
    private static final String KEY_CABIN="Cabin Number";
    private static final String KEY_ROOM="Room Number";
    private static final String KEY_FLOOR="Floor Number";
    private static final String KEY_BUILDING="Building Name";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        edName=(EditText) findViewById(R.id.teacherName);
        fsSubmit=(Button) findViewById(R.id.fbSubmit);
        spDept= (Spinner) findViewById(R.id.SpDepartment);
        spPost= (Spinner) findViewById(R.id.spPos);
        spStart = (Spinner) findViewById(R.id.spStartTime);
        spEnd = (Spinner) findViewById(R.id.spEndTime);
        edPhone=(EditText) findViewById(R.id.phoneEntry);
        edEmail=(EditText) findViewById(R.id.emailEntry);
        edCabin=(EditText) findViewById(R.id.cabinEntry);
        edRoom=(EditText) findViewById(R.id.roomEntry);
        edFloor=(EditText) findViewById(R.id.scFloorEntry);
        spBuilding=(Spinner) findViewById(R.id.buildingSpinner);




        fsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=edName.getText().toString();
                Intent intent = new Intent(getApplicationContext(), AddSchedule.class);
                intent.putExtra("Teacher Name", name);


                String dept=spDept.getSelectedItem().toString();
                String post=spPost.getSelectedItem().toString();
                String startTime=spStart.getSelectedItem().toString();
                String endTime=spEnd.getSelectedItem().toString();
                String phone=edPhone.getText().toString();
                String email=edEmail.getText().toString();
                String cabin=edCabin.getText().toString();
                String room=edRoom.getText().toString();
                String floor=edFloor.getText().toString();
                String building=spBuilding.getSelectedItem().toString();

                Map<String, Object> genDeets=new HashMap<>();

                genDeets.put(KEY_NAME, name);
                genDeets.put(KEY_DEPT, dept);
                genDeets.put(KEY_POST, post);
                genDeets.put(KEY_START, startTime);
                genDeets.put(KEY_END, endTime);
                genDeets.put(KEY_PHONE, phone);
                genDeets.put(KEY_EMAIL, email);

                Map<String, Object> staffroomLocation=new HashMap<>();
                staffroomLocation.put(KEY_CABIN, cabin);
                staffroomLocation.put(KEY_ROOM, room);
                staffroomLocation.put(KEY_FLOOR, floor);
                staffroomLocation.put(KEY_BUILDING, building);
                genDeets.put(KEY_STAFFROOM_LOCATION, staffroomLocation);

                db.collection("General Details").document(name).set(genDeets)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddFaculty.this, "In denial", Toast.LENGTH_SHORT).show();

                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddFaculty.this, "go to hell", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}