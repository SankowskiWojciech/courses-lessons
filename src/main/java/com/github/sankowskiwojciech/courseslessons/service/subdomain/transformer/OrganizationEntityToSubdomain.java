package com.github.sankowskiwojciech.courseslessons.service.subdomain.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.SubdomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationEntityToSubdomain implements Function<OrganizationEntity, Subdomain> {

    private final static OrganizationEntityToSubdomain INSTANCE = new OrganizationEntityToSubdomain();

    @Override
    public Subdomain apply(OrganizationEntity organizationEntity) {
        return Subdomain.builder()
                .name(organizationEntity.getName())
                .alias(organizationEntity.getAlias())
                .description(organizationEntity.getDescription())
                .emailAddress(organizationEntity.getEmailAddress())
                .phoneNumber(organizationEntity.getPhoneNumber())
                .websiteUrl(organizationEntity.getWebsiteUrl())
                .subdomainType(SubdomainType.ORGANIZATION)
                .build();
    }

    public static OrganizationEntityToSubdomain getInstance() {
        return INSTANCE;
    }
}
