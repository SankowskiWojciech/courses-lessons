package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntityToIndividualLessonResponse implements Function<IndividualLessonEntity, IndividualLessonResponse> {

    private static final IndividualLessonEntityToIndividualLessonResponse INSTANCE = new IndividualLessonEntityToIndividualLessonResponse();

    @Override
    public IndividualLessonResponse apply(IndividualLessonEntity individualLessonEntity) {
        return IndividualLessonResponse.builder()
                .title(individualLessonEntity.getTitle())
                .startDateOfLesson(individualLessonEntity.getStartDateOfLesson())
                .endDateOfLesson(individualLessonEntity.getEndDateOfLesson())
                .description(individualLessonEntity.getDescription())
                .subdomainName(getSubdomainName(individualLessonEntity))
                .tutorEmailAddress(individualLessonEntity.getTutorEntity().getEmailAddress())
                .studentFullName(individualLessonEntity.getStudentEntity().getFullName())
                .studentEmailAddress(individualLessonEntity.getStudentEntity().getEmailAddress())
                .build();
    }

    private String getSubdomainName(IndividualLessonEntity individualLessonEntity) {
        return individualLessonEntity.getOrganizationEntity() != null ? individualLessonEntity.getOrganizationEntity().getAlias() : individualLessonEntity.getTutorEntity().getAlias();
    }

    public static IndividualLessonEntityToIndividualLessonResponse getInstance() {
        return INSTANCE;
    }
}
