package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.subdomainuseraccess.SubdomainUserAccessEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubdomainUserAccessRepositoryTest {

    @Autowired
    private SubdomainUserAccessRepository testee;

    @Test
    public void shouldFindAllEntitiesCorrectly() {
        //given

        //when
        List<SubdomainUserAccessEntity> subdomainUserAccessEntities = testee.findAll();

        //then
        assertFalse(subdomainUserAccessEntities.isEmpty());
    }

    @Test
    public void shouldFindAllEntitiesBySubdomainEmailAddress() {
        //given
        String subdomainEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String tutorEmailAddress = TUTOR_EMAIL_ADDRESS_STUB;

        //when
        List<SubdomainUserAccessEntity> subdomainUserAccessEntities = testee.findAllBySubdomainEmailAddressAndUserEmailAddressIsNot(subdomainEmailAddressStub, tutorEmailAddress);

        //then
        assertNotNull(subdomainUserAccessEntities);
        assertFalse(subdomainUserAccessEntities.isEmpty());
    }
}