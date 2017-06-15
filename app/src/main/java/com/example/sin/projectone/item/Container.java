package com.example.sin.projectone.item;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.R;
import com.example.sin.projectone.payment.*;

/**
 * Created by nanth on 12/11/2016.
 */

public class Container extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_container, container, false);
        String tag = Constant.TAG_FRAGMENT_ITEM_MAIN;
        Fragment newFragment = new Main();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment checkFragment = getFragmentManager().findFragmentByTag(tag);
        if(checkFragment!=null){
            transaction.remove(checkFragment);
        }
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.frame_container_item, newFragment, Constant.TAG_FRAGMENT_PAYMENT_MAIN);
// Commit the transaction
        transaction.commit();
        return view;
    }
}
