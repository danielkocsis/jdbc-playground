package com.liferay.staging.jdbcplayground.domain;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
public class ChangesetEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long collectionId;

    @Column(nullable = false)
    private long resourceId;

    @Column(nullable = false)
    private long versionId;

    @Column(nullable = false)
    private long versionNumber;

    private Date createdDate;

}
