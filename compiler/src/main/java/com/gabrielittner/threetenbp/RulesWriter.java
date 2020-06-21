package com.gabrielittner.threetenbp;

import java.io.IOException;
import java.util.Set;

public interface RulesWriter {
  void writeZoneIds(String version, Set<String> zoneIds) throws IOException;
}