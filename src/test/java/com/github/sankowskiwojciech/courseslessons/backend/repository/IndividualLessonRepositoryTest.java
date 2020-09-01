package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.INDIVIDUAL_LESSON_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.ORGANIZATION_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IndividualLessonRepositoryTest {

    @Autowired
    private IndividualLessonRepository testee;

    @Test
    public void shouldFindAllEntitiesCorrectly() {
        //given

        //when
        List<IndividualLessonEntity> individualLessonEntities = testee.findAll();

        //then
        assertFalse(individualLessonEntities.isEmpty());
    }

    @Test
    public void shouldFindEntityByIdCorrectly() {
        //given
        long individualLessonIdStub = INDIVIDUAL_LESSON_ID_STUB;
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;
        String studentEmailAddressStub = STUDENT_EMAIL_ADDRESS_STUB;

        //when
        Optional<IndividualLessonEntity> individualLessonEntityOptional = testee.findById(individualLessonIdStub);

        //then
        assertTrue(individualLessonEntityOptional.isPresent());
        IndividualLessonEntity individualLessonEntity = individualLessonEntityOptional.get();
        assertEquals(individualLessonIdStub, individualLessonEntity.getLessonId());

        OrganizationEntity organizationEntity = individualLessonEntity.getOrganizationEntity();
        assertNotNull(organizationEntity);
        assertEquals(organizationEmailAddressStub, organizationEntity.getEmailAddress());

        TutorEntity tutorEntity = individualLessonEntity.getTutorEntity();
        assertNotNull(tutorEntity);
        assertEquals(tutorEmailAddressStub, tutorEntity.getEmailAddress());

        StudentEntity studentEntity = individualLessonEntity.getStudentEntity();
        assertNotNull(studentEntity);
        assertEquals(studentEmailAddressStub, studentEntity.getEmailAddress());
    }

    @Test
    public void shouldSaveEntityCorrectly() {
        //given
        OrganizationEntity organizationEntityStub = OrganizationEntityStub.create();
        TutorEntity tutorEntityStub = TutorEntityStub.create();
        StudentEntity studentEntityStub = StudentEntityStub.create();
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithExternalEntities(organizationEntityStub, tutorEntityStub, studentEntityStub);

        //when
        IndividualLessonEntity savedIndividualLessonEntity = testee.save(individualLessonEntityStub);

        //then
        assertNotNull(savedIndividualLessonEntity);
        assertEquals(individualLessonEntityStub.getLessonId(), savedIndividualLessonEntity.getLessonId());
        assertEquals(individualLessonEntityStub.getTitle(), savedIndividualLessonEntity.getTitle());
        assertEquals(individualLessonEntityStub.getStartDateOfLesson(), savedIndividualLessonEntity.getStartDateOfLesson());
        assertEquals(individualLessonEntityStub.getEndDateOfLesson(), savedIndividualLessonEntity.getEndDateOfLesson());
        assertEquals(individualLessonEntityStub.getDescription(), savedIndividualLessonEntity.getDescription());
    }

    @Test
    public void shouldFindAllIndividualLessonsWhichCollideWithNewIndividualLesson() {
        //given
        final LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime existingIndividualLessonStartDate = currentDateTime;
        LocalDateTime existingIndividualLessonEndDate = currentDateTime.plusHours(2);
        IndividualLessonEntity existingIndividualLessonStub = IndividualLessonEntityStub.createWithDatesOfLesson(existingIndividualLessonStartDate, existingIndividualLessonEndDate);
        LocalDateTime newIndividualLessonStartDate = currentDateTime.minusHours(1);
        LocalDateTime newIndividualLessonEndDate = currentDateTime.plusHours(1);
        String tutorEmailAddressStub = TUTOR_EMAIL_ADDRESS_STUB;

        testee.save(existingIndividualLessonStub);

        //when
        List<IndividualLessonEntity> individualLessonEntities = testee.findAllIndividualLessonsWhichCanCollideWithNewIndividualLesson(newIndividualLessonStartDate, newIndividualLessonEndDate, tutorEmailAddressStub);

        //then
        assertNotNull(individualLessonEntities);
        assertFalse(individualLessonEntities.isEmpty());
    }
}