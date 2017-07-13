package com.example.sin.projectone.item;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sin.projectone.ApplicationHelper;
import com.example.sin.projectone.Constant;
import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductAdapter;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.ProductDetailDialog;
import com.example.sin.projectone.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewProduct2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewProduct2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProduct2 extends Fragment implements UpdatePageFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FragmentManager fragmentManager;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products = new ArrayList<Product>();
    private ListView listProduct;
    private ProductDetailDialog productDetailDialog;
    private Product targetProduct;

    public ViewProduct2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProduct2.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProduct2 newInstance(String param1, String param2, OnFragmentInteractionListener mListener) {
        ViewProduct2 fragment = new ViewProduct2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.mListener = mListener;
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
        View view = inflater.inflate(R.layout.fragment_item_view2, container, false);;
        fragmentManager = getFragmentManager();
        //set View
        listProduct = (ListView) view.findViewById(R.id.list_view_item);
        products = ProductDBHelper.getInstance(getActivity().getApplicationContext()).getAllProductFromDB();
        productAdapter = new ProductAdapter(ApplicationHelper.getAppContext(), products, R.layout.list_product_view);
        listProduct.setOnItemClickListener(onItemClickListener());
        listProduct.setAdapter(productAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private ListView.OnItemClickListener onItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = productAdapter.getItem(position);
                String a = product.imgName;
                Bundle bundle = new Bundle();
                String tag = Constant.TAG_FRAGMENT_DIALOG_PRODUCT_DETAIL;
                bundle.putParcelable(Constant.KEY_BUNDLE_PRODUCT, product);
                productDetailDialog = ProductDetailDialog.newInstance(bundle, onBackDialogPress(), onEditDialogPress());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment prev = fragmentManager.findFragmentByTag(tag);
                if(prev!=null){
                    fragmentTransaction.remove(prev);
                }
                ViewProduct2.this.targetProduct = product;
                productDetailDialog.show(fragmentManager,tag);


            }
        };
    }

    private View.OnClickListener onBackDialogPress(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailDialog.dismiss();
            }
        };
    }

    private View.OnClickListener onEditDialogPress(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailDialog.dismiss();
                Bundle productBundle = new Bundle();
                productBundle.putParcelable(Constant.KEY_BUNDLE_PRODUCT, ViewProduct2.this.targetProduct);
                mListener.onFragmentChange(new EditProduct2(), productBundle);
            }
        };
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG &&
                resultCode==Constant.RESULT_CODE_PRODUCT_PAYMENT_DIALOG_SUBMIT){
            updatePage();
        }
    }

    @Override
    public void updatePage() {
        this.products = ProductDBHelper.getInstance(getActivity().getApplicationContext()).getAllProductFromDB();
        productAdapter = new ProductAdapter(getActivity(), products, R.layout.list_product_view);
        listProduct.setAdapter(productAdapter);
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
        void onFragmentChange(Fragment fragment, Bundle bundle);
    }
}
