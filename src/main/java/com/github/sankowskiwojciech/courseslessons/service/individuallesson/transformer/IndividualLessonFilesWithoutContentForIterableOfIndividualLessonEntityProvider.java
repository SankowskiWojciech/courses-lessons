package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
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
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider implements Function<Iterable<IndividualLessonEntity>, Map<String, List<FileWithoutContent>>> {

    private final LessonFileAccessRepository lessonFileAccessRepository;
    private final FileRepository fileRepository;

    @Override
    public Map<String, List<FileWithoutContent>> apply(Iterable<IndividualLessonEntity> individualLessonEntities) {
        List<String> lessonsIds = StreamSupport.stream(individualLessonEntities.spliterator(), false)
                .map(IndividualLessonEntity::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(lessonsIds)) {
            return Collections.emptyMap();
        }
        List<LessonFileAccessEntity> individualLessonFileEntities = lessonFileAccessRepository.findAllByLessonIdIn(lessonsIds);
        Map<String, FileWithoutContent> lessonFilesWithoutContentMap = getLessonFilesWithoutContent(individualLessonFileEntities);
        Map<String, List<FileWithoutContent>> individualLessonFilesWithoutContent = new HashMap<>();
        individualLessonFileEntities.forEach(individualLessonFileEntity -> {
            if (individualLessonFilesWithoutContent.containsKey(individualLessonFileEntity.getLessonId())) {
                individualLessonFilesWithoutContent.get(individualLessonFileEntity.getLessonId()).add(lessonFilesWithoutContentMap.get(individualLessonFileEntity.getFileId()));
            } else {
                List<FileWithoutContent> lessonFilesWithoutContent = new ArrayList<>();
                lessonFilesWithoutContent.add(lessonFilesWithoutContentMap.get(individualLessonFileEntity.getFileId()));
                individualLessonFilesWithoutContent.put(individualLessonFileEntity.getLessonId(), lessonFilesWithoutContent);
            }
        });
        return individualLessonFilesWithoutContent;
    }

    private Map<String, FileWithoutContent> getLessonFilesWithoutContent(List<LessonFileAccessEntity> individualLessonFileEntities) {
        Set<String> filesIds = individualLessonFileEntities.stream()
                .map(LessonFileAccessEntity::getFileId)
                .collect(Collectors.toSet());
        List<FileWithoutContent> lessonFilesWithoutContent = fileRepository.findAllByIdIn(filesIds);
        return lessonFilesWithoutContent.stream()
                .collect(Collectors.toMap(FileWithoutContent::getId, lessonFileWithoutContent -> lessonFileWithoutContent));
    }
}
