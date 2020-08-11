package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_ALIAS_STUB;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TutorRepositoryTest {

    @Autowired
    private TutorRepository testee;

    @Test
    public void shouldFindAllEntitiesCorrectly() {
        //given

        //when
        List<TutorEntity> tutorEntities = testee.findAll();

        //then
        assertFalse(tutorEntities.isEmpty());
    }

    @Test
    public void shouldFindEntityByAliasCorrectly() {
        //given
        String tutorAlias = TUTOR_ALIAS_STUB;

        //when
        Optional<TutorEntity> tutorEntity = testee.findByAlias(tutorAlias);

        //then
        assertTrue(tutorEntity.isPresent());
    }
}