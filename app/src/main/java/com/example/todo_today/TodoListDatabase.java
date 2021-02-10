package com.example.todo_today;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {TodoListItem.class}, version = 1)
public abstract class TodoListDatabase extends RoomDatabase {

    private static final String DB_NAME = "todo_list_database.db";
    private static volatile TodoListDatabase instance;

    public static synchronized TodoListDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public TodoListDatabase() {}

    private static TodoListDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                TodoListDatabase.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract TodoListTableDao todoListTableDao();
}
