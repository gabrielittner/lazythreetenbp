package org.threeten.bp.zone;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

import android.support.annotation.RestrictTo;
import java.io.DataInputStream;
import java.io.IOException;

@RestrictTo(LIBRARY)
public class ZoneRulesCompat {

    public static ZoneRules readZoneRules(DataInputStream stream) throws IOException, ClassNotFoundException {
        return StandardZoneRules.readExternal(stream);
    }

    private ZoneRulesCompat() {
        throw new AssertionError("No instances");
    }
}
