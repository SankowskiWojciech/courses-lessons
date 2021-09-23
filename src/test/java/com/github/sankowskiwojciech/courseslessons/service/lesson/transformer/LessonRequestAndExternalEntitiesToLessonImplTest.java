package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import com.github.sankowskiwojciech.courseslessons.stub.LessonRequestStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonRequestAndExternalEntitiesToLessonImplTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonRequest requestStub = LessonRequestStub.create();
        OrganizationEntity organizationStub = OrganizationEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();

        //when
        Lesson lesson = LessonRequestAndExternalEntitiesToLessonImpl.transform(requestStub, organizationStub, tutorStub);

        //then
        assertNotNull(lesson);
        assertEquals(requestStub.getTitle(), lesson.getTitle());
        assertEquals(requestStub.getStartDate(), lesson.getStartDate());
        assertEquals(requestStub.getEndDate(), lesson.getEndDate());
        assertEquals(requestStub.getDescription(), lesson.getDescription());
        assertEquals(organizationStub, lesson.getOrganizationEntity());
        assertEquals(tutorStub, lesson.getTutorEntity());
        assertEquals(requestStub.getFilesIds(), lesson.getFilesIds());
    }
}