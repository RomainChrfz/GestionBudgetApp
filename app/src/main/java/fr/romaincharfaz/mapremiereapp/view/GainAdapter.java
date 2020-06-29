package fr.romaincharfaz.mapremiereapp.view;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Calendar;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Gain;

public class GainAdapter extends ListAdapter<Gain,GainAdapter.GainHolder> {
    private OnItemClickListener mListener;
    private int[] mCategoryList;


    public GainAdapter() {
        super(DIFF_CALLBACK);
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
        void OnCatClick(int position, View view);
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
                    newItem.getDescription().equals(oldItem.getDescription()) &&
                    newItem.getDay() == oldItem.getDay() &&
                    newItem.getMonth() == oldItem.getMonth() &&
                    newItem.getYear() == oldItem.getYear() &&
                    newItem.getCategory() == oldItem.getCategory() &&
                    newItem.getUrlJustif().equals(oldItem.getUrlJustif());
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
        initList();
        holder.cat.setImageResource(mCategoryList[currentGain.getCategory()]);
    }

    public Gain getGainAt(int position) {
        return getItem(position);
    }

    class GainHolder extends RecyclerView.ViewHolder {
        private TextView gain_txt;
        private TextView description;
        private TextView date;
        private ImageView cat;

        public GainHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            gain_txt = itemView.findViewById(R.id.txt_of_recycler);
            description = itemView.findViewById(R.id.recycler_description);
            date = itemView.findViewById(R.id.recycler_date);
            cat = itemView.findViewById(R.id.recycler_category);

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

            cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnCatClick(position,v);
                        }
                    }
                }
            });
        }
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
