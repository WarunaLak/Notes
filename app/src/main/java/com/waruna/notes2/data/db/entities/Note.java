package com.waruna.notes2.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;
    @ColumnInfo(name = "is_sync")
    private int isSync;
    @ColumnInfo(defaultValue = "0")
    private int remove;

    public Note(String title, String description, int priority, int isSync) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isSync = isSync;
        this.remove = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }
}
