package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider implements Function<Iterable<IndividualLessonEntity>, Map<Long, List<IndividualLessonFileEntity>>> {

    private final IndividualLessonFileRepository individualLessonFileRepository;

    @Override
    public Map<Long, List<IndividualLessonFileEntity>> apply(Iterable<IndividualLessonEntity> individualLessonEntities) {
        List<Long> lessonsIds = StreamSupport.stream(individualLessonEntities.spliterator(), false)
                .map(IndividualLessonEntity::getLessonId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(lessonsIds)) {
            return Collections.emptyMap();
        }
        List<IndividualLessonFileEntity> individualLessonFileEntities = individualLessonFileRepository.findAllByLessonIdIn(lessonsIds);
        Map<Long, List<IndividualLessonFileEntity>> individualLessonFileEntitiesMap = new HashMap<>();
        individualLessonFileEntities.forEach(individualLessonFileEntity -> {
            if (individualLessonFileEntitiesMap.containsKey(individualLessonFileEntity.getLessonId())) {
                individualLessonFileEntitiesMap.get(individualLessonFileEntity.getLessonId()).add(individualLessonFileEntity);
            } else {
                List<IndividualLessonFileEntity> individualLessonFileEntityList = new ArrayList<>();
                individualLessonFileEntityList.add(individualLessonFileEntity);
                individualLessonFileEntitiesMap.put(individualLessonFileEntity.getLessonId(), individualLessonFileEntityList);
            }
        });
        return individualLessonFileEntitiesMap;
    }
}
