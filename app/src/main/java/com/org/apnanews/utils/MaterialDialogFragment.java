package com.org.apnanews.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.org.apnanews.R;

/**
 * Created by Zafar.Hussain on 07/06/2018.
 */

public class MaterialDialogFragment extends DialogFragment {

    public static void show(AppCompatActivity context) {
        MaterialDialogFragment dialog = new MaterialDialogFragment();
        dialog.show(context.getSupportFragmentManager(), "[ABOUT_DIALOG]");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialStyledDialog.Builder(getActivity())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.app_name)
                .setDescription("")
                .setPositiveText(android.R.string.ok)
                .setNegativeText("Cancel")
                .build();
    }

}
