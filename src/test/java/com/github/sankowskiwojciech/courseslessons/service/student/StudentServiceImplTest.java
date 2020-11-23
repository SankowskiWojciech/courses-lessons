package com.github.sankowskiwojciech.courseslessons.service.student;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.SubdomainUserAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainUserAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.student.StudentResponse;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.SubdomainUserAccessEntityStub;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.ORGANIZATION_ALIAS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StudentServiceImplTest {

    private static final String STUDENT_EMAIL_ADDRESS_FIRST_STUB = "marcin.b@gmail.com";
    private static final String STUDENT_EMAIL_ADDRESS_SECOND_STUB = "rawskaka@gmail.com";
    private static final List<String> STUDENTS_EMAIL_ADDRESSES_STUB = Lists.newArrayList(
            STUDENT_EMAIL_ADDRESS_FIRST_STUB,
            STUDENT_EMAIL_ADDRESS_SECOND_STUB
    );

    private final SubdomainUserAccessRepository subdomainUserAccessRepositoryMock = mock(SubdomainUserAccessRepository.class);
    private final StudentRepository studentRepositoryMock = mock(StudentRepository.class);
    private final StudentService testee = new StudentServiceImpl(subdomainUserAccessRepositoryMock, studentRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(subdomainUserAccessRepositoryMock, studentRepositoryMock);
    }

    @Test
    public void shouldReadInformationAboutStudentsCorrectly() {
        //given
        String subdomainAliasStub = ORGANIZATION_ALIAS_STUB;
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        List<SubdomainUserAccessEntity> subdomainUserAccessEntitiesStub = createSubdomainUserAccessEntitiesStub(subdomainAliasStub);
        List<StudentEntity> studentEntitiesStub = createStudentEntitiesStub();

        when(subdomainUserAccessRepositoryMock.findAllBySubdomainUserAccessEntityIdSubdomainIdAndSubdomainUserAccessEntityIdUserEmailAddressIsNot(eq(subdomainAliasStub), eq(tutorEmailAddressStub))).thenReturn(subdomainUserAccessEntitiesStub);
        when(studentRepositoryMock.findAllById(anyList())).thenReturn(studentEntitiesStub);

        //when
        List<StudentResponse> studentResponses = testee.readStudents(subdomainAliasStub, tutorEmailAddressStub);

        //then
        verify(subdomainUserAccessRepositoryMock).findAllBySubdomainUserAccessEntityIdSubdomainIdAndSubdomainUserAccessEntityIdUserEmailAddressIsNot(eq(subdomainAliasStub), eq(tutorEmailAddressStub));
        verify(studentRepositoryMock).findAllById(anyList());

        assertNotNull(studentResponses);
        assertEquals(studentEntitiesStub.size(), studentResponses.size());
    }

    private List<SubdomainUserAccessEntity> createSubdomainUserAccessEntitiesStub(String subdomainEmailAddressStub) {
        return STUDENTS_EMAIL_ADDRESSES_STUB.stream()
                .map(studentEmailAddress -> SubdomainUserAccessEntityStub.create(subdomainEmailAddressStub, studentEmailAddress))
                .collect(Collectors.toList());
    }

    private List<StudentEntity> createStudentEntitiesStub() {
        return STUDENTS_EMAIL_ADDRESSES_STUB.stream()
                .map(StudentEntityStub::createWithEmailAddress)
                .collect(Collectors.toList());
    }
}