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

package it.francescotonini.univraule.helpers;

import java.util.Calendar;

public class DateTimeInterpreter implements com.alamkanak.weekview.DateTimeInterpreter {
    @Override public String interpretDate(Calendar date) {
        String dayOfWeek = "";
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "DOM";
                break;
            case 2:
                dayOfWeek = "LUN";
                break;
            case 3:
                dayOfWeek = "MAR";
                break;
            case 4:
                dayOfWeek = "MER";
                break;
            case 5:
                dayOfWeek = "GIO";
                break;
            case 6:
                dayOfWeek = "VEN";
                break;
            case 7:
                dayOfWeek = "SAB";
                break;
        }

        return String.format("%s %s/%s", dayOfWeek.toUpperCase(), date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1);
    }

    @Override public String interpretTime(int hour) {
        return String.format("%s:00", hour);
    }
}