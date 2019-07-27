package com.simplemobiletools.calendar.pro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.adapters.TaskAdapter;
import com.simplemobiletools.calendar.pro.helpers.Config;
import com.simplemobiletools.calendar.pro.models.Event;
import com.simplemobiletools.calendar.pro.models.Task;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends SimpleActivity {

    private ListView lvTasks;
    private TaskAdapter taskAdapter;
    ArrayList<Task> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        lvTasks = (ListView) findViewById(R.id.IvTask);
        ArrayList<Task> aTasks = new ArrayList<Task>();
        taskAdapter = new TaskAdapter(this, aTasks);
        lvTasks.setAdapter(taskAdapter);
        getTask();
    }

    public void getTask(){

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("scheduler_todo_getall_byuser");

        new AsyncHttpTask(TaskListActivity.this, getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 1, 0);//계정 정보를 서버로 전달
    }

    public void checkTask(Task task){
        if (TaskAddActivity.importance.equals(task.getImportance()) && TaskAddActivity.urgency.equals(task.getUrgency())){
            taskAdapter.add(task);
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout

            if (msg.what == 1 ) {
                String result = msg.obj.toString();

                if(result == null || result == ""){

                }else{
                    events = getEventfromJson(result);
           //         Toast.makeText(TaskListActivity.this,result,Toast.LENGTH_LONG).show();

                    for(Task task: events){
                        checkTask(task);
                }

                }

        }
    };
};
    // 받아온 Task 객체 Task 리스트로 매핑
    public  static ArrayList<Task> getEventfromJson(String content) {
        ArrayList<Task> yourArray = null;
        try {
            JSONArray array = new JSONArray(content);
            yourArray   = new Gson().fromJson(array.toString(), new TypeToken<List<Task>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return yourArray;
    }
}
