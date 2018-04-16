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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.Calendar;
import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.adapters.RoomsAdapter;
import it.francescotonini.univraule.databinding.ActivityMainBinding;
import it.francescotonini.univraule.helpers.SnackBarHelper;
import it.francescotonini.univraule.models.Room;
import it.francescotonini.univraule.viewmodels.RoomsViewModel;

/**
 * Activity of R.layout.activity_main
 */
public class MainActivity extends BaseActivity
        implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener,
        RoomsAdapter.OnItemClickListener {
    @Override protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override protected void setToolbar() {
        setSupportActionBar((Toolbar)binding.toolbar);
    }

    @Override protected RoomsViewModel getViewModel() {
        return new RoomsViewModel(this);
    }

    @Override protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.menu_main_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_change_offices:
                startActivity(new Intent(this, OfficesActivity.class));
                break;
            case R.id.menu_main_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new RoomsAdapter(this);
        binding.activityMainRecyclerView.setAdapter(adapter);
        binding.activityMainRefreshlayout.setOnRefreshListener(this);
    }

    @Override protected void onResume() {
        super.onResume();

        // If today is sunday, show a message
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            binding.activityMainRefreshlayout.setVisibility(View.GONE);
            binding.activityMainAlternativeText.setVisibility(View.VISIBLE);
            binding.activityMainAlternativeText.setText(R.string.activity_main_sunday);

            return;
        }

        // Standard loading
        binding.activityMainRefreshlayout.setRefreshing(true);
        getViewModel().getRooms().observe(this, rooms -> {
            if (rooms == null) {
                SnackBarHelper.show(binding.activityMainRecyclerView, R.string.error_generic_message);

                return;
            }

            Logger.v(MainActivity.class.getSimpleName(), "# of rooms: " + rooms.size());
            if (rooms.size() == 0) {
                return;
            }

            binding.activityMainRefreshlayout.setRefreshing(false);
            adapter.update(rooms);
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Logger.v(MainActivity.class.getSimpleName(), "query text is " + newText);

        adapter.getFilter().filter(newText);
        return true;
    }

    @Override public void onRefresh() {
        Logger.v(MainActivity.class.getSimpleName(), "refreshing rooms");

        binding.activityMainRefreshlayout.setRefreshing(true);
        onResume();
    }

    @Override public void onItemClick(Room room) {
        Intent i = new Intent(this, RoomActivity.class);
        i.putExtra("roomName", room.getName());
        i.putExtra("officeName", room.getOfficeName());

        startActivity(i);
    }

    private RoomsAdapter adapter;
    private ActivityMainBinding binding;
}
