package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.view.CategoryAdapter;
import fr.romaincharfaz.mapremiereapp.view.CategoryItem;
import fr.romaincharfaz.mapremiereapp.view.GainAdapter;
import fr.romaincharfaz.mapremiereapp.view.GainViewModel;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static java.lang.Math.abs;

public class Dashboard extends AppCompatActivity {
    public static final String CURRENT_LIVRET = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_LIVRET";
    public static final String CURRENT_LIVRET_NAME = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_LIVRET_NAME";
    public static final String CURRENT_USER = "fr.romaincharfaz.mapremiereapp.controleur.Dashboard.CURRENT_USER";

    private GainViewModel gainViewModel;

    private ArrayList<CategoryItem> mCategoryList;

    public static boolean edit;
    public static boolean expense;
    public static int categoryselected = 0;
    private Gain deletedGain;
    private Gain modifiedGain;
    private long currentLivret;
    private String currentUser;

    private TextView perc_txt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(1);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra(CURRENT_USER);
        currentLivret = intent.getLongExtra(CURRENT_LIVRET,-1);
        String currentLivretName = intent.getStringExtra(CURRENT_LIVRET_NAME);
        setTitle(currentLivretName);

        try{
            initList();
        }catch(Exception e){
            Toast.makeText(Dashboard.this,e.toString(),Toast.LENGTH_LONG).show();
        }


