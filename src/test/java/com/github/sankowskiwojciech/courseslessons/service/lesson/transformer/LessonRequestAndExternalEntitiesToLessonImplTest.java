package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonRequestStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonRequestAndExternalEntitiesToLessonImplTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonRequest requestStub = LessonRequestStub.create();
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();

        //when
        Lesson lesson = LessonRequestAndExternalEntitiesToLessonImpl.transform(requestStub, subdomainStub, tutorStub);

        //then
        assertNotNull(lesson);
        assertEquals(requestStub.getTitle(), lesson.getTitle());
        assertEquals(requestStub.getStartDate(), lesson.getStartDate());
        assertEquals(requestStub.getEndDate(), lesson.getEndDate());
        assertEquals(requestStub.getDescription(), lesson.getDescription());
        assertEquals(subdomainStub, lesson.getSubdomainEntity());
        assertEquals(tutorStub, lesson.getTutorEntity());
        assertEquals(requestStub.getFilesIds(), lesson.getFilesIds());
    }
}