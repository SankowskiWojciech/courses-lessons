package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileEntityToLessonFileResponse implements Function<LessonFileEntity, LessonFileResponse> {

    private static final LessonFileEntityToLessonFileResponse INSTANCE = new LessonFileEntityToLessonFileResponse();

    @Override
    public LessonFileResponse apply(LessonFileEntity lessonFileEntity) {
        return LessonFileResponse.builder()
                .fileId(lessonFileEntity.getFileId())
                .name(lessonFileEntity.getName())
                .extension(lessonFileEntity.getExtension())
                .createdBy(lessonFileEntity.getCreatedBy())
                .creationDateTime(lessonFileEntity.getCreationDateTime())
                .build();
    }

    public static LessonFileEntityToLessonFileResponse getInstance() {
        return INSTANCE;
    }
}
