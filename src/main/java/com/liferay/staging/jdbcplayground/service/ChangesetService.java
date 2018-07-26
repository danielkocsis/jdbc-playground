package com.liferay.staging.jdbcplayground.service;

import com.liferay.staging.jdbcplayground.domain.ChangesetCollection;
import com.liferay.staging.jdbcplayground.domain.ChangesetEntry;
import com.liferay.staging.jdbcplayground.repository.ChagesetCollectionRepository;
import com.liferay.staging.jdbcplayground.repository.ChagesetEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChangesetService {

    private ChagesetCollectionRepository chagesetCollectionRepository;
    private ChagesetEntryRepository chagesetEntryRepository;

    public List<ChangesetCollection> getAllChangeseSetCollection() {
        return chagesetCollectionRepository.findAll();
    }

    public ChangesetCollection addChangesetCollection() {
        ChangesetCollection changesetCollection = new ChangesetCollection();

        changesetCollection.setName(UUID.randomUUID().toString());
        changesetCollection.setDescription(UUID.randomUUID().toString());
        changesetCollection.setCreatedDate(new Date());

        return chagesetCollectionRepository.save(changesetCollection);
    }

    public ChangesetEntry addChange(
            ChangesetCollection changesetCollection, long resourceId, long versionId, int version) {

        ChangesetEntry changesetEntry =  new ChangesetEntry();

        changesetEntry.setCollectionId(changesetCollection.getId());
        changesetEntry.setResourceId(resourceId);
        changesetEntry.setVersionId(versionId);
        changesetEntry.setVersionNumber(version);

        changesetEntry.setCreatedDate(new Date());

        return chagesetEntryRepository.save(changesetEntry);
    }

}
