package org.threeten.bp.zone;

import java.io.DataInputStream;
import java.io.IOException;

public class ZoneRulesCompat {

    public static ZoneRules readZoneRules(DataInputStream stream) throws IOException, ClassNotFoundException {
        return StandardZoneRules.readExternal(stream);
    }

    private ZoneRulesCompat() {
        throw new AssertionError("No instances");
    }
}
