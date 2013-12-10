package com.ao.rememberus;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    Singleton singleton=null;
    private TaskListBaseAdapter currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (singleton.getInstance(this).getArrayList().isEmpty())
            restoreFromDb();
        //getFromInternet();
        //singleton.getInstance(this).getDb().onUpgrade(singleton.getInstance(this).getDb().getWritableDatabase(), 1,2);
        //updateListView();

        currentList = new TaskListBaseAdapter(this, singleton.getInstance(this).getArrayList());
        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                editReminder(v);
            }
        });

        CreateRepeatAlarmManager();
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("MainActivity onResume()");
        updateListView();
        currentList.notifyDataSetChanged();
    }

    private void CreateRepeatAlarmManager() {
        //----(
        Calendar calendar = Calendar.getInstance();
        // 9 AM
        calendar.set(Calendar.HOUR_OF_DAY, 01);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(this, myService.class);
        intent.putExtra("this",this.toString());
        PendingIntent pi = PendingIntent.getService(this, 55555, intent, PendingIntent./*FLAG_CANCEL_CURRENT*/FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        //----)

        //----(
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, myService.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Date curr=new Date();
//        curr.setHours(h);
//        curr.setMinutes(m);
//        c.setTime(curr);
//        c.set(Calendar.SECOND, 0);
//
//        Calendar c1 = Calendar.getInstance();
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),24*60*60*1000, pendingIntent);
        ///-----)
    }

    public void restoreFromDb(){
        List<Task> list = singleton.getInstance(this).getDb().getAllTasks();
        for(Task task : list){
            String str = new String( task.getTaskMessage() );
            singleton.getInstance(this).getArrayList().add(0,task);
        }
    }
//    public void saveToDb(){
//        for(Task task : singleton.getInstance(this).getArrayList()){
//            String str = new String( task.getTaskMessage() );
//            if (str!=null){
//                Task newTask = new Task(str);
//                singleton.getInstance(this).getDb().addTask(newTask);
//            }
//        }
//    }

    public void openCreateTaskActivity (View view)
    {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }

    // Update the list-View
    public void updateListView(){
        ListView lv = (ListView) findViewById(R.id.lvListReminders);
        TaskListBaseAdapter currentList = new TaskListBaseAdapter(this, singleton.getInstance(this).getArrayList());
        lv.setAdapter(currentList);
    }

    public void editReminder (View view) {

        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "edited item : " + " " +
                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
        currentList.notifyDataSetChanged();

    }

    public void done(View view) {
        ListView listView = (ListView) findViewById(R.id.lvListReminders);
        int position = listView.getPositionForView(view);
        Task selectedTask = (Task) listView.getItemAtPosition(position);
        Toast.makeText(MainActivity.this, "deleted item : " + " " +
                selectedTask.getTaskMessage(), Toast.LENGTH_LONG).show();
        Delete(selectedTask, position);
        //currentList.notifyDataSetChanged();
    }

    public void Delete(Task taskToDelete, int position){
        singleton.getInstance(this).getArrayList().remove(position);
        singleton.getInstance(this).getDb().deleteTask( taskToDelete);
        updateListView();
    }




    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
