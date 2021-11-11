package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentsGroupEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonAndGroupEntityToGroupLessonTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        Lesson lessonStub = IndividualLessonStub.createWithExternalEntities(SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        GroupEntity groupEntity = StudentsGroupEntityStub.create();

        //when
        GroupLesson groupLesson = LessonAndGroupEntityToGroupLesson.transform(lessonStub, groupEntity);

        //then
        assertNotNull(groupLesson);
        assertEquals(lessonStub.getTitle(), groupLesson.getTitle());
        assertEquals(lessonStub.getStartDate(), groupLesson.getStartDate());
        assertEquals(lessonStub.getEndDate(), groupLesson.getEndDate());
        assertEquals(lessonStub.getDescription(), groupLesson.getDescription());
        assertEquals(lessonStub.getSubdomainEntity(), groupLesson.getSubdomainEntity());
        assertEquals(lessonStub.getTutorEntity(), groupLesson.getTutorEntity());
        assertEquals(lessonStub.getFilesIds(), groupLesson.getFilesIds());
        assertEquals(groupEntity, groupLesson.getGroupEntity());
    }
}