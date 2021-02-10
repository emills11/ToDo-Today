package com.example.todo_today;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_list_table")
public class TodoListItem {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "list_item_name")
    public String listItemName;
}
