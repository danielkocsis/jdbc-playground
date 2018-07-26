package com.liferay.staging.jdbcplayground.repository;

import com.liferay.staging.jdbcplayground.domain.ChangesetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChagesetEntryRepository extends JpaRepository<ChangesetEntry, Long> {
}
