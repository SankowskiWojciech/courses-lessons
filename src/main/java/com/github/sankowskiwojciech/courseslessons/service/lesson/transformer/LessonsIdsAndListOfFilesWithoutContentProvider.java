package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LessonsIdsAndListOfFilesWithoutContentProvider implements Function<List<String>, Map<String, List<FileWithoutContent>>> {
    private final LessonFileAccessRepository lessonFileAccessRepository;
    private final FileRepository fileRepository;

    @Override
    public Map<String, List<FileWithoutContent>> apply(List<String> lessonsIds) {
        if (CollectionUtils.isEmpty(lessonsIds)) {
            return Collections.emptyMap();
        }
        List<LessonFileAccessEntity> lessonFileAccessList = lessonFileAccessRepository.findAllByLessonIdIn(lessonsIds);
        Map<String, FileWithoutContent> lessonFilesWithoutContentMap = getLessonFilesWithoutContent(lessonFileAccessList);
        Map<String, List<FileWithoutContent>> individualLessonFilesWithoutContent = new HashMap<>();
        lessonFileAccessList.forEach(lessonFileAccess -> {
            if (individualLessonFilesWithoutContent.containsKey(lessonFileAccess.getLessonId())) {
                individualLessonFilesWithoutContent.get(lessonFileAccess.getLessonId()).add(lessonFilesWithoutContentMap.get(lessonFileAccess.getFileId()));
            } else {
                List<FileWithoutContent> lessonFilesWithoutContent = new ArrayList<>();
                lessonFilesWithoutContent.add(lessonFilesWithoutContentMap.get(lessonFileAccess.getFileId()));
                individualLessonFilesWithoutContent.put(lessonFileAccess.getLessonId(), lessonFilesWithoutContent);
            }
        });
        return individualLessonFilesWithoutContent;
    }

    private Map<String, FileWithoutContent> getLessonFilesWithoutContent(List<LessonFileAccessEntity> individualLessonFileEntities) {
        Set<String> filesIds = individualLessonFileEntities.stream()
                .map(LessonFileAccessEntity::getFileId)
                .collect(Collectors.toSet());
        List<FileWithoutContent> files = fileRepository.findAllByIdIn(filesIds);
        return files.stream()
                .collect(Collectors.toMap(FileWithoutContent::getId, file -> file));
    }
}
