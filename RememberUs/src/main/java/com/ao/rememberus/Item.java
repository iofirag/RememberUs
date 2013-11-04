package com.ao.rememberus;

/**
 * Created by joe on 25/10/13.
 */
public class Item {
    private String remindMessage ;
    public Item (String str)
    {
        remindMessage = str;
    }

    public String getRemindMessage() {
        return remindMessage;
    }
    public void setRemindMessage(String name) {
        this.remindMessage = name;
    }

    @Override
    public String toString ()
    {
        return this.remindMessage;
    }
}
