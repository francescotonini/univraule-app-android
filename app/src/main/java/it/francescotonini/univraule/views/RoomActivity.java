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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.alamkanak.weekview.MonthLoader.MonthChangeListener;
import com.alamkanak.weekview.WeekViewEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.databinding.ActivityRoomBinding;
import it.francescotonini.univraule.models.Room;
import it.francescotonini.univraule.viewmodels.RoomViewModel;

public class RoomActivity extends BaseActivity implements MonthChangeListener {
    @Override protected int getLayoutId() {
        return R.layout.activity_room;
    }

    @Override protected void setToolbar() {
        setSupportActionBar((Toolbar)binding.toolbar);
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

        getSupportActionBar().setTitle(roomName);
        binding.activityRoomWeekView.setMonthChangeListener(this);
        binding.activityRoomWeekView.goToHour(7);
    }

    @Override protected void onResume() {
        super.onResume();

        getViewModel().getEvents(roomName, officeName)
        .observe(this, result -> {
            events = result;
            binding.activityRoomWeekView.notifyDatasetChanged();
        });
    }

    @Override public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.YEAR) != newYear || today.get(Calendar.MONTH) != newMonth) {
            return new ArrayList<>();
        }

        if (events == null) {
            return new ArrayList<>();
        }

        List<WeekViewEvent> result = new ArrayList<>();
        for (Room.Event e : events) {
            Calendar start = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
            start.setTimeInMillis(e.getStartTimestamp());

            Calendar end = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
            end.setTimeInMillis(e.getEndTimestamp());
            end.set(Calendar.MINUTE, end.get(Calendar.MINUTE) - 1);

            result.add(new WeekViewEvent(e.getName().hashCode(), e.getName(), "", start, end));
        }

        return result;
    }

    private String officeName;
    private String roomName;
    private List<Room.Event> events;
    private RoomViewModel viewModel;
    private ActivityRoomBinding binding;
}