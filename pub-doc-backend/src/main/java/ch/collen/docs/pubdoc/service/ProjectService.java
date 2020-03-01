package ch.collen.docs.pubdoc.service;

import ch.collen.docs.pubdoc.Project;
import ch.collen.docs.pubdoc.config.ZasciiDocConfiguration;
import ch.collen.docs.pubdoc.data.ProjectInfoRepository;
import ch.collen.docs.pubdoc.processing.TextProcessor;
import ch.collen.docs.pubdoc.publisher.LocalPublisher;
import ch.collen.docs.pubdoc.scm.RemoteRepository;
import ch.collen.docs.pubdoc.utils.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectInfoRepository projectInfoRepository;
    private final ZasciiDocConfiguration zasciiDocConfiguration;
    private final RemoteRepository remoteRepository;
    private final TextProcessor textProcessor;
    private final LocalPublisher localPublisher;

    public ProjectService(ProjectInfoRepository projectInfoRepository,
            ZasciiDocConfiguration zasciiDocConfiguration,
            RemoteRepository remoteRepository,
            TextProcessor textProcessor, LocalPublisher localPublisher) {
        this.projectInfoRepository = projectInfoRepository;
        this.zasciiDocConfiguration = zasciiDocConfiguration;
        this.remoteRepository = remoteRepository;
        this.textProcessor = textProcessor;
        this.localPublisher = localPublisher;
    }


    public String initProject(Project project) {
        this.cloneProject(project);
        return project.getProjectName();
    }

    public Mono<Integer> synchronise(Project project) {
        return Mono.just(project)
                .map(this::generateDocumentation)
                .map(paths -> paths.stream().map(this.localPublisher::publish).collect(Collectors.toList()))
                .map(List::size);
    }

//    public Mono<Integer> refreshProject(Project project) {
//        return Mono.just(project)
//                .map(this::cloneProject)
//                .map(this::synchronizedBranches)
//                .map(this::generateDocumentation)
//                .map(paths -> paths.stream().map(this.localPublisher::publish).collect(Collectors.toList()))
//                .map(List::size);
//    }

    private Project cloneProject(Project project) {
        try {
            final var repoPaths = this.zasciiDocConfiguration.getRepoPath();

            final var baseProjectDirectory = repoPaths.resolve(project.getProjectName());
            if (!remoteRepository.repoExist(baseProjectDirectory, project.getGitRepository())) {
                Files.createDirectories(baseProjectDirectory);
                remoteRepository.cloneRepo(project.getGitRepository(), baseProjectDirectory);
            } else {
                LOGGER.warn("target repository already exist {}", baseProjectDirectory);
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        return project;
    }

    private ByBranchProject synchronizedBranches(Project project) {
        final var baseDirProject = this.zasciiDocConfiguration.getOutputPath()
                .resolve(project.getProjectName());
        if (!Files.isDirectory(baseDirProject)) {
            try {
                Files.createDirectories(baseDirProject);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        final Path repositoryWorkspace = this.zasciiDocConfiguration.getRepoPath()
                .resolve(project.getProjectName());
        final var allBranches = remoteRepository.findAllBranches(repositoryWorkspace);
        cleanUpProjectDirectory(baseDirProject, allBranches);
        allBranches.forEach(branch -> {
            remoteRepository.checkoutBranch(repositoryWorkspace, branch);
            FilesUtils.copy(repositoryWorkspace, this.zasciiDocConfiguration.getOutputPath().resolve(branch));
        });
        return new ByBranchProject(allBranches, project);

    }

    private void cleanUpProjectDirectory(Path baseDirectory, Set<String> branches) {
        try {
            final var output = Files.list(baseDirectory);
            output.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
//            branches.forEach(b -> {
//                try {
//                    Files.createDirectories(baseDirectory.resolve(b));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private List<Path> generateDocumentation(Project project) {
        final Set<String> allBranches = this.remoteRepository.findAllBranches(zasciiDocConfiguration.getRepoPath().resolve(project.getProjectName()));

        return allBranches.stream()
                .map(b -> generateDocsForBranches(b, project))
                .collect(Collectors.toList());
      }

    private Path generateDocsForBranches(String branch, Project project) {
        final Path workspace = zasciiDocConfiguration.getRepoPath().resolve(project.getProjectName());
        remoteRepository.checkoutBranch(workspace, branch);
        final Path documentationPaths = workspace.resolve(project.getPathToDocumentation());
        final Path path = textProcessor.convertProject(documentationPaths);
        LOGGER.info("Documentation generated under {}", path);
        return path;
    }

}
