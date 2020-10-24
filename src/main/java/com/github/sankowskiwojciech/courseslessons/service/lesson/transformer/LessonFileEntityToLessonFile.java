package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileEntityToLessonFile implements Function<LessonFileEntity, LessonFile> {

    private static final LessonFileEntityToLessonFile INSTANCE = new LessonFileEntityToLessonFile();

    @Override
    public LessonFile apply(LessonFileEntity lessonFileEntity) {
        return LessonFile.builder()
                .fileId(lessonFileEntity.getFileId())
                .name(lessonFileEntity.getName())
                .extension(lessonFileEntity.getExtension())
                .content(lessonFileEntity.getContent())
                .createdBy(lessonFileEntity.getCreatedBy())
                .creationDateTime(lessonFileEntity.getCreationDateTime())
                .build();
    }

    public static LessonFileEntityToLessonFile getInstance() {
        return INSTANCE;
    }
}
