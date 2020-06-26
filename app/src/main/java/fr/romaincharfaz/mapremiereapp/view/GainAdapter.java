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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

import fr.romaincharfaz.mapremiereapp.R;
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
        private ImageView cat;

        public GainHolder(View itemView) {
            super(itemView);
            gain_txt = itemView.findViewById(R.id.txt_of_recycler);
            description = itemView.findViewById(R.id.recycler_description);
            date = itemView.findViewById(R.id.recycler_date);
            cat = itemView.findViewById(R.id.recycler_category);
        }
    }
}
