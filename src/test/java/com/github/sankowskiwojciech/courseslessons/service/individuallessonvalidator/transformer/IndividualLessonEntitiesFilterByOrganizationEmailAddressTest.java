package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.OrganizationEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.ORGANIZATION_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.ORGANIZATION_EMAIL_ADDRESS_STUB_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IndividualLessonEntitiesFilterByOrganizationEmailAddressTest {

    private final IndividualLessonEntitiesFilterByOrganizationEmailAddress testee = IndividualLessonEntitiesFilterByOrganizationEmailAddress.getInstance();

    @Test
    public void shouldFilterCorrectlyWhenOrganizationEmailAddressIsProvided() {
        //given
        String organizationEmailAddressStub = ORGANIZATION_EMAIL_ADDRESS_STUB;
        IndividualLessonEntity individualLessonEntityWithoutOrganizationStub = IndividualLessonEntityStub.createWithExternalEntities(null, TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity individualLessonEntityWithCorrectOrganizationStub = IndividualLessonEntityStub.createWithExternalEntities(OrganizationEntityStub.createWithEmailAddress(organizationEmailAddressStub), TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity individualLessonEntityWithDifferentOrganization = IndividualLessonEntityStub.createWithExternalEntities(OrganizationEntityStub.createWithEmailAddress(ORGANIZATION_EMAIL_ADDRESS_STUB_2), TutorEntityStub.create(), StudentEntityStub.create());
        List<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityWithoutOrganizationStub, individualLessonEntityWithCorrectOrganizationStub, individualLessonEntityWithDifferentOrganization);

        //when4
        List<IndividualLessonEntity> filteredIndividualLessonEntities = testee.apply(organizationEmailAddressStub, individualLessonEntitiesStub);

        //then
        assertNotNull(filteredIndividualLessonEntities);
        assertEquals(1, filteredIndividualLessonEntities.size());
        assertEquals(individualLessonEntityWithCorrectOrganizationStub, filteredIndividualLessonEntities.stream().findFirst().get());
    }

    @Test
    public void shouldFilterCorrectlyWhenOrganizationEmailAddressIsNotProvided() {
        //given
        String organizationEmailAddressStub = null;
        IndividualLessonEntity individualLessonEntityWithoutOrganizationStub = IndividualLessonEntityStub.createWithExternalEntities(null, TutorEntityStub.create(), StudentEntityStub.create());
        IndividualLessonEntity individualLessonEntityWithOrganization = IndividualLessonEntityStub.createWithExternalEntities(OrganizationEntityStub.createWithEmailAddress(ORGANIZATION_EMAIL_ADDRESS_STUB), TutorEntityStub.create(), StudentEntityStub.create());
        List<IndividualLessonEntity> individualLessonEntitiesStub = Lists.newArrayList(individualLessonEntityWithoutOrganizationStub, individualLessonEntityWithOrganization);

        //when4
        List<IndividualLessonEntity> filteredIndividualLessonEntities = testee.apply(organizationEmailAddressStub, individualLessonEntitiesStub);

        //then
        assertNotNull(filteredIndividualLessonEntities);
        assertEquals(1, filteredIndividualLessonEntities.size());
        assertEquals(individualLessonEntityWithoutOrganizationStub, filteredIndividualLessonEntities.stream().findFirst().get());
    }
}