package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursestestlib.stub.IndividualLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LessonAndStudentEntityToIndividualLessonTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        Lesson lessonStub = IndividualLessonStub.createWithExternalEntities(SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        StudentEntity studentStub = StudentEntityStub.create();

        //when
        IndividualLesson lesson = LessonAndStudentEntityToIndividualLesson.transform(lessonStub, studentStub);

        //then
        assertNotNull(lesson);
        assertEquals(lessonStub.getTitle(), lesson.getTitle());
        assertEquals(lessonStub.getStartDate(), lesson.getStartDate());
        assertEquals(lessonStub.getEndDate(), lesson.getEndDate());
        assertEquals(lessonStub.getDescription(), lesson.getDescription());
        assertEquals(lessonStub.getSubdomainEntity(), lesson.getSubdomainEntity());
        assertEquals(lessonStub.getTutorEntity(), lesson.getTutorEntity());
        assertEquals(lessonStub.getFilesIds(), lesson.getFilesIds());
        assertEquals(studentStub, lesson.getStudentEntity());
    }
}