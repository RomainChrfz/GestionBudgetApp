package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.view.CustomDeleteDialog;
import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.view.CategoryItem;
import fr.romaincharfaz.mapremiereapp.view.GainViewModel;
import fr.romaincharfaz.mapremiereapp.view.LivretAdapter;
import fr.romaincharfaz.mapremiereapp.view.LivretViewModel;

public class PreDashboard extends AppCompatActivity implements CustomDeleteDialog.CustomDeleteDialogListener {

    private String currentUser;
    private LivretViewModel livretViewModel;
    private GainViewModel gainViewModel;
    private ArrayList<CategoryItem> mCategoryList;
    private Livret deletedLivret;

    private Button creer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_dashboard);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra(MainActivity.CURRENT_USER);

        RecyclerView recyclerView = findViewById(R.id.recycler_livret);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final LivretAdapter adapter = new LivretAdapter();
        recyclerView.setAdapter(adapter);

        gainViewModel = new ViewModelProvider(this).get(GainViewModel.class);

        livretViewModel = new ViewModelProvider(this).get(LivretViewModel.class);
        livretViewModel.getUserLivrets(currentUser).observe(this, new Observer<List<Livret>>() {
            @Override
            public void onChanged(List<Livret> livrets) {
                adapter.setLivrets(livrets);
            }
        });

        adapter.setOnItemClickListener(new LivretAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Livret livret) {
                Intent intent = new Intent(PreDashboard.this, Dashboard.class);
                intent.putExtra(Dashboard.CURRENT_LIVRET,livret.getId());
                intent.putExtra(Dashboard.CURRENT_LIVRET_NAME,livret.getName());
                intent.putExtra(Dashboard.CURRENT_USER, currentUser);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new LivretAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view) {
                deletedLivret = adapter.getLivretAt(position);
                showPopup(view);
            }
        });


    }

    private void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_livret :
                        openDialog();
                        return true;
                    default :
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.livret_long_click_menu);
        popupMenu.show();
    }

    private void openDialog() {
        CustomDeleteDialog dialog = new CustomDeleteDialog();
        dialog.show(getSupportFragmentManager(),"Caution dialog");
    }

    private void addLivret() {
        Intent intent = new Intent(PreDashboard.this,CreationStepOne.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUser);
        startActivity(intent);
    }

    @Override
    public void onYesClicked() {
        try {
            gainViewModel.deleteLivret(deletedLivret);
            livretViewModel.delete(deletedLivret);
        }catch (Exception e) {Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}
    }

    @Override
    public void onNoClicked() {
        Toast.makeText(this,"Aucune suppression effectuée",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_livret_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_livret:
                addLivret();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}