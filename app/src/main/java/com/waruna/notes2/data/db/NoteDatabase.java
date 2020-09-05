package com.waruna.notes2.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.User;

@Database(entities = {Note.class, User.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    public abstract UserDao userDao();

}
