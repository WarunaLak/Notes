package com.waruna.notes2.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.waruna.notes2.data.db.entities.User;

import static com.waruna.notes2.data.db.entities.User.CURRENT_USER_ID;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("SELECT * FROM user_table WHERE id = " + CURRENT_USER_ID)
    LiveData<User> getUser();

}
