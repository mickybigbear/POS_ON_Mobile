package com.example.sin.projectone.item;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainItem extends Fragment implements TabLayout.OnTabSelectedListener, ViewProduct2.OnFragmentInteractionListener
, AddProduct2.OnFragmentInteractionListener{
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
    private OnFragmentInteractionListener mListener;

    public MainItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainItem.
     */
    // TODO: Rename and change types and number of parameters
    public static MainItem newInstance(String param1, String param2) {
        MainItem fragment = new MainItem();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_item, container, false);
        //Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("View product"));
        tabLayout.addTab(tabLayout.newTab().setText("Add product"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this);

        // Inflate the layout for this fragment

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), this);
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void onFragmentChange(Fragment newFragment, Bundle bundle) {
        //mListener.onRepleceFragment(newFragment, bundle);
        String tag = Constant.TAG_FRAGMENT_CONTAINER;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container_main, newFragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRepleceFragment(Fragment fragment, Bundle bundle);

    }



    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private int numPage = 0;
        Fragment parent;
        public SectionsPagerAdapter(FragmentManager fm, int numPage,Fragment parent) {
            super(fm);
            this.numPage = numPage;
            this.parent = parent;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                //return ScanPayment2.newInstance("", "", (ScanPayment2.OnFragmentInteractionListener)parent);
                return  ViewProduct2.newInstance("","", (ViewProduct2.OnFragmentInteractionListener)parent);
            } else {
                //return EndPayment2.newInstance("", "", basketProduct, (EndPayment2.OnFragmentInteractionListener)parent);
                return AddProduct2.newInstance("","",(AddProduct2.OnFragmentInteractionListener)parent);
            }
        }

        @Override
        public int getCount() {
            return numPage;
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment fragment = (Fragment)object;
            if (fragment instanceof UpdatePageFragment) {
                ((UpdatePageFragment) fragment).updatePage();
            }
            return super.getItemPosition(object);
        }
    }
}
