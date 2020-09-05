package com.waruna.notes2.data.network.responses;

import com.waruna.notes2.data.db.entities.Note;

import java.util.List;

public class NotesResponse {

    private boolean inserted;
    private String message;
    private List<Note> notes;

    public NotesResponse(boolean inserted, String message, List<Note> notes) {
        this.inserted = inserted;
        this.message = message;
        this.notes = notes;
    }

    public boolean isInserted() {
        return inserted;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
