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

public class TransDetailListCursor extends CursorAdapter {
    public TransDetailListCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_receipt_detail_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView textName = (TextView) view.findViewById(R.id.receipt_detail_list_product_name);
        TextView textPrice = (TextView) view.findViewById(R.id.receipt_detail_list_product_price);
        TextView textQty = (TextView) view.findViewById(R.id.receipt_detail_list_product_qty);
        // Extract properties from cursor
        String nameValue = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        Double priceValue = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        String qtyValue = cursor.getString(cursor.getColumnIndexOrThrow("qty"));
        // Populate fields with extracted properties
        textName.setText(nameValue);
        textPrice.setText(String.valueOf(priceValue));
        textQty.setText(qtyValue);
    }
}
