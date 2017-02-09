package com.example.sohan.verticalcustomcalender;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HorizontalAdapter.DateSelectionListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomVerticalCalenderView customVerticalCalenderView = (CustomVerticalCalenderView)
                findViewById(R.id.custom_calender_view);
        customVerticalCalenderView.setFromYear(2016);
        customVerticalCalenderView.setToYear(2017);
        customVerticalCalenderView.updateCalendar();
        customVerticalCalenderView.setDateSelectionColor(ContextCompat.getColor(this, R.color.colorAccent));
        customVerticalCalenderView.setListener(this);

    }

    @Override
    public void onDateSelected(List<Date> dateList) {
        Log.d(TAG, "Main Activity date list" + dateList.size());
    }
}
