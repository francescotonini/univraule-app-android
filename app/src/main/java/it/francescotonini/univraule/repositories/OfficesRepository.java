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

package it.francescotonini.univraule.repositories;

import androidx.lifecycle.MutableLiveData;
import java.util.List;

import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.models.Office;
import it.francescotonini.univraule.views.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles communication between data and view model
 */
public class OfficesRepository extends BaseRepository {
    public OfficesRepository(BaseActivity owner) {
        super(owner);

        offices = new MutableLiveData<>();
    }

    /**
     * Gets a live list of {@link Office}
     * @return a live list of {@link Office}
     */
    public MutableLiveData<List<Office>> getOffices() {
        loadOffices();
        return offices;
    }

    /**
     * Saves a list of {@link Office} to show on the main page
     * @param offices a list of {@link Office}
     */
    public void setPrefferedOffices(List<Office> offices) {
        getExecutor().execute(() -> {
            getDatabase().officeDao().deleteAll();
            getDatabase().roomDao().deleteAll();
            getDatabase().officeDao().insert(offices);
        });
    }

    private void loadOffices() {
        getExecutor().execute(() -> {
            getApi().getOffices().enqueue(new Callback<List<Office>>() {
                @Override public void onResponse(Call<List<Office>> call, Response<List<Office>> response) {
                    offices.setValue(response.body());
                }

                @Override public void onFailure(Call<List<Office>> call, Throwable t) {
                    Logger.e(OfficesRepository.class.getSimpleName(), t.getMessage());

                    offices.setValue(null);
                }
            });
        });
    }

    private MutableLiveData<List<Office>> offices;
}
