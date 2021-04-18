package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset,savetime;
    private boolean running;
    private TextView spendtime;
    private EditText type;
    String enteredtype,save;
    private ImageButton btnstart, btnpause, btnstop;
    SharedPreferences sharedPreferences;
    Boolean clicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        btnstart = findViewById(R.id.btnstart);
        btnpause = findViewById(R.id.btnpause);
        btnstop = findViewById(R.id.btnstop);
        spendtime = findViewById(R.id.spendtime);
        type = findViewById(R.id.type);
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        chronometer.setFormat("%s");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        display();

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!running){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;

                }
            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;

                }
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredtype = type.getText().toString();
                type.setText(enteredtype);
                long time = (SystemClock.elapsedRealtime()- chronometer.getBase())/1000;
                savetime = time;
                editor.putLong("TIME",time);
                editor.putString("TYPE", enteredtype);
                editor.apply();
                spendtime.setText("You spent "  + savetime  + " secconds on " + enteredtype);
                save = spendtime.getText().toString();
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                chronometer.stop();
                running = false;



            }
        });

    }

    private void display() {

            String inputtype = sharedPreferences.getString("TYPE", "");
            Long inputtime = sharedPreferences.getLong("TIME", 0);

            spendtime.setText("You spent " + inputtime + " secconds on " + inputtype);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("running", running);
        outState.putLong("pauseOffset", pauseOffset);
        outState.putLong("chronometer", chronometer.getBase() -1);
        outState.putLong("time", savetime);
        outState.putString("save", save);

    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        running = savedInstanceState.getBoolean("running");
        pauseOffset = savedInstanceState.getLong("pauseOffset");
        chronometer.setBase(savedInstanceState.getLong("chronometer"));
        savetime = savedInstanceState.getLong("time");
        save = savedInstanceState.getString("save");
        spendtime.setText(save);

        if(running){

            chronometer.start();

        }else
            {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }

    }
}
