package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.romaincharfaz.mapremiereapp.R;
import fr.romaincharfaz.mapremiereapp.model.User;
import fr.romaincharfaz.mapremiereapp.view.CustomPasswordForgottenDialog;
import fr.romaincharfaz.mapremiereapp.view.UserViewModel;

public class MainActivity extends AppCompatActivity implements CustomPasswordForgottenDialog.CustomPasswordForgottenDialogListener {
    public static final String CURRENT_USER = "fr.romaincharfaz.mapremiereapp.controleur.MainActivity.CURRENT_USER";
    public static final int ADD_USER_REQUEST = 1;

    //private static App mInstance;
    //private static Resources res;

    private TextInputLayout mUsername;
    private TextInputLayout mPassword;
    private TextView mAccountCreation;

    private UserViewModel userViewModel;
    private List<String> usernames = new ArrayList<>();
    private List<String> emails = new ArrayList<>();
    private List<String> passwords = new ArrayList<>();
    private List<User> allmyusers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- TRANSPARENT NOTIFICATION BAR ---
        //requestWindowFeature(1);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        mUsername = (TextInputLayout) findViewById(R.id.username);
        mPassword = (TextInputLayout) findViewById(R.id.password);
        Button mLoginBtn = (Button) findViewById(R.id.login_btn);
        mAccountCreation = (TextView) findViewById(R.id.account_creation);
        TextView mForgotten = (TextView) findViewById(R.id.forgotten_pwd);

        mForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

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
                usernames = new ArrayList<>();
                emails = new ArrayList<>();
                for (int i=0 ; i<users.size() ; i++) {
                    usernames.add(users.get(i).getUsername());
                    passwords.add(users.get(i).getPassword());
                    emails.add(users.get(i).getEmailAdress());
                }
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                }catch (Exception e){Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();}
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

    }

    public void openNewUserActivity() {
        Intent NewUserIntent = new Intent(MainActivity.this, AccountCreationFirst.class);
        startActivityForResult(NewUserIntent,ADD_USER_REQUEST);
    }

    private void showDialog() {
        CustomPasswordForgottenDialog dialog = new CustomPasswordForgottenDialog();
        dialog.show(getSupportFragmentManager(),"forgotten");
    }

    @Override
    public void sendEmail(String txt) {
        if (emails.contains(txt)) {
            Snackbar.make(mAccountCreation,"Cet email existe", Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(mAccountCreation,"Cet email n'existe pas",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_USER_REQUEST && resultCode == RESULT_OK) {
            String user = data.getStringExtra(AccountCreationFirst.USER);
            String pwd = data.getStringExtra(AccountCreationFirst.PASSWORD);
            String email = data.getStringExtra(AccountCreationFirst.EMAIL);
            String url = data.getStringExtra(AccountCreationFirst.URL);
            int status = 0;
            User nuser = new User(user,pwd,email,url,status);
            userViewModel.insert(nuser);
        }
    }
}