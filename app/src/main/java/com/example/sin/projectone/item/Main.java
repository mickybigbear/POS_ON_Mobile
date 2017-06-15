package com.example.sin.projectone.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.R;

/**
 * Created by naki_ on 11/25/2016.
 */

public class Main extends Fragment {

    private FragmentManager fragmentManager;
    private Button btn_add_product, btn_view_product;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_main, container, false);
        fragmentManager = getFragmentManager();
        btn_add_product = (Button) view.findViewById(R.id.btn_add_product);
        btn_view_product = (Button) view.findViewById(R.id.btn_view_product);
        btn_add_product.setOnClickListener(onBtnAddClick());
        btn_view_product.setOnClickListener(onBtnViewClick());
        return view;
    }

    private View.OnClickListener onBtnAddClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = Constant.TAG_FRAGMENT_ITEM_ADD;
                FragmentTransaction tran = fragmentManager.beginTransaction();
                Fragment addFragment = new AddProduct();
                Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
                if(prevFragment!=null){
                    tran.remove(prevFragment);
                }
                tran.addToBackStack(null);
                tran.replace(R.id.frame_container_item, addFragment, tag).commit();
            }
        };
    }

    private View.OnClickListener onBtnViewClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = Constant.TAG_FRAGMENT_ITEM_VIEW;
                FragmentTransaction tran = fragmentManager.beginTransaction();
                Fragment viewFragment = new ViewProduct();
                Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
                if(prevFragment!=null){
                    tran.remove(prevFragment);
                }
                tran.addToBackStack(null);
                tran.replace(R.id.frame_container_item, viewFragment, tag).commit();
            }
        };
    }


}
