package com.example.sin.projectone.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.sin.projectone.ApplicationHelper;
import com.example.sin.projectone.Constant;
import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductAdapter;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.ProductDetailDialog;
import com.example.sin.projectone.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nanth on 12/11/2016.
 */

public class ViewProduct extends Fragment {
    private FragmentManager fragmentManager;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayAdapter<CharSequence> spinerAdapter;
    private ListView listProduct;
    private Spinner spinner;
    private ProductDetailDialog productDetailDialog;
    private Product targetProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_view, container, false);
        fragmentManager = getFragmentManager();
        //set View
        listProduct = (ListView) view.findViewById(R.id.list_view_item);
        products = ProductDBHelper.getInstance(getActivity().getApplicationContext()).getAllProductFromDB();
        productAdapter = new ProductAdapter(ApplicationHelper.getAppContext(), products, R.layout.list_product_view);
        listProduct.setOnItemClickListener(onItemClickListener());
        listProduct.setAdapter(productAdapter);
        return view;
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
                ViewProduct.this.targetProduct = product;
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
                String tag = Constant.TAG_FRAGMENT_ITEM_EDIT;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment editProduct = new EditProduct();
                Bundle productBundle = new Bundle();
                productBundle.putParcelable(Constant.KEY_BUNDLE_PRODUCT, ViewProduct.this.targetProduct);
                editProduct.setArguments(productBundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame_container_item, editProduct, tag).commit();
            }
        };
    }

    public void RefreshProduct(){
        this.products = ProductDBHelper.getInstance(getActivity().getApplicationContext()).getAllProductFromDB();
        productAdapter = new ProductAdapter(getActivity(), products, R.layout.list_product_view);
        listProduct.setAdapter(productAdapter);
    }
}
