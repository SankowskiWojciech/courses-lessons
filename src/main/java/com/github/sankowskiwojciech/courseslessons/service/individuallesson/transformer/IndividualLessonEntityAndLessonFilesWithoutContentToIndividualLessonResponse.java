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
    public IndividualLessonResponse apply(IndividualLessonEntity lesson, List<FileWithoutContent> files) {
        return new IndividualLessonResponse(lesson.getId(), lesson.getTitle(), lesson.getStartDate(), lesson.getEndDate(), lesson.getDescription(), lesson.getSubdomainEntity().getSubdomainId(), lesson.getTutorEntity().getEmailAddress(), transformFilesWithoutContentToLessonFileResponses(files), lesson.getStudentEntity().getFullName(), lesson.getStudentEntity().getEmailAddress());
    }

    private List<LessonFileResponse> transformFilesWithoutContentToLessonFileResponses(List<FileWithoutContent> files) {
        if (CollectionUtils.isNotEmpty(files)) {
            return files.stream()
                    .map(file -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(file))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse getInstance() {
        return INSTANCE;
    }
}
