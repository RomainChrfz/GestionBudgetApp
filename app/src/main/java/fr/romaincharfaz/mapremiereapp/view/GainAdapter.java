package fr.romaincharfaz.mapremiereapp.view;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.controleur.Dashboard;
import fr.romaincharfaz.mapremiereapp.model.Gain;

import static java.lang.String.valueOf;

public class GainAdapter extends ListAdapter<Gain,GainAdapter.GainHolder> {
    private OnItemClickListener mListener;


    public GainAdapter() {
        super(DIFF_CALLBACK);
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
        void OnCatClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static final DiffUtil.ItemCallback<Gain> DIFF_CALLBACK = new DiffUtil.ItemCallback<Gain>() {
        @Override
        public boolean areItemsTheSame(@NonNull Gain oldItem, @NonNull Gain newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Gain oldItem, @NonNull Gain newItem) {
            return newItem.getGainValue() == oldItem.getGainValue() &&
                    newItem.getDescription().equals(oldItem.getDescription());
        }
    };

    @NonNull
    @Override
    public GainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gainItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.gain_item, parent,false);
        return new GainHolder(gainItem, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GainHolder holder, int position) {
        Gain currentGain = getItem(position);
        double value = currentGain.getGainValue();
        value = Math.round(value * 100d)/100d;
        String txt = String.valueOf(value)+" €";
        SpannableString ss = new SpannableString(txt);
        if (value >= 0) {
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.GREEN);
            ss.setSpan(fcs, 0,txt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
            ss.setSpan(fcs,0,txt.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.gain_txt.setText(ss);

        String ndescription = currentGain.getDescription();
        holder.description.setText(ndescription);

        int nday = currentGain.getDay();
        int nmonth = currentGain.getMonth();
        int nyear = currentGain.getYear();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,nyear);
        c.set(Calendar.MONTH,nmonth);
        c.set(Calendar.DAY_OF_MONTH,nday);
        String date = DateFormat.getDateInstance().format(c.getTime());
        holder.date.setText(date);
    }

    public Gain getGainAt(int position) {
        return getItem(position);
    }

    class GainHolder extends RecyclerView.ViewHolder {
        private TextView gain_txt;
        private TextView description;
        private TextView date;
        private Spinner cat;
        private ArrayList<CategoryItem> mCategoryList;
        private fr.romaincharfaz.mapremiereapp.view.CategoryAdapter mAdapter;

        public GainHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            gain_txt = itemView.findViewById(R.id.txt_of_recycler);
            description = itemView.findViewById(R.id.recycler_description);
            date = itemView.findViewById(R.id.recycler_date);
//            mCategoryList = new ArrayList<>();
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_base), R.drawable.ic_category_base));
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_courses), R.drawable.ic_cat_courses));
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_fuel), R.drawable.ic_cat_fuel));
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_gift), R.drawable.ic_cat_gift));
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_phone), R.drawable.ic_cat_phone));
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_transport_commun), R.drawable.ic_cat_transport_commun));
//            mCategoryList.add(new CategoryItem(Resources.getSystem().getString(R.string.cat_trip), R.drawable.ic_cat_trip));
//
//            Spinner spinnerCategory = itemView.findViewById(R.id.recycler_category);
//
//            mAdapter = new fr.romaincharfaz.mapremiereapp.view.CategoryAdapter(itemView.getContext(), mCategoryList);
//            spinnerCategory.setAdapter(mAdapter);
//
//            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    CategoryItem clickedItem = (CategoryItem) parent.getItemAtPosition(position);
//                    String categoryName = clickedItem.getCategoryName();
//                    Toast.makeText(itemView.getContext(), categoryName, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

//            cat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.OnCatClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}
