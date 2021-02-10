package com.example.todo_today;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TodoListTable {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "list_item_name")
    public String listItemName;
}
