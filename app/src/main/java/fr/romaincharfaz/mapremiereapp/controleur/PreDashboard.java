package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.view.LivretAdapter;
import fr.romaincharfaz.mapremiereapp.view.LivretViewModel;

public class PreDashboard extends AppCompatActivity {

    private String currentUser;
    private LivretViewModel livretViewModel;

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
                intent.putExtra(Dashboard.CURRENT_LIVRET, livret.getName());
                intent.putExtra(Dashboard.CURRENT_USER, currentUser);
                startActivity(intent);
            }
        });

    }

    private void addLivret() {
        Intent intent = new Intent(PreDashboard.this,CreationStepOne.class);
        intent.putExtra(MainActivity.CURRENT_USER, currentUser);
        startActivity(intent);
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