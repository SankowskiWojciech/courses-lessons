package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.AccountInfoAndIndividualLessonRequestParamsToBooleanExpression;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonToIndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.LessonIdAndFilesIdsToIndividualLessonFileEntities;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class IndividualLessonServiceImpl implements IndividualLessonService {

    private final IndividualLessonRepository individualLessonRepository;
    private final LessonFileAccessRepository lessonFileAccessRepository;
    private final FileRepository fileRepository;
    private final IndividualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider;

    @Transactional
    @Override
    public IndividualLessonResponse createIndividualLesson(IndividualLesson individualLesson) {
        IndividualLessonEntity individualLessonEntity = IndividualLessonToIndividualLessonEntity.getInstance().apply(individualLesson);
        IndividualLessonEntity savedIndividualLessonEntity = individualLessonRepository.save(individualLessonEntity);
        List<LessonFileAccessEntity> savedIndividualLessonFileEntities = saveFilesIdsIfAnyProvided(savedIndividualLessonEntity.getId(), individualLesson.getFilesIds());
        Set<String> filesIds = savedIndividualLessonFileEntities.stream().map(LessonFileAccessEntity::getFileId).collect(Collectors.toSet());
        List<FileWithoutContent> lessonFilesWithoutContent = filesIds.isEmpty() ? Collections.emptyList() : fileRepository.findAllByIdIn(filesIds);
        return IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(individualLessonEntity, lessonFilesWithoutContent);
    }

    @Override
    public List<IndividualLessonResponse> readIndividualLessons(AccountInfo accountInfo, LessonRequestParams lessonRequestParams) {
        BooleanExpression queryBooleanExpression = AccountInfoAndIndividualLessonRequestParamsToBooleanExpression.getInstance().apply(accountInfo, lessonRequestParams);
        Iterable<IndividualLessonEntity> individualLessonEntities = individualLessonRepository.findAll(queryBooleanExpression);
        Map<String, List<FileWithoutContent>> individualLessonFilesWithoutContent = individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider.apply(individualLessonEntities);
        return StreamSupport.stream(individualLessonEntities.spliterator(), false)
                .map(individualLessonEntity -> IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(individualLessonEntity, individualLessonFilesWithoutContent.getOrDefault(individualLessonEntity.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private List<LessonFileAccessEntity> saveFilesIdsIfAnyProvided(String lessonId, List<String> filesIds) {
        if (CollectionUtils.isNotEmpty(filesIds)) {
            List<LessonFileAccessEntity> individualLessonFileEntities = LessonIdAndFilesIdsToIndividualLessonFileEntities.getInstance().apply(lessonId, filesIds);
            return lessonFileAccessRepository.saveAll(individualLessonFileEntities);
        }
        return Collections.emptyList();
    }
}
