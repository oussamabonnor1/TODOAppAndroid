package com.miamme.jetlightstudio.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText textField;
    Button add, remove;
    TextView tasks;
    SQLiteHelperMine helper;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textField = (EditText) findViewById(R.id.textField);
        add = (Button) findViewById(R.id.addButton);
        remove = (Button) findViewById(R.id.removeNutton);
        //tasks = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        helper = new SQLiteHelperMine(this, null, null, 1);
        printDB();
    }

    public void addTask(View v) {
        helper.addTask(textField.getText().toString());
        printDB();
    }

    public void removeTask(View v) {
        helper.removeTask(textField.getText().toString());
        printDB();
    }

    public void printDB() {
        listView.setAdapter(helper.readFromDB(this));
        textField.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            Toast.makeText(this, "Made by JetLight studio", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
