package com.github.sankowskiwojciech.courseslessons.model.db.organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ORGANIZATION")
@EqualsAndHashCode
public class OrganizationEntity {

    @Id
    @Column(name = "EMAIL_ADDRESS", length = 50, unique = true, nullable = false, updatable = false)
    private String emailAddress;

    @Column(name = "ALIAS", length = 20, unique = true, nullable = false, updatable = false)
    private String alias;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "PHONE_NUMBER", length = 9)
    private String phoneNumber;

    @Column(name = "WEBSITE_URL", length = 30)
    private String websiteUrl;
}
