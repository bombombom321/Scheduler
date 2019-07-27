package com.simplemobiletools.calendar.pro.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.models.Task;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    private static class ViewHolder{
        public TextView tvTitle;
        public TextView tvContent;
    }

    public TaskAdapter(Context context, ArrayList<Task> task) {
        super(context, 0, task);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tvContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(task.getTitle());
        viewHolder.tvContent.setText(task.getContent());

        return convertView;
    }


}
