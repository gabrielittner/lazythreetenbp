package org.threeten.bp.zone;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

import androidx.annotation.RestrictTo;
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
