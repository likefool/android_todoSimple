package com.yahoo.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private final int REQUEST_CODE = 200;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    ArrayList<TodoItem> todoItems;
    private TodoAdapter todoItemsAdapter;
    private String editItemText;
    // Create our sqlite database object
    private TodoItemDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<String>();
        //items.add("First Item");
        //items.add("Second Item");
        db = new TodoItemDatabase(this);
        readItems(); // fill items, todoItems
        //itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        todoItemsAdapter = new TodoAdapter(this, todoItems);
        lvItems.setAdapter(todoItemsAdapter);

        setupListViewListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedText = intent.getExtras().getString("edit_item_text");
            int position = intent.getIntExtra("position", -1);
            if (editedText != null && editedText.length() > 0) {
                todoItems.set(position, new TodoItem(editedText, 3));
                todoItemsAdapter.notifyDataSetChanged();

                writeItems();
            }
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        todoItemsAdapter.add(new TodoItem(itemText, 3)); //TODO: input priority
        etNewItem.setText("");
        writeItems();

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        todoItems.remove(position);
                        todoItemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        editItemText = todoItems.get(position).getBody();
                        intent.putExtra("edit_item_text", editItemText);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
        );
    }

    private void readItems() {
        try {
            /*
             File filesDir = getFilesDir();
             File todoFile = new File(filesDir, "todo.txt");
             items = new ArrayList<String>(FileUtils.readLines(todoFile));
*/
            //items = new ArrayList<String>();
            todoItems = new ArrayList<TodoItem>();
            List<TodoItem> tItems = db.getAllTodoItems();
            // Print out properties
            for (TodoItem ti : tItems) {
                //items.add(ti.getBody());
                todoItems.add(ti);
            }
        } catch (SQLiteException e) {
            //items = new ArrayList<String>();
            todoItems = new ArrayList<TodoItem>();
        }
    }

    private void writeItems() {
        try {
            /*
            File filesDir = getFilesDir();
            File todoFile = new File(filesDir, "todo.txt");
            FileUtils.writeLines(todoFile, items);
            */

            List<TodoItem> tItems = db.getAllTodoItems();
            // clean db
            for (TodoItem ti : tItems) {
                db.deleteTodoItem(ti);
            }
            for (TodoItem ti : todoItems) {
                db.addTodoItem(ti);

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

}
