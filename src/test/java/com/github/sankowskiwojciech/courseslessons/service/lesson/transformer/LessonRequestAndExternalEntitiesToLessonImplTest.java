package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonRequestAndExternalEntitiesToLessonImplTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonRequest lessonRequestStub = LessonRequestStub.create();
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();

        //when
        Lesson lesson = LessonRequestAndExternalEntitiesToLessonImpl.transform(lessonRequestStub, organizationEntityStub, tutorEntityStub);

        //then
        assertNotNull(lesson);
        assertEquals(lessonRequestStub.getTitle(), lesson.getTitle());
        assertEquals(lessonRequestStub.getStartDateOfLesson(), lesson.getStartDateOfLesson());
        assertEquals(lessonRequestStub.getEndDateOfLesson(), lesson.getEndDateOfLesson());
        assertEquals(lessonRequestStub.getDescription(), lesson.getDescription());
        assertEquals(organizationEntityStub, lesson.getOrganizationEntity());
        assertEquals(tutorEntityStub, lesson.getTutorEntity());
        assertEquals(lessonRequestStub.getFilesIds(), lesson.getFilesIds());
    }
}