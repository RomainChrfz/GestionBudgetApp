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
    public static final String ACCOUNT_NAME_ = "fr.romaincharfaz.mapremiereapp.controler.CreationStepTwo.ACCOUNT_NAME_";
    public static final String ACCOUNT_SOLDE = "fr.romaincharfaz.mapremiereapp.controler.CreationStepTwo.ACCOUNT_SOLDE";
    public static final String CURRENT_USER__ = "fr.romaincharfaz.mapremiereapp.controler.CreationStepTwo.CURRENT_USER__";

    private String Name;
    private String Solde;
    private  String currentUser;

    private EditText accountSolde;
    private TextView nextTwo;
    private TextView prevTwo;

    private UserViewModel userViewModel;
    private LivretViewModel livretViewModel;
    private List<User> allmyusers;
    private int indexer;
    private List<String> usernames = new ArrayList<String>();
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
                allmyusers = users;
                for (int i=0; i<users.size(); i++) {
                    usernames.add(users.get(i).getUsername());
                }
                indexer = usernames.indexOf(currentUser);
                updater = users.get(indexer);
            }
        });

        livretViewModel = new ViewModelProvider(this).get(LivretViewModel.class);

        accountSolde = (EditText) findViewById(R.id.et_solde_compte);
        nextTwo = (TextView) findViewById(R.id.btn_next_two);
        prevTwo = (TextView) findViewById(R.id.btn_prev_two);

        nextTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Solde = accountSolde.getText().toString().trim();
                    if (Solde.isEmpty()) {
                        Toast.makeText(CreationStepTwo.this,"Ce champ ne peut pas être vide",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updateStatusUser();
                    Livret livret = new Livret(Name,currentUser);
                    livretViewModel.insert(livret);
                    Intent newintent = new Intent(CreationStepTwo.this, PreDashboard.class);
                    newintent.putExtra(MainActivity.CURRENT_USER,currentUser);
                    startActivity(newintent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(CreationStepTwo.this,e.toString(),Toast.LENGTH_LONG).show();
                    return;
                }
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
        String username;
        String password;
        String emailAdress;
        String urlPicture;
        int userStatus;
        username = updater.getUsername();
        password = updater.getPassword();
        emailAdress = updater.getEmailAdress();
        urlPicture = updater.getUrlPicture();
        userStatus = 1;
        User updatedUser = new User(username,password,emailAdress,urlPicture,userStatus);
        userViewModel.update(updatedUser);
    }
}