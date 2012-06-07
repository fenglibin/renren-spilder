package org.zirco;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

/*
 * Zirco Browser for Android Copyright (C) 2010 - 2011 J. Devauchelle and contributors. This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
/**
 * via http://code.google.com/p/acra
 */
@ReportsCrashes(formKey = "dElYbUNBc0hvSHN2UEhGc0VSMnREakE6MQ")
public class MyACRAApplication extends Application {

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
}
