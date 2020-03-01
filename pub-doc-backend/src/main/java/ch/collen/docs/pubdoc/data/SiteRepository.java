package ch.collen.docs.pubdoc.data;

import ch.collen.docs.pubdoc.domain.Site;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends ReactiveMongoRepository<Site, String> {
}
