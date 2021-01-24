package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonIdAndFilesIdsToIndividualLessonFileEntities implements BiFunction<String, List<String>, List<IndividualLessonFileEntity>> {

    private static final LessonIdAndFilesIdsToIndividualLessonFileEntities INSTANCE = new LessonIdAndFilesIdsToIndividualLessonFileEntities();

    @Override
    public List<IndividualLessonFileEntity> apply(String lessonId, List<String> filesIds) {
        return filesIds.stream()
                .map(fileId -> new IndividualLessonFileEntity(lessonId, fileId))
                .collect(Collectors.toList());
    }

    public static LessonIdAndFilesIdsToIndividualLessonFileEntities getInstance() {
        return INSTANCE;
    }
}
