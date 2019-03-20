/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2019 Francesco Tonini <francescoantoniotonini@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.francescotonini.univraule.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import it.francescotonini.univraule.App;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.helpers.AlertDialogHelper;
import it.francescotonini.univraule.helpers.SnackBarHelper;
import it.francescotonini.univraule.viewmodels.SettingsViewModel;

@SuppressLint("ValidFragment")
public class SettingsFragment extends PreferenceFragmentCompat
    implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    @SuppressLint("ValidFragment") public SettingsFragment(SettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cleardDb = findPreference("clear_db");
        leaveFeedback = findPreference("leave_feedback");
        appVersion = findPreference("app_version");
        darkTheme = (SwitchPreference)findPreference("dark_theme");

        int currentNightMode = viewModel.getUITheme();
        darkTheme.setDefaultValue(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);

        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            darkTheme.setSummary(R.string.preferences_force_dark_theme_true);
        }
    }

    @Override public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override public void onResume() {
        super.onResume();

        cleardDb.setOnPreferenceClickListener(this);
        darkTheme.setOnPreferenceChangeListener(this);
        leaveFeedback.setOnPreferenceClickListener(this);
        try {
            appVersion.setSummary(App.getContext().getPackageManager().getPackageInfo(App.getContext().getPackageName(), 0).versionName);
        } catch(PackageManager.NameNotFoundException ex) { }
    }

    @Override public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "clear_db":
                viewModel.clearDB();
                SnackBarHelper.show(this.getView(), R.string.preferences_clean_db_result);
                break;
            case "leave_feedback":
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:francescoantoniotonini@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UniVR Aule - Feedback");
                startActivity(emailIntent);
                break;
        }

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if ((Boolean) newValue) {
            viewModel.setUITheme(AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            darkTheme.setSummary(R.string.preferences_force_dark_theme_true);
        }
        else {
            viewModel.setUITheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

            darkTheme.setSummary(R.string.preferences_force_dark_theme_false);
        }

        // Show popup
        AlertDialogHelper.show(getContext(), R.string.info, R.string.preferences_force_dark_theme_message, R.string.ok);

        return true;
    }

    private SettingsViewModel viewModel;
    private SwitchPreference darkTheme;
    private Preference cleardDb;
    private Preference leaveFeedback;
    private Preference appVersion;
}
