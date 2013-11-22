package com.ao.rememberus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class CreateTaskActivity extends Activity {

    Singleton singleton=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);

//      EditText editText = (EditText) findViewById(R.id.description);
//      editText.requestFocus();
//      editText.setFocusable(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    // On click '+' button
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
      public void saveTask(View view){
        EditText description = (EditText) findViewById(R.id.description);
        if (description.getText().toString().isEmpty()) return;
        Task task = new Task(description.getText().toString());
        singleton.getInstance(this).getArrayList().add(0, task);
        saveToDb(task);
        description.setText("");
        finish();
      }

    public void saveToDb(Task newTask){
        singleton.getInstance(this).getDb().addTask(newTask);
    }




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
