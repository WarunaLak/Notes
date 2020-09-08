package com.waruna.notes2.ui.main.main.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.TempNote;

public class Item {
    public static final int ITEM_NOTE_DEFAULT = 1;
    public static final int ITEM_NOTE_TEMP = 2;
    public static final int ITEM_TITLE = 3;
    public static final int ITEM_START = 4;
    public static final int ITEM_END = 4;

    private Note note;
    private TempNote tempNote;
    private int noteType;
    private int itemType;
    private long time;

    public Item(Note note, TempNote tempNote, int noteType, long time) {
        this.note = note;
        this.tempNote = tempNote;
        this.noteType = noteType;
        this.time = time;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public TempNote getTempNote() {
        return tempNote;
    }

    public void setTempNote(TempNote tempNote) {
        this.tempNote = tempNote;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format(
                " { note : %s, tempNote : %s, noteType : %d, type : %d, time : %d }; ",
                (note == null) ? "null" : note.toString(),
                (tempNote == null) ? "null" : tempNote.toString(),
                noteType,
                itemType,
                time
        );
    }
}
