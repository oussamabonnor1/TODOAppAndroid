package com.miamme.jetlightstudio.foodapp.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;
import com.miamme.jetlightstudio.foodapp.R;
import com.miamme.jetlightstudio.foodapp.Toolbox.SQLiteToDoTable;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    SQLiteToDoTable helper;
    ListView listView;
    TextView activityTitle;
    EditText addTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        //naming the activity
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityTitle.setText(getIntent().getStringExtra("listName"));

        addTask = (EditText) findViewById(R.id.editTextAddTask);

        listView = (ListView) findViewById(R.id.listTodo);
        helper = new SQLiteToDoTable(this, null, null, 1);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                helper.removeTask(listView.getAdapter().getItem(position).toString());
                printDB();
                return false;
            }
        });
        printDB();
    }

    public void addTask(View v) {
        helper.addTask(false, addTask.getText().toString());
        printDB();
    }

    public void printDB() {
        CustumAdapter custumAdapter = new CustumAdapter(helper.readFromDB());
        listView.setAdapter(custumAdapter);
        addTask.setText("");
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

    public class CustumAdapter extends BaseAdapter {
        ArrayList<TodoItem> todoList;

        public CustumAdapter(ArrayList todoList) {
            this.todoList = todoList;
        }

        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.task_todo_item, null);
            final TextView title = view.findViewById(R.id.todoName);
            title.setText(todoList.get(i).getTaskName());
            RadioButton button = view.findViewById(R.id.radioButton);
            button.setChecked(todoList.get(i).isStatus());
            return view;
        }

        void listSelected(String listName) {
        }
    }
}
