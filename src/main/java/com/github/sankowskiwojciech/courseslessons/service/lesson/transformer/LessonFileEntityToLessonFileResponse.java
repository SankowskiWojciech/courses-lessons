package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileEntityToLessonFileResponse implements Function<FileEntity, LessonFileResponse> {

    private static final LessonFileEntityToLessonFileResponse INSTANCE = new LessonFileEntityToLessonFileResponse();

    @Override
    public LessonFileResponse apply(FileEntity fileEntity) {
        return LessonFileResponse.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .extension(fileEntity.getExtension())
                .createdBy(fileEntity.getCreatedBy())
                .creationDateTime(fileEntity.getCreationDateTime())
                .build();
    }

    public static LessonFileEntityToLessonFileResponse getInstance() {
        return INSTANCE;
    }
}
