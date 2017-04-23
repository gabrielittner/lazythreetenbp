package com.gabrielittner.threetenbp;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import java.nio.file.Path;
import java.util.List;

public final class CompilerOptions extends OptionsBase {

    @Option(
            name = "version",
            help = "Version of the time zone data, e.g. 2017b.",
            defaultValue = ""
    )
    public String version;

    @Option(
            name = "srcdir",
            help = "Directory containing the unpacked tzdb files-",
            defaultValue = "",
            converter = PathConverter.class
    )
    public Path srcDir;

    @Option(
            name = "srcfiles",
            help = "Names of the tzdb files to process-",
            defaultValue = "africa,antarctica,asia,australasia,backward,etcetera,europe,northamerica,southamerica",
            converter = StringListConverter.class
    )
    public List<String> srcFileNames;

    @Option(
            name = "codeoutdir",
            help = "Output directory for the generated java code.",
            defaultValue = "",
            converter = PathConverter.class
    )
    public Path codeOutputDir;

    @Option(
            name = "tzdboutdir",
            help = "Output directory for the generated tzdb files.",
            defaultValue = "",
            converter = PathConverter.class
    )
    public Path tzdbOutputDir;

    @Option(
            name = "verbose",
            help = "Verbose output.",
            defaultValue = "false"
    )
    public boolean verbose;
}
