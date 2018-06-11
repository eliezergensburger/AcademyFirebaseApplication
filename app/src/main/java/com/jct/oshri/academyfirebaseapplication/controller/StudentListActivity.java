package com.jct.oshri.academyfirebaseapplication.controller;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.jct.oshri.academyfirebaseapplication.R;
import com.jct.oshri.academyfirebaseapplication.model.datasourse.Firebase_DBManager;
import com.jct.oshri.academyfirebaseapplication.model.entities.Student;

import java.util.List;

//import com.bumptech.glide.Glide;

public class StudentListActivity extends Activity {

    private RecyclerView studentsRecycleView;
    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);


        studentsRecycleView = findViewById(R.id.studentsRecycleView);
        studentsRecycleView.setHasFixedSize(true);
        studentsRecycleView.setLayoutManager(new LinearLayoutManager(this));

        Firebase_DBManager.NotifyToStudentList(new Firebase_DBManager.NotifyDataChange<List<Student>>() {
            @Override
            public void OnDataChanged(List<Student> obj) {
                if (studentsRecycleView.getAdapter() == null) {
                    students = obj;
                    studentsRecycleView.setAdapter(new StudentsRecycleViewAdapter());
                } else
                    studentsRecycleView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getBaseContext(), "error to get students list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        Firebase_DBManager.UNotifyToStudentList();
        super.onDestroy();
    }

    public class StudentsRecycleViewAdapter extends RecyclerView.Adapter<StudentsRecycleViewAdapter.StudentViewHolder> {

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.student_item_view, parent, false);
            return new StudentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {

            Student student = students.get(position);
            holder.nameTextView.setText(student.getName());
            holder.phoneTextView.setText(student.getPhone());
            //Load the image using Glide
            Glide.with(getBaseContext())
                    .load(student.getImageFirebaseUrl())
                    .centerCrop()
                    .override(150, 150)
                    .placeholder(R.mipmap.person)
                    .into(holder.personImageView);


//            Glide.with(getBaseContext() /* context */)
//                    .load(student.getImageFirebaseUrl())
//                 //   .override(450,450)
//                    .centerCrop()
//                    .placeholder(R.mipmap.person)
//                    .into(holder.personImageView);


        }

        @Override
        public int getItemCount() {
            return students.size();
        }

        public class StudentViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView nameTextView;
            TextView phoneTextView;
            ImageView personImageView;


            public StudentViewHolder(View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.cv);
                personImageView = itemView.findViewById(R.id.personImageView);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                phoneTextView = itemView.findViewById(R.id.phoneTextView);

                // itemView.setOnClickListener();
                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        //   Toast.makeText(getBaseContext(),"OnCreateContextMenuListener",Toast.LENGTH_LONG).show();
                        menu.setHeaderTitle("Select Action");

                        MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

                        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int position = getAdapterPosition();
                                long id = students.get(position).getId();

                                Firebase_DBManager.removeStudent(id, new Firebase_DBManager.Action<Long>() {
                                    @Override
                                    public void onSuccess(Long obj) {

                                    }

                                    @Override
                                    public void onFailure(Exception exception) {

                                    }

                                    @Override
                                    public void onProgress(String status, double percent) {

                                    }
                                });

                                return true;
                            }
                        });
                    }
                });
            }
        }
    }


}