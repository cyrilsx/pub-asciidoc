package ch.collen.docs.pubdoc.data;

import ch.collen.docs.pubdoc.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInfoRepository extends ReactiveMongoRepository<Project, String> {

}
