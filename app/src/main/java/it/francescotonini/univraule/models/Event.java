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

package it.francescotonini.univraule.models;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.TimeZone;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Represents an event inside a {@link Room}
 */
@Entity
public class Event implements WeekViewDisplayable<Event> {
    /**
     * Gets the name of the event
     * @return name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event
     * @param name name of the event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the start timestamp of the event
     * @return start timestamp of the event
     */
    public long getStartTimestamp() {
        return startTimestamp * 1000;
    }

    /**
     * Sets the start timestamp of the event
     * @param startTimestamp start timestamp of the event
     */
    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /**
     * Gets the end timestamp of the event
     * @return end timestamp of the event
     */
    public long getEndTimestamp() {
        return endTimestamp * 1000;
    }

    /**
     * Sets the end timestamp of the event
     * @param endTimestamp end timestamp of the event
     */
    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    @Override public WeekViewEvent<Event> toWeekViewEvent() {
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        start.setTimeInMillis(getStartTimestamp());
        Calendar end = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        end.setTimeInMillis(getEndTimestamp());
        end.set(Calendar.MINUTE, end.get(Calendar.MINUTE) - 1);

        return new WeekViewEvent<>(getStartTimestamp(), getName(), start, end, "", 0, false, null);
    }

    @ColumnInfo(name = "event_name")
    private String name;
    @ColumnInfo(name = "event_start_timestamp")
    private long startTimestamp;
    @ColumnInfo(name = "event_end_timestamp")
    private long endTimestamp;
}
