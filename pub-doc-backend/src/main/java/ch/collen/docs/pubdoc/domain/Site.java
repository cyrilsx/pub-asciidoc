package ch.collen.docs.pubdoc.domain;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@Document(collection = "site")
public class Site {

    enum Type {
        LOCAL
    }

    private @NonNull Type type;
    @Id
    private String remoteUrl;
    private String directory;
}
