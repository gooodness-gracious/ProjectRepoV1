package com.example.cufacultyfacilityfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddSchedule extends AppCompatActivity {

    EditText edName;
    Spinner spScDay;
    Spinner spScPeriod;
    Spinner spScStartTime;
    Spinner spScEndTime;
    EditText edScSubjectName;
    EditText edScRoomNumber;
    EditText edScRoomName;
    EditText edScFloorNumber;
    Spinner spScBuilding;
    Button scBtnAddPeriod;
    Button scBtnSubmit;

    private static final String KEY_DAY="Day";
    private static final String KEY_PERIOD="Period";
    private static final String KEY_START="Start Time";
    private static final String KEY_END="End Time";
    private static final String KEY_SUBJECTNAME="Subject Name";
    private static final String KEY_CLASSROOM_LOCATION="Classroom Location";
    private static final String KEY_ROOMNAME="Room Name";
    private static final String KEY_ROOMNUMBER="Room Number";
    private static final String KEY_FLOOR="Floor Number";
    private static final String KEY_BUILDING="Building Name";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        edName=(EditText) findViewById(R.id.teacherName);

        spScDay=(Spinner) findViewById(R.id.scDaySpin);
        spScPeriod = (Spinner) findViewById(R.id.scPeriodSpin);
        spScStartTime=(Spinner) findViewById(R.id.scStartTime);
        spScEndTime=(Spinner) findViewById(R.id.scEndTime);
        edScSubjectName=(EditText) findViewById(R.id.scSubjectEntry);
        edScRoomNumber=(EditText) findViewById(R.id.scRoomNumber);
        edScRoomName=(EditText) findViewById(R.id.scRoomName);
        edScFloorNumber=(EditText) findViewById(R.id.scFloorEntry);
        spScBuilding=(Spinner) findViewById(R.id.scBuildingSpin);

        scBtnAddPeriod=(Button) findViewById(R.id.scAddPeriod);
        scBtnSubmit=(Button) findViewById(R.id.scSubmitButton);

        scBtnSubmit.setEnabled(false);

        // get the teacher name from AddFaculty.java
        Intent intent = getIntent();
        String name = intent.getStringExtra("Teacher Name");

        scBtnAddPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day=spScDay.getSelectedItem().toString();
                String period=spScPeriod.getSelectedItem().toString();
                String startTime=spScStartTime.getSelectedItem().toString();
                String endTime=spScEndTime.getSelectedItem().toString();
                String subjectName=edScSubjectName.getText().toString();
                String roomNumber=edScRoomNumber.getText().toString();
                String roomName=edScRoomName.getText().toString();
                String floorNumber=edScFloorNumber.getText().toString();
                String buildingName=spScBuilding.getSelectedItem().toString();

                Map<String, Object> dayMap=new HashMap<>();
                Map<String, Object> periodMap=new HashMap<>();
                Map<String, Object> classroomLocation=new HashMap<>();

                classroomLocation.put(KEY_ROOMNAME, roomName);
                classroomLocation.put(KEY_ROOMNUMBER, roomNumber);
                classroomLocation.put(KEY_FLOOR, floorNumber);
                classroomLocation.put(KEY_BUILDING, buildingName);

                periodMap.put(KEY_PERIOD, period);
                periodMap.put(KEY_START, startTime);
                periodMap.put(KEY_END, endTime);
                periodMap.put(KEY_SUBJECTNAME, subjectName);
                periodMap.put(KEY_CLASSROOM_LOCATION,classroomLocation);

                dayMap.put(period, periodMap);

                db.collection("General Details").document(name).collection("Schedule").document(day).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // If the document exists then update the database, else create a new document for the same.
                                if (documentSnapshot.exists()) {
                                    db.collection("General Details").document(name).collection("Schedule").document(day).update(dayMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AddSchedule.this, "Maybe....", Toast.LENGTH_SHORT).show();
                                                    clearEntries();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddSchedule.this, "go to hell" + e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    db.collection("General Details").document(name).collection("Schedule").document(day).set(dayMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AddSchedule.this, "Maybe....", Toast.LENGTH_SHORT).show();
                                                    clearEntries();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddSchedule.this, "go to hell" + e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });


                }
        });

        scBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day=spScDay.getSelectedItem().toString();

                db.collection("General Details").document(name).collection("Schedule").document(day).update(KEY_DAY, day)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddSchedule.this, "Check Matey", Toast.LENGTH_SHORT).show();
                                spScDay.setSelection(((ArrayAdapter)spScDay.getAdapter()).getPosition(spScDay.getSelectedItem().toString())+1);
                                spScPeriod.setSelection(0);
                                if(day=="Saturday"){
                                    startActivity(new Intent(getApplicationContext(), AddFaculty.class));
                                    Toast.makeText(AddSchedule.this, "Add New Teacher Details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddSchedule.this, "go to hell pt 2"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
    public void clearEntries(){
        scBtnSubmit.setEnabled(true);
        edScSubjectName.setText("");
        edScFloorNumber.setText("");
        edScRoomName.setText("");
        edScRoomNumber.setText("");
        spScBuilding.setSelection(0);
        spScEndTime.setSelection(0);
        spScStartTime.setSelection(0);
        spScBuilding.setSelection(0);
        spScPeriod.setSelection(((ArrayAdapter)spScPeriod.getAdapter()).getPosition(spScPeriod.getSelectedItem().toString())+1);
    }
}