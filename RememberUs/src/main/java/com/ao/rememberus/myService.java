package com.ao.rememberus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
    Context context;

    public myService() {
        super("myService");
        context=this;
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
            String str = getFromWeb(url);
            if (str!=null){
                onPostExecute(str);
            }
                                    System.out.println("Task created completed.");
        }
    }

    private String getFromWeb(URL url) {
        String response = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    protected void onPostExecute(String result){
        try {
                                    System.out.println("onPostExecute");
            JSONObject taskJSON = new JSONObject( result );
                                    System.out.println("JSON object");
            //create task and save it to array
            Task task = new Task(taskJSON.getInt("id"), taskJSON.getString("description") , new Date() );

            Singleton.getInstance( context ).getDb().addTask(task);
                                    System.out.println("add task to db");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
