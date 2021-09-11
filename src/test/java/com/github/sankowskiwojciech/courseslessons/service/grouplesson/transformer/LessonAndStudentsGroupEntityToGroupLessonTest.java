package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.StudentsGroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonAndStudentsGroupEntityToGroupLessonTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        Lesson lessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        StudentsGroupEntity studentsGroupEntity = StudentsGroupEntityStub.create();

        //when
        GroupLesson groupLesson = LessonAndStudentsGroupEntityToGroupLesson.transform(lessonStub, studentsGroupEntity);

        //then
        assertNotNull(groupLesson);
        assertEquals(lessonStub.getTitle(), groupLesson.getTitle());
        assertEquals(lessonStub.getStartDateOfLesson(), groupLesson.getStartDateOfLesson());
        assertEquals(lessonStub.getEndDateOfLesson(), groupLesson.getEndDateOfLesson());
        assertEquals(lessonStub.getDescription(), groupLesson.getDescription());
        assertEquals(lessonStub.getOrganizationEntity(), groupLesson.getOrganizationEntity());
        assertEquals(lessonStub.getTutorEntity(), groupLesson.getTutorEntity());
        assertEquals(lessonStub.getFilesIds(), groupLesson.getFilesIds());
        assertEquals(studentsGroupEntity, groupLesson.getStudentsGroupEntity());
    }
}