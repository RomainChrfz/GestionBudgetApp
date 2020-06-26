package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.romaincharfaz.mapremiereapp.R;

public class CreationStepOne extends AppCompatActivity {
    public static final String ACCOUNT_NAME = "fr.romaincharfaz.mapremiereapp.controler.CreationStepOne.ACCOUNT_NAME";
    public static final String CURRENT_USER_ = "fr.romaincharfaz.mapremiereapp.controler.CreationStepOne.CURENT_USER_";

    private String currentUser;
    private String string;

    private EditText accountName;
    private TextView nextOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_step_one);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra(MainActivity.CURRENT_USER);

        accountName = (EditText) findViewById(R.id.et_nom_compte);
        nextOne = (TextView) findViewById(R.id.btn_next_one);

        nextOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string = accountName.getText().toString().trim();
                if (string.isEmpty()) {
                    Toast.makeText(CreationStepOne.this,"Le champ ne peut pas être vide",Toast.LENGTH_SHORT).show();
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