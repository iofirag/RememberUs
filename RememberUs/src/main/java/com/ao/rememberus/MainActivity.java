package com.ao.rememberus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<Item> results = new ArrayList<Item>();
    private ItemListBaseAdapter currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentList = new com.ao.rememberus.ItemListBaseAdapter(this, results);
        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                editReminder(v);
                //updateListView();
            }
        });
    }


    // On click '+' button
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void addTask (View view)
    {
        EditText description = (EditText) findViewById(R.id.description);
        if (description.getText().toString().isEmpty()) return;
        String des = description.getText().toString();
        Item item = new Item(des);
        results.add(0, item);
        description.setText("");
        updateListView();
    }

    // Update the list-View
    public void updateListView(){
        ListView lv = (ListView) findViewById(R.id.lvListReminders);
        com.ao.rememberus.ItemListBaseAdapter currentList = new com.ao.rememberus.ItemListBaseAdapter(this, results);
        lv.setAdapter(currentList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void done(View view)
    {
        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        int position = listView.getPositionForView(view);
        Item selectedItem = (Item) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "deleted item : " + " " +
                selectedItem.getRemindMessage(), Toast.LENGTH_LONG).show();
        Delete(position);
        //updateListView();
        currentList.notifyDataSetChanged();
    }

    public void editReminder (View view)
    {

        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        int position = listView.getPositionForView(view);
        Item selectedItem = (Item) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "edited item : " + " " +
                selectedItem.getRemindMessage(), Toast.LENGTH_LONG).show();
        updateListView();
        currentList.notifyDataSetChanged();

    }

    public void Delete(int position){
        results.remove(position);
        updateListView();
    }
}
