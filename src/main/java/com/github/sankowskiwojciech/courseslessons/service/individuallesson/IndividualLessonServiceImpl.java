package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.file.FilePermissionsService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonToIndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonsQueryProvider;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonsIdsAndListOfFilesWithoutContentProvider;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class IndividualLessonServiceImpl implements IndividualLessonService {
    private final IndividualLessonRepository individualLessonRepository;
    private final LessonFileService lessonFileService;
    private final FileRepository fileRepository;
    private final LessonsIdsAndListOfFilesWithoutContentProvider lessonsIdsAndListOfFilesWithoutContentProvider;
    private final FilePermissionsService filePermissionsService;

    @Transactional
    @Override
    public IndividualLessonResponse createIndividualLesson(IndividualLesson lesson) {
        IndividualLessonEntity lessonEntity = IndividualLessonToIndividualLessonEntity.getInstance().apply(lesson);
        IndividualLessonEntity savedLessonEntity = individualLessonRepository.save(lessonEntity);
        List<LessonFileAccessEntity> savedLessonFileAccessEntities = lessonFileService.attachFilesToLesson(savedLessonEntity.getId(), lesson.getFilesIds());
        Set<String> filesIds = savedLessonFileAccessEntities.stream().map(LessonFileAccessEntity::getFileId).collect(Collectors.toSet());
        filePermissionsService.addUserPermissionsToFiles(savedLessonEntity.getStudentEntity().getEmailAddress(), filesIds);
        List<FileWithoutContent> filesWithoutContent = filesIds.isEmpty() ? Collections.emptyList() : fileRepository.findAllByIdIn(filesIds);
        return IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(lessonEntity, filesWithoutContent);
    }

    @Override
    public List<IndividualLessonResponse> readIndividualLessons(AccountInfo accountInfo, LessonRequestParams requestParams) {
        BooleanExpression queryBooleanExpression = IndividualLessonsQueryProvider.getInstance().apply(accountInfo, requestParams);
        Iterable<IndividualLessonEntity> lessons = individualLessonRepository.findAll(queryBooleanExpression);
        List<String> lessonsIds = StreamSupport.stream(lessons.spliterator(), false)
                .map(IndividualLessonEntity::getId)
                .collect(Collectors.toList());
        Map<String, List<FileWithoutContent>> lessonFilesWithoutContent = lessonsIdsAndListOfFilesWithoutContentProvider.apply(lessonsIds);
        return StreamSupport.stream(lessons.spliterator(), false)
                .map(lesson -> IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance().apply(lesson, lessonFilesWithoutContent.getOrDefault(lesson.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }
}
