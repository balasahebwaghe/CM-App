package com.example.College_Media_Application.Model;

public class NotesModel {
    private String id;
    private String filename;

    // Constructor
    public NotesModel(String id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
