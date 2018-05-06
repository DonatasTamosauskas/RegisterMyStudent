package com.jjanonis.donatas.registermystudent.adapters;

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

        TextView lectureText = (TextView) convertView.findViewById(R.id.lecture_list_text);
        TextView reasonText = (TextView) convertView.findViewById(R.id.reason_list_text);

        lectureText.setText(absenceDay.getLecture());
        reasonText.setText(absenceDay.getReason());

        return convertView;
    }

}
