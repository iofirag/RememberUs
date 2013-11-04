package com.ao.rememberus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joe on 25/10/13.
 */
public class ItemListBaseAdapter extends BaseAdapter {

    private static ArrayList<Item> itemDetailsrrayList;
    private LayoutInflater l_Inflater;

    public ItemListBaseAdapter(Context context, ArrayList<Item> results) {
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView remindMessage;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.item_details_view, null);
            holder = new ViewHolder();
            holder.remindMessage = (TextView) convertView.findViewById(R.id.rememberMessaage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.remindMessage.setText(itemDetailsrrayList.get(position).getRemindMessage());

        return convertView;
    }
}