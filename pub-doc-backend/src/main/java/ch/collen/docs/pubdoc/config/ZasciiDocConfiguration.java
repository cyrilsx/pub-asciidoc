package ch.collen.docs.pubdoc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties(prefix = "zasdoc")
public class ZasciiDocConfiguration {

    private String outputPath = "/var/www/";
    private String repoPath = "build/";

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Path getOutputPath() {
        return Paths.get(outputPath);
    }

    public Path getRepoPath() {
        return Paths.get(repoPath);
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

}
