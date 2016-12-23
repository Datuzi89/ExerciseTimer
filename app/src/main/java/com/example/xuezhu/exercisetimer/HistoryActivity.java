package com.example.xuezhu.exercisetimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import data.CustonListviewAdapter;
import data.DatabaseHandler;
import model.Timer;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private ArrayList<Timer> dbTimers = new ArrayList<>();
    private CustonListviewAdapter timerAdapter;
    private ListView listView;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.listview);
        refreshData();
    }

    private void refreshData() {
        dbTimers.clear();
        databaseHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<Timer> timersFromDB = databaseHandler.getAllTimer();
        for (int i = 0; i < timersFromDB.size(); i++) {
            int id = timersFromDB.get(i).getTimerId();
            int seconds = timersFromDB.get(i).getSeconds();
            int rounds = timersFromDB.get(i).getRounds();
            int rest = timersFromDB.get(i).getRest();
            int sets = timersFromDB.get(i).getSet();

            timer = new Timer(id, seconds, rounds, rest, sets);
            dbTimers.add(timer);
        }
        databaseHandler.close();
        timerAdapter = new CustonListviewAdapter(HistoryActivity.this, R.layout.list_row, dbTimers);
        listView.setAdapter(timerAdapter);
        timerAdapter.notifyDataSetChanged();
    }
}
