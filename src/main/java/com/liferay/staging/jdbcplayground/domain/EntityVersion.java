package com.liferay.staging.jdbcplayground.domain;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class EntityVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long entityId;

    private int version;

    private String name;
    private String description;

    private Date createdDate;

}
