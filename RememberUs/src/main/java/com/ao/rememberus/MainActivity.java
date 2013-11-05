package com.ao.rememberus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Singleton singleton=null;
    private ItemListBaseAdapter currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singleton.getInstance(this);
        updateListView();

        currentList = new ItemListBaseAdapter(this, singleton.getInstance(this).getArrayList());
        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                editReminder(v);
            }
        });
    }


    public void openCreateTaskActivity (View view)
    {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }

    // Update the list-View
    public void updateListView(){
        ListView lv = (ListView) findViewById(R.id.lvListReminders);
        ItemListBaseAdapter currentList = new ItemListBaseAdapter(this, singleton.getInstance(this).getArrayList());
        lv.setAdapter(currentList);
    }

    public void editReminder (View view) {

        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        int position = listView.getPositionForView(view);
        Item selectedItem = (Item) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "edited item : " + " " +
                selectedItem.getRemindMessage(), Toast.LENGTH_LONG).show();
        currentList.notifyDataSetChanged();
    }

    public void done(View view) {
        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        int position = listView.getPositionForView(view);
        Item selectedItem = (Item) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "deleted item : " + " " +
                selectedItem.getRemindMessage(), Toast.LENGTH_LONG).show();
        Delete(position);
        //updateListView();
        currentList.notifyDataSetChanged();
    }

    public void Delete(int position){
        singleton.getInstance(this).getArrayList().remove(position);
        updateListView();
    }




    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
