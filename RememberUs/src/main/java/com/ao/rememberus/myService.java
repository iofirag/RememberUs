package com.ao.rememberus;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;

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

/**
 * Created by joe on 08/12/13.
 */
public class myService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public myService() {
        super("myService");
        System.out.println("service ctor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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







    private class GetFromWebTask  extends AsyncTask<URL, Integer, String> {

        GetFromWebTask(){
            System.out.println("GetFromWebTask created");
        }
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

                //create task and save it to array
                Task task = new Task(taskJSON.getInt("id"), taskJSON.getString("description") , new Date() );
                Singleton.getInstance( getApplicationContext() ).getArrayList().add(0, task);

                // Save to DB
                //ListView lv = (ListView) findViewById(R.id.lvListReminders);
                //TaskListBaseAdapter currentList = new TaskListBaseAdapter(this, Singleton.getInstance(this).getArrayList());

                Singleton.getInstance( getApplicationContext() ).getDb().addTask(task);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
