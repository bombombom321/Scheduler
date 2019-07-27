package com.simplemobiletools.calendar.pro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.simplemobiletools.calendar.pro.R;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Button bothAddbutton =(Button)findViewById(R.id.bothbutton);
        Button bothListbutton =(Button)findViewById(R.id.bothbutton1);

        Button urgentAddbutton =(Button)findViewById(R.id.urgentbutton);
        Button urgentListbutton =(Button)findViewById(R.id.urgentbutton1);

        Button importantAddbutton =(Button)findViewById(R.id.importantbutton);
        Button importantListbutton =(Button)findViewById(R.id.importantbutton1);

        Button nothingAddbutton =(Button)findViewById(R.id.nothingbutton);
        Button nothingListbutton =(Button)findViewById(R.id.nothingbutton1);

        bothAddbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent bothIntent =new Intent(TaskActivity.this,TaskAddActivity.class);
                clickEvent(bothIntent,"1","1");

            }
        });
        bothListbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent both_Intent =new Intent(TaskActivity.this,TaskListActivity.class);
                clickEvent(both_Intent,"1","1");

            }
        });
        urgentAddbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent urgentIntent =new Intent(TaskActivity.this,TaskAddActivity.class);
                clickEvent(urgentIntent,"1","0");

            }
        });
        urgentListbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent urgentIntent =new Intent(TaskActivity.this,TaskListActivity.class);
                clickEvent(urgentIntent,"1","0");
            }
        });
        importantAddbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent importantIntent =new Intent(TaskActivity.this,TaskAddActivity.class);
                clickEvent(importantIntent,"0","1");

            }
        });
        importantListbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent important_Intent =new Intent(TaskActivity.this,TaskListActivity.class);
                clickEvent(important_Intent,"0","1");
            }
        });
        nothingAddbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nothingIntent =new Intent(TaskActivity.this,TaskAddActivity.class);
                clickEvent(nothingIntent,"0","0");
            }
        });
        nothingListbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nothing_Intent =new Intent(TaskActivity.this,TaskListActivity.class);
                clickEvent(nothing_Intent,"0","0");
            }
        });

    }
    public void clickEvent(Intent intent, String urgency, String importance){
        TaskActivity.this.startActivity(intent);
        TaskAddActivity.urgency = urgency;
        TaskAddActivity.importance = importance;

    }


}
