package data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.xuezhu.exercisetimer.MainActivity;
import com.example.xuezhu.exercisetimer.R;

import java.util.ArrayList;

import model.Timer;



public class CustonListviewAdapter extends ArrayAdapter<Timer>{
    private int layoutResource;
    private Activity activity;
    private ArrayList<Timer> timers = new ArrayList<>();

    public CustonListviewAdapter(Activity activity, int layoutResource, ArrayList<Timer> timers) {
        super(activity, layoutResource, timers);
        this.layoutResource = layoutResource;
        this.activity = activity;
        this.timers = timers;
    }

    @Override
    public int getCount() {
        return timers.size();
    }

    @Nullable
    @Override
    public Timer getItem(int position) {
        return timers.get(position);
    }

    @Override
    public int getPosition(Timer item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null || (row.getTag() == null)) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResource, null);
            holder = new ViewHolder();
            holder.seconds = (TextView) row.findViewById(R.id.secondHistory);
            holder.rounds = (TextView) row.findViewById(R.id.roundsHistory);
            holder.rest = (TextView) row.findViewById(R.id.restHistory);
            holder.sets = (TextView) row.findViewById(R.id.setHistory);
            row.setTag(holder);
        }
        else holder = (ViewHolder) row.getTag();
        holder.timer = getItem(position);
        holder.seconds.setText("Seconds: " + holder.timer.getSeconds());
        holder.rounds.setText("Rounds: " + holder.timer.getRounds());
        holder.rest.setText("Rest: " + holder.timer.getRest());
        holder.sets.setText("Set: " + holder.timer.getSet());
        final ViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);

                // Passing timer info back to main activity
                Bundle bundle = new Bundle();
                bundle.putSerializable("userObj", finalHolder.timer);
                intent.putExtras(bundle);

                activity.startActivity(intent);
            }
        });
        return row;

    }

    public class ViewHolder{
        Timer timer;
        TextView seconds;
        TextView rounds;
        TextView rest;
        TextView sets;

    }
}
