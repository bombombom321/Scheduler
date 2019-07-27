package com.simplemobiletools.calendar.pro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.models.Task;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskAddActivity extends SimpleActivity {
    static String urgency;
    static String importance;
    private boolean isSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        Button saveButton = findViewById(R.id.saveButton);
        final EditText title = findViewById(R.id.etTitle);
        final EditText content = findViewById(R.id.etContent);
        final EditText deadline = findViewById(R.id.etDeadline);
        final EditText duration = findViewById(R.id.etDuration);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task(title.getText().toString(), content.getText().toString(), deadline.getText().toString(), duration.getText().toString());
                task.setUrgency(Integer.parseInt(urgency));
                task.setImportance(Integer.parseInt(importance));


                // 문자열(String) 을 TimeStamp 타입으로 변환한다.
               String deadline = task.getTaskDeadline();
               String pattern = "yyyy-MM-dd HH:mm";
                //입력된 시간 형식 확인
               if(deadline.length() == pattern.length()) {
                   try {
                       // String 타입을 Date 로 변환한다.
                       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                       Date date = formatter.parse(deadline);
                       long timestamp = date.getTime();
                       //timestamp 값으로 task객체 속성변경
                       task.setTaskDuration(String.valueOf(timestamp));
                   } catch (Exception ex) {
                   }

                   transmitTaskData(task);
                   Intent intent = new Intent(TaskAddActivity.this, TaskActivity.class);
                   TaskAddActivity.this.startActivity(intent);
               }
               else {
                   Toast.makeText(TaskAddActivity.this, "마감기한의 형식이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
               }

            }
        });
    }

        private void transmitTaskData(Task task) {
            ArrayList<String> Paramname = new ArrayList<String>();
            Paramname.add("a");
            Paramname.add("title");
            Paramname.add("content");
            Paramname.add("end_timestamp");
            Paramname.add("task_size_hour");
            Paramname.add("urgency");
            Paramname.add("importance");

            ArrayList<String> Paramvalue = new ArrayList<String>();
            Paramvalue.add("scheduler_todo_create");
            Paramvalue.add(task.getTitle());
            Paramvalue.add(task.getContent());
            Paramvalue.add(task.getTaskDeadline());
            Paramvalue.add(task.getTaskDuration());
            Paramvalue.add(String.valueOf(task.getUrgency()));
            Paramvalue.add(String.valueOf(task.getImportance()));


            new AsyncHttpTask(TaskAddActivity.this, getString(R.string.server_api_auth), mHandler, Paramname,
                    Paramvalue, null, 1, 0);//계정 정보를 서버로 전달

        }
    public Handler mHandler = new Handler() {//회원가입 되었는지 확인
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String result = msg.obj.toString();
                Toast.makeText(TaskAddActivity.this, result, Toast.LENGTH_LONG).show();

            }
            else
            Toast.makeText(TaskAddActivity.this, "실패", Toast.LENGTH_LONG).show();
        }
    };
}


