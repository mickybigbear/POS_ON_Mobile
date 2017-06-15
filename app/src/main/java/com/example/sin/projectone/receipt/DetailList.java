package com.example.sin.projectone.receipt;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;

/**
 * Created by naki_ on 11/25/2016.
 */

public class DetailList extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_receipt_detail, container, false);
        TextView  textTrans = (TextView) view.findViewById(R.id.receipt_detail_text_transaction_id);
        TextView  textUSer = (TextView) view.findViewById(R.id.receipt_detail_text_username);
        TextView  textTotal = (TextView) view.findViewById(R.id.receipt_detail_text_total);
        TextView  textDisc= (TextView) view.findViewById(R.id.receipt_detail_text_discount);
        TextView  textDate = (TextView) view.findViewById(R.id.receipt_detail_text_date);
        TextView textSubTotal = (TextView) view.findViewById(R.id.receipt_detail_text_subtotal);
        String receiTrans = getArguments().getString("transactionID");
        String receiUser = getArguments().getString("username");
        String receiTotal = getArguments().getString("total");
        String receiDate = getArguments().getString("create");
        String receiDisc = getArguments().getString("discount");
        Double dTotal = Double.parseDouble(receiTotal);
        Double dDisc = Double.parseDouble(receiDisc);
        Double dSubTotal = dTotal - dDisc;
        textTrans.setText(receiTrans);
        textUSer.setText(receiUser);
        textTotal.setText(receiTotal);
        textDisc.setText(receiDisc);
        textDate.setText(receiDate);
        textSubTotal.setText(dSubTotal.toString());
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String value = getArguments().getString("transactionID");
        Cursor todoCursor = ProductDBHelper.getInstance(this.getActivity()).getTransactionDetail(value);
//        System.out.println(transList);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.Planets, android.R.layout.simple_list_item_1);
        TransDetailListCursor todoAdapter = new TransDetailListCursor(this.getActivity(), todoCursor);
        setListAdapter(todoAdapter);
        getListView().setDivider(null);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//        String str = cursor.getString(cursor.getColumnIndex("transactionID"));
//        Log.d("listView", str);
    }
}
