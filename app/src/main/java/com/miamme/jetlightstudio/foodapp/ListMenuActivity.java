package com.miamme.jetlightstudio.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    }

    public class CustumAdapter extends BaseAdapter {
        int size;
        ArrayList<String> lists;

        public CustumAdapter(int size, ArrayList lists) {
            this.size = size;
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return size;
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
            TextView title = view.findViewById(R.id.listName);
            title.setText(lists.get(i));
            return view;
        }
    }
}
