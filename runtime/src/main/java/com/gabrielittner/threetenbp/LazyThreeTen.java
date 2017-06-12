package com.gabrielittner.threetenbp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import org.threeten.bp.format.DateTimeTextProvider;
import org.threeten.bp.zone.ZoneRulesInitializer;
import org.threeten.bp.zone.ZoneRulesProvider;
import java.util.concurrent.atomic.AtomicBoolean;

public final class LazyThreeTen {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    /**
     * Initialize threetenbp to use LazyThreeTenBp's ZoneRulesProvider
     */
    @MainThread
    public static void init(Context context) {
        if (INITIALIZED.getAndSet(true)) {
            return;
        }

        DateTimeTextProvider.setInitializer(new AndroidDateTimeTextProvider());
        ZoneRulesInitializer.setInitializer(
                new LazyZoneRulesInitializer((Application) context.getApplicationContext()));
    }

    /**
     * Call on background thread to eagerly load all zones.
     */
    @WorkerThread
    public static void cacheZones() {
        for (String zoneId : ZoneRulesProvider.getAvailableZoneIds()) {
            ZoneRulesProvider.getRules(zoneId, true);
        }
    }

    private LazyThreeTen() {
        throw new AssertionError("No instances.");
    }
}
