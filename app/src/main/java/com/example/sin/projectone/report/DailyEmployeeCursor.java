package com.example.sin.projectone.report;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sin.projectone.R;

/**
 * Created by naki_ on 12/10/2016.
 */

public class DailyEmployeeCursor extends CursorAdapter {
    public DailyEmployeeCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_report_daily_employee_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView usernameTxt = (TextView) view.findViewById(R.id.report_daily_list_employee_username);
        TextView totalTxt = (TextView) view.findViewById(R.id.report_daily_list_employee_total);

        // Extract properties from cursor
        String nameValue = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        Double priceValue = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        // Populate fields with extracted properties
        usernameTxt.setText(nameValue);
        totalTxt.setText(String.valueOf(priceValue));
    }
}
