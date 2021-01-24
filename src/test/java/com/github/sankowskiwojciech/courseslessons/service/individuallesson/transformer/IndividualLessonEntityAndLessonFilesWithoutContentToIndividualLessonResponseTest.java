package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponseTest {

    private final IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse testee = IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance();

    @Test
    public void shouldTransformCorrectlyWhenSubdomainIsOrganization() {
        //given
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        List<LessonFileWithoutContent> lessonFilesWithoutContent = Lists.newArrayList(
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()),
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString())
        );

        //when
        IndividualLessonResponse individualLessonResponse = testee.apply(individualLessonEntityStub, lessonFilesWithoutContent);

        //then
        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonEntityStub.getLessonId(), individualLessonResponse.getLessonId());
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonEntityStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getOrganizationEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(lessonFilesWithoutContent.size(), individualLessonResponse.getFilesInformation().size());
    }

    @Test
    public void shouldTransformCorrectlyWhenSubdomainIsTutor() {
        //given
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithExternalEntities(null, TutorEntityStub.create(), StudentEntityStub.create());
        List<LessonFileWithoutContent> lessonFilesWithoutContent = Lists.newArrayList(
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()),
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString())
        );

        //when
        IndividualLessonResponse individualLessonResponse = testee.apply(individualLessonEntityStub, lessonFilesWithoutContent);

        //then
        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonEntityStub.getLessonId(), individualLessonResponse.getLessonId());
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getStartDateOfLesson(), individualLessonResponse.getStartDateOfLesson());
        assertEquals(individualLessonEntityStub.getEndDateOfLesson(), individualLessonResponse.getEndDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getTutorEntity().getAlias(), individualLessonResponse.getSubdomainName());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(lessonFilesWithoutContent.size(), individualLessonResponse.getFilesInformation().size());
    }
}