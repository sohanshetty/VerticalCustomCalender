package com.example.sohan.verticalcustomcalender;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sohan on 6/2/17.
 */

public class CustomVerticalCalenderView extends LinearLayout implements VerticalAdapter.DateSelectionListener {
    private static final int DAYS_COUNT = 42;
    private static final String TAG = CustomVerticalCalenderView.class.getSimpleName();
    private static int mDateSelectionColor;
    private static int mCurrentDateColor;
    // current displayed month
    private Calendar currentDate = Calendar.getInstance();
    private RecyclerView mRecyclerVerticalView;
    private VerticalAdapter.DateSelectionListener mListener;
    private int mFromYear = 2017;
    private int mToYear = 2017;
    private static boolean mRange;
    private static boolean mIsSingleClick;
    private static boolean mDisablePreviousDate;

    public CustomVerticalCalenderView(Context context) {
        super(context);
    }

    public CustomVerticalCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(attrs);
    }

    public CustomVerticalCalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(null);
    }

    private void initControl(AttributeSet attributeSet) {
        if (attributeSet != null) {
            initXMLAttributeValues(attributeSet);
        }
        inflate(getContext());
    }

    private void initXMLAttributeValues(AttributeSet attributeSet) {
        TypedArray styledAttrs = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomCalenderYear);
        try {
            mFromYear = styledAttrs.getInt(R.styleable.CustomCalenderYear_fromYear, 2017);
            mToYear = styledAttrs.getInt(R.styleable.CustomCalenderYear_toYear, 2017);
            mCurrentDateColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
            mDateSelectionColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        } finally {
            styledAttrs.recycle();
        }

    }

    private void inflate(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_vertical_calender_view, this);
        mRecyclerVerticalView = (RecyclerView) view.findViewById(R.id.vertical_wrapper_recycler_view);
        VerticalAdapter adapter = new VerticalAdapter(new LinkedHashMap<String, List<Date>>(), this);
        mRecyclerVerticalView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
        mRecyclerVerticalView.setAdapter(adapter);
        clearDateSelectionList();
        updateCalendar();
    }

    private void clearDateSelectionList() {
        VerticalAdapter.mselectedDateList.clear();
    }

    public void updateCalendar() {
        if (!isInEditMode()) {
            int currentYear = currentDate.get(Calendar.YEAR);
            int resetCalenderTo = currentYear - mFromYear;
            int yearUpto = mToYear - mFromYear;//
            LinkedHashMap<String, List<Date>> verticalList = new LinkedHashMap<>();
            currentDate.add(Calendar.YEAR, -resetCalenderTo);// reset to previous year
            currentDate.set(Calendar.MONTH, 0);
            for (int y = 0; y <= yearUpto ; y++) {
                for (int i = 0; i < 12; i++) {
                    ArrayList<Date> cells = getDatesForMonth();
                    int month = currentDate.get(Calendar.MONTH);
                    int year =  currentDate.get(Calendar.YEAR);
                    String key = generateKey(month, year);
                    Log.d(TAG, key);
                    verticalList.put(key, cells);
                    currentDate.add(Calendar.MONTH, 1);
                }
            }
            updateAdater(verticalList);
        }
    }

    /**
     * Method use to calculate last 6 month from current month
     * @param lastNoOfMonth last no of month to display
     */
    public  void updateCalendar(int lastNoOfMonth){
        if (!isInEditMode()) {
            currentDate = Calendar.getInstance();
            LinkedHashMap<String, List<Date>> verticalList = new LinkedHashMap<>();
            currentDate.add(Calendar.MONTH, -lastNoOfMonth);// reset to last no of month
                for (int i = 0; i <= lastNoOfMonth; i++) {
                    ArrayList<Date> cells = getDatesForMonth();
                    int month = currentDate.get(Calendar.MONTH);
                    int year =  currentDate.get(Calendar.YEAR);
                    String key = generateKey(month, year);
                    Log.d(TAG, key);
                    verticalList.put(key, cells);
                    currentDate.add(Calendar.MONTH, 1);
                }

            updateAdater(verticalList);
        }
    }

    public void setDateSelectionColor(int color){
        mDateSelectionColor = color;
    }

    public static int getmDateSelectionColor(){
        return mDateSelectionColor;
    }
    public static void setCurrentDateTextColor(int color){
        mCurrentDateColor = color;
    }

    public static int getmCurrentDateColor(){
        return mCurrentDateColor;
    }
    public void setFromYear(int fromYear){
        mFromYear = fromYear;
    }

    public void setToYear(int toYear){
        mToYear = toYear;
    }

    private String generateKey(int month, int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(year).append("_").append(month);
        return builder.toString();
    }

    @NonNull
    private ArrayList<Date> getDatesForMonth() {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells (42 days calendar as per our business logic)
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return cells;
    }

    private void updateAdater(LinkedHashMap<String, List<Date>> verticalWrapperList){
        ((VerticalAdapter) mRecyclerVerticalView.getAdapter()).setData(verticalWrapperList);
        int positionToScroll = getCurrentYearMonthListPosition(verticalWrapperList);
        mRecyclerVerticalView.scrollToPosition(positionToScroll);
    }

    private int getCurrentYearMonthListPosition(LinkedHashMap<String, List<Date>> verticalWrapperList){
        Calendar curentDate = Calendar.getInstance();
        int currentMonth = curentDate.get(Calendar.MONTH);
        int currentYear =  curentDate.get(Calendar.YEAR);
        int i = 0;
        for (Map.Entry<String, List<Date>> stringListEntry : verticalWrapperList.entrySet()) {
            i++;
            String key = stringListEntry.getKey();
            String[] values = key.split("_");
            int year =  Integer.parseInt(values[0]);
            int month =  Integer.parseInt(values[1]);
            if (currentYear == year && currentMonth == month) {
                return --i;
            }
        }
        return 0;
    }

    public void setListener(VerticalAdapter.DateSelectionListener listener){
        mListener = listener;

    }

    public static void setRange(boolean isRange){
        mRange = isRange;
    }

    public static boolean isRange(){
        return mRange;
    }

    public static boolean isSingleClick(){
        return mIsSingleClick;

    }

    public void setRangeByDefault(Date startDateObj, Date endDateObj){
        Calendar startCalender = Calendar.getInstance();
        startCalender.setTime(startDateObj);

        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endDateObj);

        int startDate = startCalender.get(Calendar.DATE);
        int endDate = endCalender.get(Calendar.DATE);

        while (startDate <= endDate) {
            Date date = startCalender.getTime();
            VerticalAdapter.mselectedDateList.add(date);
            startCalender.add(Calendar.DATE, 1); // incrementing date by 1  i,e next date
            startDate = startCalender.get(Calendar.DATE);
        }
    }

    public static void disablePreviousDate(){
        mDisablePreviousDate = true;
    }

    public static boolean isPreviousDateDisabled() {
        return mDisablePreviousDate;
    }

    public static void setSingleClick(){
        mIsSingleClick = true;
    }

    @Override
    public void onDateSelected(List<Date> dateList) {
        Log.d(TAG, "date list" + dateList.size());
        if (mListener != null) {
            mListener.onDateSelected(dateList);
        }
    }
}
