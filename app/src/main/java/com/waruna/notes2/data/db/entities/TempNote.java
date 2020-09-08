package com.waruna.notes2.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_note_table")
public class TempNote {
    public static final String PENDING_TO_UPLOAD = "u";
    public static final String PENDING_TO_REMOVE = "r";

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(defaultValue = PENDING_TO_UPLOAD)
    private String state;
    @Embedded(prefix = "nt_")
    private Note note;

    public TempNote(String state, Note note) {
        this.state = state;
        this.note = note;
    }

    // this.state = PENDING_TO_UPLOAD;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @NonNull
    @Override
    public String toString() {
        return " { id : " + id + ", state : " + state + ", note : " + note.toString() + " };" ;
    }
}
