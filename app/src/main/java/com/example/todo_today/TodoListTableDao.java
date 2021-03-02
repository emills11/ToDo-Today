package com.example.todo_today;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoListTableDao {

    @Query("SELECT * FROM todo_list_table")
    List<TodoListItem> getAll();

    @Query("INSERT INTO todo_list_table (list_item_name, list_item_position) VALUES (:listItemName, :listItemPosition)")
    void insertNewItem(String listItemName, int listItemPosition);

    @Query("DELETE FROM todo_list_table WHERE list_item_position = :listItemPosition")
    void deleteItem(int listItemPosition);

    @Query("UPDATE todo_list_table SET list_item_position = :i WHERE list_item_position = :listItemPosition")
    void updateId(int i, int listItemPosition);

    @Query("SELECT list_item_position FROM todo_list_table ORDER BY list_item_position DESC LIMIT 1")
    Integer getLastListItemPosition();
}
