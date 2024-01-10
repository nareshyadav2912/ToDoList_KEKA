package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class AddTask extends AppCompatActivity {
    private  Task t;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText task=findViewById(R.id.task);
        EditText description=findViewById(R.id.description);
        TextView from=findViewById(R.id.from);
        TextView to=findViewById(R.id.to);
        Spinner color=findViewById(R.id.color);
        Button submit=findViewById(R.id.submit);
        TextView delete=findViewById(R.id.delete);

        Database database=new Database(this);
        t=new Task();
        String date=getIntent().getStringExtra("Date");
        t.setID(database.getNextID(date));
        String[] colors={"High","Medium","Low"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,colors);
        color.setAdapter(adapter);
        if(getIntent().hasExtra("Task")){
            t.setID(getIntent().getIntExtra("ID",0));
            t.setTask(getIntent().getStringExtra("Task"));
            t.setDescription(getIntent().getStringExtra("Description"));
            t.setFrom(getIntent().getStringExtra("From"));
            t.setTo(getIntent().getStringExtra("To"));
            t.setColor(getIntent().getStringExtra("Color"));
            color.setSelection(adapter.getPosition(t.getColor()));

//            GradientDrawable background= (GradientDrawable) color.getBackground();
//            background.setColor(t.getColorID(AddTask.this));

            task.setText(t.getTask());
            description.setText(t.getDescription());
            from.setText(t.getFromToString());
            to.setText(t.getToToString());

            submit.setOnClickListener(v->{
                if(task.getText().toString().equals("")){
                    task.setError("Task Cannot be Empty,");
                    return;
                }
                if(from.getText().equals("Click Here")){
                    Toast.makeText(this,"Select Time: From",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(to.getText().equals("Click Here")){
                    Toast.makeText(this,"Select Time; To",Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setTask(task.getText().toString());
                t.setDescription(task.getText().toString());
                database.updateTask(t,date);
                Toast.makeText(this,"Task Updated ",Toast.LENGTH_SHORT).show();
                finish();
            });
            delete.setOnClickListener(v->{
                database.deleteTask(t.getID(),date);
                Toast.makeText(this,"Task Deleted", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
        else{
            submit.setOnClickListener(v->{
                if(task.getText().toString().equals("")){
                    task.setError("Task Cannot be Empty,");
                    return;
                }
                if(from.getText().equals("Click Here")){
                    Toast.makeText(this,"Select Time: From",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(to.getText().equals("Click Here")){
                    Toast.makeText(this,"Select Time; To",Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setTask(task.getText().toString());
                t.setDescription(description.getText().toString());
                database.addTask(t,date);
                Toast.makeText(this,"Task Added",Toast.LENGTH_SHORT).show();
                finish();
            });
            delete.setVisibility(View.GONE);
        }
        from.setOnClickListener(v->{
            TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String ho=new DecimalFormat("00").format(hourOfDay);
                    String min=new DecimalFormat("00").format(minute);

                    from.setText(ho+":"+min);
                    t.setFrom(ho+":"+min);
                }
            },t.getFrom().getHour(),t.getFrom().getMinute(),false);
            timePickerDialog.show();
        });
        to.setOnClickListener(v->{
            TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String ho=new DecimalFormat("00").format(hourOfDay);
                    String min=new DecimalFormat("00").format(minute);

                    to.setText(ho+":"+min);
                    t.setFrom(ho+":"+min);
                }
            },t.getTo().getHour(),t.getTo().getMinute(),false);
            timePickerDialog.show();
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedColor = color.getSelectedItem().toString();
                t.setColor(selectedColor); // Set the color name
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}