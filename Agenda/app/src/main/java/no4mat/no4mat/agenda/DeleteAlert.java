package no4mat.no4mat.agenda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteAlert extends DialogFragment {

    DialogInterface.OnClickListener positive;
    DialogInterface.OnClickListener negative;

    public DeleteAlert(DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        this.positive = positive;
        this.negative = negative;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea eliminar esta entrada?")
                .setPositiveButton("Si", positive)
                .setNegativeButton("No", negative);
        return builder.create();
    }
}
