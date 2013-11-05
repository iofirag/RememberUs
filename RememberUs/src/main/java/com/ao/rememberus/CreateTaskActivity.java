package com.ao.rememberus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CreateTaskActivity extends Activity {

    Singleton singleton=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);
        singleton.getInstance(this);

    }

    // On click '+' button
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
      public void saveTask(View view){
          EditText description = (EditText) findViewById(R.id.description);
          if (description.getText().toString().isEmpty()) return;
          Item item = new Item(description.getText().toString());
          singleton.getInstance(this).getArrayList().add(0, item);
          description.setText("");
          finish();
//        updateListView();
      }
//    public void openCreateTaskActivity (View view)
//    {
//        EditText description = (EditText) findViewById(R.id.description);
//        if (description.getText().toString().isEmpty()) return;
//        String des = description.getText().toString();
//        Item item = new Item(des);
//        results.add(0, item);
//        description.setText("");
//        updateListView();
//    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
