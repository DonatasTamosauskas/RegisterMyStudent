package com.jjanonis.donatas.registermystudent.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jjanonis.donatas.registermystudent.R;
import com.jjanonis.donatas.registermystudent.models.AbsenceDay;

import java.util.ArrayList;

public class AbsenceDaysAdapter extends ArrayAdapter<AbsenceDay> {

    public AbsenceDaysAdapter(Context context, ArrayList<AbsenceDay> absenceDays) {
        super(context, 0, absenceDays);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsenceDay absenceDay = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.absence_day_element, parent, false);
        }

        TextView absenceDate = (TextView) convertView.findViewById(R.id.date_text);
        absenceDate.setText(String.valueOf(absenceDay.getDate().toString()));

        return convertView;
    }

}
