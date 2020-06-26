package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.view.CategoryAdapter;
import fr.romaincharfaz.mapremiereapp.view.CategoryItem;
import fr.romaincharfaz.mapremiereapp.view.GainAdapter;
import fr.romaincharfaz.mapremiereapp.view.GainViewModel;
import fr.romaincharfaz.mapremiereapp.view.UserViewModel;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Dashboard extends AppCompatActivity {
    public static final int ADD_GAIN_REQUEST = 1;
    public static final String CURRENT_LIVRET = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_LIVRET";
    public static final String CURRENT_USER = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_USER";

    private UserViewModel userViewModel;
    private GainViewModel gainViewModel;

    private boolean edit;
    private Gain deletedGain;
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
                edit = false;
                Gain gainclicked = new Gain(0.0,"",0,0,0,"","");
                bottomsheetconfiguration(gainclicked);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setHasFixedSize(true);

        final GainAdapter adapter = new GainAdapter();
        try {
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG);
        }
        adapter.setOnItemClickListener(new GainAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Gain gainclicked = adapter.getGainAt(position);
                edit = true;
                bottomsheetconfiguration(gainclicked);
            }

            @Override
            public void OnCatClick(int position) {

            }
        });

        gainViewModel = new ViewModelProvider(this).get(GainViewModel.class);
        gainViewModel.getAllGains(currentLivret).observe(this, new Observer<List<Gain>>() {
            @Override
            public void onChanged(List<Gain> gains) {
                adapter.submitList(gains);
                total_calcul(gains);
            }
        });

        new ItemTouchHelper((new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT :
                        deletedGain = adapter.getGainAt(position);
                        gainViewModel.delete(deletedGain);
                        Snackbar.make(recyclerView, getString(R.string.expense_deleted), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gainViewModel.insert(deletedGain);
                            }
                        }).show();
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(Dashboard.this,R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_sweep_white)
                        .addSwipeLeftLabel(getString(R.string.delete))
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        })).attachToRecyclerView(recyclerView);

    }

    private void bottomsheetconfiguration(final Gain gainclicked) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Dashboard.this, R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout) findViewById(R.id.bottom_sheet_container));

        final NumberPicker day = (NumberPicker)bottomSheetView.findViewById(R.id.day_picker);
        final EditText mDescription = (EditText) bottomSheetView.findViewById(R.id.et_description);
        final EditText mValue = (EditText) bottomSheetView.findViewById(R.id.et_valeur);
        day.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d",value);
            }
        });
        final NumberPicker month = (NumberPicker)bottomSheetView.findViewById(R.id.month_picker);
        final NumberPicker year = (NumberPicker)bottomSheetView.findViewById(R.id.year_picker);
        final String[] months = new String[]{"jan.","fév.","mar.","avr.","mai","juin","juil.","août","sept.","oct.","nov.","déc."};
        day.setMinValue(1);
        day.setMaxValue(31);
        month.setDisplayedValues(months);
        month.setMinValue(0);
        month.setMaxValue(months.length-1);
        year.setMinValue(1789);
        year.setMaxValue(2100);

        Calendar c = Calendar.getInstance();
        if(edit) {
            day.setValue(gainclicked.getDay());
            month.setValue(gainclicked.getMonth());
            year.setValue(gainclicked.getYear());
            mDescription.setText(gainclicked.getDescription());
            mValue.setText(String.valueOf(gainclicked.getGainValue()));
        }else {
            day.setValue(c.get(Calendar.DAY_OF_MONTH));
            month.setValue(c.get(Calendar.MONTH));
            year.setValue(c.get(Calendar.YEAR));
        }

        bottomSheetView.findViewById(R.id.btn_valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mDescription.getText().toString().trim();
                String value = mValue.getText().toString().trim();
                if (description.isEmpty() || value.isEmpty()) {
                    Toast.makeText(Dashboard.this,"Ces champs ne peuvent pas être vide",Toast.LENGTH_SHORT).show();
                    return;
                }
                double nvalue = Double.valueOf(value);
                Gain newGain = new Gain(nvalue, description, day.getValue(), month.getValue(),year.getValue(), "", currentLivret);
                if (edit) {
                    gainViewModel.update(newGain);
                }else{
                    gainViewModel.insert(newGain);
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
}