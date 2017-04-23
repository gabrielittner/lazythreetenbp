package com.gabrielittner.threetenbp;

import static com.google.devtools.common.options.OptionsParser.HelpVerbosity.LONG;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.STATIC;

import com.google.devtools.common.options.OptionsParser;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.threeten.bp.zone.ZoneRules;
import org.threeten.bp.zone.ZoneRulesCompilerCompat;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public final class LazyZoneRulesCompiler {

    public static void main(String[] args) {
        OptionsParser parser = OptionsParser.newOptionsParser(CompilerOptions.class);
        parser.parseAndExitUponError(args);
        CompilerOptions options = parser.getOptions(CompilerOptions.class);
        if (options.version.isEmpty()) {
            required("version");
        }
        if (options.srcDir == null) {
            required("srcdir");
        }
        if (options.codeOutputDir == null) {
            required("codeoutputdir");
        }
        if (options.tzdbOutputDir == null) {
            required("tzdboutdir");
        }
        new LazyZoneRulesCompiler(options).run();
    }

    private static void required(String arg) {
        System.out.println(String.format("--%s is required", arg));
    }

    private static final TypeName STRING_SET = ParameterizedTypeName.get(Set.class, String.class);
    private static final String REGION_IDS = "REGION_IDS";

    private final CompilerOptions options;
    private final ZoneRulesCompilerCompat compiler;

    private LazyZoneRulesCompiler(CompilerOptions options) {
        this.options = options;
        this.compiler = createCompiler(options);
    }

    private void run() {
        try {
            compiler.compile();
            SortedMap<String, ZoneRules> zones = compiler.getZones();

            TypeSpec typeSpec = generateClass(options.version, zones);

            if (!Files.exists(options.codeOutputDir)) {
                Files.createDirectory(options.codeOutputDir);
            }
            JavaFile.builder("com.gabrielittner.threetenbp", typeSpec)
                    .build()
                    .writeTo(options.codeOutputDir);

            if (Files.exists(options.tzdbOutputDir)) {
                Files.walkFileTree(options.tzdbOutputDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                Files.delete(options.tzdbOutputDir);
            }
            Files.createDirectory(options.tzdbOutputDir);
            for (Map.Entry<String, ZoneRules> entry : zones.entrySet()) {
                Path out = options.tzdbOutputDir.resolve(zoneFileName(entry.getKey()));
                writeZoneRulesFile(out, entry.getValue());
            }
        } catch (Exception ex) {
            System.out.println("Failed: " + ex.toString());
            ex.printStackTrace();
        }
    }

    private ZoneRulesCompilerCompat createCompiler(CompilerOptions options) {
        List<File> srcFiles = new ArrayList<>();
        for (String srcFileName : options.srcFileNames) {
            File file = options.srcDir.resolve(srcFileName).toFile();
            if (file.exists()) {
                srcFiles.add(file);
            }
        }
        File leapSecondsFile = options.srcDir.resolve("leapseconds").toFile();
        if (!leapSecondsFile.exists()) {
            System.out.println("Version " + options.srcDir.toString() + " does not include leap seconds information.");
            leapSecondsFile = null;
        }
        return new ZoneRulesCompilerCompat(options.version, srcFiles, leapSecondsFile, options.verbose);
    }

    private TypeSpec generateClass(String version, Map<String, ZoneRules> allBuiltZones) {
        return TypeSpec.classBuilder("LazyZoneRules")
                .addField(version(version))
                .addField(STRING_SET, REGION_IDS, STATIC, FINAL)
                .addStaticBlock(regionIdSet(allBuiltZones.keySet()))
                .addMethod(provideFileName(allBuiltZones.keySet()))
                .build();
    }

    private FieldSpec version(String version) {
        return FieldSpec.builder(String.class, "VERSION", STATIC, FINAL).initializer("$S", version).build();
    }

    private CodeBlock regionIdSet(Set<String> allRegionIds) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("$T ids = new $T<>($L)", STRING_SET, LinkedHashSet.class, allRegionIds.size());
        for (String regionId : allRegionIds) {
            builder.addStatement("ids.add($S)", regionId);
        }
        return builder
                .addStatement("$L = $T.unmodifiableSet(ids)", REGION_IDS, Collections.class)
                .build();
    }

    private MethodSpec provideFileName(Set<String> allRegionIds) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .beginControlFlow("switch (zoneId)");
        for (String regionId : allRegionIds) {
            builder.addStatement("case $S: return $S", regionId, zoneFileName(regionId));
        }
        builder.addStatement("default: throw new $T($S + zoneId)", IllegalArgumentException.class, "Unknown zoneId: ")
            .endControlFlow();
        return MethodSpec.methodBuilder("provideFileName")
                .addModifiers(STATIC)
                .returns(String.class)
                .addParameter(String.class, "zoneId")
                .addCode(builder.build())
                .build();
    }

    private void writeZoneRulesFile(Path path, ZoneRules rules) throws IOException {
        Files.deleteIfExists(path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectory(path.getParent());
        }
        Files.createFile(path);
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(path))) {
            out.writeByte(1);
            out.writeUTF("TZDB-ZONE");
            compiler.writeZoneRules(rules, out);
        }
    }

    private String zoneFileName(String regionId) {
        return "tzdb/" + regionId.replaceAll("/", "-").toLowerCase() + ".dat";
    }
}
