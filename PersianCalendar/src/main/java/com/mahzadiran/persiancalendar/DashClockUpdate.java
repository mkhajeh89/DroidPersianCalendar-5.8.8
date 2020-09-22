package com.mahzadiran.persiancalendar;

import com.mahzadiran.persiancalendar.util.UpdateUtils;
import com.google.android.apps.dashclock.api.DashClockExtension;

public class DashClockUpdate extends DashClockExtension {

    @Override
    protected void onUpdateData(int reason) {
        setUpdateWhenScreenOn(true);
        UpdateUtils.update(getApplicationContext(), false);
        publishUpdate(UpdateUtils.getExtensionData());
    }

}
