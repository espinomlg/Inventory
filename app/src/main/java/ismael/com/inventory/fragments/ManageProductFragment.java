package ismael.com.inventory.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ismael.com.inventory.DB.DatabaseContract;
import ismael.com.inventory.R;
import ismael.com.inventory.adapters.MySimpleCursorAdapter;
import ismael.com.inventory.interfaces.ManageProductPresenter;
import ismael.com.inventory.models.Product;
import ismael.com.inventory.presenter.ManageProductPresenterImpl;

/**
 * Created by espino on 8/05/17.
 */

public class ManageProductFragment extends Fragment  implements ManageProductPresenter.View{


    private TextInputLayout serial, shortname, description;
    private Spinner category, subcategory, productclass;
    private Button action;

    private ManageProductListener callback;
    private ManageProductPresenter presenter;
    private MySimpleCursorAdapter categoryAdapter, subcategoryAdapter;


    public interface ManageProductListener{
        void onManageProductListener();
    }

    public static ManageProductFragment newInstance(Bundle args){
        ManageProductFragment fragment = new ManageProductFragment();
        if(args != null)
            fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ManageProductPresenterImpl(this);
        categoryAdapter = new MySimpleCursorAdapter(getContext(),android.R.layout.simple_spinner_item, null,
                new String[]{DatabaseContract.CategoryEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        subcategoryAdapter = new MySimpleCursorAdapter(getContext(),android.R.layout.simple_spinner_item, null,
                new String[]{DatabaseContract.SubCategoryEntry.COLUMN_NAME},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getAllCategories(getLoaderManager());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            callback = (ManageProductListener)activity;
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + " must implement interface ManageProductListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_addproduct, container, false);
        serial = (TextInputLayout) v.findViewById(R.id.addpoduct_serial);
        shortname = (TextInputLayout) v.findViewById(R.id.addpoduct_shortname);
        description = (TextInputLayout) v.findViewById(R.id.addpoduct_description);

        category = (Spinner) v.findViewById(R.id.addproduct_spn_category);
        subcategory = (Spinner) v.findViewById(R.id.addproduct_spn_subcategory);
        productclass = (Spinner) v.findViewById(R.id.addproduct_spn_productclass);

        action = (Button) v.findViewById(R.id.addproduct_btn_action);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(i + 1));
                presenter.getAllSubcategories(getLoaderManager(), args);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        category.setAdapter(categoryAdapter);
        subcategory.setAdapter(subcategoryAdapter);
        productclass.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                getResources().getStringArray(R.array.productclass)));

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product p = new Product();
                p.setSerial(checkData(serial.getEditText().getText().toString()));
                p.setShortName(shortname.getEditText().getText().toString());
                p.setDescription(description.getEditText().getText().toString());
                p.setCategory(categoryAdapter.getItem(category.getSelectedItemPosition()));
                p.setSubcategory(subcategoryAdapter.getItem(category.getSelectedItemPosition()));
                p.setProductClass(productclass.getSelectedItem().toString());

                presenter.addProduct(p);
                callback.onManageProductListener();
            }
        });
    }


    @Override
    public void setCategorySpnAdapter(Cursor c) {
        categoryAdapter.changeCursor(c);
       // categoryAdapter.changeCursorAndColumns(c, new String[]{DatabaseContract.CategoryEntry.COLUMN_NAME}, new int[]{android.R.layout.simple_spinner_dropdown_item});
        //categoryAdapter.setStringConversionColumn(1);
        //Log.e("lenght:", "objeto " + categoryAdapter.getItem(1));

    }

    @Override
    public void setSubcategorySpnAdapter(Cursor c) {
        subcategoryAdapter.changeCursor(c);
       // subcategoryAdapter.changeCursorAndColumns(c, new String[]{DatabaseContract.SubCategoryEntry.COLUMN_NAME}, new int[]{android.R.layout.simple_spinner_dropdown_item});
        //subcategoryAdapter.setStringConversionColumn(2);
        //Log.e("lenght:", "objeto " + subcategoryAdapter.getItem(0));
    }

    private String checkData(String s){
        if(!TextUtils.isEmpty(s))
            return s;
        else
            return "";

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
