package fr.romaincharfaz.mapremiereapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class CustomDeleteDialog extends AppCompatDialogFragment {
    private CustomDeleteDialogListener listener1;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.caution)
                .setMessage(R.string.caution_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener1.onYesClicked();
                    }
                });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener1.onNoClicked();
            }
        });
        return builder.create();

    }

    public interface CustomDeleteDialogListener{
        void onYesClicked();
        void onNoClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener1 = (CustomDeleteDialogListener) context;
    }
}