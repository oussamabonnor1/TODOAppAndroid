package com.miamme.jetlightstudio.foodapp.Controllers;

import android.support.v4.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;
import com.miamme.jetlightstudio.foodapp.R;
import com.miamme.jetlightstudio.foodapp.Toolbox.DataBaseManager;
import com.miamme.jetlightstudio.foodapp.Toolbox.SQLiteManager;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    DataBaseManager manager;
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
        //getting list color
        String color = getIntent().getStringExtra("listColor");

        addTask = (EditText) findViewById(R.id.editTextAddTask);

        listView = (ListView) findViewById(R.id.listTodo);
        manager = new DataBaseManager(getApplicationContext(), activityTitle.getText().toString(), color);
        System.out.println(manager);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                manager.removeTask(listView.getAdapter().getItem(position).toString());
                printDB();
                return false;
            }
        });
        printDB();
    }

    public void addTask(View v) {
        manager.addTask(false, addTask.getText().toString());
        printDB();
    }

    public void printDB() {
        CustumAdapter custumAdapter = new CustumAdapter(manager.readFromDB());
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

            //region setting background color
            RelativeLayout layout = view.findViewById(R.id.itemBackground);
            String color = todoList.get(i).getColor();
            if (color.matches("blue"))
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_item_background_blue));
            else if (color.matches("green"))
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_item_background_green));
            else
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_item_background_violet));
            //endregion

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
