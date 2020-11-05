package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
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
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProviderTest {

    private static final long FIRST_INDIVIDUAL_LESSON_ENTITY_STUB = 1;
    private static final long SECOND_INDIVIDUAL_LESSON_ENTITY_STUB = 2;
    private static final int EXPECTED_SIZE_OF_FIRST_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY = 1;
    private static final int EXPECTED_SIZE_OF_SECOND_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY = 1;

    private final IndividualLessonFileRepository individualLessonFileRepositoryMock = mock(IndividualLessonFileRepository.class);
    private final LessonFileRepository lessonFileRepositoryMock = mock(LessonFileRepository.class);
    private final IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider testee = new IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider(individualLessonFileRepositoryMock, lessonFileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonFileRepositoryMock, lessonFileRepositoryMock);
    }

    @Test
    public void shouldTransformCorrectlyWhenIndividualLessonEntityIterableIsEmpty() {
        //given
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList();

        //when
        Map<Long, List<LessonFileWithoutContent>> individualLessonFilesWithoutContent = testee.apply(individualLessonEntitiesStub);

        //then
        verifyNoInteractions(individualLessonFileRepositoryMock);

        assertNotNull(individualLessonFilesWithoutContent);
        assertTrue(individualLessonFilesWithoutContent.isEmpty());
    }

    @Test
    public void shouldTransformCorrectly() {
        //given
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(IndividualLessonEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB), IndividualLessonEntityStub.create(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB));
        List<IndividualLessonFileEntity> individualLessonFileEntitiesStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB, 1),
                IndividualLessonFileEntityStub.create(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB, 2)
        );
        List<LessonFileWithoutContent> lessonFilesWithoutContentStub = Lists.newArrayList(
                LessonFileWithoutContentStub.createWithFileId(1),
                LessonFileWithoutContentStub.createWithFileId(2)
        );

        when(individualLessonFileRepositoryMock.findAllByLessonIdIn(anyList())).thenReturn(individualLessonFileEntitiesStub);
        when(lessonFileRepositoryMock.findAllByFileIdIn(anySet())).thenReturn(lessonFilesWithoutContentStub);

        //when
        Map<Long, List<LessonFileWithoutContent>> individualLessonFilesWithoutContent = testee.apply(individualLessonEntitiesStub);

        //then
        verify(individualLessonFileRepositoryMock).findAllByLessonIdIn(anyList());
        verify(lessonFileRepositoryMock).findAllByFileIdIn(anySet());

        assertNotNull(individualLessonFilesWithoutContent);
        assertFalse(individualLessonFilesWithoutContent.isEmpty());
        assertEquals(EXPECTED_SIZE_OF_FIRST_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY, individualLessonFilesWithoutContent.get(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB).size());
        assertEquals(EXPECTED_SIZE_OF_SECOND_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY, individualLessonFilesWithoutContent.get(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB).size());
    }
}