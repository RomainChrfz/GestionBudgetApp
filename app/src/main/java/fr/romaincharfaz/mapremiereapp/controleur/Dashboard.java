package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.view.GainAdapter;
import fr.romaincharfaz.mapremiereapp.view.GainViewModel;
import fr.romaincharfaz.mapremiereapp.view.UserViewModel;

public class Dashboard extends AppCompatActivity {
    public static final int ADD_GAIN_REQUEST = 1;
    public static final String CURRENT_LIVRET = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_LIVRET";
    public static final String CURRENT_USER = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_USER";

    private UserViewModel userViewModel;
    private GainViewModel gainViewModel;

    private String currentLivret;
    private String currentUser;
    private String testtxt = new String();

    private FloatingActionButton mAddBtn;
    private TextView total_txt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(1);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra(CURRENT_USER);
        currentLivret = intent.getStringExtra(CURRENT_LIVRET);
        setTitle(currentLivret);

        total_txt = (TextView) findViewById(R.id.total_txt);

        mAddBtn = findViewById(R.id.btn_add_gain);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GainAdding.class);
                startActivityForResult(intent, ADD_GAIN_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setHasFixedSize(true);

        final GainAdapter adapter = new GainAdapter();
        recyclerView.setAdapter(adapter);

        gainViewModel = new ViewModelProvider(this).get(GainViewModel.class);
        gainViewModel.getAllGains(currentLivret).observe(this, new Observer<List<Gain>>() {
            @Override
            public void onChanged(List<Gain> gains) {
                adapter.submitList(gains);
                total_calcul(gains);
            }
        });

        new ItemTouchHelper((new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                gainViewModel.delete(adapter.getGainAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Dashboard.this,"Dépense suprrimée",Toast.LENGTH_SHORT).show();
            }
        })).attachToRecyclerView(recyclerView);

    }

    private void total_calcul(List<Gain> totgain) {
        double tot = 0.0;
        for (int i=0; i<totgain.size(); i++) {
            tot += Double.valueOf(totgain.get(i).getGainValue());
        }
        tot = Math.round(tot * 100d) / 100d;
        String tot_ss = String.valueOf(tot);
        SpannableString ss = new SpannableString(tot_ss);
        if (tot >= 0) {
            ForegroundColorSpan fcsg = new ForegroundColorSpan(Color.GREEN);
            ss.setSpan(fcsg,0,tot_ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            ForegroundColorSpan fcsr = new ForegroundColorSpan(Color.RED);
            ss.setSpan(fcsr,0,tot_ss.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        total_txt.setText(ss);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GAIN_REQUEST && resultCode == RESULT_OK) {
            String newBanque = data.getStringExtra(GainAdding.EXTRA_BANQUE);
            String newValuetxt = data.getStringExtra(GainAdding.EXTRA_VALUE);
            double newValue = Double.valueOf(newValuetxt);
            Gain gain = new Gain(newValue,"","", "",currentLivret);
            gainViewModel.insert(gain);
        }
    }
}