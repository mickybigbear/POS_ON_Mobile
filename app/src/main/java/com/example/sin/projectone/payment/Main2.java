package com.example.sin.projectone.payment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link Main2.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link Main2#newInstance} factory method to
// * create an instance of this fragment.
// */
public class Main2 extends Fragment implements TabLayout.OnTabSelectedListener, ScanPayment2.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public ArrayList<Product> products = new ArrayList<Product>();


    //private OnFragmentInteractionListener mListener;

    public Main2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main2.
     */
    // TODO: Rename and change types and number of parameters
    public static Main2 newInstance(String param1, String param2) {
        Main2 fragment = new Main2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main2, container, false);


        //Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Scan Barcode"));
        tabLayout.addTab(tabLayout.newTab().setText("Payment Items"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this);

        // Inflate the layout for this fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), tabLayout.getTabCount(), this);
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDetectBarcode(String barcode) {
        Product newProduct = ProductDBHelper.getInstance(getActivity().getApplicationContext()).searchProductByBarCode(barcode);
        if(newProduct!=null){
            boolean found = false;
            for(int i=0; i<products.size();i++){
                Product checkP = products.get(i);
                if(newProduct.id.equals(checkP.id)){
                    checkP.qty +=1;
                    found = true;
                    break;
                }
            } if(!found){
                products.add(newProduct);
            }
            Toast.makeText(getActivity().getApplicationContext(), "finish add item", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getActivity().getApplicationContext(), "Not found product barcode: "+barcode, Toast.LENGTH_SHORT).show();
        }





    }


//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment parent;
        public int numSection = 0;

        public SectionsPagerAdapter(FragmentManager fm, int tabCount, Fragment parent) {
            super(fm);
            numSection = tabCount;
            this.parent = parent;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ScanPayment2.newInstance("", "", (ScanPayment2.OnFragmentInteractionListener)parent);
            } else {
                return EndPayment2.newInstance("", "");
            }
        }

        @Override
        public int getCount() {
            return numSection;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Scan";
                case 1:
                    return "Camera";
            }
            return null;
        }
    }
}
