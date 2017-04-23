package org.threeten.bp.zone;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public final class ZoneRulesCompilerCompat {

    private final TzdbZoneRulesCompiler compiler;

    public ZoneRulesCompilerCompat(String version, List<File> sourceFiles, File leapSecondsFile, boolean verbose) {
        this.compiler = new TzdbZoneRulesCompiler(version, sourceFiles, leapSecondsFile, verbose);
        compiler.setDeduplicateMap(new HashMap<>());
    }

    public void compile() throws Exception {
        compiler.compile();
    }

    public SortedMap<String, ZoneRules> getZones() {
        return compiler.getZones();
    }

    public void writeZoneRules(ZoneRules rules, DataOutputStream stream) throws IOException {
        ((StandardZoneRules) rules).writeExternal(stream);
    }
}
