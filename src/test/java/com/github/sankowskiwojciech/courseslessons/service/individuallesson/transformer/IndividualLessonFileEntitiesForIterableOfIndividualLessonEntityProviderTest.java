package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProviderTest {

    private static final long FIRST_INDIVIDUAL_LESSON_ENTITY_STUB = 1;
    private static final long SECOND_INDIVIDUAL_LESSON_ENTITY_STUB = 2;
    private static final int EXPECTED_SIZE_OF_FIRST_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY = 3;
    private static final int EXPECTED_SIZE_OF_SECOND_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY = 2;

    private final IndividualLessonFileRepository individualLessonFileRepositoryMock = mock(IndividualLessonFileRepository.class);
    private final IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider testee = new IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider(individualLessonFileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonFileRepositoryMock);
    }

    @Test
    public void shouldTransformCorrectlyWhenIndividualLessonEntityIterableIsEmpty() {
        //given
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList();

        //when
        Map<Long, List<IndividualLessonFileEntity>> individualLessonFileEntities = testee.apply(individualLessonEntitiesStub);

        //then
        verifyNoInteractions(individualLessonFileRepositoryMock);

        assertNotNull(individualLessonFileEntities);
        assertTrue(individualLessonFileEntities.isEmpty());
    }

    @Test
    public void shouldTransformCorrectly() {
        //given
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB), IndividualLessonEntityStub.create(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB));
        List<IndividualLessonFileEntity> individualLessonFileEntitiesStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB, 1L),
                IndividualLessonFileEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB, 2L),
                IndividualLessonFileEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB, 3L),
                IndividualLessonFileEntityStub.create(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB, 4L),
                IndividualLessonFileEntityStub.create(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB, 5L)
        );

        when(individualLessonFileRepositoryMock.findAllByLessonIdIn(anyList())).thenReturn(individualLessonFileEntitiesStub);

        //when
        Map<Long, List<IndividualLessonFileEntity>> individualLessonFileEntities = testee.apply(individualLessonEntitiesStub);

        //then
        verify(individualLessonFileRepositoryMock).findAllByLessonIdIn(anyList());

        assertNotNull(individualLessonFileEntities);
        assertFalse(individualLessonFileEntities.isEmpty());
        assertEquals(EXPECTED_SIZE_OF_FIRST_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY, individualLessonFileEntities.get(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB).size());
        assertEquals(EXPECTED_SIZE_OF_SECOND_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY, individualLessonFileEntities.get(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB).size());
    }
}