package com.waruna.notes2.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.TempNote;

import java.util.List;

@Dao
public interface TempNoteDao {

    @Insert
    void insert(TempNote note);

    @Insert
    void insertAll(List<TempNote> note);

    @Update
    void update(TempNote note);

    @Delete
    void delete(TempNote note);

    @Query("UPDATE temp_note_table SET state = :state WHERE id = :id")
    void setState(int id, String state);

    @Query("DELETE FROM temp_note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM temp_note_table ORDER BY nt_priority DESC")
    LiveData<List<TempNote>> getAllNote();

}
