package com.example.sin.projectone.receipt;

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

public class TransListCursor extends CursorAdapter {
    public TransListCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_receipt_list_fragement_detail, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView transID = (TextView) view.findViewById(R.id.receipt_lis_fragment_trans_id);
        TextView total = (TextView) view.findViewById(R.id.receipt_lis_fragment_total);
        TextView createAt = (TextView) view.findViewById(R.id.receipt_lis_fragment_create_at);
        // Extract properties from cursor
        String tranValue = cursor.getString(cursor.getColumnIndexOrThrow("transactionID"));
        Double totalValue = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        Double discValue = cursor.getDouble(cursor.getColumnIndexOrThrow("discount"));
        String createValue = cursor.getString(cursor.getColumnIndexOrThrow("createAt"));
        totalValue = totalValue- discValue;
        // Populate fields with extracted properties
        transID.setText(tranValue);
        total.setText(String.valueOf(totalValue));
        createAt.setText(createValue);
    }
}
