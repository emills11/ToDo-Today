package com.example.todo_today;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewItemActivity extends AppCompatActivity {

    private Button newItemFormButton;
    private EditText newItemFormItemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        //Initialize UI elements
        newItemFormItemEditText = findViewById(R.id.newItemFormItemEditText);
        newItemFormButton = findViewById(R.id.newItemFormButton);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //When "add" button is clicked, a new entry is added to database table and activity finishes
        newItemFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListTableDao dao = MainActivity.db.todoListTableDao();

                int id;
                if (dao.getAll().isEmpty()) {
                    id = 1;
                } else {
                    id = dao.getLastId() + 1;
                }

                dao.insertNewItem(id, newItemFormItemEditText.getText().toString());
                NewItemActivity.this.finish();
            }
        });
    }

}