package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentsGroupEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonAndStudentsGroupEntityToGroupLessonTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        Lesson lessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        GroupEntity groupEntity = StudentsGroupEntityStub.create();

        //when
        GroupLesson groupLesson = LessonAndStudentsGroupEntityToGroupLesson.transform(lessonStub, groupEntity);

        //then
        assertNotNull(groupLesson);
        assertEquals(lessonStub.getTitle(), groupLesson.getTitle());
        assertEquals(lessonStub.getStartDate(), groupLesson.getStartDate());
        assertEquals(lessonStub.getEndDate(), groupLesson.getEndDate());
        assertEquals(lessonStub.getDescription(), groupLesson.getDescription());
        assertEquals(lessonStub.getOrganizationEntity(), groupLesson.getOrganizationEntity());
        assertEquals(lessonStub.getTutorEntity(), groupLesson.getTutorEntity());
        assertEquals(lessonStub.getFilesIds(), groupLesson.getFilesIds());
        assertEquals(groupEntity, groupLesson.getGroupEntity());
    }
}