        perc_txt = findViewById(R.id.txt_percentage);
        progressBar = findViewById(R.id.progressBar);
        ImageView add_expense = findViewById(R.id.add_expense_btn);
        ImageView add_income = findViewById(R.id.add_income_btn);

        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = false;
                expense = true;
                categoryselected = 0;
                Gain gainclicked = new Gain(0.0,"",0,0,0,0,"",0,"");
                bottomsheetconfiguration(gainclicked);
            }
        });

        add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = false;
                expense = false;
                categoryselected = 10;
                Gain gainclicked = new Gain(0.0,"",0,0,0,0,"",0,"");
                bottomsheetconfiguration(gainclicked);
            }
        });

        final RecyclerView recyclerView_expense = findViewById(R.id.recycler_expense);
        recyclerView_expense.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_expense.setHasFixedSize(true);

        final RecyclerView recyclerView_income = findViewById(R.id.recycler_income);
        recyclerView_income.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_income.setHasFixedSize(true);

        final GainAdapter adapter_expense = new GainAdapter();
        final GainAdapter adapter_income = new GainAdapter();

        recyclerView_income.setAdapter(adapter_income);
        adapter_income.setOnItemClickListener(new GainAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Gain gainclicked = adapter_income.getGainAt(position);
                edit = true;
                expense = false;
                categoryselected = gainclicked.getCategory();
                bottomsheetconfiguration(gainclicked);
            }

            @Override
            public void OnCatClick(int position, View view) {

            }
        });

        recyclerView_expense.setAdapter(adapter_expense);
        adapter_expense.setOnItemClickListener(new GainAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Gain gainclicked = adapter_expense.getGainAt(position);
                edit = true;
                expense = true;
                categoryselected = gainclicked.getCategory();
                bottomsheetconfiguration(gainclicked);
            }

            @Override
            public void OnCatClick(int position, View view) {
                modifiedGain = adapter_expense.getGainAt(position);
                showPopup(view);
            }
        });

        gainViewModel = new ViewModelProvider(Dashboard.this).get(GainViewModel.class);
        gainViewModel.getAllGains(currentLivret,currentUser).observe(this, new Observer<List<Gain>>() {
            @Override
            public void onChanged(List<Gain> gains) {
                List<Gain> gain_pos = new ArrayList<>();
                List<Gain> gain_neg = new ArrayList<>();
                for (int i=0; i<gains.size(); i++) {
                    Gain currentGain = gains.get(i);
                    if (currentGain.getGainValue() >= 0) {
                        gain_pos.add(currentGain);
                    }else {
                        gain_neg.add(currentGain);
                    }
                }
                adapter_expense.submitList(gain_neg);
                adapter_income.submitList(gain_pos);
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
                        deletedGain = adapter_expense.getGainAt(position);
                        gainViewModel.delete(deletedGain);
                        Snackbar.make(recyclerView_expense, getString(R.string.expense_deleted), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), new View.OnClickListener() {
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
        })).attachToRecyclerView(recyclerView_expense);

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
                        deletedGain = adapter_income.getGainAt(position);
                        gainViewModel.delete(deletedGain);
                        Snackbar.make(recyclerView_income, getString(R.string.expense_deleted), Snackbar.LENGTH_LONG).setAction(getString(R.string.undo), new View.OnClickListener() {
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
        })).attachToRecyclerView(recyclerView_income);

    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPopup(View view) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        initList();
        CategoryAdapter mAdapter = new CategoryAdapter(this, mCategoryList);
        listPopupWindow.setAdapter(mAdapter);
        listPopupWindow.setAnchorView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listPopupWindow.setBackgroundDrawable(getDrawable(R.drawable.custom_spinner));
        }
        listPopupWindow.setContentWidth(800);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Gain gain = new Gain(modifiedGain.getGainValue(), modifiedGain.getDescription(), modifiedGain.getDay(), modifiedGain.getMonth(), modifiedGain.getYear(), position, modifiedGain.getUrlJustif(), modifiedGain.getUserId(), modifiedGain.getUsernameId());
                    gain.setId(modifiedGain.getId());
                    gainViewModel.update(gain);
                    listPopupWindow.dismiss();
                }catch (Exception e){ Toast.makeText(Dashboard.this, e.toString(), Toast.LENGTH_LONG).show(); }
            }
        });
        listPopupWindow.show();
    }

   private void initList() {
       mCategoryList = new ArrayList<>();
       mCategoryList.add(new CategoryItem(getString(R.string.cat_base), R.drawable.ic_cat_unknown));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_courses), R.drawable.ic_cat_courses));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_fuel), R.drawable.ic_cat_fuel));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_gift), R.drawable.ic_cat_gift));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_phone), R.drawable.ic_cat_phone));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_transport_commun), R.drawable.ic_cat_transport_commun));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_trip), R.drawable.ic_cat_trip));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_diy), R.drawable.ic_cat_diy));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_cinema), R.drawable.ic_cat_cinema));
       mCategoryList.add(new CategoryItem(getString(R.string.cat_restaurant), R.drawable.ic_cat_restaurant));
   }

    private void bottomsheetconfiguration(final Gain gainclicked) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Dashboard.this, R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout) findViewById(R.id.bottom_sheet_container));

        final NumberPicker day = bottomSheetView.findViewById(R.id.day_picker);
        final EditText mDescription = bottomSheetView.findViewById(R.id.et_description);
        final EditText mValue = bottomSheetView.findViewById(R.id.et_valeur);

        day.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d",value);
            }
        });
        final NumberPicker month = bottomSheetView.findViewById(R.id.month_picker);
        final NumberPicker year = bottomSheetView.findViewById(R.id.year_picker);
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
            mValue.setText(String.valueOf(abs(gainclicked.getGainValue())));
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
                double nvalue = Double.parseDouble(value);
                if (expense) { nvalue = - nvalue; }
                Gain newGain = new Gain(nvalue, description, day.getValue(), month.getValue(),year.getValue(),categoryselected, "", currentLivret,currentUser);
                if (edit) {
                    newGain.setId(gainclicked.getId());
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
        double pos = 0.0;
        for (int i=0; i<totgain.size(); i++) {
            double val = totgain.get(i).getGainValue();
            tot += val;
            if(val >= 0.0) {
                pos += val;
            }
        }
        tot = Math.round(tot * 100d) / 100d;
        pos = Math.round(pos * 100d) / 100d;
        int percentage = (int) (tot*100/pos);
        String tot_ss = String.valueOf(tot);
        SpannableString ss = new SpannableString(tot_ss);
        if (tot >= 0) {
            ForegroundColorSpan fcsg = new ForegroundColorSpan(Color.GREEN);
            ss.setSpan(fcsg,0,tot_ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            ForegroundColorSpan fcsr = new ForegroundColorSpan(Color.RED);
            ss.setSpan(fcsr,0,tot_ss.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        progressBar.setProgress(percentage);
        perc_txt.setText("Reste : " + ss + " | " + percentage + " %");
    }
}