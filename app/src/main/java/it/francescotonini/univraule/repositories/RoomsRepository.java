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

package it.francescotonini.univraule.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import java.util.List;

import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.models.Office;
import it.francescotonini.univraule.models.Room;
import it.francescotonini.univraule.views.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles communication between data and view model
 */
public class RoomsRepository extends BaseRepository {
    /**
     * Initializes a new instance of this class
     */
    public RoomsRepository(BaseActivity owner) {
        super(owner);
        rooms = new MutableLiveData<>();

        getDatabase().roomDao().getAll().observe(getOwner(), result -> {
            Logger.i(RoomsRepository.class.getSimpleName(), "# of rooms on db: " + result.size());
            rooms.setValue(result);
        });
    }

    /**
     * Gets a live list of {@link Room}
     * @return a live list of {@link Room}
     */
    public LiveData<List<Room>> getRooms() {
        loadRooms();
        return rooms;
    }

    /**
     * Deletes every {@link Room} from database
     */
    public void clearAll() {
        getExecutor().execute(() -> {
            Logger.i(RoomsRepository.class.getSimpleName(), "Deleting all rooms");
            getDatabase().roomDao().deleteAll();
        });
    }

    private LiveData<List<Office>> getPreferredOffices() {
        return getDatabase().officeDao().getAll();
    }

    private void loadRooms() {
        Logger.i(RoomsRepository.class.getSimpleName(), "Loading rooms");

        getPreferredOffices()
        .observe(getOwner(), offices -> {
            if (offices == null) {
                return;
            }

            for (Office office : offices) {
                Logger.i(RoomsRepository.class.getSimpleName(), "Loading rooms from office " + office.getName());

                getExecutor().execute(() -> getApi()
                .getRooms(office.getId())
                .enqueue(new Callback<List<Room>>() {
                    @Override public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                        for (Room room : response.body()) {
                            room.setOfficeName(office.getName());
                        }

                        getExecutor()
                        .execute(() -> {
                            getDatabase().roomDao().deleteByOffice(office.getName());
                            getDatabase().roomDao().insert(response.body());
                        });
                    }

                    @Override public void onFailure(Call<List<Room>> call, Throwable t) {
                        Logger.e(OfficesRepository.class.getSimpleName(), t.getMessage());

                        rooms.setValue(null);
                    }
                }));
            }
        });
    }

    private MutableLiveData<List<Room>> rooms;
}
