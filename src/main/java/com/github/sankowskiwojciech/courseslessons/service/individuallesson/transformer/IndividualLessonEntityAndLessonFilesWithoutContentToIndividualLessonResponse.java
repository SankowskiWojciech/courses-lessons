package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileWithoutContentToLessonFileResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse implements BiFunction<IndividualLessonEntity, List<LessonFileWithoutContent>, IndividualLessonResponse> {

    private static final IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse INSTANCE = new IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse();

    @Override
    public IndividualLessonResponse apply(IndividualLessonEntity individualLessonEntity, List<LessonFileWithoutContent> lessonFilesWithoutContent) {
        return IndividualLessonResponse.builder()
                .lessonId(individualLessonEntity.getLessonId())
                .title(individualLessonEntity.getTitle())
                .startDateOfLesson(individualLessonEntity.getStartDateOfLesson())
                .endDateOfLesson(individualLessonEntity.getEndDateOfLesson())
                .description(individualLessonEntity.getDescription())
                .subdomainName(getSubdomainName(individualLessonEntity))
                .tutorEmailAddress(individualLessonEntity.getTutorEntity().getEmailAddress())
                .studentFullName(individualLessonEntity.getStudentEntity().getFullName())
                .studentEmailAddress(individualLessonEntity.getStudentEntity().getEmailAddress())
                .filesInformation(transformFilesWithoutContentToLessonFileResponses(lessonFilesWithoutContent))
                .build();
    }

    private String getSubdomainName(IndividualLessonEntity individualLessonEntity) {
        return individualLessonEntity.getOrganizationEntity() != null ? individualLessonEntity.getOrganizationEntity().getAlias() : individualLessonEntity.getTutorEntity().getAlias();
    }

    private List<LessonFileResponse> transformFilesWithoutContentToLessonFileResponses(List<LessonFileWithoutContent> lessonFilesWithoutContent) {
        if (lessonFilesWithoutContent != null && !lessonFilesWithoutContent.isEmpty()) {
            return lessonFilesWithoutContent.stream()
                    .map(lessonFileWithoutContent -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(lessonFileWithoutContent))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse getInstance() {
        return INSTANCE;
    }
}
