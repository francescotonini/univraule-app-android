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

package it.francescotonini.univraule.views;

import android.content.ContextWrapper;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.pixplicity.easyprefs.library.Prefs;
import it.francescotonini.univraule.BuildConfig;
import it.francescotonini.univraule.Logger;
import it.francescotonini.univraule.viewmodels.BaseViewModel;

/**
 * Standard Activity skeleton
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Gets the layout id
     * @return The layout id
     */
    protected abstract int getLayoutId();

    /**
     * Sets the toolbar
     */
    protected abstract void setToolbar();

    /**
     * Gets the view model
     * @return view model
     */
    protected abstract BaseViewModel getViewModel();

    /**
     * Sets the UI binding
     */
    protected abstract void setBinding();

    /**
     * Gets the current activity
     * @return current activity
     */
    public BaseActivity getCurrentActivity() {
        return this;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Builds preferences
        // TODO: code is executed with every activity. Find a better place ASAP
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(BuildConfig.APPLICATION_ID)
                .setUseDefaultSharedPreference(true)
                .build();

        // Why? An Activity may not have a layout. If that is the case, layoutId is zero
        if (getLayoutId() == 0) {
            Logger.w(BaseActivity.class.getSimpleName(), "Layout id is zero");

            return;
        }

        setBinding();
        setToolbar();
    }
}
