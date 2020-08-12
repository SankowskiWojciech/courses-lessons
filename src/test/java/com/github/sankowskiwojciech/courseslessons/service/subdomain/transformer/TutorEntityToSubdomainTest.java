package com.github.sankowskiwojciech.courseslessons.service.subdomain.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.SubdomainType;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TutorEntityToSubdomainTest {

    private final static String TUTOR_FIRST_NAME_LAST_NAME_DELIMITER = " ";

    private final TutorEntityToSubdomain testee = TutorEntityToSubdomain.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        TutorEntity tutorEntityStub = TutorEntityStub.create();

        //when
        Subdomain subdomain = testee.apply(tutorEntityStub);

        //then
        assertNotNull(subdomain);
        assertSubdomainName(subdomain.getName(), tutorEntityStub.getFirstName(), tutorEntityStub.getLastName());
        assertEquals(tutorEntityStub.getAlias(), subdomain.getAlias());
        assertEquals(tutorEntityStub.getDescription(), subdomain.getDescription());
        assertEquals(tutorEntityStub.getEmailAddress(), subdomain.getEmailAddress());
        assertEquals(tutorEntityStub.getPhoneNumber(), subdomain.getPhoneNumber());
        assertNull(subdomain.getWebsiteUrl());
        assertEquals(SubdomainType.TUTOR, subdomain.getSubdomainType());
    }

    private void assertSubdomainName(String resultSubdomainName, String tutorFirstName, String tutorLastName) {
        assertEquals(String.join(TUTOR_FIRST_NAME_LAST_NAME_DELIMITER, tutorFirstName, tutorLastName), resultSubdomainName);
    }
}