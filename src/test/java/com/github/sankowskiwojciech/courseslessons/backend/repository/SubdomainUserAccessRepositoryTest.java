package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.subdomainuseraccess.SubdomainUserAccessEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;

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
}