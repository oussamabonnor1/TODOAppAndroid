package com.miamme.jetlightstudio.foodapp.Controllers;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
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

import org.xmlpull.v1.XmlPullParser;

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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.task_todo_item, null);

            final TextView title = view.findViewById(R.id.todoName);
            title.setText(todoList.get(i).getTaskName());
            AppCompatRadioButton button = view.findViewById(R.id.radioButton);
            button.setChecked(todoList.get(i).isStatus());

            //region the fucking shit i do to get procedurally generated stuff -.-
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked}, //disabled
                    new int[]{android.R.attr.state_checked} //enabled
            };
            @SuppressLint("ResourceType") int[] colorBlue = new int[]{Color.parseColor(getString(R.color.colorBlue)),
                    Color.parseColor(getString(R.color.colorBlue))};
            @SuppressLint("ResourceType") int[] colorGreen = new int[]{Color.parseColor(getString(R.color.colorGreen)),
                    Color.parseColor(getString(R.color.colorGreen))};
            @SuppressLint("ResourceType") int[] colorViolet = new int[]{Color.parseColor(getString(R.color.colorViolet)),
                    Color.parseColor(getString(R.color.colorViolet))};
            //endregion

            //region setting UI colors
            RelativeLayout layout = view.findViewById(R.id.itemBackground);
            String color = todoList.get(i).getColor();
            final String taskName = todoList.get(i).getTaskName();
            if (color.matches("blue")) {
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_item_background_blue));
                button.setButtonTintList(new ColorStateList(states, colorBlue));
            } else if (color.matches("green")) {
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_item_background_green));
                button.setButtonTintList(new ColorStateList(states, colorGreen));
            } else {
                layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_item_background_violet));
                button.setButtonTintList(new ColorStateList(states, colorViolet));
            }
            //endregion

            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    itemChecked(b, taskName);
                }
            });

            return view;
        }

        void itemChecked(boolean taskStatus, String taskName) {
            manager.updateTask(taskStatus, taskName);
        }
    }
}
