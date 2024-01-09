package com.example.myapplication;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private LocalDate showeddate;
    private ArrayList<Task> tasks;
    private final DateTimeFormatter mainDate=DateTimeFormatter.ofPattern("EEEE dd/MM");
    private Database database;
    private TextView date;
    private ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date=findViewById(R.id.date);
        ImageView left=findViewById(R.id.left);
        ImageView right=findViewById(R.id.right);
        ListView listView=findViewById(R.id.listview);
        LinearLayout add=findViewById(R.id.add);

        database =new Database(this);
        tasks=new ArrayList<>();
        listAdapter=new ListAdapter();
        listView.setAdapter(listAdapter);

        Calendar calendar=Calendar.getInstance();
        showeddate= LocalDate.now();
        RefreshData();

        date.setOnClickListener(v->{
            DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    showeddate= LocalDate.of(year,month+1,dayOfMonth);
                    RefreshData();
                }

            },showeddate.getYear(),showeddate.getMonthValue()-1,showeddate.getDayOfMonth());
            datePickerDialog.show();
        });

        left.setOnClickListener(v->{
            showeddate=showeddate.minusDays(1);
            RefreshData();
        });
        right.setOnClickListener(v->{
            showeddate=showeddate.plusDays(1);
            RefreshData();
        });
        add.setOnClickListener(v->{
            Intent i =new Intent(MainActivity.this,AddTask.class);
            i.putExtra("Date",showeddate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            startActivity(i);
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        RefreshData();
    }
    public void RefreshData(){
        date.setText(showeddate.format(mainDate));
        ArrayList<Task> ts=database.getAllTasks(showeddate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Collections.sort(ts);
        tasks=ts;
        listAdapter.notifyDataSetChanged();

    }
    public class ListAdapter extends BaseAdapter{
        public ListAdapter(){

        }
        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Task getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            @SuppressLint("ViewHolder") View v=inflater.inflate(R.layout.task,null);
            TextView from=v.findViewById(R.id.from);
            TextView to= v.findViewById(R.id.to);
            TextView task=v.findViewById(R.id.task);
            TextView description=v.findViewById(R.id.description);
            Task t=tasks.get(position);

            from.setText(t.getFromToString());
            to.setText(t.getToToString());
            task.setText(t.getTask());
            description.setText(t.getDescription());

            GradientDrawable backDrawable=(GradientDrawable) task.getBackground();
            backDrawable.setColor(t.getColorID(MainActivity.this));

            task.setOnLongClickListener(v1->{
                Intent i=new Intent(MainActivity.this,AddTask.class);
                i.putExtra("ID",t.getID());
                i.putExtra("Task",t.getTask());
                i.putExtra("Description",t.getDescription());
                i.putExtra("From",t.getFromToString());
                i.putExtra("To",t.getToToString());
                i.putExtra("Color",t.getColor());
                i.putExtra("Date",showeddate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                startActivity(i);
                return true;
            });
            return v;
        }
    }
}