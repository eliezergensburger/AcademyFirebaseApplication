package com.jct.oshri.academyfirebaseapplication.model.entities;

import java.util.Date;

/**
 * Created by mailo on 26/11/2016.
 */

public class StudentCourse {
    private long id;
    private long studentId;
    private long courseId;
    private Date regDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
}
