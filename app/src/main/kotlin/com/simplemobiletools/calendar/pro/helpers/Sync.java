package com.simplemobiletools.calendar.pro.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.models.Event;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Sync {

    Context  context = null;

    public Sync(Context context){
        this.context = context;
    }


    public void SyncAct() {

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("scheduler_sync_info");



        new AsyncHttpTask(context, context.getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 1, 0);
    }

    public void SyncEvents(Context context){
        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");
        Paramname.add("mtimestamp");


        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("scheduler_event_get_lastupdates_byuser");
        Paramvalue.add(String.valueOf(new Config(context).getLast_update()));



        new AsyncHttpTask(context, context.getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 2, 0);
    }


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout
            //    setSupportProgressBarIndeterminateVisibility(false);
            if (msg.what == -1) {
                //Global.ConnectionError(HomeActivity.this);

                //Global.toast("fail" );
            }

            if (msg.what == 1) {
                String result = msg.obj.toString();
                Map resultMap = Global.getJsonObject(result);

                if(Long.parseLong(String.valueOf(resultMap.get("last_update"))) > new Config(context).getLast_update()){
                    SyncEvents(context);
                    new Config(context).setLast_update(Long.parseLong(String.valueOf(resultMap.get("last_update"))));
                }

            }


            if(msg.what == 2){
                final String result = msg.obj.toString();
                final EventsHelper eventsHelper = new EventsHelper(context);



                // 간단하게 Thread 생성자만으로 스레드 실행
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                       ArrayList<Event> events = getEventfromJson(result);
                        Log.d("ASDFASDF", Arrays.toString(events.toArray()));
                       eventsHelper.insertEvents(events,false);

                    }
                }).start();



            }

        }
    };

    public  ArrayList<Event> getEventfromJson(String content) {
        ArrayList<Event> yourArray = null;
        try {
            JSONArray array = new JSONArray(content);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Event.class, new EventDeserilizer())
                    .create();
            yourArray   =gson.fromJson(array.toString(), new TypeToken<List<Event>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return yourArray;
    }
}
