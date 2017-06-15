package com.example.sin.projectone.report;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by naki_ on 11/25/2016.
 */

public class Daily extends Fragment {
    private Button goDetail;
    private Calendar myCalendar;
    private EditText dateEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_report_daily, container, false);
        dateEdit = (EditText) view.findViewById(R.id.report_daily_edit_pick_date) ;
        goDetail = (Button) view.findViewById(R.id.report_daily_btn_go);
//        layoutText =(TextInputLayout) view.findViewById(R.id.report_daily_layout_pick_date) ;
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        goDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLick", "onClick:GOO ");
                if(dateEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(Daily.this.getActivity(), "Plase choose date", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    processDetail();
                }

            }
        });
        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DatePickerDialog(Daily.this.getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }else {
                }
            }
        });
        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Daily.this.getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    return view;
    }
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEdit.setText(sdf.format(myCalendar.getTime()));
    }
    private void processDetail(){
        if(!dateEdit.getText().toString().equals("")){
            if(ProductDBHelper.getInstance(Daily.this.getActivity()).getDailyReport(dateEdit.getText().toString())){
                Log.d("read", "rady to go: ");
                JSONArray temp = ProductDBHelper.getInstance(Daily.this.getActivity()).getDailyDetail(dateEdit.getText().toString());
                Bundle args = new Bundle();
                args.putString("createAt", dateEdit.getText().toString());
                try {
                    args.putString("json", temp.getString(0));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Fragment newFragment = new DailyDetail();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                newFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_report_container, newFragment).commit();
            }
            else{
                Toast toast = Toast.makeText(this.getActivity(), "No data found", Toast.LENGTH_SHORT);
                toast.show();
            }


        }

    }
}
