package com.gabrielittner.threetenbp;

import android.app.Application;
import android.content.Context;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import org.threeten.bp.ZoneId;
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
        ZoneRulesInitializer.setInitializer(
                new LazyZoneRulesInitializer((Application) context.getApplicationContext()));
    }

    /**
     * Call on background thread to eagerly load all zones. Starts with loading
     * {@link ZoneId#systemDefault()} which is the one most likely to be used.
     */
    @WorkerThread
    public static void cacheZones() {
        ZoneId.systemDefault().getRules();
        for (String zoneId : ZoneRulesProvider.getAvailableZoneIds()) {
            ZoneRulesProvider.getRules(zoneId, true);
        }
    }

    private LazyThreeTen() {
        throw new AssertionError("No instances.");
    }
}
