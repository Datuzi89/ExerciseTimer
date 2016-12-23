package com.example.xuezhu.exercisetimer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import data.DatabaseHandler;
import model.Timer;

public class MainActivity extends AppCompatActivity {

    private int seconds, rounds, rest, sets;
    private EditText secondText, roundText, restText, setText;
    private Button startButton, pauseButton, stopButton;
    private CountDownTimer countDownTimer;
    private boolean isClear = false, isPause = false;
    private long timeRemaining = 0;

    private TextView history, save;
    private DatabaseHandler databaseHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secondText = (EditText) findViewById(R.id.secondText);
        roundText = (EditText) findViewById(R.id.roundText);
        restText = (EditText) findViewById(R.id.restText);
        setText = (EditText) findViewById(R.id.setText);
        startButton = (Button) findViewById(R.id.startButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        pauseButton.setEnabled(false);

        startButton.setOnClickListener(btnClickListener);
        pauseButton.setOnClickListener(btnClickListener);
        stopButton.setOnClickListener(btnClickListener);

        if(secondText.getText().toString().equals("Done!"))
            stopButton.setText("clear");

        history = (TextView) findViewById(R.id.historyText);
        history.setOnClickListener(btnClickListener);
        save = (TextView) findViewById(R.id.saveText);
        save.setOnClickListener(btnClickListener);

        databaseHandler = new DatabaseHandler(MainActivity.this);

    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startButton:
                    start();
                    break;
                case R.id.pauseButton:
                    pause();
                    break;
                case R.id.stopButton:
                    stop();
                    break;
                case R.id.historyText:
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    break;
                case R.id.saveText:
                    submitToDB();

            }
        }
    };

    private void getData(){
        String secondStr = secondText.getText().toString().trim();
        String roundStr = roundText.getText().toString().trim();
        String restStr = restText.getText().toString().trim();
        String setStr = setText.getText().toString().trim();
        if (secondStr == "" || roundStr == "" || restStr == "" || setStr == "") {
            Toast.makeText(getApplicationContext(), "No empty fields allowed", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                seconds = Integer.parseInt(secondStr);
                rounds = Integer.parseInt(roundStr);
                rest = Integer.parseInt(restStr);
                sets = Integer.parseInt(setStr);

            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Not a num", Toast.LENGTH_LONG).show();
            }


        }
    }

    private void start(){
        getData();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        isPause = false;
        final long totalSeconds = (seconds * rounds + rest) * sets * 1000;
        long makeup = 1000;
        long countDownInterval = 1000;
        countDownTimer = new CountDownTimer(totalSeconds + makeup, countDownInterval) {
            int secondRemain = seconds;
            int roundRemain = rounds;
            int restRemain = rest;
            int setRemain = sets;
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Remain", String.valueOf(millisUntilFinished));
                timeRemaining = millisUntilFinished;
                if(!isPause){
                    if(roundRemain != 0) {
                        secondText.setText("" + secondRemain);
                        roundText.setText("" + roundRemain);
                        secondRemain--;
                        if (secondRemain == 0) {
                            try {
                                playSound();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            secondRemain = seconds;
                            roundRemain--;
                        }
                    }
                    else {
                        roundText.setText("" + roundRemain);
                        secondText.setText("" + restRemain);
                        restRemain--;
                        if (restRemain == 0) {
                            try {
                                playSound();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            roundRemain = rounds;
                            restRemain = rest;
                            setRemain--;
                        }
                    }
                    setText.setText("" + setRemain);
                }
                else this.cancel();
            }

            @Override
            public void onFinish() {
                secondText.setText("Done!");
                stopButton.setText("Clear");
                isClear = true;
                pauseButton.setEnabled(false);
                startButton.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    private void pause() {
        if(pauseButton.getText().toString().equals("Pause")){
            pauseButton.setText("Resume");
            isPause = true;
            if (countDownTimer != null) countDownTimer.cancel();
        }
        else {
            pauseButton.setText("Pause");
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            isPause = false;
            long countDownInterval = 1000;
            countDownTimer = new CountDownTimer(timeRemaining, countDownInterval) {
                int secondRemain = Integer.parseInt(secondText.getText().toString());
                int roundRemain = Integer.parseInt(roundText.getText().toString());
                int restRemain = Integer.parseInt(restText.getText().toString());
                int setRemain = Integer.parseInt(setText.getText().toString());
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.v("Remain", String.valueOf(millisUntilFinished));
                    timeRemaining = millisUntilFinished;
                    if(!isPause){
                        if(roundRemain != 0) {
                            secondText.setText("" + secondRemain);
                            secondRemain--;
                            if (secondRemain == 0) {
                                try {
                                    playSound();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                secondRemain = seconds;
                                roundRemain--;
                                roundText.setText("" + roundRemain);
                            }
                        }
                        else {
                            roundText.setText("" + roundRemain);
                            secondText.setText("" + restRemain);
                            restRemain--;
                            if (restRemain == 0) {
                                try {
                                    playSound();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                roundRemain = rounds;
                                restRemain = rest;
                                setRemain--;
                            }
                        }
                        setText.setText("" + setRemain);
                    }
                    else this.cancel();
                }

                @Override
                public void onFinish() {
                    secondText.setText("Done!");
                    stopButton.setText("Clear");
                    isClear = true;
                    pauseButton.setEnabled(false);
                    startButton.setEnabled(true);
                }
            };
            countDownTimer.start();
        }
    }

    private void stop() {
        if(isClear) {
            stopButton.setText("Stop");
            isClear = false;
        }
        if(countDownTimer != null )countDownTimer.cancel();
        secondText.setText("");
        roundText.setText("");
        setText.setText("");
        restText.setText("");
        startButton.setEnabled(true);
    }

    private void submitToDB() {
        getData();
        //System.out.println("" + seconds +" "+ rounds+" "+rest+" "+sets);

        Timer timer = new Timer();
        timer.setSeconds(seconds);
        timer.setRounds(rounds);
        timer.setRest(rest);
        timer.setSet(sets);

        System.out.println(timer.getRest());
        databaseHandler.addTimer(timer);
    }

    private void playSound() throws IOException {
        Uri defaultTingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setDataSource(getApplicationContext(),defaultTingtone);
        mp.prepare();
        mp.start();
    }

}
