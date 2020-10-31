package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.AccountInfoAndIndividualLessonRequestParamsToBooleanExpression;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonToIndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.LessonIdAndFilesIdsToIndividualLessonFileEntities;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class IndividualLessonServiceImpl implements IndividualLessonService {

    private final IndividualLessonRepository individualLessonRepository;
    private final IndividualLessonFileRepository individualLessonFileRepository;
    private final IndividualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider individualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider;

    @Transactional
    @Override
    public IndividualLessonResponse createIndividualLesson(IndividualLesson individualLesson) {
        IndividualLessonEntity individualLessonEntity = IndividualLessonToIndividualLessonEntity.getInstance().apply(individualLesson);
        IndividualLessonEntity savedIndividualLessonEntity = individualLessonRepository.save(individualLessonEntity);
        List<IndividualLessonFileEntity> savedIndividualLessonFileEntities = saveFilesIdsIfAnyProvided(savedIndividualLessonEntity.getLessonId(), individualLesson.getFilesIds());
        return IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse.getInstance().apply(individualLessonEntity, savedIndividualLessonFileEntities);
    }

    @Override
    public List<IndividualLessonResponse> readIndividualLessons(AccountInfo accountInfo, IndividualLessonRequestParams individualLessonRequestParams) {
        BooleanExpression queryBooleanExpression = AccountInfoAndIndividualLessonRequestParamsToBooleanExpression.getInstance().apply(accountInfo, individualLessonRequestParams);
        Iterable<IndividualLessonEntity> individualLessonEntities = individualLessonRepository.findAll(queryBooleanExpression);
        Map<Long, List<IndividualLessonFileEntity>> individualLessonFileEntities = individualLessonFileEntitiesForIterableOfIndividualLessonEntityProvider.apply(individualLessonEntities);
        return StreamSupport.stream(individualLessonEntities.spliterator(), false)
                .map(individualLessonEntity -> IndividualLessonEntityAndIndividualLessonFileEntitiesToIndividualLessonResponse.getInstance().apply(individualLessonEntity, individualLessonFileEntities.getOrDefault(individualLessonEntity.getLessonId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private List<IndividualLessonFileEntity> saveFilesIdsIfAnyProvided(Long lessonId, List<Long> filesIds) {
        if (CollectionUtils.isNotEmpty(filesIds)) {
            List<IndividualLessonFileEntity> individualLessonFileEntities = LessonIdAndFilesIdsToIndividualLessonFileEntities.getInstance().apply(lessonId, filesIds);
            return individualLessonFileRepository.saveAll(individualLessonFileEntities);
        }
        return Collections.emptyList();
    }
}
