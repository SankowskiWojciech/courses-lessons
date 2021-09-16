package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileWithoutContentToLessonFileResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse implements BiFunction<IndividualLessonEntity, List<FileWithoutContent>, IndividualLessonResponse> {

    private static final IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse INSTANCE = new IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse();

    @Override
    public IndividualLessonResponse apply(IndividualLessonEntity individualLessonEntity, List<FileWithoutContent> lessonFilesWithoutContent) {
        return new IndividualLessonResponse(individualLessonEntity.getId(), individualLessonEntity.getTitle(), individualLessonEntity.getStartDate(), individualLessonEntity.getEndDate(), individualLessonEntity.getDescription(), getSubdomainName(individualLessonEntity), individualLessonEntity.getTutorEntity().getEmailAddress(), transformFilesWithoutContentToLessonFileResponses(lessonFilesWithoutContent), individualLessonEntity.getStudentEntity().getFullName(), individualLessonEntity.getStudentEntity().getEmailAddress());
    }

    private String getSubdomainName(IndividualLessonEntity individualLessonEntity) {
        return individualLessonEntity.getOrganizationEntity() != null ? individualLessonEntity.getOrganizationEntity().getAlias() : individualLessonEntity.getTutorEntity().getAlias();
    }

    private List<LessonFileResponse> transformFilesWithoutContentToLessonFileResponses(List<FileWithoutContent> lessonFilesWithoutContent) {
        if (CollectionUtils.isNotEmpty(lessonFilesWithoutContent)) {
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
