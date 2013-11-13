package com.ao.rememberus;

/**
 * Created by joe on 25/10/13.
 */
public class Task {
    int _id;
    private String _taskMessage ;

    public Task(int id, String str) {
        this._id = id;
        this._taskMessage = str;
    }

    public Task(String str) {
        this._taskMessage = str;
    }

    public Task() {
    }

    public String getTaskMessage() {
        return _taskMessage;
    }
    public void setTaskMessage(String name) {
        this._taskMessage = name;
    }

    // getting ID
    public int getID(){
        return this._id;
    }
    // setting id
    public void setID(int id){
        this._id = id;
    }

    @Override
    public String toString (){
        return this._taskMessage;
    }
}
