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

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import it.francescotonini.univraule.App;
import it.francescotonini.univraule.Constants;
import it.francescotonini.univraule.api.UniVRApi;
import it.francescotonini.univraule.database.UniVRDatabase;
import it.francescotonini.univraule.views.BaseActivity;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Gets the base repository
 */
public class BaseRepository {
    /**
     * Initializes a new instance of this class
     */
    public BaseRepository(BaseActivity owner) {
        this.owner = owner;

        // Init db
        database = Room.databaseBuilder(App.getContext(),
                UniVRDatabase.class,
                "univrauledb")
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("DELETE FROM room");
                    }
                })
                .build();

        // Init api
        api = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UniVRApi.class);
    }

    /**
     * Gets an instance of the api
     * @return the api
     */
    public UniVRApi getApi() {
        return api;
    }

    /**
     * Gets an instance of the database
     * @return the database
     */
    public UniVRDatabase getDatabase() {
        return database;
    }

    /**
     * Gets an executor
     * @return and executor
     */
    public Executor getExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    /**
     * Gets the owner of this repository
     * @return
     */
    public BaseActivity getOwner() {
        return owner;
    }

    private UniVRApi api;
    private UniVRDatabase database;
    private BaseActivity owner;
}
