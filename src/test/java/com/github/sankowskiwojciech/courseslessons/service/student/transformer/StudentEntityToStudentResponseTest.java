package com.github.sankowskiwojciech.courseslessons.service.student.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.student.StudentResponse;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentEntityToStudentResponseTest {

    private final StudentEntityToStudentResponse testee = StudentEntityToStudentResponse.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        StudentEntity studentEntityStub = StudentEntityStub.create();

        //when
        StudentResponse studentResponse = testee.apply(studentEntityStub);

        //then
        assertNotNull(studentResponse);
        assertEquals(studentEntityStub.getFullName(), studentResponse.getFullName());
        assertEquals(studentEntityStub.getEmailAddress(), studentResponse.getEmailAddress());
    }
}