package it.francescotonini.univraule.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 * Represents an event inside a {@link Room}
 */
@Entity
public class Event {
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

    @ColumnInfo(name = "event_name")
    private String name;
    @ColumnInfo(name = "event_start_timestamp")
    private long startTimestamp;
    @ColumnInfo(name = "event_end_timestamp")
    private long endTimestamp;
}
