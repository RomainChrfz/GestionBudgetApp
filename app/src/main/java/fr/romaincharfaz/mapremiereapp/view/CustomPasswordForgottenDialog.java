package fr.romaincharfaz.mapremiereapp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import fr.romaincharfaz.mapremiereapp.R;

public class CustomPasswordForgottenDialog extends AppCompatDialogFragment {

    private EditText email;
    private CustomPasswordForgottenDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_pwd, null);

        builder.setView(view)
                .setTitle(getString(R.string.forgotten_password))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailtxt = email.getText().toString().trim();
                        listener.sendEmail(emailtxt);
                    }
                });

        email = view.findViewById(R.id.email_forgot_et);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (CustomPasswordForgottenDialogListener) context;
    }

    public interface CustomPasswordForgottenDialogListener{
        void sendEmail(String txt);
    }
}