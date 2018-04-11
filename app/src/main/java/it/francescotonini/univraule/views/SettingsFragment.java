/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Francesco Tonini <francescoantoniotonini@gmail.com>
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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import it.francescotonini.univraule.App;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.helpers.AlertDialogHelper;
import it.francescotonini.univraule.viewmodels.SettingsViewModel;

@SuppressLint("ValidFragment")
public class SettingsFragment extends PreferenceFragmentCompat
    implements Preference.OnPreferenceClickListener {
    @SuppressLint("ValidFragment") public SettingsFragment(SettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cleardDb = findPreference("clear_db");
        leaveFeedback = findPreference("leave_feedback");
        appVersion = findPreference("app_version");
    }

    @Override public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override public void onResume() {
        super.onResume();

        cleardDb.setOnPreferenceClickListener(this);
        leaveFeedback.setOnPreferenceClickListener(this);
        try {
            appVersion.setSummary(App.getContext().getPackageManager().getPackageInfo(App.getContext().getPackageName(), 0).versionName);
        } catch(PackageManager.NameNotFoundException ex) { }
    }

    @Override public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "clear_db":
                viewModel.clearDB();
                break;
            case "leave_feedback":
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:francescoantoniotonini@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UniVR Orari - Feedback");
                startActivity(emailIntent);
                break;
        }

        return true;
    }

    private SettingsViewModel viewModel;
    private Preference cleardDb;
    private Preference leaveFeedback;
    private Preference appVersion;
}
