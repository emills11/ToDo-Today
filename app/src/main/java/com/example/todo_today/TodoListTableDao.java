package com.example.todo_today;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoListTableDao {

    @Query("SELECT * FROM todo_list_table")
    List<TodoListItem> getAll();

    @Query("INSERT INTO todo_list_table (id, list_item_name) VALUES (:id, :listItemName)")
    void insertNewItem(int id, String listItemName);

    @Query("DELETE FROM todo_list_table WHERE id=:id")
    void deleteItem(int id);

    @Query("UPDATE todo_list_table SET id = id - 1 WHERE id > :deleted")
    void updateIds(int deleted);

    @Query("SELECT id FROM todo_list_table ORDER BY id DESC LIMIT 1")
    Integer getLastId();
}
