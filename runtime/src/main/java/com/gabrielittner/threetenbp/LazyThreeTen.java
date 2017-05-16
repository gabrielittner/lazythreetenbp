package com.gabrielittner.threetenbp;

import android.app.Application;
import android.content.Context;

import org.threeten.bp.zone.ZoneRulesInitializer;
import org.threeten.bp.zone.ZoneRulesProvider;

import java.util.concurrent.atomic.AtomicBoolean;

public final class LazyThreeTen {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    public static void init(Context context) {
        if (INITIALIZED.getAndSet(true)) {
            return;
        }
        ZoneRulesInitializer.setInitializer(
                new LazyZoneRulesInitializer((Application) context.getApplicationContext()));
    }

    /**
     * Call on background thread to eagerly load all zones.
     */
    public static void cacheZones() {
        for (String zoneId : ZoneRulesProvider.getAvailableZoneIds()) {
            ZoneRulesProvider.getRules(zoneId, true);
        }
    }

    private LazyThreeTen() {
        throw new AssertionError("No instances.");
    }
}
