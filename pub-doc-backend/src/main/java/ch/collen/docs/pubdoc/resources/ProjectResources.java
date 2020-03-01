package ch.collen.docs.pubdoc.resources;

import ch.collen.docs.pubdoc.Project;
import ch.collen.docs.pubdoc.data.ProjectInfoRepository;
import ch.collen.docs.pubdoc.service.ProjectService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "api/project", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin(origins = "http://localhost:4300")
public class ProjectResources {

    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectService projectService;

    public ProjectResources(ProjectInfoRepository projectInfoRepository, ProjectService projectService) {
        this.projectInfoRepository = projectInfoRepository;
        this.projectService = projectService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<String> createOrUpdate(@RequestBody Project project) {
        return projectInfoRepository.findById(project.getProjectName()).defaultIfEmpty(project)
                .flatMap(projectInfoRepository::save)
                .map(projectService::initProject);
    }

    @PostMapping(path = "sync")
    public Mono<Integer> forceSync(@RequestBody Project project) {
        return projectInfoRepository.findById(project.getProjectName())
                .flatMap(projectService::synchronise);
    }

    @GetMapping
    public Flux<Project> findAll() {
        return projectInfoRepository.findAll();
    }
}
