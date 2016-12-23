package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import model.Timer;

/**
 * Created by Xuezhu on 12/22/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TIMER = "CREATE TABLE " + Constants.TABLE_TIMER +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_SECONDS +
                " INTEGER," + Constants.KEY_ROUNDS + " INTEGER," + Constants.KEY_REST +
                " INTEGER," + Constants.KEY_SETS + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_TIMER);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_TIMER);
        // Create tables again
        onCreate(db);
    }

    // Add new timer
    public void addTimer(Timer timer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_SECONDS, timer.getSeconds());
        values.put(Constants.KEY_ROUNDS, timer.getRounds());
        values.put(Constants.KEY_REST, timer.getRest());
        values.put(Constants.KEY_SETS, timer.getSet());

        long rowID = db.insert(Constants.TABLE_TIMER, null, values);
        Log.v("Add new timer", rowID != -1 ? "Yes" : "No");
        db.close();
    }

    // Get all timers
    public ArrayList<Timer> getAllTimer() {
        ArrayList<Timer> timers = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_TIMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Timer timer = new Timer();
                timer.setTimerId(Integer.parseInt(cursor.getString(0)));
                timer.setSeconds(Integer.parseInt(cursor.getString(1)));
                timer.setRounds(Integer.parseInt(cursor.getString(2)));
                timer.setRest(Integer.parseInt(cursor.getString(3)));
                timer.setSet(Integer.parseInt(cursor.getString(4)));
                // Adding timer to list
                timers.add(timer);
            } while (cursor.moveToNext());
        }

        // return timer list
        return timers;
    }

    // Deleting single timer
    public void deleteTimer(Timer timer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_TIMER, Constants.KEY_ID + " = ?",
                new String[] { String.valueOf(timer.getTimerId()) });
        db.close();
    }


}
