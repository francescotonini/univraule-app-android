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

package it.francescotonini.univraule.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import it.francescotonini.univraule.database.EventTypeConverter;

/**
 * Represent a room
 */
@Entity
@TypeConverters(EventTypeConverter.class)
public class Room implements Comparator<Room> {
    /**
     * Gets the name of the room
     * @return room name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a list of {@link Event}
     * @return a list of {@link Event}
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Sets the name of this room
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a list of {@link Event}
     * @param events list of {@link Event}
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Gets the name of the {@link Office} associated with this room
     * @return name of the {@link Office}
     */
    public String getOfficeName() {
        return officeName;
    }

    /**
     * Sets the name of the {@link Office} associated with this {@link Room}
     * @param officeName name of the {@link Office}
     */
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    /**
     * Gets the id of the {@link Room}
     * @return {@link Room} id
     */
    @NonNull public int getId() {
        return id;
    }

    /**
     * Sets the id of the {@link Room}
     * @param id id of the {@link Room}
     */
    public void setId(@NonNull int id) {
        this.id = id;
    }

    @Override public int compare(Room o1, Room o2) {
        return o1.getName().compareTo(o2.getName());
    }

    @PrimaryKey(autoGenerate = true) @NonNull @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "office_name")
    private String officeName;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "events")
    private List<Event> events;
}
