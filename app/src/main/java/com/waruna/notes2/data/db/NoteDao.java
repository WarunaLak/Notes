package com.waruna.notes2.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waruna.notes2.data.db.entities.Note;

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

    @Query("UPDATE note_table SET remove = 1 WHERE id=:id")
    void removeNote(int id);

    @Query("SELECT * FROM note_table WHERE remove = 0 ORDER BY priority DESC")
    LiveData<List<Note>> getAllNote();

}
