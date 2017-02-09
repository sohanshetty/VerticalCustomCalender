/*
package com.example.sohan.verticalcustomcalender;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

*/
/**
 * Created by sohan on 6/2/17.
 *//*


public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private List<Date> mData;
    private int mMonthName;
    public static List<Date> mselectedDate = new ArrayList<>();
    private static DateSelectionListener mDateSelectListener;

    public HorizontalAdapter(ArrayList<Date> data, int month, DateSelectionListener dateSelectListener) {
        mData = data;
        mMonthName = month;
        mDateSelectListener = dateSelectListener;
    }

    public void setData(List<Date> data, int monthName) {
        mData = new ArrayList<>(data);
        mMonthName = monthName;
    }

    public List<Date> getData(){
        return mData.size() > 0 ? mData : null;
    }

    private Date getItem(int position) {
        return mData.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view =  LayoutInflater.from(parent.getContext()).
               inflate(R.layout.horizontal_recycler_view_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Date date = getItem(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        holder.mCheckBox.setText(String.valueOf(calendar.get(Calendar.DATE)));
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Calendar today = Calendar.getInstance();
        holder.mCheckBox.setTag(date);
        setBackgroundColor(holder.mCheckBox, day, month, year, today);
    }

    private void setBackgroundColor(CheckBox checkBox, int day, int month, int year, Calendar today) {
        // clear styling
        checkBox.setTypeface(null, Typeface.NORMAL);
        checkBox.setTextColor(Color.BLACK);
        if (month != mMonthName) {
            // if this day is outside current month, grey it out
            checkBox.setTextColor(Color.GRAY);
            checkBox.setEnabled(false);
        } else {
            checkBox.setTextColor(Color.BLACK);
            checkBox.setEnabled(true);
        }
        if (day == today.get(Calendar.DATE) && month == today.get(Calendar.MONTH) &&
                year == today.get(Calendar.YEAR) && month == mMonthName) {
            // if it is today, set it to blue/bold
            checkBox.setTypeface(null, Typeface.BOLD);
            checkBox.setTextColor(ContextCompat.getColor(checkBox.getContext(), R.color.colorPrimary));
        }

        if (mselectedDate.size() > 0 && month == mMonthName) {
            Calendar selectedCalender = Calendar.getInstance();
            for (Date date : mselectedDate) {
                selectedCalender.setTime(date);
                int selectedDay = selectedCalender.get(Calendar.DATE);
                int selectedMonth = selectedCalender.get(Calendar.MONTH);
                int seletedYear = selectedCalender.get(Calendar.YEAR);
                if (selectedDay == day && selectedMonth == month && seletedYear == year) {
                    checkBox.setOnCheckedChangeListener(null);
                    checkBox.setChecked(true);
                    checkBoxSelectedState(checkBox);
                    checkBox.setOnCheckedChangeListener(this);
                    break;
                } else {
                    Calendar todayCalender = Calendar.getInstance();
                    checkBoxUnSelectedState(checkBox, todayCalender, selectedCalender);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        CheckBox checkBox = (CheckBox) compoundButton;
        Date date = (Date) checkBox.getTag();
        Calendar calendar = Calendar.getInstance();
        Calendar seletedDate = Calendar.getInstance();
        seletedDate.setTime(date);
        if (checked) {
            checkBoxSelectedState(checkBox);
            if (!mselectedDate.contains(date)) {
                mselectedDate.add(date);
            }
        } else {
            mselectedDate.remove(date);
            checkBoxUnSelectedState(checkBox, calendar, seletedDate);
        }
        mDateSelectListener.onDateSelected(mselectedDate);
    }

    private void checkBoxSelectedState(CheckBox checkBox){
        checkBox.setTextColor(ContextCompat.getColor(checkBox.getContext(), R.color.white));
        checkBox.setBackgroundColor(CustomVerticalCalenderView.getmDateSelectionColor());
    }

    private void checkBoxUnSelectedState(CheckBox checkBox, Calendar calendar, Calendar seletedDate) {
        checkBox.setBackgroundColor(ContextCompat.getColor(checkBox.getContext(), R.color.white));
        if (calendar.get(Calendar.MONTH) == seletedDate.get(Calendar.MONTH) &&
                calendar.get(Calendar.DATE) == seletedDate.get(Calendar.DATE)) {

            checkBox.setTextColor(CustomVerticalCalenderView.getmCurrentDateColor()); // retain color for current date
        } else {
            checkBox.setTextColor(ContextCompat.getColor(checkBox.getContext(), R.color.black));
        }
    }

    public final  class ViewHolder extends RecyclerView.ViewHolder {
        protected CheckBox mCheckBox;

        public ViewHolder(final View view) {
            super(view);
            mCheckBox = (CheckBox)view.findViewById(R.id.days);
            mCheckBox.setOnCheckedChangeListener(HorizontalAdapter.this);
        }
    }

    public interface DateSelectionListener{
        void onDateSelected(List<Date> dateList);
    }
}
*/
