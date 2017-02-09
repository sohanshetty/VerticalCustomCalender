package com.example.sohan.verticalcustomcalender;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.Holder>{
    private final HorizontalAdapter.DateSelectionListener mDateSelectListener;
    private LinkedHashMap<String, List<Date>> mVerticalWrapperDataList;

    public VerticalAdapter(LinkedHashMap<String, List<Date>> verticalWrapperList, HorizontalAdapter.DateSelectionListener listener) {
        mVerticalWrapperDataList = verticalWrapperList;
        mDateSelectListener = listener;
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
        int month = getMonth(position);
        int year = getYear(position);
        holder.mMonthTextView.setText(getMonthName(month) + " " + year);
        holder.mAdapter.setData(dateList, month);
        holder.mAdapter.notifyDataSetChanged();

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

    public final  class Holder extends RecyclerView.ViewHolder {
        protected RecyclerView horizontalRecyclerView;
        protected HorizontalAdapter mAdapter;
        protected TextView mMonthTextView;

        public Holder(View view) {
            super(view);
            this.horizontalRecyclerView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
            this.mMonthTextView = (TextView)view.findViewById(R.id.month_text);
            this.horizontalRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
            this.horizontalRecyclerView.setNestedScrollingEnabled(false);
            mAdapter = new HorizontalAdapter(new ArrayList<Date>(), 0 , mDateSelectListener);
            horizontalRecyclerView.setAdapter(mAdapter);
        }
    }
}
