package com.ao.rememberus;

import android.content.Context;
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
 * Created by Avishay on 06/12/13.
 */

class GetFromWebTask  extends AsyncTask<URL, Integer, String> {

    Context context;
    GetFromWebTask (Context context)
    {
        this.context = context;
    }
    GetFromWebTask ()
    {
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

            Task task = new Task(taskJSON.getInt("id"), taskJSON.getString("description") , new Date());
            Singleton.getInstance(context).getArrayList().add(0, task);

            // Save to DB
            //ListView lv = (ListView) findViewById(R.id.lvListReminders);
            //TaskListBaseAdapter currentList = new TaskListBaseAdapter(this, Singleton.getInstance(this).getArrayList());

            Singleton.getInstance(context).getDb().addTask(task);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

//somewhere in the activity
//new GetFromWebTask("www.google.com").execute();


