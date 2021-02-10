package com.example.todo_today;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private RecyclerView todoListRecyclerView;
    private TodoListRecyclerViewAdapter adapter;

    private ArrayList<TodoListItem> todoListItemArray;

    public static TodoListDatabase db;

    private RelativeLayout removeCompletedRelLayout;
    private FloatingActionButton newItemFAB;

    private HashSet<Integer> itemsToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize database
        db = TodoListDatabase.getInstance(this);

        removeCompletedRelLayout = findViewById(R.id.removeCompletedRelLayout);
        newItemFAB = findViewById(R.id.newItemFAB);

        //Initialize RecyclerView, adapter, and adapterHandler
        todoListRecyclerView = findViewById(R.id.todoListRecyclerView);
        adapter = new TodoListRecyclerViewAdapter();
        adapter.adapterHandler = new TodoListAdapterHandler() {
            @Override
            public void handleItemRemoval(int position, boolean isChecked) {
                if (itemsToRemove == null) {
                    itemsToRemove = new HashSet<>();
                }
                if (isChecked) {
                    if (removeCompletedRelLayout.getVisibility() == View.GONE) {
                        newItemFAB.setVisibility(View.GONE);
                        removeCompletedRelLayout.setVisibility(View.VISIBLE);
                    }

                    itemsToRemove.add(position + 1);
                } else {
                    itemsToRemove.remove(position + 1);

                    if (itemsToRemove.isEmpty() && removeCompletedRelLayout.getVisibility() == View.VISIBLE) {
                        removeCompletedRelLayout.setVisibility(View.GONE);
                        newItemFAB.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }

    //onStart sets date info and sets some parameters for RecyclerView
    @Override
    protected void onStart() {
        super.onStart();
        updateDateDisplay();

        todoListRecyclerView.setAdapter(adapter);
        todoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adds a divider line between each item in todoListRecyclerView
        todoListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    //Loads data for RecyclerView every time app returns to this activity
    @Override
    protected void onResume() {
        super.onResume();
        TodoListTableDao dao = db.todoListTableDao();
        todoListItemArray = (ArrayList<TodoListItem>) dao.getAll();
        adapter.setTodoListItemArrayList(todoListItemArray);
    }

    //This method is called when the add new item FAB is clicked
    public void addNewItem(View view) {
        Intent intent = new Intent(this, NewItemActivity.class);
        startActivity(intent);
    }

    private void updateDateDisplay() {
        //Set the current date at top of screen
        TextView dateDisplay = findViewById(R.id.topBannerDateTextView);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);
    }

    public void removeItems(View view) {
        if (!itemsToRemove.isEmpty()) {
            TodoListTableDao dao = db.todoListTableDao();

            for (int position : itemsToRemove) {
                dao.deleteItem(position);
                dao.updateIds(position);
            }

            itemsToRemove.clear();

            todoListItemArray = (ArrayList<TodoListItem>) dao.getAll();
            adapter.setTodoListItemArrayList(todoListItemArray);

            removeCompletedRelLayout.setVisibility(View.GONE);
            newItemFAB.setVisibility(View.VISIBLE);
        }
    }
}