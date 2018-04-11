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
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Represents an office (every office has one or more rooms)
 */
@Entity
public class Office implements Comparable<Office> {
    /**
     * Gets the name of the office
     * @return gets name of the office
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the office
     * @return the id of the office
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the id
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override public int compareTo(@NonNull Office o) {
        return id.compareTo(o.id);
    }

    @ColumnInfo(name = "name")
    private String name;
    @PrimaryKey @NonNull @ColumnInfo(name = "id")
    private String id;
}
