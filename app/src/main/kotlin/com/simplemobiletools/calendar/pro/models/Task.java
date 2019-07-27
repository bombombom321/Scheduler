package com.simplemobiletools.calendar.pro.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Task implements Serializable, Parcelable {
    private String title;
    private String content;
    private String taskDeadline;
    private String taskDuration;
    private int urgency;
    private int importance;

    protected Task(Parcel in) {
        title = in.readString();
        content = in.readString();
        taskDeadline = in.readString();
        taskDuration = in.readString();
        urgency = in.readInt();
        importance = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title;}

    public String getContent(){ return content; }
    public void setContent(String content){ this.content = content;}

    public String getTaskDeadline(){ return taskDeadline; }
    public void setTaskDeadline(String taskDeadline){ this.taskDeadline = taskDeadline;}

    public String getTaskDuration(){ return taskDuration; }
    public void setTaskDuration(String taskDuration){ this.taskDuration = taskDuration;}

    public int getUrgency(){ return urgency; }
    public void setUrgency(int urgency){ this.urgency = urgency;}

    public int getImportance(){ return importance; }
    public void setImportance(int importance){ this.importance = importance;}


    public Task(){}
    public Task(String title, String content, String taskDeadline, String taskDuration){
        this.title = title;
        this.content = content;
        this.taskDeadline = taskDeadline;
        this.taskDuration = taskDuration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());

    }
}
