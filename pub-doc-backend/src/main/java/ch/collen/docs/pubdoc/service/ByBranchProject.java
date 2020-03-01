package ch.collen.docs.pubdoc.service;

import ch.collen.docs.pubdoc.Project;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class ByBranchProject {

    private @NonNull Set<String> branches;
    private @NonNull Project project;

}
