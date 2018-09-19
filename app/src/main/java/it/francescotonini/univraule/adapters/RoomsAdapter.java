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

package it.francescotonini.univraule.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.R;
import it.francescotonini.univraule.databinding.ItemRoomBinding;
import it.francescotonini.univraule.helpers.DateToStringFormatter;
import it.francescotonini.univraule.models.Event;
import it.francescotonini.univraule.models.Room;

/**
 * Adapter for a list of {@link Room}
 */
public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    /**
     * Initializes a new instance of this adapter
     */
    public RoomsAdapter(OnItemClickListener listener) {
        queryValue = "";
        this.listener = listener;
        rooms = new ArrayList<>();
    }

    /**
     * Update the list of {@link Room} shown
     * @param rooms list of {@link Room}
     */
    public void update(List<Room> rooms) {
        this.rooms = rooms;
        Collections.sort(this.rooms, ((o1, o2) -> o1.getName().compareTo(o2.getName())));
        for (Room room: rooms) {
            Collections.sort(room.getEvents(), ((e1, e2) -> (int)(e1.getStartTimestamp() - e2.getStartTimestamp())));
        }

        this.notifyDataSetChanged();
    }

    /**
     * Item click interface
     */
    public interface OnItemClickListener {
        void onItemClick(Room room);
    }

    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoomBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_room,
                parent, false
        );

        return new ViewHolder(binding.getRoot());
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(getRooms().get(position));
    }

    @Override public int getItemCount() {
        return getRooms().size();
    }

    /**
     * Gets the filter of this list
     * @return filter
     */
    public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence constraint) {
                queryValue = constraint.toString();
                return null;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    private List<Room> getRooms() {
        if (queryValue.isEmpty()) {
            return rooms;
        }

        List<Room> filteredRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getName().toLowerCase().contains(queryValue.toLowerCase())) {
                filteredRooms.add(room);
            }
        }

        return filteredRooms;
    }

    /**
     * View holder for this adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Initializes a new instance of this View holder
         * @param itemView view
         */
        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        /**
         * Sets the room to show
         * @param room room
         */
        public void set(Room room) {
            this.room = room;

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(room));
            binding.itemRoomText.setText(this.room.getName());
            binding.itemRoomOfficeText.setText(this.room.getOfficeName());

            Event nowEvent = getCurrentEvent(this.room);
            Event nextEvent = getNextEvent(this.room);

            if (nowEvent != null) {
                binding.itemRoomTimeDescriptionText.setText(R.string.item_room_available_from);
                binding.itemRoomEtaDescriptionText.setText(R.string.item_room_busy_for);
                binding.itemRoomTimeText.setText(DateToStringFormatter.getTimeString(getCurrentEndTimestamp(this.room, nowEvent)));
                binding.itemRoomEtaText.setText(DateToStringFormatter.getETAString(getCurrentEndTimestamp(this.room, nowEvent)));

                binding.itemRoomTopRelativelayout.setBackgroundResource(R.color.dark_red);
                binding.itemRoomBottomLinearlayout.setBackgroundResource(R.color.red);
            }
            else if (nextEvent != null) {
                binding.itemRoomTimeDescriptionText.setText(R.string.item_room_busy_from);
                binding.itemRoomEtaDescriptionText.setText(R.string.item_room_available_for);
                binding.itemRoomTimeText.setText(DateToStringFormatter.getTimeString(nextEvent.getStartTimestamp()));
                binding.itemRoomEtaText.setText(DateToStringFormatter.getETAString(nextEvent.getStartTimestamp()));

                binding.itemRoomTopRelativelayout.setBackgroundResource(R.color.dark_green);
                binding.itemRoomBottomLinearlayout.setBackgroundResource(R.color.green);
            }
            else {
                Logger.e(RoomsAdapter.class.getSimpleName(), "Both nextEvent and nowEvent are null.");
            }
        }

        private Room room;
        private ItemRoomBinding binding;
    }

    private long getCurrentEndTimestamp(Room room, Event from) {
        Event next = null;
        for (Event possibleNextEvent: room.getEvents()) {
            if (possibleNextEvent.getStartTimestamp() > from.getStartTimestamp()) {
                next = possibleNextEvent;
            }
        }

        if (next == null) {
            return from.getEndTimestamp();
        }

        Calendar nextCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        nextCalendar.setTimeInMillis(next.getStartTimestamp());
        Calendar fromCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        fromCalendar.setTimeInMillis(from.getEndTimestamp());

        if (nextCalendar.get(Calendar.HOUR_OF_DAY) != fromCalendar.get(Calendar.HOUR_OF_DAY) ||
                nextCalendar.get(Calendar.MINUTE) != fromCalendar.get(Calendar.MINUTE)) {
            return from.getEndTimestamp();
        }
        else {
            return getCurrentEndTimestamp(room, next);
        }
    }

    private Calendar getClosingCalendar() {
        Calendar closingCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        closingCalendar.set(Calendar.HOUR_OF_DAY, 19);
        closingCalendar.set(Calendar.MINUTE, 30);

        return closingCalendar;
    }

    private Calendar getOpeningCalendar() {
        Calendar openingCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        openingCalendar.set(Calendar.HOUR_OF_DAY, 7);
        openingCalendar.set(Calendar.MINUTE, 30);

        return openingCalendar;
    }

    private Event getNextEvent(Room room) {
        Date now = new Date();
        for (Event e : room.getEvents()) {
            Date startTime = new Date(e.getStartTimestamp());

            if (startTime.after(now)) {
                return e;
            }
        }

        if (now.before(getClosingCalendar().getTime())) {
            return getCloseEvent();
        }

        return null;
    }

    private Event getCurrentEvent(Room room) {
        Date now = new Date();

        // Are you using the app after the uni closed?
        if (now.after(getClosingCalendar().getTime())) {
            return getCloseEvent();
        }

        // Are you here before opening time?
        if (now.before(getOpeningCalendar().getTime())) {
            Event e = new Event();
            e.setStartTimestamp(now.getTime() / 1000L);
            e.setEndTimestamp(getOpeningCalendar().getTime().getTime() / 1000L);

            return e;
        }

        // Check for events
        for (Event e : room.getEvents()) {
            Date startTime = new Date(e.getStartTimestamp());
            Date endTime = new Date(e.getEndTimestamp());

            if (startTime.before(now) && endTime.after(now)) {
                return e;
            }
        }

        return null;
    }

    private Event getCloseEvent() {
        Event event = new Event();
        event.setName("Aula chiusa."); // TODO: replace

        Calendar closingCalendar = getClosingCalendar();
        Calendar openingCalendar = getOpeningCalendar();
        openingCalendar.set(Calendar.DAY_OF_MONTH, openingCalendar.get(Calendar.DAY_OF_MONTH) + 1);

        event.setStartTimestamp(closingCalendar.getTimeInMillis() / 1000L);
        event.setEndTimestamp(openingCalendar.getTimeInMillis() / 1000L);

        return event;
    }

    private OnItemClickListener listener;
    private String queryValue;
    private List<Room> rooms;
}
