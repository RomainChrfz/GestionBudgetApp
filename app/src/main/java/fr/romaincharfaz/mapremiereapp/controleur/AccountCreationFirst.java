package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.view.UserViewModel;

public class AccountCreationFirst extends AppCompatActivity {
    public static final String USER = "fr.romaincharfaz.mapremiereapp.controleur.AccountCreationFirst.USER";
    public static final String PASSWORD = "fr.romaincharfaz.mapremiereapp.controleur.AccountCreationFirst.PASSWORD";
    public static final String EMAIL = "fr.romaincharfaz.mapremiereapp.controleur.AccountCreationFirst.EMAIL";
    public static final String URL = "fr.romaincharfaz.mapremiereapp.controleur.AccountCreationFirst.URL";

    private TextView mWelcomeText;
    private TextInputLayout mUsernameNew;
    private TextInputLayout mPasswordOne;
    private TextInputLayout mPasswordTwo;
    private TextInputLayout mEmailAdress;
    private TextInputEditText mUsernametxt;
    private TextInputEditText mEmailAdresstxt;
    private TextInputEditText mPasswordTwotxt;
    private TextInputEditText mPasswordOnetxt;
    private Button mCreationButton;

    private List<String> usernames = new ArrayList<String>();
    private List<String> emailAdresses = new ArrayList<String>();

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- FOR TRANSPARENT NOTIF BAR ---
        //requestWindowFeature(1  );
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        // --- SETTING THE CONTENT VIEW ---
        setContentView(R.layout.activity_account_creation_first);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                //Up to date list of Users
                for (int i=0 ; i < users.size() ; i++) {
                    usernames.add(users.get(i).getUsername());
                    emailAdresses.add(users.get(i).getEmailAdress());
                }
            }
        });

        mWelcomeText = (TextView) findViewById(R.id.welcome_msg);
        mUsernameNew = (TextInputLayout) findViewById(R.id.username_new);
        mEmailAdress = (TextInputLayout) findViewById(R.id.email_et);
        mPasswordOne = (TextInputLayout) findViewById(R.id.password_one);
        mPasswordTwo = (TextInputLayout) findViewById(R.id.password_two);
        mUsernametxt = (TextInputEditText) findViewById(R.id.username_txt);
        mEmailAdresstxt = (TextInputEditText) findViewById(R.id.email_txt);
        mPasswordTwotxt = (TextInputEditText) findViewById(R.id.password_two_txt);
        mPasswordOnetxt = (TextInputEditText) findViewById(R.id.password_one_txt);
        mCreationButton = (Button) findViewById(R.id.creation_btn);

        mUsernametxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputUsername = mUsernameNew.getEditText().getText().toString().trim();
                if (usernames.contains(inputUsername)) { mUsernameNew.setError(getString(R.string.existing_username_error)); }
                else { mUsernameNew.setError(null); }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mEmailAdresstxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputEmail = mEmailAdress.getEditText().getText().toString().trim();
                if (emailAdresses.contains(inputEmail)) { mEmailAdress.setError(getString(R.string.existing_email_error)); }
                else { mEmailAdress.setError(null); }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mPasswordOnetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputOne = mPasswordOne.getEditText().getText().toString().trim();
                String inputTwo = mPasswordTwo.getEditText().getText().toString().trim();
                if (inputOne.equals(inputTwo)) {
                    mPasswordTwo.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mPasswordTwotxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputOne = mPasswordOne.getEditText().getText().toString().trim();
                String inputTwo = mPasswordTwo.getEditText().getText().toString().trim();
                if (!(inputOne.equals(inputTwo))) {
                    mPasswordTwo.setError(getString(R.string.password_not_corresponding));
                }else {
                    mPasswordTwo.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });
    }


    private boolean validateUsername() {
        String usernameInput = mUsernameNew.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            mUsernameNew.setError(getString(R.string.empty_error));
            return false;
        }else if (this.usernames.contains(usernameInput)) {
            mUsernameNew.setError(getString(R.string.existing_username_error));
            return false;
        }else {
            mUsernameNew.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = mEmailAdress.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            mEmailAdress.setError(getString(R.string.empty_error));
            return false;
        }else if (this.emailAdresses.contains(emailInput)) {
            mEmailAdress.setError(getString(R.string.existing_email_error));
            return false;
        }else {
            mEmailAdress.setError(null);
            return true;
        }
    }

    private boolean validatePasswordOne() {
        String pwdInput = mPasswordOne.getEditText().getText().toString().trim();

        if (pwdInput.isEmpty()) {
            mPasswordOne.setError(getString(R.string.empty_error));
            return false;
        }else {
            mPasswordOne.setError(null);
            return true;
        }
    }

    private boolean validatePasswordTwo() {
        String pwdInputOne = mPasswordOne.getEditText().getText().toString().trim();
        String pwdInputTwo = mPasswordTwo.getEditText().getText().toString().trim();

        if (pwdInputTwo.isEmpty()) {
            mPasswordTwo.setError(getString(R.string.empty_error));
            return false;
        }else if (!(pwdInputOne.equals(pwdInputTwo))) {
            mPasswordTwo.setError(getString(R.string.password_not_corresponding));
            return false;
        }else {
            mPasswordTwo.setError(null);
            return true;
        }
    }

    private void confirmInput() {
        String usernameInput = mUsernameNew.getEditText().getText().toString().trim();
        String emailInput = mEmailAdress.getEditText().getText().toString().trim();
        String passwordInput = mPasswordOne.getEditText().getText().toString().trim();
        String urlInput = "randomUrl";
        if (!validateUsername() | !validateEmail() | !validatePasswordOne() | !validatePasswordTwo()) {
            return;
        }
        Intent data = new Intent();
        data.putExtra(USER,usernameInput);
        data.putExtra(PASSWORD,passwordInput);
        data.putExtra(EMAIL,emailInput);
        data.putExtra(URL,urlInput);
        setResult(RESULT_OK,data);
        finish();
    }
}