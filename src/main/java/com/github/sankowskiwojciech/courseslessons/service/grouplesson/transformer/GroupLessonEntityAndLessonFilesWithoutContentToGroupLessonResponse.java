package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
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
public class GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse implements BiFunction<GroupLessonEntity, List<FileWithoutContent>, GroupLessonResponse> {

    private static final GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse INSTANCE = new GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse();

    @Override
    public GroupLessonResponse apply(GroupLessonEntity lesson, List<FileWithoutContent> files) {
        return new GroupLessonResponse(lesson.getId(), lesson.getTitle(), lesson.getStartDate(), lesson.getEndDate(), lesson.getDescription(), getSubdomainName(lesson), lesson.getTutorEntity().getEmailAddress(), transformFilesWithoutContentToLessonFileResponses(files), lesson.getGroupEntity().getName());
    }

    private String getSubdomainName(GroupLessonEntity lesson) {
        return lesson.getOrganizationEntity() != null ? lesson.getOrganizationEntity().getAlias() : lesson.getTutorEntity().getAlias();
    }

    private List<LessonFileResponse> transformFilesWithoutContentToLessonFileResponses(List<FileWithoutContent> files) {
        if (CollectionUtils.isNotEmpty(files)) {
            return files.stream()
                    .map(file -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(file))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse getInstance() {
        return INSTANCE;
    }
}
