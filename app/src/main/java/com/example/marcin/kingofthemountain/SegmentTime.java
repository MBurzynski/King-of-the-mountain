package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DateRangeLimiter;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class SegmentTime extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private int year, month, day, hour;
    RadioButton radioButtonNow, radioButtonFuture;
    TextView date, time;
    private Calendar now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_time);
        radioButtonNow = findViewById(R.id.radioButtonNow);
        radioButtonFuture = findViewById(R.id.radioButtonTime);
        date = findViewById(R.id.textViewDate);
        time = findViewById(R.id.textViewTime);
        now = Calendar.getInstance();
        year = -1;
        month = -1;
        day = -1;
        hour = -1;
    }


    public void setTime(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonNow:
                if (checked) {
                    date.setText(null);
                    time.setText(null);
                    break;
                }
            case R.id.radioButtonTime:
                if (checked){
                    changeDate(view);

                }
                    break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year_, int monthOfYear, int dayOfMonth) {
        year = year_;
        month = monthOfYear + 1;
        day = dayOfMonth;

        TimePickerDialog tmd = TimePickerDialog.newInstance(
                SegmentTime.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );
        tmd.enableMinutes(false);
        tmd.setTimeInterval(3);
        tmd.show(getFragmentManager(), "Timepickerdialog");

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        hour = hourOfDay;
        date.setText(String.valueOf(day)+ "/" + String.valueOf(month)+ "/" + String.valueOf(year));
        time.setText(String.valueOf(hour)+ ":00");

    }

    public void goToOptions(View view){
        Intent intent = new Intent(SegmentTime.this, SegmentOptions.class);
        if(radioButtonFuture.isChecked()) {
            if(year == -1 ||month == -1 ||day == -1 || hour == -1) {
                changeDate(view);
                Toast.makeText(getApplicationContext(), "Wybierz datę i godzinę", Toast.LENGTH_SHORT).show();
            }
            else {
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("hour", hour);
                startActivity(intent);
            }
        }
        else
            startActivity(intent);
    }

    public void changeDate(View view){
        now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                SegmentTime.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.setMinDate(now);
        Calendar max = now;
        max.add(Calendar.DATE, 4);
        dpd.setMaxDate(max);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void changeTime(View view){
        now = Calendar.getInstance();
        TimePickerDialog tmd = TimePickerDialog.newInstance(
                SegmentTime.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );
        tmd.enableMinutes(false);
        tmd.setTimeInterval(3);
        tmd.show(getFragmentManager(), "Timepickerdialog");
    }
}
