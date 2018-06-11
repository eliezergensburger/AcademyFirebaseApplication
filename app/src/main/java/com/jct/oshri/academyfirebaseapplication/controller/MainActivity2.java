package com.jct.oshri.academyfirebaseapplication.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.jct.oshri.academyfirebaseapplication.R;

import static junit.framework.Assert.assertTrue;


public class MainActivity2 extends Activity  implements View.OnClickListener {

    private Button addStudentButton;
    private Button addLecturerButton;
    private Button addCourseButton;
    private Button addStudentCourseButton;
    private Button showStudentListButton;
    private Button showCourseListButton;
 //   FirebaseAuth mAuth;

    private void findViews() {
        addStudentButton = (Button) findViewById(R.id.addStudentButton);
        addLecturerButton = (Button) findViewById(R.id.addLecturerButton);
        addCourseButton = (Button) findViewById(R.id.addCourseButton);
        addStudentCourseButton = (Button) findViewById(R.id.addStudentCourseButton);
        showStudentListButton = (Button) findViewById(R.id.showStudentListButton);
        showCourseListButton = (Button) findViewById(R.id.showCourseListButton);

        addStudentButton.setOnClickListener(this);
        addLecturerButton.setOnClickListener(this);
        addCourseButton.setOnClickListener(this);
        addStudentCourseButton.setOnClickListener(this);
        showStudentListButton.setOnClickListener(this);
        showCourseListButton.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();





      // mAuth = FirebaseAuth.getInstance();

//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            Toast.makeText(getBaseContext(),user.getDisplayName() +" signIn  Success",Toast.LENGTH_LONG).show();
//        } else {
//            signInAnonymously();
//        }


    }


//    private void signInAnonymously(){
//        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
//            @Override public void onSuccess(AuthResult authResult) {
//                // do your stuff
//                Toast.makeText(getBaseContext(),"signIn Anonymously Success",Toast.LENGTH_LONG).show();
//            }
//        }) .addOnFailureListener(this, new OnFailureListener() {
//            @Override public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(getBaseContext(),"signIn Anonymously Failure !!!!!",Toast.LENGTH_LONG).show();
//                Log.e("TAG", "signInAnonymously:FAILURE", exception);
//            }
//        });
//    }

    private void addStudent() {
        Intent intent = new Intent(this,AddStudentActivity.class);
        startActivity(intent);
    }
    private void addLecturer() {

    }
    private void addCourse() {

    }
    private void addStudentCourse() {

    }
    private void showStudentLis() {
        Intent intent = new Intent(this,StudentListActivity.class);
        startActivity(intent);
    }
    private void showCourseList() {

    }

    @Override
    public void onClick(View v) {
        if (v == addStudentButton) {
            addStudent(); // Handle clicks for addStudentButton
        } else if (v == addLecturerButton) {
            addLecturer(); // Handle clicks for addLecturerButton
        } else if (v == addCourseButton) {
            addCourse();// Handle clicks for addCourseButton
        } else if (v == addStudentCourseButton) {
            addStudentCourse(); // Handle clicks for addStudentCourseButton
        } else if (v == showStudentListButton) {
            showStudentLis();// Handle clicks for showStudentListButton
        } else if (v == showCourseListButton) {
            showCourseList();// Handle clicks for showCourseListButton
        }
    }
}