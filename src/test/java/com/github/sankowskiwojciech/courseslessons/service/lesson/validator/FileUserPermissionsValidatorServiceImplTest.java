package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonFileEntityStub;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB_2;
import static com.github.sankowskiwojciech.coursestestlib.DefaultTestValues.USER_ID_STUB;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FileUserPermissionsValidatorServiceImplTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final LessonFileAccessRepository lessonFileAccessRepositoryMock = mock(LessonFileAccessRepository.class);
    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final FileRepository fileRepositoryMock = mock(FileRepository.class);
    private final FileUserPermissionsValidatorService testee = new FileUserPermissionsValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock, lessonFileAccessRepositoryMock, individualLessonRepositoryMock, fileRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock, lessonFileAccessRepositoryMock, individualLessonRepositoryMock, fileRepositoryMock);
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

    @Test(expected = UserNotAllowedToAccessFileException.class)
    public void shouldThrowUserNotAllowedToReadFileWhenUserIsNotAllowedToReadFile() {
        //given
        String fileOwnerIdStub = TUTOR_EMAIL_ADDRESS_STUB_2;
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        String fileIdStub = FILE_ID_STUB;
        List<LessonFileAccessEntity> lessonsWhichFileBelongsToStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(INDIVIDUAL_LESSON_ID_STUB, FILE_ID_STUB));
        List<IndividualLessonEntity> lessonsFoundByUserIdAndLessonsIdsStub = Collections.emptyList();

        when(fileRepositoryMock.getFileOwnerIdByFileId(eq(fileIdStub))).thenReturn(fileOwnerIdStub);
        when(lessonFileAccessRepositoryMock.findAllByFileId(eq(fileIdStub))).thenReturn(lessonsWhichFileBelongsToStub);
        when(individualLessonRepositoryMock.findAllByUserIdAndLessonIdIn(eq(userIdStub), anyList())).thenReturn(lessonsFoundByUserIdAndLessonsIdsStub);

        //when
        try {
            testee.validateIfUserIsAllowedToAccessFile(userIdStub, fileIdStub);
        } catch (UserNotAllowedToAccessFileException e) {

            //then exception is thrown
            verify(fileRepositoryMock).getFileOwnerIdByFileId(eq(fileIdStub));
            verify(lessonFileAccessRepositoryMock).findAllByFileId(eq(fileIdStub));
            verify(individualLessonRepositoryMock).findAllByUserIdAndLessonIdIn(eq(userIdStub), anyList());
            throw e;
        }
    }

    @Test
    public void shouldDoNothingWhenUserIsAllowedToReadFile() {
        //given
        String fileOwnerIdStub = TUTOR_EMAIL_ADDRESS_STUB_2;
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        String fileIdStub = FILE_ID_STUB;
        List<LessonFileAccessEntity> lessonsWhichFileBelongsToStub = Lists.newArrayList(IndividualLessonFileEntityStub.create(INDIVIDUAL_LESSON_ID_STUB, FILE_ID_STUB));
        List<IndividualLessonEntity> lessonsFoundByUserIdAndLessonsIdsStub = Lists.newArrayList(IndividualLessonEntityStub.create(INDIVIDUAL_LESSON_ID_STUB));

        when(fileRepositoryMock.getFileOwnerIdByFileId(eq(fileIdStub))).thenReturn(fileOwnerIdStub);
        when(lessonFileAccessRepositoryMock.findAllByFileId(eq(fileIdStub))).thenReturn(lessonsWhichFileBelongsToStub);
        when(individualLessonRepositoryMock.findAllByUserIdAndLessonIdIn(eq(userIdStub), anyList())).thenReturn(lessonsFoundByUserIdAndLessonsIdsStub);

        //when
        testee.validateIfUserIsAllowedToAccessFile(userIdStub, fileIdStub);

        //then nothing happens
        verify(fileRepositoryMock).getFileOwnerIdByFileId(eq(fileIdStub));
        verify(lessonFileAccessRepositoryMock).findAllByFileId(eq(fileIdStub));
        verify(individualLessonRepositoryMock).findAllByUserIdAndLessonIdIn(eq(userIdStub), anyList());
    }

    @Test
    public void shouldDoNothingWhenUserIsAnOwnerOfAFile() {
        //given
        String userIdStub = TUTOR_EMAIL_ADDRESS_STUB;
        String fileIdStub = FILE_ID_STUB;

        when(fileRepositoryMock.getFileOwnerIdByFileId(eq(fileIdStub))).thenReturn(userIdStub);

        //when
        testee.validateIfUserIsAllowedToAccessFile(userIdStub, fileIdStub);

        //then nothing happens
        verify(fileRepositoryMock).getFileOwnerIdByFileId(eq(fileIdStub));
        verifyNoInteractions(lessonFileAccessRepositoryMock, individualLessonRepositoryMock);
    }
}