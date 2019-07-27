package com.simplemobiletools.calendar.pro.activities;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.adapters.TodoAdapter;
import com.simplemobiletools.calendar.pro.models.Task;
import com.simplemobiletools.calendar.pro.models.TodoGroup;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.simplemobiletools.calendar.pro.activities.TaskListActivity.getEventfromJson;


public class TodoActivity extends SimpleActivity {

    public TodoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(true);
        }

        adapter = new TodoAdapter(makeTodoGroup());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Button convertToEventsButton = findViewById(R.id.toggle_button);


        convertToEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getTask();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }


    public static List<TodoGroup> makeTodoGroup() {
        // ArrayList<Task> ar = ;

        return Arrays.asList(new TodoGroup("긴급 + 중요", new ArrayList<Task>(), 0), new TodoGroup("중요", new ArrayList<Task>(), 0), new TodoGroup("긴급", new ArrayList<Task>(), 0), new TodoGroup("일반", new ArrayList<Task>(), 0));
    }


    public void getTask() {

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("scheduler_todo_getall_byuser");

        new AsyncHttpTask(TodoActivity.this, getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 1, 0);//계정 정보를 서버로 전달
    }




    public void convertToEvent() {

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("scheduler_convert_to_event");

        new AsyncHttpTask(TodoActivity.this, getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 2, 0);//계정 정보를 서버로 전달
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout

            if (msg.what == 1) {
                String result = msg.obj.toString();

                if (result == null || result == "") {

                } else {
                    ArrayList<Task> tasks = getEventfromJson(result);
                    //         Toast.makeText(TaskListActivity.this,result,Toast.LENGTH_LONG).show();

                    for (Task task : tasks) {
                        addTask(task);
                    }
                    expandAll();

                }

            }

            if(msg.what == 2){
                String result = msg.obj.toString();
                if (result.contains("success")) {
                    Toast.makeText(TodoActivity.this, getString(R.string.todo_convert_to_event_success_des), Toast.LENGTH_LONG);

                }else{
                    Toast.makeText(TodoActivity.this, getString(R.string.an_error_occurred), Toast.LENGTH_LONG);
                }
            }
        }

        ;
    };

    public void addTask(Task task) {
        int category_index = 0;
        if (task.getImportance() == 1 && task.getUrgency() == 1) category_index = 0;
        if (task.getImportance() == 1 && task.getUrgency() == 0) category_index = 1;
        if (task.getImportance() == 0 && task.getUrgency() == 1) category_index = 2;
        if (task.getImportance() == 0 && task.getUrgency() == 0) category_index = 3;
        adapter.getGroups().get(category_index).getItems().add(task);
        adapter.notifyDataSetChanged();


    }

    public void expandAll() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (!adapter.isGroupExpanded(i)) {
                adapter.toggleGroup(i);
            }
        }
    }


}
