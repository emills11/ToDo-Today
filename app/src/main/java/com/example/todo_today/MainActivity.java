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

    //itemsToRemove is a HashSet to ensure no items are added twice, and for constant time operations
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

        //AdapterHandler is used to handle arguments from a ViewHolder while using resources from MainActivity
        //handleItemRemoval adds or removes RecyclerView items to a removal queue depending on
        // whether or not the item's associated checkbox is checked. It additionally updates the
        // activity's UI as needed.
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

    //Sets the date at the top of the screen
    private void updateDateDisplay() {
        TextView dateDisplay = findViewById(R.id.topBannerDateTextView);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String date = dateFormat.format(calendar.getTime());
        dateDisplay.setText(date);
    }

    //removeItems is called when removeCompletedRelLayout layout is clicked. For each item
    // currently in itemsToRemove, the table entry associated with that item is removed, and
    // all ids in the table are appropriately updated. itemsToRemove is then cleared, and
    // all UI components are also appropriately updated.
    public void removeItems(View view) {
        if (!itemsToRemove.isEmpty()) {
            TodoListTableDao dao = db.todoListTableDao();

            for (int position : itemsToRemove) {
                dao.deleteItem(position);
            }

            //TODO: fix this so it doesn't overflow list size
            dao.updateAllIds(itemsToRemove.size());

            itemsToRemove.clear();

            todoListItemArray = (ArrayList<TodoListItem>) dao.getAll();
            adapter.setTodoListItemArrayList(todoListItemArray);

            removeCompletedRelLayout.setVisibility(View.GONE);
            newItemFAB.setVisibility(View.VISIBLE);
        }
    }
}