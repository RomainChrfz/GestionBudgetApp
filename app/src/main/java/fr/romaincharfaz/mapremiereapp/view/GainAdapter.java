package fr.romaincharfaz.mapremiereapp.view;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.controleur.Dashboard;
import fr.romaincharfaz.mapremiereapp.controleur.GainAdding;
import fr.romaincharfaz.mapremiereapp.model.Gain;

import static java.lang.String.valueOf;

public class GainAdapter extends ListAdapter<Gain,GainAdapter.GainHolder> {

    public GainAdapter() {
        super(DIFF_CALLBACK);
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
        return new GainHolder(gainItem);
    }

    @Override
    public void onBindViewHolder(@NonNull GainHolder holder, int position) {
        Gain currentGain = getItem(position);
        double value = currentGain.getGainValue();
        value = Math.round(value * 100d)/100d;
        String txt = String.valueOf(value);
        SpannableString ss = new SpannableString(txt);
        if (value >= 0) {
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.GREEN);
            ss.setSpan(fcs, 0,txt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
            ss.setSpan(fcs,0,txt.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.gain_txt.setText(ss);
    }

    public Gain getGainAt(int position) {
        return getItem(position);
    }

    class GainHolder extends RecyclerView.ViewHolder {
        private TextView gain_txt;

        public GainHolder(View itemView) {
            super(itemView);
            gain_txt = itemView.findViewById(R.id.txt_of_recycler);
        }
    }
}
