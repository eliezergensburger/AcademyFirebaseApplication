package com.jct.oshri.academyfirebaseapplication.model.datasourse;

import android.net.Uri;
import android.support.annotation.NonNull;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jct.oshri.academyfirebaseapplication.model.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class Firebase_DBManager {

    private static ChildEventListener studentRefChildEventListener;

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    static DatabaseReference StudentsRef;
    static List<Student> studentList;

    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        StudentsRef = database.getReference("students");
        studentList = new ArrayList<>();
    }


    public static void addStudent(final Student student, final Action<Long> action) {
        if (student.getImageLocalUri() != null) {
            // upload image
            StorageReference imagesRef = FirebaseStorage.getInstance().getReference();
            imagesRef = imagesRef.child("images").child(System.currentTimeMillis() + ".jpg");

            imagesRef.putFile(student.getImageLocalUri())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            action.onProgress("upload student data", 90);
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            student.setImageFirebaseUrl(downloadUrl.toString());

                            // add student
                            addStudentToFirebase(student, action);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            action.onFailure(exception);
                            action.onProgress("error upload student image", 100);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double uploadBytes = taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    double progress = (90.0 * uploadBytes);
                    action.onProgress("upload image", progress);
                }
            });
        } else
            action.onFailure(new Exception("select image first ..."));
    }

    private static void addStudentToFirebase(final Student student, final Action<Long> action) {
        String key = student.getId().toString();
        StudentsRef.child(key).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(student.getId());
                action.onProgress("upload student data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload student data", 100);

            }
        });
    }


    public static void removeStudent(long id, final Action<Long> action) {

        final String key = ((Long) id).toString();

        StudentsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Student value = dataSnapshot.getValue(Student.class);
                if (value == null)
                    action.onFailure(new Exception("student not find ..."));
                else {
                    StorageReference imagesRef = FirebaseStorage.getInstance().getReferenceFromUrl(value.getImageFirebaseUrl());
                    imagesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            StudentsRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    action.onSuccess(value.getId());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    action.onFailure(e);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            action.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                action.onFailure(databaseError.toException());
            }
        });
    }

    public static void updateStudent(final Student toUpdate, final Action<Long> action) {
        final String key = ((Long) toUpdate.getId()).toString();

        removeStudent(toUpdate.getId(), new Action<Long>() {
            @Override
            public void onSuccess(Long obj) {
                addStudent(toUpdate, action);
            }

            @Override
            public void onFailure(Exception exception) {
                action.onFailure(exception);
            }

            @Override
            public void onProgress(String status, double percent) {
                action.onProgress(status, percent);
            }
        });
    }


    public static void NotifyToStudentList(final NotifyDataChange<List<Student>> notifyDataChange) {
        if (notifyDataChange != null) {

            if (studentRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify student list"));
                return;
            }
            studentList.clear();

            studentRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Student student = dataSnapshot.getValue(Student.class);
                    String id = dataSnapshot.getKey();
                    student.setId(Long.parseLong(id));
                    studentList.add(student);


                    notifyDataChange.OnDataChanged(studentList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Student student = dataSnapshot.getValue(Student.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    student.setId(id);


                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId().equals(id)) {
                            studentList.set(i, student);
                            break;
                        }

                    }

                    notifyDataChange.OnDataChanged(studentList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Student student = dataSnapshot.getValue(Student.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    student.setId(id);


                    for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getId() ==  id) {
                            studentList.remove(i);
                            break;
                        }
                    }

                    notifyDataChange.OnDataChanged(studentList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            StudentsRef.addChildEventListener(studentRefChildEventListener);

        }
    }


    public static void UNotifyToStudentList() {
        StudentsRef.removeEventListener(studentRefChildEventListener);
        studentRefChildEventListener = null;
    }

}
