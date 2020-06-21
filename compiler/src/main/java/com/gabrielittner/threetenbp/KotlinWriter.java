package com.gabrielittner.threetenbp;

import com.squareup.kotlinpoet.CodeBlock;
import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.ParameterizedTypeName;
import com.squareup.kotlinpoet.PropertySpec;
import com.squareup.kotlinpoet.TypeName;
import com.squareup.kotlinpoet.TypeSpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

final class KotlinWriter implements RulesWriter {

    private final Path outputDir;

    KotlinWriter(Path outputDir) {
         this.outputDir = outputDir;
    }

    @Override
    public void writeZoneIds(String version, Set<String> zoneIds) throws IOException {
        TypeSpec typeSpec = TypeSpec.objectBuilder("LazyZoneRules")
                .addProperty(version(version))
                .addProperty(regionId(zoneIds))
                .build();

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
        FileSpec.get("com.gabrielittner.threetenbp", typeSpec)
                .writeTo(outputDir);
    }

    private PropertySpec version(String version) {
        return PropertySpec.builder("VERSION", String.class)
                .initializer("%S", version)
                .build();
    }

    private PropertySpec regionId(Set<String> allRegionIds) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("arrayOf(\n$>$>");
        Iterator<String> iterator = allRegionIds.iterator();
        while (iterator.hasNext()) {
            builder.add("%S", iterator.next());
            if (iterator.hasNext()) {
                builder.add(",\n");
            }
        }
        builder.add("%<$<)");
        TypeName listType = ParameterizedTypeName.get(List.class, String.class);
        return PropertySpec.builder("REGION_IDS", listType)
                .initializer(builder.build())
                .build();
    }
}
