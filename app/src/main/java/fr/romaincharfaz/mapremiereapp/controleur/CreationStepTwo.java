package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Livret;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.view.LivretViewModel;
import fr.romaincharfaz.mapremiereapp.view.UserViewModel;

public class CreationStepTwo extends AppCompatActivity {
    private String Name;
    private String Solde;
    private String currentUser;

    private EditText accountSolde;

    private UserViewModel userViewModel;
    private LivretViewModel livretViewModel;
    private int indexer;
    private List<String> usernames = new ArrayList<>();
    private User updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_step_two);

        Intent intent = getIntent();
        Name = intent.getStringExtra(CreationStepOne.ACCOUNT_NAME);
        currentUser = intent.getStringExtra(MainActivity.CURRENT_USER);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (int i=0; i<users.size(); i++) {
                    usernames.add(users.get(i).getUsername());
                }
                indexer = usernames.indexOf(currentUser);
                updater = users.get(indexer);
            }
        });

        livretViewModel = new ViewModelProvider(this).get(LivretViewModel.class);

        accountSolde = (EditText) findViewById(R.id.et_solde_compte);
        TextView nextTwo = (TextView) findViewById(R.id.btn_next_two);
        TextView prevTwo = (TextView) findViewById(R.id.btn_prev_two);

        nextTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Solde = accountSolde.getText().toString().trim();
                if (Solde.isEmpty()) {
                    Toast.makeText(CreationStepTwo.this,getString(R.string.empty_error),Toast.LENGTH_SHORT).show();
                    return;
                }
                updateStatusUser();
                Livret livret = new Livret(Name,currentUser);
                livretViewModel.insert(livret);
                Intent newintent = new Intent(CreationStepTwo.this, PreDashboard.class);
                newintent.putExtra(MainActivity.CURRENT_USER,currentUser);
                startActivity(newintent);
                finish();
           }
        });

        prevTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateStatusUser() {
        updater.setUserStatus(1);
        userViewModel.update(updater);
    }
}