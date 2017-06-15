package com.example.sin.projectone.report;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sin.projectone.R;

/**
 * Created by naki_ on 11/25/2016.
 */

public class Main extends Fragment {
    private Button dailyReport;
    private Button topSell;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_report_main, container, false);
        dailyReport =(Button) view.findViewById(R.id.report_main_butt_daily);
        topSell = (Button) view.findViewById(R.id.report_main_butt_tops);
        dailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check BTOON CLICK", "onClick: ");
                Fragment newFragment = new Daily();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.replace(R.id.fragment_report_container, newFragment, null);
                transaction.addToBackStack(null);
// Commit the transaction
                transaction.commit();
            }
        });
        topSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check BTOON CLICK", "onClick: ");
                Fragment newFragment = new TopSeller();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_report_container, newFragment).commit();

//                transaction.addToBackStack(null);
// Commit the transaction

            }
        });
        return view;
    }
}
