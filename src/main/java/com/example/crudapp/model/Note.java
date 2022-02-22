package com.example.crudapp.model;

public class Note {
    private final int id;
    private String noteTitle;
    private String noteText;

    public Note(int id, String noteTitle, String noteText){
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
    }

    public int getId() {
        return id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
