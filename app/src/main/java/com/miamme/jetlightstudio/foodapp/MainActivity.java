package com.miamme.jetlightstudio.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText textField;
    Button add, remove;
    TextView tasks;
    SQLiteHelperMine helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textField = (EditText) findViewById(R.id.textField);
        add = (Button) findViewById(R.id.addButton);
        remove = (Button) findViewById(R.id.removeNutton);
        tasks = (TextView) findViewById(R.id.textView);
        helper = new SQLiteHelperMine(this,null,null,1);
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
        tasks.setText(helper.readFromDB());
        textField.setText("");
    }
}
