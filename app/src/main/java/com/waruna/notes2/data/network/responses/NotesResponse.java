package com.waruna.notes2.data.network.responses;

import com.waruna.notes2.data.db.entities.Note;

import java.util.List;

public class NotesResponse {

    private boolean inserted;
    private boolean updated;
    private boolean removed;
    private int noteID;
    private String message;
    private List<Note> notes;

    public NotesResponse(boolean inserted, boolean updated, boolean removed, int noteID, String message, List<Note> notes) {
        this.inserted = inserted;
        this.updated = updated;
        this.removed = removed;
        this.noteID = noteID;
        this.message = message;
        this.notes = notes;
    }

    public boolean isInserted() {
        return inserted;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
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
