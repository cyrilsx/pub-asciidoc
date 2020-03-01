package ch.collen.docs.pubdoc;

import ch.collen.docs.pubdoc.config.ZasciiDocConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties(ZasciiDocConfiguration.class)
@EnableReactiveMongoRepositories
public class PubDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubDocApplication.class, args);
    }

}
