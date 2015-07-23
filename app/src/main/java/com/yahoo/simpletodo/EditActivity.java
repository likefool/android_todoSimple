package com.yahoo.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditActivity extends Activity {
    private String editItemText = null;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getIntentFromParentActivity();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

    private void getIntentFromParentActivity() {
        editItemText = getIntent().getStringExtra("edit_item_text");
        position = getIntent().getIntExtra("position", -1);

        EditText editText = (EditText) findViewById(R.id.etEditItem);
        if (position >= 0) {
            editText.setText(editItemText);
            editText.setSelection(editText.getText().length());
        } else {
            Toast.makeText(this, "Errno", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEdit(View v) {
        EditText editText = (EditText) findViewById(R.id.etEditItem);

        Intent intent = new Intent();
        intent.putExtra("edit_item_text", editText.getText().toString());
        intent.putExtra("position", position);

        setResult(RESULT_OK, intent);

        finish();
    }
}
