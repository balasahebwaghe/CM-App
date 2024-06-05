package com.example.College_Media_Application.Model;

public class AcceptTeacherLeaveModel {
    private String id;
    private String teacherName;
    private String reason;
    private String date;
    private String status;
    private String username;

    private String phone;
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    // Constructor
    public AcceptTeacherLeaveModel(String id, String teacherName, String reason, String date, String status, String username,String phone) {
        this.id = id;
        this.teacherName = teacherName;
        this.reason = reason;
        this.date = date;
        this.status = status;
        this.username = username;
        this.phone=phone;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isAcceptedOrRejected() {
        return "accepted".equals(status) || "rejected".equals(status);
    }
}
