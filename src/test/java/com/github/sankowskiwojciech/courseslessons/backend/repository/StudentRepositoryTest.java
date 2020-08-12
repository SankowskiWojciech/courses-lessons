package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.parent.ParentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.PARENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository testee;

    @Test
    public void shouldFindAllEntitiesCorrectly() {
        //given

        //when
        List<StudentEntity> studentEntities = testee.findAll();

        //then
        assertFalse(studentEntities.isEmpty());
    }

    @Test
    public void shouldFindStudentAndHisParentAndHisLessonsCorrectly() {
        //given
        String studentEmailAddressStub = STUDENT_EMAIL_ADDRESS_STUB;
        String parentEmailAddressStub = PARENT_EMAIL_ADDRESS_STUB;

        //when
        Optional<StudentEntity> studentEntityOptional = testee.findById(studentEmailAddressStub);

        //then
        assertTrue(studentEntityOptional.isPresent());
        StudentEntity studentEntity = studentEntityOptional.get();
        assertEquals(studentEmailAddressStub, studentEntity.getEmailAddress());
        ParentEntity parentEntity = studentEntity.getParent();
        assertNotNull(parentEntity);
        assertEquals(parentEmailAddressStub, parentEntity.getEmailAddress());
        assertFalse(studentEntity.getIndividualLessons().isEmpty());
    }
}