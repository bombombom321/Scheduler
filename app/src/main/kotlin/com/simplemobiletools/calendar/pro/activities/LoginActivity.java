package com.simplemobiletools.calendar.pro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.simplemobiletools.commons.extensions.*;
import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.helpers.Config;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;
import com.simplemobiletools.commons.extensions.ContextKt;

import java.util.ArrayList;


public class LoginActivity extends SimpleActivity {

    private EditText emailText;
    private EditText pwText;
    private Button Loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button RegisterButton = (Button) findViewById(R.id.RegisterButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);

         ViewGroup linearLayout = findViewById(R.id.login_layout);
        final EditText email_et = findViewById(R.id.emailText);
        final EditText password_et = findViewById(R.id.pwText);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//Regist 버튼 누르면 액티비티 이동
                Intent welcomeIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(welcomeIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {//사용자로부터 계정 정보 받기
            @Override
            public void onClick(View v) {
                loginAct(email_et.getText().toString(), password_et.getText().toString());
            }
        });
        emailText = (EditText) findViewById(R.id.emailText);
        pwText = (EditText) findViewById(R.id.pwText);
    }

    public void loginAct(String email, String password) {//서버로 계정 정보를 보내주는 메소드

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("a");
        Paramname.add("email");
        Paramname.add("password");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("account_auth");
        Paramvalue.add(email);
        Paramvalue.add(password);

        new AsyncHttpTask(LoginActivity.this, getString(R.string.server_api_auth), mHandler, Paramname,
                Paramvalue, null, 1, 0);//계정 정보를 서버로 전달
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout

            if (msg.what == 1 ) {//서버에 계정이 있다면 토스트 메시지 출력
                String result = msg.obj.toString();

                if(!result.contains("error")){
                Config config = new Config(LoginActivity.this);
                config.setFirstLaunch(false);//첫 실행이 아님
                config.setAuth(result);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);//계정이 있다면 메인 액티비티로 이동

                    finish();
            }  else {
                    Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
