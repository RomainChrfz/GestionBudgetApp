package fr.romaincharfaz.mapremiereapp.view;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.Livret;

public class LivretAdapter extends RecyclerView.Adapter<LivretAdapter.LivretHolder> {
    private List<Livret> livrets = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public LivretHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View livretItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.livret_item, parent,false);
        return new LivretHolder(livretItem);
    }

    @Override
    public void onBindViewHolder(@NonNull LivretHolder holder, int position) {
        Livret currentLivret = livrets.get(position);
        holder.livret_txt.setText(currentLivret.getName());
    }

    @Override
    public int getItemCount() {
        return livrets.size();
    }

    public void setLivrets(List<Livret> livrets) {
        this.livrets = livrets;
        notifyDataSetChanged();
    }

    class LivretHolder extends RecyclerView.ViewHolder {
        private TextView livret_txt;

        public LivretHolder(View itemView) {
            super(itemView);
            livret_txt = itemView.findViewById(R.id.livret_of_recycler);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(livrets.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Livret livret);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
