package ch.collen.docs.pubdoc.processing;

import java.nio.file.Path;

public interface TextProcessor {

    Path convertProject(Path sourceDir);

}
