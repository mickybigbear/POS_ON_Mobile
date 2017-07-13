package com.example.sin.projectone.setting;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.R;

/**
 * Created by naki_ on 7/13/2017.
 */

public class Container extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_setting_container, container, false);

        Fragment newFragment = new Main();
//        JSONArray transList = ProductDBHelper.getInstance(Container.this.getActivity()).getTrans();
//        System.out.println(transList);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_setting_container, new SettingsFragment())
                .commit();
        return view;
    }
}