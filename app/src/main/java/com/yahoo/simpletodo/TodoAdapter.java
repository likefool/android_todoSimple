package com.yahoo.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aliku on 2015/7/24.
 */
public class TodoAdapter extends ArrayAdapter<TodoItem> {

    public TodoAdapter(Context context, ArrayList<TodoItem> todoItem) {
        super(context, 0, todoItem);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvTodo);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvPriority);

        // Populate the data into the template view using the data object
        tvName.setText(todoItem.body);
        tvHome.setText("3");//todoItem.priority);

        // Return the completed view to render on screen
        return convertView;

    }
}
