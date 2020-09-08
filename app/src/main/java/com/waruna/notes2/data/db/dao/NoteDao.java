package com.waruna.notes2.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.User;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Insert
    void insertAll(List<Note> note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNote();

}
