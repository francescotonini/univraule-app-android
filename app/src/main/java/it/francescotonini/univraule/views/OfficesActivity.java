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

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pixplicity.easyprefs.library.Prefs;

import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.adapters.OfficesAdapter;
import it.francescotonini.univraule.databinding.ActivityOfficesBinding;
import it.francescotonini.univraule.helpers.AlertDialogHelper;
import it.francescotonini.univraule.viewmodels.OfficesViewModel;

/**
 * Activity of R.layout.activity_offices
 */
public class OfficesActivity extends BaseActivity {
    @Override protected int getLayoutId() {
        return R.layout.activity_offices;
    }

    @Override protected void setToolbar() {
        setSupportActionBar((Toolbar)binding.toolbar);

        if (!Prefs.getBoolean("isFirstStart", true)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override protected OfficesViewModel getViewModel() {
        return new OfficesViewModel(this);
    }

    @Override protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offices, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_offices_save) {
            getViewModel().saveOffices(adapter.getSelectedOffices());
            Prefs.putBoolean("isFirstStart", false);
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new OfficesAdapter();
        binding.activityOfficesRecyclerView.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();

        binding.activityOfficesRefreshlayout.setRefreshing(true);
        getViewModel().getOffices().observe(this, offices -> {
            if (offices == null) {
                AlertDialogHelper.show(getCurrentActivity(), R.string.generic_error_title, R.string.generic_error_message);

                return;
            }

            Logger.v(MainActivity.class.getSimpleName(), "# of offices: " + offices.size());

            binding.activityOfficesRefreshlayout.setRefreshing(false);
            adapter.update(offices);
        });
    }

    private OfficesAdapter adapter;
    private ActivityOfficesBinding binding;
}
