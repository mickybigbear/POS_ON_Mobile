package com.example.sin.projectone.payment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sin.projectone.MessageAlertDialog;
import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;
import com.example.sin.projectone.item.AddProduct;
import com.example.sin.projectone.main.MainActivity;

import java.util.ArrayList;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MainPayment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MainPayment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MainPayment extends Fragment implements TabLayout.OnTabSelectedListener, ScanPayment.OnFragmentInteractionListener,
EndPayment.OnFragmentInteractionListener, ItemFragment.OnFragmentInteractionListener{
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
    private MenuItem addToCart;
    public ArrayList<Product> basketProduct = new ArrayList<Product>();


    private OnFragmentInteractionListener mListener;

    public MainPayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPayment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPayment newInstance(String param1, String param2, MenuItem addToCart) {
        MainPayment fragment = new MainPayment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.addToCart = addToCart;
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
        View view = inflater.inflate(R.layout.fragment_main_payment, container, false);


        //Initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Scan Barcode"));
        tabLayout.addTab(tabLayout.newTab().setText("Payment Items"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this);

        // Inflate the layout for this fragment

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), this);
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if(addToCart!=null){
            //addToCart.setEnabled(false);
            addToCart.setVisible(true);
            TextView textView  = (TextView) addToCart.getActionView();
            textView.setText(getString(R.string.Checkout));
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_add_shop_cart, 0, 0, 0);
            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(MainPayment.this.basketProduct.size()>0){
                        MainPayment.this.mListener.onRepleceFragment(EndPayment.newInstance("", "",
                                MainPayment.this.basketProduct, MainPayment.this));
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.no_items_added_), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


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
    public void onDestroyView(){
        super.onDestroyView();
        if(addToCart!=null){
            addToCart.setVisible(false);
            addToCart.getActionView().setOnClickListener(null);
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
    public void onScanResult(String barcode) {
        Product newProduct = ProductDBHelper.getInstance(getActivity().getApplicationContext()).searchProductByBarCode(barcode);
        if(newProduct!=null){
            addProductToBasket(newProduct);
//            if(newProduct.qty<=0){
//                return;
//            }
//            newProduct.qty=1;
//            boolean found = false;
//            for(int i = 0; i< basketProduct.size(); i++){
//                Product checkP = basketProduct.get(i);
//                if(newProduct.id.equals(checkP.id)){
//                    checkP.qty +=1;
//                    found = true;
//                    break;
//                }
//            } if(!found){
//                basketProduct.add(newProduct);
//            }
//            Toast.makeText(getActivity().getApplicationContext(), "finish add item", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getActivity().getApplicationContext(), "Not found product barcode: "+barcode, Toast.LENGTH_SHORT).show();
        }
//        if(basketProduct.size()>0){
//            addToCart.setEnabled(true);
//        }else {addToCart.setEnabled(false);}
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessPayment() {
        basketProduct.clear();
        mViewPager.setCurrentItem(0);
        mSectionsPagerAdapter.notifyDataSetChanged();
        ((MainActivity)getActivity()).showBackToolbar(false); //temp
        getFragmentManager().popBackStack();
    }

    @Override
    public void onFailurePayment() {
        basketProduct.clear();
        mViewPager.setCurrentItem(0);
        mSectionsPagerAdapter.notifyDataSetChanged();
        ((MainActivity)getActivity()).showBackToolbar(false); // temp
        getFragmentManager().popBackStack();
    }

    @Override
    public void onCancelPayment() {
        basketProduct.clear();
        mViewPager.setCurrentItem(0);
        mSectionsPagerAdapter.notifyDataSetChanged();
        getFragmentManager().popBackStack();

    }

    @Override
    public void onItemSelected(Product product) {
        addProductToBasket(product);
    }

    private boolean addProductToBasket(Product product){
        if(product.qty<=0){
            return false;
        }
        Product target = (Product) product.clone();
        Boolean found = false;
        Product checkInBasket = null;
        for(int i=0;i<basketProduct.size();i++){
            checkInBasket = basketProduct.get(i);
            if(checkInBasket.id.equals(target.id)){
                found = true;
                break;
            }
        }
        if(!found){
            target.qty=1;
            basketProduct.add(target);
        }else if(found && (checkInBasket.qty+1)<=product.qty){
            checkInBasket.qty+=1;
        }else {
            Toast.makeText(getActivity().getApplicationContext(), "Can not add this item", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(getActivity().getApplicationContext(), "finish add item", Toast.LENGTH_SHORT).show();
        return true;
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
        void onRepleceFragment(Fragment fragment);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
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
                return ScanPayment.newInstance("", "", (ScanPayment.OnFragmentInteractionListener)parent);
            } else {
                //return EndPayment.newInstance("", "", basketProduct, (EndPayment.OnFragmentInteractionListener)parent);
                return  ItemFragment.newInstance("", "", (ItemFragment.OnFragmentInteractionListener)parent);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment fragment = (Fragment)object;
            if (fragment instanceof UpdatePageFragment) {
                ((UpdatePageFragment) fragment).updatePage();
            }
            return super.getItemPosition(object);
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
