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

package it.francescotonini.univraule.views;

import android.content.Intent;
import android.os.Bundle;
import com.pixplicity.easyprefs.library.Prefs;

import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.viewmodels.BaseViewModel;

public class BootstrapActivity extends BaseActivity {
    @Override protected int getLayoutId() {
        return 0;
    }

    @Override protected void setToolbar() { }

    @Override protected BaseViewModel getViewModel() { return null; }

    @Override protected void setBinding() { }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isFirstStart = Prefs.getBoolean("isFirstBoot", true);
        Logger.v(BootstrapActivity.class.getSimpleName(), "isFirstBoot is " + isFirstStart);
        if (isFirstStart) {
            startActivity(new Intent(this, OfficesActivity.class));
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
