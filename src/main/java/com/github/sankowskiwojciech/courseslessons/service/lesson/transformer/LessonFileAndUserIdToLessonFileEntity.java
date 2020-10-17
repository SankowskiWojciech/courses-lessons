package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileAndUserIdToLessonFileEntity implements BiFunction<LessonFile, String, LessonFileEntity> {

    private static final LessonFileAndUserIdToLessonFileEntity INSTANCE = new LessonFileAndUserIdToLessonFileEntity();

    @Override
    public LessonFileEntity apply(LessonFile lessonFile, String userId) {
        return LessonFileEntity.builder()
                .name(lessonFile.getName())
                .extension(lessonFile.getExtension())
                .content(lessonFile.getContent())
                .createdBy(userId)
                .creationDateTime(LocalDateTime.now())
                .build();
    }

    public static LessonFileAndUserIdToLessonFileEntity getInstance() {
        return INSTANCE;
    }
}
