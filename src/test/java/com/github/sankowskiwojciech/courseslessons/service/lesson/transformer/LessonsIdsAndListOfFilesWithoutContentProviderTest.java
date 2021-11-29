package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonFileEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileWithoutContentStub;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

public class LessonsIdsAndListOfFilesWithoutContentProviderTest {
    private static final String FIRST_INDIVIDUAL_LESSON_ENTITY_STUB = UUID.randomUUID().toString();
    private static final String SECOND_INDIVIDUAL_LESSON_ENTITY_STUB = UUID.randomUUID().toString();
    private static final int EXPECTED_SIZE_OF_FIRST_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY = 1;
    private static final int EXPECTED_SIZE_OF_SECOND_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY = 1;

    private final LessonFileAccessRepository lessonFileAccessRepositoryMock = mock(LessonFileAccessRepository.class);
    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final LessonsIdsAndListOfFilesWithoutContentProvider testee = new LessonsIdsAndListOfFilesWithoutContentProvider(lessonFileAccessRepositoryMock, fileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(lessonFileAccessRepositoryMock, fileRepositoryMock);
    }

    @Test
    public void shouldTransformCorrectlyWhenIndividualLessonEntityIterableIsEmpty() {
        //given
        List<String> lessonsIdsStub = Lists.newArrayList();

        //when
        Map<String, List<FileWithoutContent>> individualLessonFilesWithoutContent = testee.apply(lessonsIdsStub);

        //then
        verifyNoInteractions(lessonFileAccessRepositoryMock);

        assertNotNull(individualLessonFilesWithoutContent);
        assertTrue(individualLessonFilesWithoutContent.isEmpty());
    }

    @Test
    public void shouldTransformCorrectly() {
        //given
        List<String> lessonsIdsStub = Lists.newArrayList(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB, SECOND_INDIVIDUAL_LESSON_ENTITY_STUB);
        List<LessonFileAccessEntity> individualLessonFileEntitiesStub = Lists.newArrayList(
                IndividualLessonFileEntityStub.create(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB, UUID.randomUUID().toString()),
                IndividualLessonFileEntityStub.create(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB, UUID.randomUUID().toString())
        );
        List<FileWithoutContent> lessonFilesWithoutContentStub = Lists.newArrayList(
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()),
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString())
        );

        when(lessonFileAccessRepositoryMock.findAllByLessonIdIn(anyList())).thenReturn(individualLessonFileEntitiesStub);
        when(fileRepositoryMock.findAllByIdIn(anySet())).thenReturn(lessonFilesWithoutContentStub);

        //when
        Map<String, List<FileWithoutContent>> individualLessonFilesWithoutContent = testee.apply(lessonsIdsStub);

        //then
        verify(lessonFileAccessRepositoryMock).findAllByLessonIdIn(anyList());
        verify(fileRepositoryMock).findAllByIdIn(anySet());

        assertNotNull(individualLessonFilesWithoutContent);
        assertFalse(individualLessonFilesWithoutContent.isEmpty());
        assertEquals(EXPECTED_SIZE_OF_FIRST_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY, individualLessonFilesWithoutContent.get(FIRST_INDIVIDUAL_LESSON_ENTITY_STUB).size());
        assertEquals(EXPECTED_SIZE_OF_SECOND_LIST_OF_INDIVIDUAL_LESSON_FILE_ENTITY, individualLessonFilesWithoutContent.get(SECOND_INDIVIDUAL_LESSON_ENTITY_STUB).size());
    }
}