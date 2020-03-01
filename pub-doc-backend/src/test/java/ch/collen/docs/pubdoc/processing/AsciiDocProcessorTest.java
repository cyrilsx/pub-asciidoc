package ch.collen.docs.pubdoc.processing;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


public class AsciiDocProcessorTest {

    private AsciiDocProcessor asciiDocProcessor = new AsciiDocProcessor();

    @Test
    void testConvert() {
        Path path = asciiDocProcessor.convertProject(Paths.get("src/test/resources/adoc"));

        assertThat(path.resolve("test.html"))
                .hasSameContentAs(Paths.get("src/test/resources/expected/asciidoc/test.html"));
    }

}