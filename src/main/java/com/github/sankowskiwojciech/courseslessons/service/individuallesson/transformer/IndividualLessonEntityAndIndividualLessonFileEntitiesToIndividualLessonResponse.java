package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse implements BiFunction<IndividualLessonEntity, List<IndividualLessonFileEntity>, IndividualLessonResponse> {

    private static final IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse INSTANCE = new IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse();

    @Override
    public IndividualLessonResponse apply(IndividualLessonEntity individualLessonEntity, List<IndividualLessonFileEntity> individualLessonFileEntities) {
        return IndividualLessonResponse.builder()
                .title(individualLessonEntity.getTitle())
                .startDateOfLesson(individualLessonEntity.getStartDateOfLesson())
                .endDateOfLesson(individualLessonEntity.getEndDateOfLesson())
                .description(individualLessonEntity.getDescription())
                .subdomainName(getSubdomainName(individualLessonEntity))
                .tutorEmailAddress(individualLessonEntity.getTutorEntity().getEmailAddress())
                .studentFullName(individualLessonEntity.getStudentEntity().getFullName())
                .studentEmailAddress(individualLessonEntity.getStudentEntity().getEmailAddress())
                .filesIds(getSavedFilesIds(individualLessonFileEntities))
                .build();
    }

    private String getSubdomainName(IndividualLessonEntity individualLessonEntity) {
        return individualLessonEntity.getOrganizationEntity() != null ? individualLessonEntity.getOrganizationEntity().getAlias() : individualLessonEntity.getTutorEntity().getAlias();
    }

    private List<Long> getSavedFilesIds(List<IndividualLessonFileEntity> individualLessonFileEntities) {
        if (CollectionUtils.isNotEmpty(individualLessonFileEntities)) {
            return individualLessonFileEntities.stream()
                    .map(IndividualLessonFileEntity::getFileId)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse getInstance() {
        return INSTANCE;
    }
}
