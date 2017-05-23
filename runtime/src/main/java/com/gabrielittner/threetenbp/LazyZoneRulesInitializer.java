package com.gabrielittner.threetenbp;

import android.app.Application;
import org.threeten.bp.zone.ZoneRulesInitializer;
import org.threeten.bp.zone.ZoneRulesProvider;

final class LazyZoneRulesInitializer extends ZoneRulesInitializer {

    private final Application application;

    LazyZoneRulesInitializer(final Application application) {
        this.application = application;
    }

    @Override
    protected void initializeProviders() {
        ZoneRulesProvider.registerProvider(new LazyZoneRulesProvider(application));
    }
}
