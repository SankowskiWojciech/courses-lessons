package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.courseslessons.stub.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonAndStudentEntityToIndividualLessonTest {

    @Test
    public void shouldTransformCorrectly() {
        //given
        Lesson lessonStub = IndividualLessonStub.createWithExternalEntities(OrganizationEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        StudentEntity studentEntityStub = StudentEntityStub.create();

        //when
        IndividualLesson individualLesson = LessonAndStudentEntityToIndividualLesson.transform(lessonStub, studentEntityStub);

        //then
        assertNotNull(individualLesson);
        assertEquals(lessonStub.getTitle(), individualLesson.getTitle());
        assertEquals(lessonStub.getStartDateOfLesson(), individualLesson.getStartDateOfLesson());
        assertEquals(lessonStub.getEndDateOfLesson(), individualLesson.getEndDateOfLesson());
        assertEquals(lessonStub.getDescription(), individualLesson.getDescription());
        assertEquals(lessonStub.getOrganizationEntity(), individualLesson.getOrganizationEntity());
        assertEquals(lessonStub.getTutorEntity(), individualLesson.getTutorEntity());
        assertEquals(lessonStub.getFilesIds(), individualLesson.getFilesIds());
        assertEquals(studentEntityStub, individualLesson.getStudentEntity());
    }
}