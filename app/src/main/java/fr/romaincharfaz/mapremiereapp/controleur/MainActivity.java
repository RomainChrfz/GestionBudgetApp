package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Application;
import android.content.res.Resources;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.Gain;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.view.GainViewModel;
import fr.romaincharfaz.mapremiereapp.view.UserViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String CURRENT_USER = "fr.romaincharfaz.mapremiereapp.controleur.MainActivity.CURRENT_USER";
    public static final String CURRENT_PASSWORD = "fr.romaincharfaz.mapremiereapp.controleur.MainActivity.CURRENT_PASSWORD";

    //private static App mInstance;
    //private static Resources res;

    private TextInputLayout mUsername;
    private TextInputLayout mPassword;
    private Button mLoginBtn;
    private TextView mAccountCreation;

    private TextView mRandom;
    private TextView mRandom2;
    private String random = new String();
    private String random2 = new String();

    private UserViewModel userViewModel;
    private List<String> usernames = new ArrayList<String>();
    private List<String> passwords = new ArrayList<String>();
    private List<User> allmyusers = new ArrayList<User>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- TRANSPARENT NOTIFICATION BAR ---
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        mUsername = (TextInputLayout) findViewById(R.id.username);
        mPassword = (TextInputLayout) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mAccountCreation = (TextView) findViewById(R.id.account_creation);

        mRandom = (TextView) findViewById(R.id.random_txt);
        mRandom2 = (TextView) findViewById(R.id.random2_txt);

        String text = getString(R.string.account_creation_txt);
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fcsb = new ForegroundColorSpan(Color.BLACK);
        ClickableSpan cs1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openNewUserActivity();
            }
        };
        if (Locale.getDefault().getLanguage().equals("fr")) {
            ss.setSpan(fcsb,text.length()-11,text.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(cs1, text.length()-11,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            ss.setSpan(fcsb, text.length()-10, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(cs1, text.length()-10, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mAccountCreation.setText(ss);
        mAccountCreation.setMovementMethod(LinkMovementMethod.getInstance());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                allmyusers = users;
                for (int i=0 ; i<users.size() ; i++) {
                    usernames.add(users.get(i).getUsername());
                    passwords.add(users.get(i).getPassword());
                    random += (" "+users.get(i).getUsername());
                    random2 += (" "+users.get(i).getPassword());
                }
                mRandom.setText(random);
                mRandom2.setText(random2);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String usernameInput = mUsername.getEditText().getText().toString().trim();
        String passwordInput = mPassword.getEditText().getText().toString().trim();
        int indexer = usernames.indexOf(usernameInput);
        if (!(usernames.contains(usernameInput))) {
            mUsername.setError(getString(R.string.not_existing_username));
            return;
        }else if (!(allmyusers.get(indexer).getPassword().equals(passwordInput))) {
            mUsername.setError(null);
            Toast.makeText(MainActivity.this, getString(R.string.password_error), Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            if(allmyusers.get(indexer).getUserStatus() == 0) {
                Intent intent = new Intent(MainActivity.this, CreationStepOne.class);
                intent.putExtra(CURRENT_USER, usernameInput);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(MainActivity.this,PreDashboard.class);
                intent.putExtra(CURRENT_USER, usernameInput);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void openNewUserActivity() {
        Intent NewUserIntent = new Intent(MainActivity.this, AccountCreationFirst.class);
        startActivity(NewUserIntent);
    }

}