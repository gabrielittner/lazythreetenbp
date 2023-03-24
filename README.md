# LazyThreeTenBp

A lazy loading ZoneRuleProvider for ThreeTenBp.

## Usage

You have to initialize LazyThreeTenBp as early as possible, before your code accesses any threetenbp
class. Usually the best place is in your `Application.onCreate()` method:

```java
@Override
public void onCreate() {
    super.onCreate();
    LazyThreeTen.init(this);
}
```

Afterwards you can call `LazyThreeTen.cacheZones()` on a background thread to cache the timezone
information without blocking the startup of your app. If you decide not to do that the individual
timezones will be loaded on demand when they are accessed for the first time.

## Download

Add a Gradle dependency:

```groovy
implementation "org.threeten:threetenbp:1.6.7:no-tzdb"
implementation "com.gabrielittner.threetenbp:lazythreetenbp:0.15.0"
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

## Changes

Compiler
- generate java code for list of all timezone ids
- generate a separate .dat file for each zone
- only support one timezone data version at a time (makes some things easier)

Runtime
- custom `ZoneRulesProvider`
- provides generated timezone id list
- only reads timezone from assets/disk when that timezone was requested

## Update tzdb data

1. Check for the latest tzdb version at https://www.iana.org/time-zones
2. Update `tzdbVersion` in [runtime/gradle.properties](runtime/gradle.properties)
3. Run `./gradlew generateLazyZoneRules

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
