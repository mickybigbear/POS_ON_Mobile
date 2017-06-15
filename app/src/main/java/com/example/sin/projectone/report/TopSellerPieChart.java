package com.example.sin.projectone.report;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by naki_ on 12/13/2016.
 */

public class TopSellerPieChart extends android.app.Fragment {

    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    private Button weekBtn ;
    private Button monthBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        weekBtn =(Button) rootView.findViewById(R.id.report_top_week_btn) ;
        monthBtn =(Button) rootView.findViewById(R.id.report_top_month_btn) ;
        chart = (PieChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLick", "onClick:GOO ");
                queryData("week");
            }
        });
        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLick", "onClick:GOO ");
                queryData("month");

            }
        });
        queryData("week");

        return rootView;
    }

    // MENU
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.pie_chart, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_reset) {
//            reset();
//            generateData();
//            return true;
//        }
//        if (id == R.id.action_explode) {
//            explodeChart();
//            return true;
//        }
//        if (id == R.id.action_center_circle) {
//            hasCenterCircle = !hasCenterCircle;
//            if (!hasCenterCircle) {
//                hasCenterText1 = false;
//                hasCenterText2 = false;
//            }
//
//            generateData();
//            return true;
//        }
//        if (id == R.id.action_center_text1) {
//            hasCenterText1 = !hasCenterText1;
//
//            if (hasCenterText1) {
//                hasCenterCircle = true;
//            }
//
//            hasCenterText2 = false;
//
//            generateData();
//            return true;
//        }
//        if (id == R.id.action_center_text2) {
//            hasCenterText2 = !hasCenterText2;
//
//            if (hasCenterText2) {
//                hasCenterText1 = true;// text 2 need text 1 to by also drawn.
//                hasCenterCircle = true;
//            }
//
//            generateData();
//            return true;
//        }
//        if (id == R.id.action_toggle_labels) {
//            toggleLabels();
//            return true;
//        }
//        if (id == R.id.action_toggle_labels_outside) {
//            toggleLabelsOutside();
//            return true;
//        }
//        if (id == R.id.action_animate) {
//            prepareDataAnimation();
//            chart.startDataAnimation();
//            return true;
//        }
//        if (id == R.id.action_toggle_selection_mode) {
//            toggleLabelForSelected();
//            Toast.makeText(getActivity(),
//                    "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void reset() {
        chart.setCircleFillRatio(1.0f);
        hasLabels = false;
        hasLabelsOutside = false;
        hasCenterCircle = false;
        hasCenterText1 = false;
        hasCenterText2 = false;
        isExploded = false;
        hasLabelForSelected = false;
    }

    private void queryData(String type){
//        int numValues = 10;
        List<SliceValue> values = new ArrayList<SliceValue>();
        String color[] = {"#f44336","#C2185B","#7B1FA2","#8BC34A","#FFEB3B"};
        JSONObject json = new JSONObject();
        JSONArray temp = ProductDBHelper.getInstance(TopSellerPieChart.this.getActivity()).getTopDetail(type);
        for(int i = 0;i< temp.length();i++){
            try {
//                Log.d("check obj", );
                SliceValue sliceValue = new SliceValue(temp.getJSONObject(i).getInt("qty"), ChartUtils.pickColor());
                sliceValue.setLabel((temp.getJSONObject(i).getString("name")+" "+ (int)sliceValue.getValue()));
                values.add(sliceValue);
                data = new PieChartData(values);
                data.setHasLabelsOnlyForSelected(true);
                data.setHasLabelsOutside(true);
                data.setHasCenterCircle(true);
                data.setCenterText1(type.toUpperCase());
                data.setCenterText1Color(Color.WHITE);
                data.setCenterText2("10 Best Sell");
                data.setCenterText2Color(Color.WHITE);
                chart.setPieChartData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
//    SliceValue sliceValue = new SliceValue(44, Color.RED);
//    sliceValue.setLabel(("Students " + (int)sliceValue.getValue() + "%" ).toCharArray());
//
//    SubcolumnValue subcolumn = new SubcolumnValue((float)Math.random(), Color.RED);
//    subcolumn.setLabel("some_label".toCharArray());
//    values.add(subcolumn);
    private void generateData() {
        int numValues = 6;

        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("Hello!");

            // Get roboto-italic font.
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
//            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
//                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data.setCenterText2Typeface(tf);
//            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
//                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }

        chart.setPieChartData(data);
    }

//    private void explodeChart() {
//        isExploded = !isExploded;
//        generateData();
//
//    }
//
//    private void toggleLabelsOutside() {
//        // has labels have to be true:P
//        hasLabelsOutside = !hasLabelsOutside;
//        if (hasLabelsOutside) {
//            hasLabels = true;
//            hasLabelForSelected = false;
//            chart.setValueSelectionEnabled(hasLabelForSelected);
//        }
//
//        if (hasLabelsOutside) {
//            chart.setCircleFillRatio(0.7f);
//        } else {
//            chart.setCircleFillRatio(1.0f);
//        }
//
//        generateData();
//
//    }
//
//    private void toggleLabels() {
//        hasLabels = !hasLabels;
//
//        if (hasLabels) {
//            hasLabelForSelected = false;
//            chart.setValueSelectionEnabled(hasLabelForSelected);
//
//            if (hasLabelsOutside) {
//                chart.setCircleFillRatio(0.7f);
//            } else {
//                chart.setCircleFillRatio(1.0f);
//            }
//        }
//
//        generateData();
//    }
//
//    private void toggleLabelForSelected() {
//        hasLabelForSelected = !hasLabelForSelected;
//
//        chart.setValueSelectionEnabled(hasLabelForSelected);
//
//        if (hasLabelForSelected) {
//            hasLabels = false;
//            hasLabelsOutside = false;
//
//            if (hasLabelsOutside) {
//                chart.setCircleFillRatio(0.7f);
//            } else {
//                chart.setCircleFillRatio(1.0f);
//            }
//        }
//
//        generateData();
//    }

    /**
     * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
     * method(don't confuse with View.animate()).
     */
    private void prepareDataAnimation() {
        for (SliceValue value : data.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getActivity(),  String.valueOf(value.getLabelAsChars())+" ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}


