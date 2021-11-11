package com.github.sankowskiwojciech.courseslessons.service.student.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.student.StudentResponse;
import com.github.sankowskiwojciech.coursestestlib.stub.StudentEntityStub;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentEntityToStudentResponseTest {

    private final StudentEntityToStudentResponse testee = StudentEntityToStudentResponse.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        StudentEntity stub = StudentEntityStub.create();

        //when
        StudentResponse response = testee.apply(stub);

        //then
        assertNotNull(response);
        assertEquals(stub.getFullName(), response.getFullName());
        assertEquals(stub.getEmailAddress(), response.getEmailAddress());
    }
}