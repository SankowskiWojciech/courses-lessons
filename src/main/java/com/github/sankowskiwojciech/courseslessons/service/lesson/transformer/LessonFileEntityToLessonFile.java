package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileEntityToLessonFile implements Function<FileEntity, LessonFile> {

    private static final LessonFileEntityToLessonFile INSTANCE = new LessonFileEntityToLessonFile();

    @Override
    public LessonFile apply(FileEntity entity) {
        return LessonFile.builder()
                .id(entity.getId())
                .name(entity.getName())
                .extension(entity.getExtension())
                .content(entity.getContent())
                .createdBy(entity.getCreatedBy())
                .creationDateTime(entity.getCreationDateTime())
                .build();
    }

    public static LessonFileEntityToLessonFile getInstance() {
        return INSTANCE;
    }
}
