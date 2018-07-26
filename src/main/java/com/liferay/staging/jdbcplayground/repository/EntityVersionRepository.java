package com.liferay.staging.jdbcplayground.repository;

import com.liferay.staging.jdbcplayground.domain.EntityVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityVersionRepository extends JpaRepository<EntityVersion, Long> {

    public List<EntityVersion> getEntityVersionByEntityIdOrderByVersionDesc(long entityId);

}
