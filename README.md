# ThreeTenBp Lazy

A lazy loading ZoneRuleProvider for ThreeTenBp.

## TODO

- benchmarks
- replace generated zoneId -> fileName switch with string maniputation
- search for alternative to using package-private `StandardZoneRules`
- documentation

## Update tzdb data

1. Download latest tzdb https://www.iana.org/time-zones
2. unpack the files
3. Run `./gradlew localRun -Psrcdir=/path/to/tzdb/files -Ptzdbversion=VERSION`

## Download

Add a Gradle dependency:

```groovy
compile 'com.gabrielittner.threetenbp:runtime:1.0.0'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

## License

```
Copyright 2017 Gabriel Ittner.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
