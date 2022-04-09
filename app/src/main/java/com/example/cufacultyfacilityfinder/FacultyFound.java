package com.example.cufacultyfacilityfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class FacultyFound extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
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


    private static final String KEY_DAY="Day";
    private static final String KEY_PERIOD="Period";
    private static final String KEY_PSTART="Start Time";
    private static final String KEY_PEND="End Time";
    private static final String KEY_PSUBJECTNAME="Subject Name";
    private static final String KEY_PCLASSROOM_LOCATION="Classroom Location";
    private static final String KEY_PROOMNAME="Room Name";
    private static final String KEY_PROOMNUMBER="Room Number";
    private static final String KEY_PFLOOR="Floor Number";
    private static final String KEY_PBUILDING="Building Name";

    String stGenDeets;
    String stCurrentDeets;
    String stStaffLocation;

    String name;
    TextView tvGenDeets;
    TextView tvTeacherName;
    TextView tvTeacherPosition;
    Button btGenDeets;
    Button btCurrentDeets;

    ArrayList<String> startTimePeriods=new ArrayList<>();
    ArrayList<String> endTimePeriods=new ArrayList<>();
    int flStart, flEnd, flWorkEnd, flWorkStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_found);
        tvGenDeets=(TextView)findViewById(R.id.ffGeneralDetails);
        tvTeacherName=(TextView)findViewById(R.id.ffTeacherName);
        tvTeacherPosition=(TextView) findViewById(R.id.ffTeacherPost);
        btGenDeets=(Button)findViewById(R.id.ffGenTab);
        btCurrentDeets=(Button)findViewById(R.id.ffPresentTab);

        //To get current day and time
        Calendar currentTime=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat simpleDayFormat=new SimpleDateFormat("EEEE");
        String Time=simpleDateFormat.format(currentTime.getTime());
        String Day=simpleDayFormat.format(currentTime.getTime());

        //To get searched teacher's name from teacher_finder.java
        Intent intent = getIntent();
        name = intent.getStringExtra("Teacher Name");

        db.collection("General Details").document(name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String cabin="", room="", floor="", building="";

                            String TeacherName=documentSnapshot.getString(KEY_NAME);
                            String dept=documentSnapshot.getString(KEY_DEPT);
                            String workStart=documentSnapshot.getString(KEY_START);
                            String workEnd=documentSnapshot.getString(KEY_END);
                            String post=documentSnapshot.getString(KEY_POST);
                            String phone=documentSnapshot.getString(KEY_PHONE);
                            String email=documentSnapshot.getString(KEY_EMAIL);
                            Map<String, Object> staff=(Map<String, Object>) documentSnapshot.getData().get(KEY_STAFFROOM_LOCATION);
                            if (staff != null) {

                                cabin = (String) staff.get(KEY_CABIN);
                                if (cabin.equals("")){
                                    cabin="No Cabin Allotted";
                                }
                                room=(String) staff.get(KEY_ROOM);
                                floor=(String) staff.get(KEY_FLOOR);
                                building =(String) staff.get(KEY_BUILDING);
                            }
                            stStaffLocation="Staffroom:\n\t\t\tCabin Number: "+cabin+"\n\t\t\tRoom Number: "+room+"\n\t\t\tFloor Number: "+
                            floor+"\n\t\t\tBuilding Name: "+building;
                            stGenDeets="Department: "+dept+"\n\nTimings: "+workStart+" - "+workEnd+"\n\nPhone Number: "+phone+"\n\nEmail: \n"+
                                    email+"\n\n"+stStaffLocation;

                            btGenDeets.setEnabled(false);
                            tvGenDeets.setText(stGenDeets);
                            tvTeacherName.setText(TeacherName);
                            tvTeacherPosition.setText(post);

                            db.collection("General Details").document(name).collection("Schedule").document(Day)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if ((documentSnapshot.exists())) {
                                                String periodStart = "", periodEnd = "", subjectName = "", pRoomName = "", pRoomNumber = "", pFloor = "", pBuilding = "";

                                                int i = 1, j = 0;
                                                for (i = 1; i <= 6; i++) {
                                                    String mapName = "Period " + i;
                                                    Map<String, Object> periods = (Map<String, Object>) documentSnapshot.getData().get(mapName);
                                                    if (periods != null) {
                                                        periodStart = (String) periods.get(KEY_PSTART);
                                                        periodEnd = (String) periods.get(KEY_PEND);
                                                        subjectName = (String) periods.get(KEY_PSUBJECTNAME);
                                                        //Adding the start and end times to an array to calculate free hours
                                                        startTimePeriods.add(periodStart);
                                                        endTimePeriods.add(periodEnd);
                                                        Map<String, Object> classLocation = (Map<String, Object>) periods.get("Classroom Location");
                                                        if (classLocation != null) {
                                                            pRoomName = (String) classLocation.get(KEY_PROOMNAME);
                                                            pRoomNumber = (String) classLocation.get(KEY_PROOMNUMBER);
                                                            pFloor = (String) classLocation.get(KEY_FLOOR);
                                                            pBuilding = (String) classLocation.get(KEY_BUILDING);
                                                        }

                                                        flStart = floorTime(periodStart);
                                                        flEnd = floorTime(periodEnd);
                                                        flWorkEnd = floorTime(workEnd);
                                                        flWorkStart = floorTime(workStart);
                                                        if (workEnd.substring(workEnd.length() - 4).equals("P.M.") && flWorkEnd != 12) {
                                                            flWorkEnd = flWorkEnd + 12;
                                                        }


                                                        if ((flStart == (Integer.parseInt(Time)))) {
                                                            stCurrentDeets = "Currently Teaching: " + subjectName + "\n\nClass Time: " +
                                                                    periodStart + " : " + periodEnd + "\n\nClassroom Location: \n\t\t\tRoom Name: "
                                                                    + pRoomName + "\n\t\t\tRoomNumber: " + pRoomNumber + "\n\t\t\tFloor: " + pFloor
                                                                    + "\n\t\t\tBuilding" + pBuilding;

                                                        }
                                                        else if(flStart == (Integer.parseInt(Time) - 1) && flEnd == (Integer.parseInt(Time) + 1)){
                                                            stCurrentDeets = "Currently Teaching: " + subjectName + "\n\nClass Time: " +
                                                                    periodStart + " : " + periodEnd + "\n\nClassroom Location: \n\t\t\tRoom Name: "
                                                                    + pRoomName + "\n\t\t\tRoomNumber: " + pRoomNumber + "\n\t\t\tFloor: " + pFloor
                                                                    + "\n\t\t\tBuilding" + pBuilding;
                                                        }
                                                        else if (Integer.parseInt(Time) < flWorkStart || Integer.parseInt(Time) >= flWorkEnd) {
                                                            stCurrentDeets = "Out of Working Hours\n";
                                                        } else {
                                                            stCurrentDeets = "Free Hour" + flStart;
                                                        }
                                                    }
                                                }
                                                //to calculate the free hours:
                                                stCurrentDeets+="\nFree Hours: ";
                                                if(!workStart.equals(startTimePeriods.get(0))){

                                                    int flStartPeriod=floorTime(startTimePeriods.get(0));
                                                    stCurrentDeets+="\n"+workStart+"-"+startTimePeriods.get(0);
                                                }

                                                if(!workEnd.equals(endTimePeriods.get(endTimePeriods.size()-1))){

                                                    int flEndPeriod=floorTime(endTimePeriods.get(endTimePeriods.size()-1));
                                                    stCurrentDeets+="\n"+endTimePeriods.get(endTimePeriods.size()-1)+"-"+workEnd;

                                                }

                                            }
                                            else{
                                                stCurrentDeets="No data for the day";
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(FacultyFound.this, "No Documnent"+e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            stGenDeets="No Data about Teacher";
                            Toast.makeText(FacultyFound.this, "NO DOCUMENT OF NAME EXISTS", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FacultyFound.this, "Too Bad", Toast.LENGTH_SHORT).show();
                    }
                });

        //Current Details Button
        btCurrentDeets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvGenDeets.setText(stCurrentDeets);
                btCurrentDeets.setEnabled(false);
                btGenDeets.setEnabled(true);
                btCurrentDeets.setBackgroundColor(getResources().getColor(R.color.nonchristblue));
                btGenDeets.setBackgroundColor(getResources().getColor(R.color.christblue));
            }
        });

        //General Details Button
        btGenDeets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvGenDeets.setText(stGenDeets);
                btCurrentDeets.setEnabled(true);
                btGenDeets.setEnabled(true);

                btGenDeets.setBackgroundColor(getResources().getColor(R.color.nonchristblue));
                btCurrentDeets.setBackgroundColor(getResources().getColor(R.color.christblue));
            }
        });
    }

    public int floorTime(String a){
        int j;
        String t="";
        for(j=0; j<=a.length();j++){
            char ch=a.charAt(j);
            if ( ch!= ':')
                t += ch;
            else {
                break;
            }
        }
        return Integer.parseInt(t);
    }
}