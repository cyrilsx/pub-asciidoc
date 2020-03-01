package ch.collen.docs.pubdoc.scm;

import java.nio.file.Path;
import java.util.Set;

public interface RemoteRepository {

    void cloneRepo(String url, Path outputDir);

    void updateRepo(Path repositoryWorkspace, String ref);

    void updateRepoCurrentRef(Path repositoryWorkspace);

    Set<String> findAllBranches(Path repositoryWorkspace);

    void createBranchFromDevelop(Path repositoryWorkspace, String branchName);

    Path checkoutBranch(Path repositoryWorkspace, String branchName);

    String currentBranch(Path repositoryWorkspace);

    boolean repoExist(Path repositoryWorkspace, String url);

}
