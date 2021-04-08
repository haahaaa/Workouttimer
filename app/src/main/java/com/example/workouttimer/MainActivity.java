package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    String enteredtype;
    private ImageButton btnstart, btnpause, btnstop;


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






        chronometer.setFormat("%s");

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startChronometer();
            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer();
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                stopChronometer();


            }
        });






    }
    public void startChronometer(){
        if(!running){

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;

        }
    }


    public void pauseChronometer(){
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;

        }
    }
    public void stopChronometer(){

            enteredtype = type.getText().toString();
            type.setText(enteredtype);
            long time = (SystemClock.elapsedRealtime()- chronometer.getBase())/1000;
            savetime = time;
            spendtime.setText("You spent "  + time  + " secconds on " + enteredtype);
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            chronometer.stop();
            running = false;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("running", running);
        outState.putLong("pauseOffset", pauseOffset);
        outState.putLong("chronometer", chronometer.getBase() -1);
        outState.putLong("time", savetime);




    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        running = savedInstanceState.getBoolean("running");
        pauseOffset = savedInstanceState.getLong("pauseOffset");
        chronometer.setBase(savedInstanceState.getLong("chronometer"));
        savetime = savedInstanceState.getLong("time");


        if(running){

            chronometer.start();
        }else
            {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }

    }
}
