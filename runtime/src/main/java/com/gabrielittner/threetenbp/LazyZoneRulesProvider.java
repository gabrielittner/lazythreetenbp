package com.gabrielittner.threetenbp;

import android.content.Context;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.zone.ZoneRules;
import org.threeten.bp.zone.ZoneRulesCompat;
import org.threeten.bp.zone.ZoneRulesException;
import org.threeten.bp.zone.ZoneRulesProvider;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

final class LazyZoneRulesProvider extends ZoneRulesProvider {

    private final Context context;

    private final NavigableMap<String, ZoneRules> map = new ConcurrentSkipListMap<>();

    LazyZoneRulesProvider(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Set<String> provideZoneIds() {
        return LazyZoneRules.REGION_IDS;
    }

    @Override
    protected ZoneRules provideRules(String zoneId, boolean forCaching) {
        Jdk8Methods.requireNonNull(zoneId, "zoneId");
        String fileName = LazyZoneRules.provideFileName(zoneId);
        ZoneRules rules = map.get(fileName);
        if (rules == null) {
            rules = loadData(fileName);
            map.put(fileName, rules);
        }
        return rules;
    }

    @Override
    protected NavigableMap<String, ZoneRules> provideVersions(String zoneId) {
        String versionId = LazyZoneRules.VERSION;
        ZoneRules rules = provideRules(zoneId, false);
        return new TreeMap<>(Collections.singletonMap(versionId, rules));
    }

    private ZoneRules loadData(String fileName) {
        try (InputStream is = context.getAssets().open(fileName)) {
            return loadData(is);
        } catch (Exception ex) {
            throw new ZoneRulesException("Invalid binary time-zone data: " + fileName, ex);
        }
    }

    private ZoneRules loadData(InputStream in) throws Exception {
        DataInputStream dis = new DataInputStream(in);
        if (dis.readByte() != 1) {
            throw new StreamCorruptedException("File format not recognised");
        }

        String groupId = dis.readUTF();
        if (!"TZDB-ZONE".equals(groupId)) {
            throw new StreamCorruptedException("File format not recognised");
        }
        return ZoneRulesCompat.readZoneRules(dis);
    }
}
