package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.parent.ParentEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParentRepositoryTest {

    @Autowired
    private ParentRepository testee;

    @Test
    public void shouldFindAllEntitiesCorrectly() {
        //given

        //when
        List<ParentEntity> parentEntities = testee.findAll();

        //then
        assertFalse(parentEntities.isEmpty());
    }
}