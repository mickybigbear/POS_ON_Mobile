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
import com.example.sin.projectone.receipt.list_fragment;

/**
 * Created by naki_ on 7/13/2017.
 */

public class Container extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_setting_container, container, false);

        Fragment newFragment = new list_fragment();
//        JSONArray transList = ProductDBHelper.getInstance(Container.this.getActivity()).getTrans();
//        System.out.println(transList);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_receipt_container, newFragment, Constant.TAG_FRAGMENT_RECEIPT_MAIN);
// Commit the transaction
        transaction.commit();
        return view;
    }
}