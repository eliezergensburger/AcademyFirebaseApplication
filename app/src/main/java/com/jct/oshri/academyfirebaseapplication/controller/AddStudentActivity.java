package com.jct.oshri.academyfirebaseapplication.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jct.oshri.academyfirebaseapplication.R;
import com.jct.oshri.academyfirebaseapplication.model.datasourse.Firebase_DBManager;
import com.jct.oshri.academyfirebaseapplication.model.entities.Student;
import com.jct.oshri.academyfirebaseapplication.model.entities.Year;


public class AddStudentActivity extends Activity implements View.OnClickListener {

    private EditText IdEditText;
    private EditText NameEditText;
    private EditText PhoneEditText;

    private Spinner yearSpinner;
    private Button addStudentButton;
    private ImageView urlImageView;
    private ProgressBar addStudentProgressBar;


    private void findViews() {
        IdEditText = (EditText) findViewById(R.id.IdEditText);
        NameEditText = (EditText) findViewById(R.id.NameEditText);
        PhoneEditText = (EditText) findViewById(R.id.PhoneEditText);

        addStudentButton = (Button) findViewById(R.id.addStudentButton);
        urlImageView = (ImageView) findViewById(R.id.urlImageView);

        addStudentButton.setOnClickListener(this);
        urlImageView.setOnClickListener(this);

        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        yearSpinner.setAdapter(new ArrayAdapter<Year>(this, android.R.layout.simple_list_item_1, Year.values()));

        addStudentProgressBar = findViewById(R.id.addStudentProgressBar);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        findViews();
    }


    @Override
    public void onClick(View v) {
        if (v == addStudentButton) {
            addStudent();
        } else if (v == urlImageView) {
            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            urlImageView.setImageURI(data.getData());
            urlImageView.setTag(data.getData());

        }
    }


private void addStudent() {
    try {

        Student student = getStudent();
        addStudentButton.setEnabled(false);

        Firebase_DBManager.addStudent(student, new Firebase_DBManager.Action<Long>() {
            @Override
            public void onSuccess(Long obj) {
                Toast.makeText(getBaseContext(), "insert id" + obj, Toast.LENGTH_LONG).show();
                addStudentButton.setEnabled(true);
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                addStudentButton.setEnabled(true);
            }

            @Override
            public void onProgress(String status, double percent) {
                if (percent != 100)
                    addStudentButton.setEnabled(false);
                addStudentProgressBar.setProgress((int) percent);
            }
        });
    } catch (Exception e) {
        Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_LONG).show();
        addStudentButton.setEnabled(true);
    }
}

    private Student getStudent() {
        Student student = new Student();
        long id = Long.valueOf(this.IdEditText.getText().toString());
        student.setId(id);


        student.setName(this.NameEditText.getText().toString());
        student.setPhone(this.PhoneEditText.getText().toString());

        Year year = ((Year) yearSpinner.getSelectedItem());
        student.setYear(year);

        student.setImageLocalUri((Uri) urlImageView.getTag());

        return student;
    }
}