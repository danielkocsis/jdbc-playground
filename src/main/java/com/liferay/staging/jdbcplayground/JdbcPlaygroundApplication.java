package com.liferay.staging.jdbcplayground;

import com.liferay.staging.jdbcplayground.domain.ChangesetCollection;
import com.liferay.staging.jdbcplayground.domain.Entity;
import com.liferay.staging.jdbcplayground.service.ChangesetService;
import com.liferay.staging.jdbcplayground.service.EntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class JdbcPlaygroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbcPlaygroundApplication.class, args);
    }

    @Component
    @Slf4j
    public static class CommandLineAppStartupRunner implements CommandLineRunner {

        private static final Random RANDOM = new Random();
        private static final int ENTITY_NUM = 100_000;
        private static final int ROUND_OF_CHANGES_NUM = 4;
        private static final int CHANGES_NUM = RANDOM.nextInt(2) + 1;

        private final EntityService entityService;
        private final ChangesetService changesetService;

        private List<ChangesetCollection> changesetCollectionList;

        public CommandLineAppStartupRunner(EntityService entityService, ChangesetService changesetService) {
            this.entityService = entityService;
            this.changesetService = changesetService;
        }

        @Override
        public void run(String... args) {
            final long startTime = System.currentTimeMillis();

            initChangesetCollections();

            log.info("Starting to add " + ENTITY_NUM + " entities...");

            IntStream.range(0, ENTITY_NUM)
                    .parallel()
                    .forEach(i -> {
                        ChangesetCollection changesetCollection =
                                changesetCollectionList.get(RANDOM.nextInt(changesetCollectionList.size()));

                        entityService.addEntity(changesetCollection);
                    });

            log.info("Adding " + ENTITY_NUM + " entities has been done...");
            log.info("Starting to add entity changes...");

            IntStream.range(0, ROUND_OF_CHANGES_NUM)
                    .peek(i -> log.info("Round " + (i + 1) + " of " + ROUND_OF_CHANGES_NUM + " has been started"))
                    .forEach(i -> addVersions());

            log.info("Adding entity changes has been done...");
            log.info("Total runtime(ms): " + (System.currentTimeMillis() - startTime));
        }

        private void initChangesetCollections() {
            changesetCollectionList = changesetService.getAllChangeseSetCollection();

            if (changesetCollectionList == null) {
                changesetCollectionList = new ArrayList<>();
            }

            if (changesetCollectionList.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    changesetCollectionList.add(changesetService.addChangesetCollection());
                }
            }
        }

        private void addVersions() {
            final int totalPagesCount = ENTITY_NUM / 1000;

            IntStream.range(0, totalPagesCount)
                    .parallel()
                    .forEach(
                            i -> {
                                Pageable pageable = PageRequest.of(i, 1000);

                                entityService.findAllPageable(pageable).stream()
                                        .parallel()
                                        .forEach(this::addRandomChanges);

                                log.info("Adding changes for a 1000 entities has been done");

                            });
        }

        private void addRandomChanges(Entity entity) {
            for (int i = 0; i < CHANGES_NUM; i++) {
                ChangesetCollection changesetCollection =
                        changesetCollectionList.get(RANDOM.nextInt(changesetCollectionList.size()));

                entityService.updateEntity(changesetCollection, entity);
            }
        }

    }

}
