package com.ao.rememberus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateTaskActivity extends Activity {

    Singleton singleton=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_activity);

        Intent service = new Intent(this, myService.class);
        startService(service);
        System.out.println("service intent sent");

        CheckBox satView = (CheckBox) findViewById(R.id.dateCheckBox);
        satView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAlarm();
            }
        });

/* keybord focus */


/* end keybord focus */
//      EditText editText = (EditText) findViewById(R.id.description);
//      editText.requestFocus();
//      editText.setFocusable(true);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    public void setAlarm ()
    {

        CheckBox satView = (CheckBox) findViewById(R.id.dateCheckBox);
        TimePicker date = (TimePicker) findViewById(R.id.timePicker);
        DatePicker time = (DatePicker) findViewById(R.id.datePicker);

        if(satView.isChecked())
        {
            date.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null){
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }

        }
        else
        {
            date.setVisibility(View.INVISIBLE);
            time.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null){
                imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            }

        }
    }

    // On click '+' button
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
      public void saveTask(View view){
        EditText description = (EditText) findViewById(R.id.description);
        if (!description.getText().toString().isEmpty()){

            TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
            DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
            Date date= new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour() , timePicker.getCurrentMinute() );

            int nowUseAsId = (int) (long) System.currentTimeMillis();
            if (nowUseAsId<0) nowUseAsId*=-1;

//                                    System.out.println("nowUseAsId="+nowUseAsId);

            String taskMessage = description.getText().toString();
            Task task = new Task(nowUseAsId, taskMessage, date);

            singleton.getInstance(this).getArrayList().add(0, task);

            //-----continue checking from here -> to register date & cancel alarmManager after if click done
            saveToDb(task);
            CheckBox satView = (CheckBox) findViewById(R.id.dateCheckBox);
            if(satView.isChecked())
                createAlarmAtDate(task, date);

            description.setText("");
            finish();
        }
      }

    public void saveToDb(Task newTask){
        singleton.getInstance(this).getDb().addTask(newTask);
    }

    // If user click random button
    public void getFromInternetRandomTask(View view){
        URL url = null;
        try {
            url= new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url!=null){
            new GetFromWebTask().execute( url );
            System.out.println("AsyncTask created.");
        }
    }

    // if user check the dateButton
    private void createAlarmAtDate(Task task, Date date){
        Intent intent = new Intent("com.ao.rememberus.ReminderBroadCastReceiver");
        intent.putExtra("taskMessage", task.getTaskMessage() );
//                        System.out.println("task.getTaskMessage()="+task.getTaskMessage());

        intent.putExtra("taskId", task.getID());
//                        System.out.println("task.getID()="+task.getID());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getID(), intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + millisecondsUntilDate(date) , pendingIntent);
    }

    public long millisecondsUntilDate(Date nextDate){
        Date now = new Date();
        // fix date before calculate
        now.setYear(now.getYear()+1900);

//                                System.out.println(now);

        GregorianCalendar currentDay=new  GregorianCalendar (now.getYear(),now.getMonth(),now.getDay(),now.getHours(),now.getMinutes(),0);
        GregorianCalendar nextDay=new  GregorianCalendar (nextDate.getYear(),nextDate.getMonth(),nextDate.getDay(),nextDate.getHours(),nextDate.getMinutes(),0);

//                        System.out.println( nextDate.getYear()  +" " +   now.getYear()   );
//                        System.out.println( nextDate.getMonth() +" " +  now.getMonth()   );
//                        System.out.println( nextDate.getDay()   +" " +     now.getDay()  );
//                        System.out.println( nextDate.getHours() +" " +   now.getHours()  );
//                        System.out.println(nextDate.getMinutes()+" " +  now.getMinutes() );

        long diff_in_ms = nextDay.getTimeInMillis()-currentDay.getTimeInMillis();
//                             System.out.println("diff_in_s=" +diff_in_ms/1000);
//                             System.out.println("diff_in_m=" +diff_in_ms/60000);
        return diff_in_ms;
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










    private class GetFromWebTask  extends AsyncTask<URL, Integer, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String result = getFromWeb(urls[0]);
            //bg: this takes some time and happens on a different thread
            return result;
        }

        private String getFromWeb(URL url) {
            String response = null;
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //String response=urlConnection.getInputStream() ;
                // read from urlConnection.getInputStream() to

                InputStream in = new
                        BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader inReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new
                        BufferedReader(inReader);
                StringBuilder responseBuilder = new StringBuilder();
                for (String line=bufferedReader.readLine(); line!=null;
                     line=bufferedReader.readLine()){
                    responseBuilder.append(line);
                }
                response = responseBuilder.toString();

                                System.out.println("connected");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject taskJSON = new JSONObject( result );

                EditText et = (EditText) findViewById(R.id.description);
                et.setText( taskJSON.getString("description") );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

