package com.github.sankowskiwojciech.courseslessons.model.db.subdomainuseraccess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SUBDOMAIN_USER_ACCESS")
@IdClass(SubdomainUserAccessEntityId.class)
@EqualsAndHashCode
public class SubdomainUserAccessEntity {
    @Id
    @Column(name = "SUBDOMAIN_EMAIL_ADDRESS", length = 50)
    private String subdomainEmailAddress;

    @Id
    @Column(name = "USER_EMAIL_ADDRESS", length = 50)
    private String userEmailAddress;
}
