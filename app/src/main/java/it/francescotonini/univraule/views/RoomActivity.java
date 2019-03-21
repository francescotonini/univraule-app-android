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

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.alamkanak.weekview.MonthLoader.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.databinding.ActivityRoomBinding;
import it.francescotonini.univraule.helpers.DateTimeInterpreter;
import it.francescotonini.univraule.models.Event;
import it.francescotonini.univraule.viewmodels.RoomViewModel;

public class RoomActivity extends BaseActivity implements MonthChangeListener {
    @Override protected int getLayoutId() {
        return R.layout.activity_room;
    }

    @Override protected void setToolbar() {
        setSupportActionBar((Toolbar)binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override protected RoomViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = new RoomViewModel(this);
        }

        return viewModel;
    }

    @Override protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        officeName = getIntent().getStringExtra("officeName");
        roomName = getIntent().getStringExtra("roomName");

        getSupportActionBar().setTitle(getTitle() + " " + roomName);

        binding.activityRoomWeekView.setDateTimeInterpreter(new DateTimeInterpreter());
        binding.activityRoomWeekView.setMonthChangeListener(this);
        binding.activityRoomWeekView.goToHour(7);
    }

    @Override protected void onResume() {
        super.onResume();

        getViewModel().getEvents(roomName, officeName)
        .observe(this, result -> {
            events = result;
            binding.activityRoomWeekView.notifyDataSetChanged();
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<WeekViewDisplayable> onMonthChange(Calendar startDate, Calendar endDate) {
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.YEAR) != startDate.get(Calendar.YEAR) || today.get(Calendar.MONTH) != startDate.get(Calendar.MONTH)) {
            return new ArrayList<>();
        }

        if (events == null) {
            return new ArrayList<>();
        }

        List<WeekViewDisplayable> result = new ArrayList<>();
        for (Event e : events) {
            Calendar start = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
            start.setTimeInMillis(e.getStartTimestamp());

            Calendar end = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
            end.setTimeInMillis(e.getEndTimestamp());
            end.set(Calendar.MINUTE, end.get(Calendar.MINUTE) - 1);

            WeekViewEvent event = e.toWeekViewEvent();
            event.setColor(getResources().getColor(R.color.accent));
            result.add(event);
        }

        return result;
    }

    private String officeName;
    private String roomName;
    private List<Event> events;
    private RoomViewModel viewModel;
    private ActivityRoomBinding binding;
}
