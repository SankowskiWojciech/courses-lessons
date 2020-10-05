package com.github.sankowskiwojciech.courseslessons.service.subdomain.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrganizationEntityToSubdomainTest {

    private final OrganizationEntityToSubdomain testee = OrganizationEntityToSubdomain.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();

        //when
        Subdomain subdomain = testee.apply(organizationEntityStub);

        //then
        assertNotNull(subdomain);
        assertEquals(organizationEntityStub.getName(), subdomain.getName());
        assertEquals(organizationEntityStub.getAlias(), subdomain.getAlias());
        assertEquals(organizationEntityStub.getDescription(), subdomain.getDescription());
        assertEquals(organizationEntityStub.getEmailAddress(), subdomain.getEmailAddress());
        assertEquals(organizationEntityStub.getPhoneNumber(), subdomain.getPhoneNumber());
        assertEquals(organizationEntityStub.getWebsiteUrl(), subdomain.getWebsiteUrl());
        assertEquals(SubdomainType.ORGANIZATION, subdomain.getSubdomainType());
    }
}