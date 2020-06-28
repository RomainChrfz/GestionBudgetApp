package fr.romaincharfaz.mapremiereapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.controleur.Dashboard;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    private int[] mCategoryList;

    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryList) {
        super(context, 0,categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initViewDrop(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initViewDrop(position, convertView, parent);
    }

    private View initViewDrop(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spiner_layout, parent, false);
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.spinner_icon);
        TextView textViewName = convertView.findViewById(R.id.spinner_text);

        CategoryItem currentItem = getItem(position);
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getFlagIcon());
            textViewName.setText(currentItem.getCategoryName());
        }
        return convertView;
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spiner_layout, parent, false);
        }
        initList();

        ImageView imageViewFlag = convertView.findViewById(R.id.spinner_icon);
        TextView textViewName = convertView.findViewById(R.id.spinner_text);

        CategoryItem currentItem = getItem(position);
        if (currentItem != null) {
            if (Dashboard.edit) {
                imageViewFlag.setImageResource(mCategoryList[Dashboard.categoryselected]);
                textViewName.setText(null);
            }else {
                imageViewFlag.setImageResource(currentItem.getFlagIcon());
                textViewName.setText(null);
            }
        }
        return convertView;
    }

    private void initList() {
        mCategoryList = new int[7];
        mCategoryList[0] = R.drawable.ic_cat_unknown;
        mCategoryList[1] = R.drawable.ic_cat_courses;
        mCategoryList[2] = R.drawable.ic_cat_fuel;
        mCategoryList[3] = R.drawable.ic_cat_gift;
        mCategoryList[4] = R.drawable.ic_cat_phone;
        mCategoryList[5] = R.drawable.ic_cat_transport_commun;
        mCategoryList[6] = R.drawable.ic_cat_trip;
    }
}
