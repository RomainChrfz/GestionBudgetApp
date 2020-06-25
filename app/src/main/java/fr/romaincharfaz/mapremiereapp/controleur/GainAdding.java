package fr.romaincharfaz.mapremiereapp.controleur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import fr.romaincharfaz.mapremiereapp.R;

public class GainAdding extends AppCompatActivity {
    public static final String EXTRA_BANQUE = "fr.romaincharfaz.mapremiereapp.controleur.GainAdding.EXTRA_BANQUE";
    public static final String EXTRA_VALUE = "fr.romaincharfaz.mapremiereapp.controleur.GainAdding.EXTRA_VALUE";


    private EditText mDescription;
    private EditText mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain_adding);

        mDescription = (EditText) findViewById(R.id.description_et);
        mValue = (EditText) findViewById(R.id.value_et);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        setTitle("Ajouter dépense/revenu");
    }

    private void saveGain() {
        String textDescription = mDescription.getText().toString().trim();
        String textvalue = mValue.getText().toString().trim();
        if (textDescription.isEmpty() || textvalue.isEmpty()) {
            Toast.makeText(this, "Les champs ne peuvent pas être vide", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_BANQUE, textDescription);
        data.putExtra(EXTRA_VALUE, textvalue);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_gain_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_gain:
                saveGain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}