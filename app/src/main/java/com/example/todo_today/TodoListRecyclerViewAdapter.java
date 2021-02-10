package com.example.todo_today;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoListRecyclerViewAdapter extends RecyclerView.Adapter<TodoListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TodoListItem> todoListItemArrayList = new ArrayList<>();

    public TodoListAdapterHandler adapterHandler;

    public TodoListRecyclerViewAdapter() { }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemTextView.setText(todoListItemArrayList.get(position).listItemName);
        holder.listItemCheckBox.setChecked(false);

        holder.listItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (adapterHandler != null) {
                    adapterHandler.handleItemRemoval(position, isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoListItemArrayList.size();
    }

    public void setTodoListItemArrayList(ArrayList<TodoListItem> todoListItemArrayList) {
        this.todoListItemArrayList = todoListItemArrayList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView listItemTextView;
        private CheckBox listItemCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemTextView = itemView.findViewById(R.id.listItemTextView);
            listItemCheckBox = itemView.findViewById(R.id.listItemCheckBox);
        }
    }
}
