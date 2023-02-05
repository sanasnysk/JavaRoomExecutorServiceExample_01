package com.sansan.javaroomexecutorserviceexample_01;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("select * from table_user")
    List<User> getAllUsers();

    @Delete
    int delete(User user);

    @Update
    int update(User user);

}
