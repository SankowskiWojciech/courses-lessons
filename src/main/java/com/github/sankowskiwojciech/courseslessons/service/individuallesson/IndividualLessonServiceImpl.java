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
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.LessonIdAndFilesIdsToIndividualLessonFileAccessList;
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
    public IndividualLessonResponse createIndividualLesson(IndividualLesson lesson) {
        IndividualLessonEntity lessonEntity = IndividualLessonToIndividualLessonEntity.getInstance().apply(lesson);
        IndividualLessonEntity savedLessonEntity = individualLessonRepository.save(lessonEntity);
        List<LessonFileAccessEntity> savedLessonFileAccessEntities = saveFilesIdsIfAnyProvided(savedLessonEntity.getId(), lesson.getFilesIds());
        Set<String> filesIds = savedLessonFileAccessEntities.stream().map(LessonFileAccessEntity::getFileId).collect(Collectors.toSet());
        List<FileWithoutContent> filesWithoutContent = filesIds.isEmpty() ? Collections.emptyList() : fileRepository.findAllByIdIn(filesIds);
        return IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(lessonEntity, filesWithoutContent);
    }

    @Override
    public List<IndividualLessonResponse> readIndividualLessons(AccountInfo accountInfo, LessonRequestParams requestParams) {
        BooleanExpression queryBooleanExpression = AccountInfoAndIndividualLessonRequestParamsToBooleanExpression.getInstance().apply(accountInfo, requestParams);
        Iterable<IndividualLessonEntity> lessons = individualLessonRepository.findAll(queryBooleanExpression);
        Map<String, List<FileWithoutContent>> individualLessonFilesWithoutContent = individualLessonFilesWithoutContentForIterableOfIndividualLessonEntityProvider.apply(lessons);
        return StreamSupport.stream(lessons.spliterator(), false)
                .map(lesson -> IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(lesson, individualLessonFilesWithoutContent.getOrDefault(lesson.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private List<LessonFileAccessEntity> saveFilesIdsIfAnyProvided(String lessonId, List<String> filesIds) {
        if (CollectionUtils.isNotEmpty(filesIds)) {
            List<LessonFileAccessEntity> lessonFileAccessEntities = LessonIdAndFilesIdsToIndividualLessonFileAccessList.getInstance().apply(lessonId, filesIds);
            return lessonFileAccessRepository.saveAll(lessonFileAccessEntities);
        }
        return Collections.emptyList();
    }
}
