package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileWithoutContentToLessonFileResponse implements Function<FileWithoutContent, LessonFileResponse> {

    private static final LessonFileWithoutContentToLessonFileResponse INSTANCE = new LessonFileWithoutContentToLessonFileResponse();

    @Override
    public LessonFileResponse apply(FileWithoutContent file) {
        return LessonFileResponse.builder()
                .id(file.getId())
                .name(file.getName())
                .extension(file.getExtension())
                .createdBy(file.getCreatedBy())
                .creationDateTime(file.getCreationDateTime())
                .build();
    }

    public static LessonFileWithoutContentToLessonFileResponse getInstance() {
        return INSTANCE;
    }
}
