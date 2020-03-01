package ch.collen.docs.pubdoc.processing;

import ch.collen.docs.pubdoc.utils.FilesUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.asciidoctor.extension.RubyExtensionRegistry;
import org.asciidoctor.jruby.AsciiDocDirectoryWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;


@Component
public class AsciiDocProcessor implements TextProcessor {

    private Asciidoctor asciidoctor = Asciidoctor.Factory.create();

    private static final Logger LOGGER = LoggerFactory.getLogger(AsciiDocProcessor.class);

    public AsciiDocProcessor() {
        RubyExtensionRegistry rubyExtensionRegistry = asciidoctor
                .rubyExtensionRegistry();
        rubyExtensionRegistry
                .loadClass(
                        this.getClass()
                                .getResourceAsStream(
                                        "/com/github/domgold/doctools/asciidoctor/gherkin/gherkinblockmacro.rb"))
                .blockMacro("gherkin", "GherkinBlockMacroProcessor");
    }

    @Override
    public Path convertProject(Path sourceDir) {
        try {
            final var asciidocPath = sourceDir.resolve("src/asciidoc");
            final var asciidoc_conversion = Files.createTempDirectory("asciidoc_conversion");
            LOGGER.debug("Temp conversion file under {}", asciidoc_conversion);

            FilesUtils.copy(asciidocPath, asciidoc_conversion);
            final var directoryWalker = new AsciiDocDirectoryWalker(asciidoc_conversion.toAbsolutePath().toString());

            final var options = new Options();
            options.setBackend("html5");
            options.setSafe(SafeMode.UNSAFE);
            asciidoctor.convertDirectory(directoryWalker, options);
            cleanUp(asciidoc_conversion);
            return asciidoc_conversion;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void cleanUp(Path path) {
        recuriveDelete(path, p -> p.getFileName().toString().endsWith("adoc"));
    }

    private Path recuriveDelete(Path baseDir, Predicate<Path> removalPredicate) {
        try {
            Files.list(baseDir)
                    .map(p -> Files.isDirectory(p) ? recuriveDelete(p, removalPredicate) : p)
                    .filter(removalPredicate)
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baseDir;
    }
}
