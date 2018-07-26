package com.liferay.staging.jdbcplayground.repository;

import com.liferay.staging.jdbcplayground.domain.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends JpaRepository<Entity, Long> {
}
