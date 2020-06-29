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

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {


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

}
