package ch.collen.docs.pubdoc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "project")
@Builder(toBuilder = true)
@JsonDeserialize(builder = Project.ProjectBuilder.class)
@Data
public class Project {

    @Id
    private @NonNull String projectName;
    private @NonNull String gitRepository;
    @Builder.Default
    private @NonNull String pathToDocumentation = ".";

    @JsonPOJOBuilder(withPrefix = "")
    public static class ProjectBuilder {

    }
}
