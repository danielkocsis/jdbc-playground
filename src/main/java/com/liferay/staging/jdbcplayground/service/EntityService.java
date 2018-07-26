package com.liferay.staging.jdbcplayground.service;

import com.liferay.staging.jdbcplayground.domain.ChangesetCollection;
import com.liferay.staging.jdbcplayground.domain.Entity;
import com.liferay.staging.jdbcplayground.domain.EntityVersion;
import com.liferay.staging.jdbcplayground.repository.EntityRepository;
import com.liferay.staging.jdbcplayground.repository.EntityVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class EntityService {

    private EntityRepository entityRepository;
    private EntityVersionRepository entityVersionRepository;
    private ChangesetService changesetService;

    public Page<Entity> findAllPageable(Pageable pageable) {
        return entityRepository.findAll(pageable);
    }

    public Entity addEntity(ChangesetCollection changesetCollection) {
        Entity entity = new Entity();

        entity.setCreatedDate(new Date());
        entity.setName(UUID.randomUUID().toString());
        entity.setLatestVersion(1);

        entity = entityRepository.save(entity);

        EntityVersion entityVersion = addVersion(entity, true);

        // Update changeset

        changesetService.addChange(
                changesetCollection, entity.getId(), entityVersion.getId(), entityVersion.getVersion());

        return entity;
    }

    public Entity updateEntity(ChangesetCollection changesetCollection, Entity entity) {
        EntityVersion entityVersion = addVersion(entity, false);

        entityVersionRepository.save(entityVersion);

        entity.setLatestVersion(entityVersion.getVersion());

        entityRepository.save(entity);

        // Update changeset

        changesetService.addChange(
                changesetCollection, entity.getId(), entityVersion.getId(), entityVersion.getVersion());

        return entity;
    }

    private EntityVersion addVersion(Entity entity, boolean first) {
        EntityVersion entityVersion = new EntityVersion();

        entityVersion.setEntityId(entity.getId());
        entityVersion.setName(UUID.randomUUID().toString());
        entityVersion.setDescription(UUID.randomUUID().toString());
        entityVersion.setCreatedDate(new Date());
        entityVersion.setVersion(first ? 1 : entity.getLatestVersion() + 1);

        return entityVersionRepository.save(entityVersion);
    }

}
