package ch.collen.docs.pubdoc.scm;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GitRepositoryRemote implements RemoteRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepositoryRemote.class);

    @Override
    public void cloneRepo(String url, Path outputDir) {
        LOGGER.debug("[GIT] Cloning repository {} into {}", url, outputDir);
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(outputDir.toFile())
                    .call();
        } catch (GitAPIException e) {
            LOGGER.error("[GIT] Can't clone " + url, e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void updateRepo(Path repoWorkspace, String ref) {
        LOGGER.debug("[GIT] Updating workspace {} using ref {}", repoWorkspace, ref);
        try {
            Repository existingRepo = getRepository(repoWorkspace);
            existingRepo.updateRef(ref);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void updateRepoCurrentRef(Path repositoryWorkspace) {
        try {
            Repository existingRepo = getRepository(repositoryWorkspace);

            existingRepo.updateRef(existingRepo.getBranch());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Repository getRepository(Path repositoryWorkspace) {
        try {
            return new FileRepositoryBuilder()
                    .setGitDir(repositoryWorkspace.resolve(".git").toFile())
                    .build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Set<String> findAllBranches(Path repositoryWorkspace) {
        LOGGER.debug("[GIT] Get all branches from workspace {}", repositoryWorkspace);
        try {
            Repository repo = getRepository(repositoryWorkspace);
            return new Git(repo).branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call()
                    .stream().map(Ref::getName).collect(Collectors.toSet());
        } catch (GitAPIException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void createBranchFromDevelop(Path repositoryWorkspace, String branchName) {
        try {
            Repository repo = getRepository(repositoryWorkspace);
            // Get a reference
            Ref develop = repo.getRefDatabase().getRef("develop");

            if (Objects.isNull(develop)) {
                throw new IllegalStateException("devcelop doesnt exist");
            }

            // Get the object the reference points to
            ObjectId developTip = develop.getObjectId();

            // Create a branch
            RefUpdate newBranch = repo.updateRef("refs/heads/" + branchName);
            newBranch.setNewObjectId(developTip);
            newBranch.update();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Path checkoutBranch(Path repositoryWorkspace, String branchName) {
        try {
            final Git git = Git.open(repositoryWorkspace.toFile());
            git.checkout().setName(branchName).call();
            return repositoryWorkspace;
        } catch (IOException | GitAPIException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String currentBranch(Path repositoryWorkspace) {
        Repository repo = getRepository(repositoryWorkspace);
        try {
            final String branch = repo.getBranch();
            return new Git(repo).branchList().call().stream().filter(r -> r.getTarget().getName().equals(branch)).findAny().get().getName();
        } catch (GitAPIException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean repoExist(Path repositoryWorkspace, String url) {
        if (Files.exists(repositoryWorkspace.resolve(".git"))) {
            final var repository = getRepository(repositoryWorkspace);
            return repository.getConfig().getString("remote", "origin", "url").equalsIgnoreCase(url);
        }
        return false;
    }
}
