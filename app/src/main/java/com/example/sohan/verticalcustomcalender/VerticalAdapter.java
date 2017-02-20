package com.example.sohan.verticalcustomcalender;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sohan on 6/2/17.
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.Holder> implements CompoundButton.OnCheckedChangeListener{


    private static final String TAG = VerticalAdapter.class.getSimpleName();
    private final DateSelectionListener mListener;
    private LinkedHashMap<String, List<Date>> mVerticalWrapperDataList;
    public static List<Date> mselectedDateList = new ArrayList<>();
    private List<Calendar> mfromCalender = new ArrayList<>();

    public VerticalAdapter(LinkedHashMap<String, List<Date>> verticalWrapperList,
                           DateSelectionListener listener) {
        mVerticalWrapperDataList = verticalWrapperList;
        mListener = listener;

    }

    public void setData(LinkedHashMap<String, List<Date>> verticalWrapperList) {
        mVerticalWrapperDataList = new LinkedHashMap<>(verticalWrapperList);
        notifyDataSetChanged();
    }

    private List<Date> getItem(int position) {
        List<List<Date>> date = new ArrayList<>(mVerticalWrapperDataList.values());
        return date.get(position);
    }

    private int getMonth(int position) {
       List<String> stringNameList = new ArrayList<>(mVerticalWrapperDataList.keySet());
        String[] value =   stringNameList.get(position).split("_");
        if (value.length > 1) {
            int month =  Integer.parseInt(value[1]);
            return month;
        }
        return 0;
    }

    private int getYear(int position) {
        List<String> stringNameList = new ArrayList<>(mVerticalWrapperDataList.keySet());
        String[] value =   stringNameList.get(position).split("_");
        if (value.length > 1) {
            int month =  Integer.parseInt(value[0]);
            return month;
        }
        return 0;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_recycler_view_item, parent,  false);
        return new Holder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ArrayList<Date> dateList = (ArrayList<Date>) getItem(position);
        int currentMonth = getMonth(position);
        int currentYear = getYear(position);
        holder.mMonthTextView.setText(getMonthName(currentMonth) + " " + currentYear);
        int childCount = holder.mTableLayout.getChildCount();
        int dateListIndexCount = 0;
        for (int i = 0; i < childCount ; i ++){
            TableRow tableRow = (TableRow) holder.mTableLayout.getChildAt(i);
            int tableRowChildCount = tableRow.getChildCount();
            for (int j = 0; j < tableRowChildCount; j++){
                 CheckBox checkBox = (CheckBox) tableRow.getChildAt(j);
                 Date dateOBj = dateList.get(dateListIndexCount);
                 Calendar calendar = Calendar.getInstance();
                 calendar.setTime(dateOBj);
                 int date = calendar.get(Calendar.DATE);
                /* int month = calendar.get(Calendar.MONTH);
                 int year = calendar.get(Calendar.YEAR);*/
                 checkBox.setTag(calendar);
                 checkBox.setText(String.valueOf(date));
                 setBackgroundColor(checkBox, calendar, currentMonth, Calendar.getInstance());
                 checkBox.setOnCheckedChangeListener(this);
                 dateListIndexCount ++;
            }
        }
    }

    private void setBackgroundColor(CheckBox checkBox, Calendar listCalender, int currentMonth, Calendar today) {
        int day = listCalender.get(Calendar.DATE);
        int month = listCalender.get(Calendar.MONTH);
        int year = listCalender.get(Calendar.YEAR);
        checkBox.setBackgroundColor(ContextCompat.getColor(checkBox.getContext(), R.color.white));
        // clear styling
        checkBox.setTypeface(null, Typeface.NORMAL);
        checkBox.setTextColor(Color.BLACK);

        if (day == today.get(Calendar.DATE) && month == today.get(Calendar.MONTH) &&
                year == today.get(Calendar.YEAR) && month == currentMonth) {
            // if it is today, set it to blue/bold
            checkBox.setTypeface(null, Typeface.BOLD);
            checkBox.setTextColor(ContextCompat.getColor(checkBox.getContext(), R.color.colorPrimary));

        } else {
            if (month != currentMonth) {
                // if this day is outside current month, grey it out
                checkBox.setTextColor(Color.GRAY);
                checkBox.setEnabled(false);
            } else {
                checkBox.setTextColor(Color.BLACK);
                checkBox.setEnabled(true);
            }
        }
        if (mselectedDateList.size() > 0 && month == currentMonth) {// retain  checkbox state while scrolling
            Calendar selectedCalender = Calendar.getInstance();
            for (Date date : mselectedDateList) {
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
                   // Calendar todayCalender = Calendar.getInstance();
                    //checkBoxUnSelectedState(checkBox, todayCalender, selectedCalender);
                }
            }
        }

        if (CustomVerticalCalenderView.isPreviousDateDisabled()) { // disable previous date disable flow
            int daysOfYear = listCalender.get(Calendar.DAY_OF_YEAR);
            int currentDate = today.get(Calendar.DAY_OF_YEAR);
            int listYear = listCalender.get(Calendar.YEAR);
            int currentYear = today.get(Calendar.YEAR);
            if (daysOfYear < currentDate || listYear < currentYear) {
                checkBox.setEnabled(false);
            }
        }
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

    private String getMonthName(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        SimpleDateFormat monthDate = new SimpleDateFormat("MMMM");
        String monthName = monthDate.format(calendar.getTime());
        return monthName;
    }

    @Override
    public int getItemCount() {
        return mVerticalWrapperDataList.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        CheckBox checkBox = (CheckBox) compoundButton;
        Calendar seletedDate = (Calendar) checkBox.getTag();
        Calendar calendar = Calendar.getInstance();
        if (CustomVerticalCalenderView.isRange()) {
            if (mfromCalender.size() == 0) {
                mfromCalender.add(seletedDate);
                mselectedDateList.clear();
                mselectedDateList.add(seletedDate.getTime());
                notifyDataSetChanged();

            } else {
                mselectedDateList.clear();
                final Calendar fromcalendar = mfromCalender.get(0);
                int fromDate = fromcalendar.get(Calendar.DATE);
                int fronMonth = fromcalendar.get(Calendar.MONTH);
                int fromYear = fromcalendar.get(Calendar.YEAR);

                int toDate = seletedDate.get(Calendar.DATE);
                int toMonth = seletedDate.get(Calendar.MONTH);
                int toYear = seletedDate.get(Calendar.YEAR);

                if (fromDate <= toDate || fronMonth < toMonth || fromYear < toYear) {
                    while (fromDate <= toDate || fronMonth < toMonth || fromYear < toYear) {
                        Date rangeDate = fromcalendar.getTime();
                        mselectedDateList.add(rangeDate);
                        fromcalendar.add(Calendar.DAY_OF_MONTH, 1);
                        fromDate = fromcalendar.get(Calendar.DATE);
                        fronMonth = fromcalendar.get(Calendar.MONTH);
                        fromYear = fromcalendar.get(Calendar.YEAR);

                        if (fronMonth > toMonth && fromYear == toYear) {
                            break;
                        }
                    }
                    mfromCalender.clear();
                    mListener.onDateSelected(mselectedDateList);
                } else {
                    mfromCalender.clear();
                    mfromCalender.add(seletedDate);
                    handleClick(true, checkBox, seletedDate, calendar);
                }
                notifyDataSetChanged();
                Log.d(TAG, "Range Date Size " + mselectedDateList.size());


            }
        } else if (CustomVerticalCalenderView.isSingleClick()){
            if (mselectedDateList.size() != 0) {
                Date date = mselectedDateList.get(0);
                Calendar previousDate = Calendar.getInstance();
                previousDate.setTime(date);
                int day = previousDate.get(Calendar.DATE);
                int selectedDay = seletedDate.get(Calendar.DATE);
                mselectedDateList.clear(); // clearing the list for handling single item click
                if (day != selectedDay) {// forcfully making the view state to true  if the view's state from recycler is false
                    handleClick(true, checkBox, seletedDate, calendar);
                } else {
                    handleClick(checked, checkBox, seletedDate, calendar);
                }
            } else { // for the first time
                handleClick(checked, checkBox, seletedDate, calendar);
            }
            notifyDataSetChanged();
            mListener.onDateSelected(mselectedDateList);
        } else { // by defalut multi click support
            handleClick(checked, checkBox, seletedDate, calendar);
            mListener.onDateSelected(mselectedDateList);
        }

        Log.d(TAG, "final size " + mselectedDateList.size());

    }

    private void handleClick(boolean checked, CheckBox checkBox, Calendar seletedDate,
                             Calendar calendar) {
        Date date = seletedDate.getTime();
        if (checked) {
            checkBoxSelectedState(checkBox);
            if (!mselectedDateList.contains(date)) {
                mselectedDateList.add(date);
            }
        } else {
            mselectedDateList.remove(date);
            checkBoxUnSelectedState(checkBox, calendar, seletedDate);
        }
    }

    public final  class Holder extends RecyclerView.ViewHolder {
        protected TextView mMonthTextView;
        protected TableLayout mTableLayout;
        public Holder(View view) {
            super(view);
            this.mMonthTextView = (TextView)view.findViewById(R.id.month_text);
            this.mTableLayout = (TableLayout)view.findViewById(R.id.table_layout_id);

        }
    }
    public interface DateSelectionListener{
        void onDateSelected(List<Date> dateList);
    }
}
