package com.simplemobiletools.calendar.pro.views;

import android.view.View;
import android.widget.TextView;

import com.simplemobiletools.calendar.pro.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class TaskViewHolder extends ChildViewHolder {

  private TextView childTextView;

  public TaskViewHolder(View itemView) {
    super(itemView);
    childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
  }

  public void setTodoName(String name) {
    childTextView.setText(name);
  }
}
