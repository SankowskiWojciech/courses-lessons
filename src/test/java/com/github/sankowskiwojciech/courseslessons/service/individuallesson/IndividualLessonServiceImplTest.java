package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.account.AccountInfo;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.request.IndividualLessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.stub.AccountInfoStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonRequestParamsStub;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndividualLessonServiceImplTest {

    private final IndividualLessonRepository individualLessonRepositoryMock = mock(IndividualLessonRepository.class);
    private final IndividualLessonService testee = new IndividualLessonServiceImpl(individualLessonRepositoryMock);

    @Before
    public void reset() {
        Mockito.reset(individualLessonRepositoryMock);
    }

    @Test
    public void shouldCreateIndividualLessonCorrectly() {
        //given
        IndividualLesson individualLessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());

        //when
        IndividualLessonResponse individualLessonResponse = testee.createIndividualLesson(individualLessonStub);

        //then
        verify(individualLessonRepositoryMock).save(any(IndividualLessonEntity.class));

        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonStub.getDateOfLesson(), individualLessonResponse.getDateOfLesson());
        assertEquals(individualLessonStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
    }

    @Test
    public void shouldReadIndividualLessonsCorrectly() {
        //given
        AccountInfo accountInfoStub = AccountInfoStub.create();
        IndividualLessonRequestParams individualLessonRequestParamsStub = IndividualLessonRequestParamsStub.create();
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.create();
        Iterable<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityStub);

        when(individualLessonRepositoryMock.findAll(any(BooleanExpression.class))).thenReturn(individualLessonEntitiesStub);

        //when
        List<IndividualLessonResponse> individualLessonResponseList = testee.readIndividualLessons(accountInfoStub, individualLessonRequestParamsStub);

        //then
        verify(individualLessonRepositoryMock).findAll(any(BooleanExpression.class));

        assertNotNull(individualLessonResponseList);
        assertEquals(1, individualLessonResponseList.size());
        IndividualLessonResponse individualLessonResponse = individualLessonResponseList.stream().findFirst().get();
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getDateOfLesson(), individualLessonResponse.getDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
    }
}