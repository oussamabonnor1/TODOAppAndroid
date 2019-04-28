package com.miamme.jetlightstudio.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListMenuActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);
        listView = (ListView) findViewById(R.id.listMenu);
        ArrayList<String> lists = new ArrayList<String>();
        lists.add("Home");
        lists.add("Work");
        lists.add("School");
        lists.add("Groceries");
        lists.add("Business");
        CustumAdapter custumAdapter = new CustumAdapter(lists);
        listView.setAdapter(custumAdapter);
    }

    public class CustumAdapter extends BaseAdapter {
        ArrayList<String> lists;

        public CustumAdapter(ArrayList lists) {
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
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
            view = getLayoutInflater().inflate(R.layout.task_menu_item, null);
            final TextView title = view.findViewById(R.id.listName);
            title.setText(lists.get(i));
            ImageButton button = view.findViewById(R.id.selectList);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listSelected(title.getText().toString());
                }
            });
            return view;
        }

        void listSelected(String listName) {
            Intent intent = new Intent(getApplicationContext(), ToDoListActivity.class);
            intent.putExtra("listName", listName);
            startActivity(intent);
        }
    }
}
