package com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntitiesFilterByOrganizationEmailAddress implements BiFunction<String, List<IndividualLessonEntity>, List<IndividualLessonEntity>> {

    private static final IndividualLessonEntitiesFilterByOrganizationEmailAddress INSTANCE = new IndividualLessonEntitiesFilterByOrganizationEmailAddress();

    @Override
    public List<IndividualLessonEntity> apply(String organizationEmailAddress, List<IndividualLessonEntity> individualLessonEntities) {
        return individualLessonEntities.stream()
                .filter(individualLessonEntity -> checkIndividualLessonEntity(organizationEmailAddress, individualLessonEntity))
                .collect(Collectors.toList());
    }

    private boolean checkIndividualLessonEntity(String organizationEmailAddress, IndividualLessonEntity individualLessonEntity) {
        if (organizationEmailAddress == null) {
            return individualLessonEntity.getOrganizationEntity() == null;
        }
        if (individualLessonEntity.getOrganizationEntity() == null) {
            return false;
        }
        return individualLessonEntity.getOrganizationEntity().getEmailAddress().equals(organizationEmailAddress);
    }

    public static IndividualLessonEntitiesFilterByOrganizationEmailAddress getInstance() {
        return INSTANCE;
    }
}
