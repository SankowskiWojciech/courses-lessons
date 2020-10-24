package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToReadFileException;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonFileEntityStub;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.USER_ID_STUB;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileAccessPermissionValidatorServiceImplTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final IndividualLessonFileRepository individualLessonFileRepositoryMock = mock(IndividualLessonFileRepository.class);
    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final FileAccessPermissionValidatorService testee = new FileAccessPermissionValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock, individualLessonFileRepositoryMock, individualLessonRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, individualLessonFileRepositoryMock, individualLessonRepositoryMock);
    }

    @Test
    public void shouldDoNothingWhenUserIdBelongsToTutor() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        boolean userExists = true;
        when(tutorRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);

        //when
        testee.validateIfUserIsAllowedToCreateFile(userIdStub);

        //then nothing happens
        verify(tutorRepositoryMock).existsById(eq(userIdStub));
    }

    @Test
    public void shouldDoNothingWhenUserIdBelongsToStudent() {
        //given
        String userIdStub = STUDENT_EMAIL_ADDRESS_STUB;
        boolean userExists = true;
        when(tutorRepositoryMock.existsById(eq(userIdStub))).thenReturn(!userExists);
        when(studentRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);

        //when
        testee.validateIfUserIsAllowedToCreateFile(userIdStub);

        //then nothing happens
        verify(tutorRepositoryMock).existsById(eq(userIdStub));
        verify(studentRepositoryMock).existsById(eq(userIdStub));
    }

    @Test(expected = UserNotAllowedToCreateFileException.class)
    public void shouldThrowUserNotAllowedToCreateFileExceptionWhenUserIsNotTutorAndIsNotStudent() {
        //given
        String userIdStub = USER_ID_STUB;
        boolean userExists = false;
        when(tutorRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);
        when(studentRepositoryMock.existsById(eq(userIdStub))).thenReturn(userExists);

        //when
        try {
            testee.validateIfUserIsAllowedToCreateFile(userIdStub);
        } catch (UserNotAllowedToCreateFileException e) {
            //then exception is thrown
            verify(tutorRepositoryMock).existsById(eq(userIdStub));
            verify(studentRepositoryMock).existsById(eq(userIdStub));
            throw e;
        }
    }

    @Test(expected = UserNotAllowedToReadFileException.class)
    public void shouldThrowUserNotAllowedToReadFileWhenUserIsNotAllowedToReadFile() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        long fileIdStub = FILE_ID_STUB;
        List<IndividualLessonFileEntity> lessonsWhichFileBelongsToStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(INDIVIDUAL_LESSON_ID_STUB, FILE_ID_STUB));
        List<IndividualLessonEntity> lessonsFoundByUserIdAndLessonsIdsStub = Collections.emptyList();

        when(individualLessonFileRepositoryMock.findAllByFileId(eq(fileIdStub))).thenReturn(lessonsWhichFileBelongsToStub);
        when(individualLessonRepositoryMock.findAllByUserIdAndLessonsIds(eq(userIdStub), anyList())).thenReturn(lessonsFoundByUserIdAndLessonsIdsStub);

        //when
        try {
            testee.validateIfUserIsAllowedToReadFile(userIdStub, fileIdStub);
        } catch (UserNotAllowedToReadFileException e) {

            //then exception is thrown
            verify(individualLessonFileRepositoryMock).findAllByFileId(eq(fileIdStub));
            verify(individualLessonRepositoryMock).findAllByUserIdAndLessonsIds(eq(userIdStub), anyList());
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenUserIsAllowedToReadFile() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        long fileIdStub = FILE_ID_STUB;
        List<IndividualLessonFileEntity> lessonsWhichFileBelongsToStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(INDIVIDUAL_LESSON_ID_STUB, FILE_ID_STUB));
        List<IndividualLessonEntity> lessonsFoundByUserIdAndLessonsIdsStub = Lists.newArrayList(IndividualLessonEntityStub.create());

        when(individualLessonFileRepositoryMock.findAllByFileId(eq(fileIdStub))).thenReturn(lessonsWhichFileBelongsToStub);
        when(individualLessonRepositoryMock.findAllByUserIdAndLessonsIds(eq(userIdStub), anyList())).thenReturn(lessonsFoundByUserIdAndLessonsIdsStub);

        //when
        testee.validateIfUserIsAllowedToReadFile(userIdStub, fileIdStub);

        //then nothing happens
        verify(individualLessonFileRepositoryMock).findAllByFileId(eq(fileIdStub));
        verify(individualLessonRepositoryMock).findAllByUserIdAndLessonsIds(eq(userIdStub), anyList());
    }
}