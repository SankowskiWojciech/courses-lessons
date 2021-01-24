package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider implements Function<Iterable<IndividualLessonEntity>, Map<String, List<LessonFileWithoutContent>>> {

    private final IndividualLessonFileRepository individualLessonFileRepository;
    private final LessonFileRepository lessonFileRepository;

    @Override
    public Map<String, List<LessonFileWithoutContent>> apply(Iterable<IndividualLessonEntity> individualLessonEntities) {
        List<String> lessonsIds = StreamSupport.stream(individualLessonEntities.spliterator(), false)
                .map(IndividualLessonEntity::getLessonId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(lessonsIds)) {
            return Collections.emptyMap();
        }
        List<IndividualLessonFileEntity> individualLessonFileEntities = individualLessonFileRepository.findAllByLessonIdIn(lessonsIds);
        Map<String, LessonFileWithoutContent> lessonFilesWithoutContentMap = getLessonFilesWithoutContent(individualLessonFileEntities);
        Map<String, List<LessonFileWithoutContent>> individualLessonFilesWithoutContent = new HashMap<>();
        individualLessonFileEntities.forEach(individualLessonFileEntity -> {
            if (individualLessonFilesWithoutContent.containsKey(individualLessonFileEntity.getLessonId())) {
                individualLessonFilesWithoutContent.get(individualLessonFileEntity.getLessonId()).add(lessonFilesWithoutContentMap.get(individualLessonFileEntity.getFileId()));
            } else {
                List<LessonFileWithoutContent> lessonFilesWithoutContent = new ArrayList<>();
                lessonFilesWithoutContent.add(lessonFilesWithoutContentMap.get(individualLessonFileEntity.getFileId()));
                individualLessonFilesWithoutContent.put(individualLessonFileEntity.getLessonId(), lessonFilesWithoutContent);
            }
        });
        return individualLessonFilesWithoutContent;
    }

    private Map<String, LessonFileWithoutContent> getLessonFilesWithoutContent(List<IndividualLessonFileEntity> individualLessonFileEntities) {
        Set<String> filesIds = individualLessonFileEntities.stream()
                .map(IndividualLessonFileEntity::getFileId)
                .collect(Collectors.toSet());
        List<LessonFileWithoutContent> lessonFilesWithoutContent = lessonFileRepository.findAllByFileIdIn(filesIds);
        return lessonFilesWithoutContent.stream()
                .collect(Collectors.toMap(LessonFileWithoutContent::getFileId, lessonFileWithoutContent -> lessonFileWithoutContent));
    }
}
