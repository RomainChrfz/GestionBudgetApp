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
import fr.romaincharfaz.mapremiereapp.view.LivretViewModel;

public class CreationStepOne extends AppCompatActivity {
    public static final String ACCOUNT_NAME = "fr.romaincharfaz.mapremiereapp.controler.CreationStepOne.ACCOUNT_NAME";

    private String currentUser;
    private String string;
    private ArrayList<String> livretNames;

    private EditText accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_step_one);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra(MainActivity.CURRENT_USER);

        accountName = (EditText) findViewById(R.id.et_nom_compte);
        TextView nextOne = (TextView) findViewById(R.id.btn_next_one);

        LivretViewModel livretViewModel = new ViewModelProvider(this).get(LivretViewModel.class);
        livretViewModel.getUserLivrets(currentUser).observe(this, new Observer<List<Livret>>() {
            @Override
            public void onChanged(List<Livret> livrets) {
                livretNames = new ArrayList<>();
                for (int i=0; i<livrets.size(); i++) {
                    livretNames.add(livrets.get(i).getName());
                }
            }
        });

        nextOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string = accountName.getText().toString().trim();
                if (string.isEmpty()) {
                    Toast.makeText(CreationStepOne.this,getString(R.string.empty_error),Toast.LENGTH_SHORT).show();
                    return;
                }else if (livretNames.contains(string)){
                    Toast.makeText(CreationStepOne.this,getString(R.string.existing_account),Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CreationStepOne.this, CreationStepTwo.class);
                intent.putExtra(ACCOUNT_NAME,string);
                intent.putExtra(MainActivity.CURRENT_USER,currentUser);
                startActivity(intent);
            }
        });
    }

}