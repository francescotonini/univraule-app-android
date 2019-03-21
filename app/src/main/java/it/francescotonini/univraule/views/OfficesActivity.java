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

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.pixplicity.easyprefs.library.Prefs;

import it.francescotonini.univraule.App;
import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.adapters.OfficesAdapter;
import it.francescotonini.univraule.databinding.ActivityOfficesBinding;
import it.francescotonini.univraule.helpers.AlertDialogHelper;
import it.francescotonini.univraule.helpers.SimpleDividerItemDecoration;
import it.francescotonini.univraule.helpers.SnackBarHelper;
import it.francescotonini.univraule.viewmodels.OfficesViewModel;

/**
 * Activity of R.layout.activity_offices
 */
public class OfficesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {
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

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new OfficesAdapter();
        binding.activityOfficesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(App.getContext(), this.getResources().getColor(R.color.divider), 3));
        binding.activityOfficesRecyclerView.setAdapter(adapter);
        binding.activityOfficesSaveButton.setOnClickListener(this);
    }

    @Override protected void onResume() {
        super.onResume();

        binding.activityOfficesRefreshlayout.setRefreshing(true);
        getViewModel().getOffices().observe(this, offices -> {
            if (offices == null) {
                SnackBarHelper.show(binding.activityOfficesRecyclerView, R.string.error_generic_message);

                return;
            }

            Logger.v(MainActivity.class.getSimpleName(), "# of offices: " + offices.size());

            binding.activityOfficesRefreshlayout.setRefreshing(false);
            adapter.update(offices);
        });
    }

    @Override public void onRefresh() {
        onResume();
    }

    @Override public void onClick(View v) {
        if (adapter.getSelectedOffices().size() == 0) {
            AlertDialogHelper.show(this, R.string.error_no_offices_selected_title, R.string.error_no_offices_selected_message);
            return;
        }

        getViewModel().saveOffices(adapter.getSelectedOffices());

        if (Prefs.getBoolean("isFirstBoot", true)) {
            Prefs.putBoolean("isFirstBoot", false);
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            onBackPressed();
        }
    }

    private OfficesAdapter adapter;
    private ActivityOfficesBinding binding;
}
