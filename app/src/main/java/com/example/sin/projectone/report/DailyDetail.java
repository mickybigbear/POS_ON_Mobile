package com.example.sin.projectone.report;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by naki_ on 11/25/2016.
 */

public class DailyDetail extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_report_daily_detail, container, false);
        String createAt = getArguments().getString("createAt");
        JSONObject json = new JSONObject();
        String totalTxt = "";
        String discTxt="";
        try {
            json = new JSONObject(getArguments().getString("json"));
            Log.d("json", json.getString("total"));
            Log.d("json", json.getString("discount"));
            totalTxt = json.getString("total");
            discTxt = json.getString("discount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("test", createAt);
        Log.d("test", totalTxt);
        Log.d("test", discTxt);
        ListView listProduct = (ListView)view.findViewById(R.id.report_daily_list_product);
        ListView listEmployee = (ListView)view.findViewById(R.id.report_daily_list_employee);
        TextView totalEditTxt = (TextView) view.findViewById(R.id.report_daily_txt_sum_total);
        TextView discountTxt = (TextView) view.findViewById(R.id.report_daily_txt_sum_discount);
        TextView subTotalTxt = (TextView) view.findViewById(R.id.report_daily_txt_subtotal);
        TextView reportDate = (TextView) view.findViewById(R.id.report_daily_txt_report_date);
        totalEditTxt.setText(totalTxt);
        discountTxt.setText(discTxt);
        reportDate.setText(createAt);
        Double subTotalDouble = Double.valueOf(totalTxt) - Double.valueOf(discTxt);
        subTotalTxt.setText(subTotalDouble.toString());
        Cursor productCursor = ProductDBHelper.getInstance(this.getActivity()).getDailyProduct(createAt);
        Cursor employeeCursor = ProductDBHelper.getInstance(this.getActivity()).getDailyEmployee(createAt);
        DailyProductCursor productAdapter = new DailyProductCursor(this.getActivity(), productCursor);
        DailyEmployeeCursor empAdapter = new DailyEmployeeCursor(this.getActivity(), employeeCursor);
        listProduct.setAdapter(productAdapter);
        listProduct.setDivider(null);
        listEmployee.setAdapter(empAdapter);
        listEmployee.setDivider(null);
        return view;
    }

}
