package ch.collen.docs.pubdoc.publisher;

import ch.collen.docs.pubdoc.config.ZasciiDocConfiguration;
import ch.collen.docs.pubdoc.utils.FilesUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

@Component
public class LocalPublisher implements Publisher {

    private final ZasciiDocConfiguration zasciiDocConfiguration;

    public LocalPublisher(ZasciiDocConfiguration zasciiDocConfiguration) {
        this.zasciiDocConfiguration = zasciiDocConfiguration;
    }

    // FIXME
    @Override
    public URL publish(Path path) {
        final Path docs = this.zasciiDocConfiguration.getOutputPath().resolve("docs");
        try {
        FilesUtils.copy(path, docs);
            return new URL("http://www.localhost/docs/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
