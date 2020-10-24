package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileWithoutContentToLessonFileResponse implements Function<LessonFileWithoutContent, LessonFileResponse> {

    private static final LessonFileWithoutContentToLessonFileResponse INSTANCE = new LessonFileWithoutContentToLessonFileResponse();

    @Override
    public LessonFileResponse apply(LessonFileWithoutContent lessonFileWithoutContent) {
        return LessonFileResponse.builder()
                .fileId(lessonFileWithoutContent.getFileId())
                .name(lessonFileWithoutContent.getName())
                .extension(lessonFileWithoutContent.getExtension())
                .createdBy(lessonFileWithoutContent.getCreatedBy())
                .creationDateTime(lessonFileWithoutContent.getCreationDateTime())
                .build();
    }

    public static LessonFileWithoutContentToLessonFileResponse getInstance() {
        return INSTANCE;
    }
}
