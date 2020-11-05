package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
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
public class IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider implements Function<Iterable<IndividualLessonEntity>, Map<Long, List<LessonFileWithoutContent>>> {

    private final IndividualLessonFileRepository individualLessonFileRepository;
    private final LessonFileRepository lessonFileRepository;

    @Override
    public Map<Long, List<LessonFileWithoutContent>> apply(Iterable<IndividualLessonEntity> individualLessonEntities) {
        List<Long> lessonsIds = StreamSupport.stream(individualLessonEntities.spliterator(), false)
                .map(IndividualLessonEntity::getLessonId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(lessonsIds)) {
            return Collections.emptyMap();
        }
        List<IndividualLessonFileEntity> individualLessonFileEntities = individualLessonFileRepository.findAllByLessonIdIn(lessonsIds);
        Map<Long, LessonFileWithoutContent> lessonFilesWithoutContentMap = getLessonFilesWithoutContent(individualLessonFileEntities);
        Map<Long, List<LessonFileWithoutContent>> individualLessonFilesWithoutContent = new HashMap<>();
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

    private Map<Long, LessonFileWithoutContent> getLessonFilesWithoutContent(List<IndividualLessonFileEntity> individualLessonFileEntities) {
        Set<Long> filesIds = individualLessonFileEntities.stream()
                .map(IndividualLessonFileEntity::getFileId)
                .collect(Collectors.toSet());
        List<LessonFileWithoutContent> lessonFilesWithoutContent = lessonFileRepository.findAllByFileIdIn(filesIds);
        return lessonFilesWithoutContent.stream()
                .collect(Collectors.toMap(LessonFileWithoutContent::getFileId, lessonFileWithoutContent -> lessonFileWithoutContent));
    }
}
