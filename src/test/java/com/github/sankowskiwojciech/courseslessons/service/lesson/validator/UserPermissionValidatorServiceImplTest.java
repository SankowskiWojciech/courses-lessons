package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.USER_ID_STUB;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserPermissionValidatorServiceImplTest {

    private final TutorRepository tutorRepositoryMock = mock(TutorRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final UserPermissionValidatorService testee = new UserPermissionValidatorServiceImpl(tutorRepositoryMock, studentRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(tutorRepositoryMock, studentRepositoryMock);
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
}