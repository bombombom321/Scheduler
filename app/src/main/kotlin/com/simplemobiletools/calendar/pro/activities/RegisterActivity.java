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
import com.simplemobiletools.calendar.pro.helpers.Config;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;

import java.util.ArrayList;


public class RegisterActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button summitbutton = findViewById(R.id.registerButton);
        final EditText email_et = findViewById(R.id.emailText);
        final EditText password_et = findViewById(R.id.pwText);

        summitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//사용자로부터 계정 정보 받기
              registerAct(email_et.getText().toString(), password_et.getText().toString());
            }
        });
    }

    public void registerAct(String email, String password) {//서버에 계정 정보 전달

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");
        Paramname.add("email");
        Paramname.add("password");
        Paramname.add("name_2");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("account_sign_up");
        Paramvalue.add(email);
        Paramvalue.add(password);
        Paramvalue.add("김민규");

        new AsyncHttpTask(RegisterActivity.this, getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 1, 0);
    }


    public Handler mHandler = new Handler() {//회원가입 되었는지 확인
        public void handleMessage(Message msg) {

            if (msg.what == 1 ) {

                String result = msg.obj.toString();
                if(!result.contains("error")){
                    Toast.makeText(RegisterActivity.this, "회원가입 되었습니다.", Toast.LENGTH_LONG).show();
                    Config config = new Config(RegisterActivity.this);
                    config.setFirstLaunch(false);//첫 실행이 아니라면 메인 액티비티로 넘어감
                    config.setAuth(result);
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
