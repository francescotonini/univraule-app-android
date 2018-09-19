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

package it.francescotonini.univraule.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import java.util.List;

import it.francescotonini.univraule.models.Event;
import it.francescotonini.univraule.models.Room;
import it.francescotonini.univraule.repositories.RoomsRepository;
import it.francescotonini.univraule.views.BaseActivity;

public class RoomViewModel extends BaseViewModel {
    /**
     * Initializes a new instance of this class
     *
     * @param owner activity where this view model has been declared
     */
    public RoomViewModel(BaseActivity owner) {
        super(owner);

        events = new MutableLiveData<>();
        repository = new RoomsRepository(owner);
    }

    /**
     * Gets a mutable list of {@link it.francescotonini.univraule.models.Event}
     * @param roomName room name
     * @param officeName office name
     * @return a mutable list of {@link it.francescotonini.univraule.models.Event}
     */
    public MutableLiveData<List<Event>> getEvents(String roomName, String officeName) {
        loadEvents(roomName, officeName);
        return events;
    }

    private void loadEvents(String roomName, String officeName) {
        repository.getRoom(roomName, officeName).observe(getOwner(), room -> {
            if (room == null) {
                events.setValue(null);
                return;
            }

            events.setValue(room.getEvents());
        });
    }

    private RoomsRepository repository;
    private MutableLiveData<List<Event>> events;
}
