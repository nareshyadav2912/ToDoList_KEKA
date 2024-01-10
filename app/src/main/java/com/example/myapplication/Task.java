package com.example.myapplication;

import static android.graphics.Color.*;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Task implements Comparable<Task> {
    private int ID;
    private String task;
    private String description;
    private LocalTime from;
    private LocalTime to;
    private String color;
    private final DateTimeFormatter formatter =DateTimeFormatter.ofPattern("HH:mm");

    public Task(){
        Calendar calendar= Calendar.getInstance();
        from=LocalTime.of(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE));
        to=LocalTime.of(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE));
    }
    public int getID(){
        return ID;
    }
    public void setID(int ID){
        this.ID=ID;
    }
    public String getTask(){
        return task;
    }
    public void setTask(String task){
        this.task=task;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getFromToString(){
        return from.format(formatter);
    }
    public LocalTime getFrom(){
        return from;
    }
    public void setFrom(String from){
        this.from=LocalTime.parse(from.trim());
    }
    public String getToToString(){
        return to.format(formatter);
    }
    public LocalTime getTo(){
        return to;
    }
    public void setTo(String to){

        this.to=LocalTime.parse(to.trim());
    }
    public void setColor(String color){
        this.color=color;
    }
    public String getColor(){
        return color;
    }
    public int getColorID(Context context){
        int c=0;
        switch (color){
            case "High":
                c= ResourcesCompat.getColor(context.getResources(),R.color.red,null);
                break;
            case "Medium":
                c= ResourcesCompat.getColor(context.getResources(),R.color.yellow,null);
                break;
            case "Low":
                c=ResourcesCompat.getColor(context.getResources(),R.color.green,null);
                break;
            default:
                c=ResourcesCompat.getColor(context.getResources(),R.color.gray,null);
        }
        return c;
    }

    @Override
    public int compareTo(Task task) {
        return from.compareTo(task.getFrom());
    }
}
