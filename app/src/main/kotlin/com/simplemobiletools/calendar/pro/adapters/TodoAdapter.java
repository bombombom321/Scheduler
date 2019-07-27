package com.simplemobiletools.calendar.pro.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.models.Task;
import com.simplemobiletools.calendar.pro.models.TodoGroup;
import com.simplemobiletools.calendar.pro.views.TaskViewHolder;
import com.simplemobiletools.calendar.pro.views.TodoGroupViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class TodoAdapter extends ExpandableRecyclerViewAdapter<TodoGroupViewHolder, TaskViewHolder> {

    public TodoAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public TodoGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_todogroup, parent, false);
        return new TodoGroupViewHolder(view);
    }

    @Override
    public TaskViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_todo, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(TaskViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final Task task = ((TodoGroup) group).getItems().get(childIndex);
        holder.setTodoName(task.getTitle());

    }

    @Override
    public void onBindGroupViewHolder(TodoGroupViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {


        holder.setTitle(group);

    }



}
