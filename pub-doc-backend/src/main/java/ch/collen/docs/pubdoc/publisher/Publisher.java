package ch.collen.docs.pubdoc.publisher;

import java.net.URL;
import java.nio.file.Path;

public interface Publisher {

    URL publish(Path path);
}
