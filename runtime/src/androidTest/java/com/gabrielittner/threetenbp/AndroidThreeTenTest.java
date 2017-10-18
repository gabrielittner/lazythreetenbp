package com.gabrielittner.threetenbp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.zone.ZoneRulesProvider;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class AndroidThreeTenTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void init() {
        LazyThreeTen.init(context);
        assertThat(ZoneRulesProvider.getAvailableZoneIds()).isNotEmpty();
    }

    @Test
    public void cache() {
        LazyThreeTen.init(context);
        LazyThreeTen.cacheZones();
    }
}
