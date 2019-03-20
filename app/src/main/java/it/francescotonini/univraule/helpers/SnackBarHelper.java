package it.francescotonini.univraule.helpers;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

/**
 * Handles SnackBar boilerplate
 */
public class SnackBarHelper {
    /**
     * Shows a snackbar
     * @param view view
     * @param textId textId
     */
    public static void show(View view, int textId)
    {
        Snackbar snackBar = Snackbar.make(view, textId, Snackbar.LENGTH_LONG);
        snackBar.show();
    }
}
