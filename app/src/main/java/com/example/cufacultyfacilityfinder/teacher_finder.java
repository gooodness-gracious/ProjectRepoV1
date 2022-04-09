package com.example.cufacultyfacilityfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class teacher_finder extends AppCompatActivity {
    private static final String TAG="teacher_finder";

    EditText edTfTeacher;
    ImageButton btnTfSearch;
    TextView ar;
    Spinner spTfTeacherSearch;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_finder);

        ar=(TextView) findViewById(R.id.trialSS);
        edTfTeacher=(EditText) findViewById(R.id.tfTeacherEntry);
        btnTfSearch=(ImageButton) findViewById(R.id.tfSearchButton);
        spTfTeacherSearch=(Spinner) findViewById(R.id.spTeacherSearch);
        ArrayList<String> teacherName=new ArrayList<>();

        Intent intent = new Intent(getApplicationContext(), FacultyFound.class);


        // to get the list of all teacher names
        db.collection("General Details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                teacherName.add(document.getId());
                            }
                            ar.setText("Documents are:"+ teacherName);
                        }else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                    }
                });

        //populating the spinner
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teacherName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTfTeacherSearch.setAdapter(adapter);

        btnTfSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teacherName=edTfTeacher.getText().toString();

                intent.putExtra("Teacher Name", teacherName);
                startActivity(intent);
            }
        });
    }
}