package com.example.sin.projectone.receipt;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.R;

/**
 * Created by nanth on 12/8/2016.
 */

public class Container extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_receipt_container, container, false);

        Fragment newFragment = new list_fragment();
//        JSONArray transList = ProductDBHelper.getInstance(Container.this.getActivity()).getTrans();
//        System.out.println(transList);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_receipt_container, newFragment,Constant.TAG_FRAGMENT_RECEIPT_MAIN);
// Commit the transaction
        transaction.commit();
        return view;

    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Attach", "on attach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        FragmentManager fragmentManager = getActivity().getFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_receipt_container);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(fragment);
//        fragmentTransaction.commit();
        Log.d("Attach", "on detach");
    }


}

