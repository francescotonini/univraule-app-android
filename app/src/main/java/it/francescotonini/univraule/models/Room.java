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

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

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

    /**
     * Gets a boolean indicating whether or not this room is available or not
     * @return if TRUE this room is available; otherwise not
     */
    public boolean isFree() {
        return isFree;
    }

    /**
     * Sets a boolean indicating whether or not this room is available or not
     * @param free if TRUE this room is available; otherwise not
     */
    public void setFree(boolean free) {
        isFree = free;
    }

    /**
     * Gets the timestamp when the room gets available or not
     * @return the timestamp when the room gets available or not
     */
    public long getUntil() {
        return until;
    }

    /**
     * Sets the timestamp when the room gets available or not
     * @param until the timestamp when the room gets available or not
     */
    public void setUntil(long until) {
        this.until = until;
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
    @ColumnInfo(name = "isFree")
    private boolean isFree;
    @ColumnInfo(name = "until")
    private long until;
}
