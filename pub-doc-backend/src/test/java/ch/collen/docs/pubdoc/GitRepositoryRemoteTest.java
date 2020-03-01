package ch.collen.docs.pubdoc;

import ch.collen.docs.pubdoc.scm.GitRepositoryRemote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GitRepositoryRemoteTest {

    private static final Path OUTPUT_DIR = Paths.get("build/tmp/test");

    private GitRepositoryRemote gitRepository = new GitRepositoryRemote();

    @BeforeEach
    void beforeEach() throws IOException {
        if (Files.exists(OUTPUT_DIR)) {
            deleteDirectory(OUTPUT_DIR.toFile());
        }
        Files.createDirectory(OUTPUT_DIR);
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @Test
    void cloneRepo() {
        final Path outputDir = OUTPUT_DIR.resolve("repo-test-git");
        gitRepository.cloneRepo("http://intra.zas.admin.ch/bitbucket/scm/~coc/repo-test-git.git", outputDir);
        assertThat(OUTPUT_DIR).exists();
        assertThat(outputDir).exists();
        assertThat(outputDir.resolve("README.md")).exists();
    }

    @Test
    void findAllBranches() {
        final Path outputDir = OUTPUT_DIR.resolve("repo-test-git");
        gitRepository.cloneRepo("http://intra.zas.admin.ch/bitbucket/scm/~coc/repo-test-git.git", outputDir);
        final Set<String> allBranches = gitRepository.findAllBranches(outputDir);

        assertThat(allBranches).contains("refs/remotes/origin/develop",
                "refs/remotes/origin/master");
    }

    @Test
    void repoExist() {
        final Path outputDir = OUTPUT_DIR.resolve("repo-test-git");
        gitRepository.cloneRepo("http://intra.zas.admin.ch/bitbucket/scm/~coc/repo-test-git.git", outputDir);
        assertThat(gitRepository.repoExist(outputDir, "http://intra.zas.admin.ch/bitbucket/scm/~coc/repo-test-git.git")).isTrue();
    }

    @Test
    void checkoutBranch() {
        final Path outputDir = OUTPUT_DIR.resolve("repo-test-git");
        gitRepository.cloneRepo("http://intra.zas.admin.ch/bitbucket/scm/~coc/repo-test-git.git", outputDir);
        gitRepository.checkoutBranch(outputDir, "refs/remotes/origin/develop");
        // assertThat(gitRepository.currentBranch(outputDir)).isEqualTo("refs/remotes/origin/develop");
    }


}
