package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonToGroupLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonsQueryProvider;
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
public class GroupLessonServiceImpl implements GroupLessonService {
    private final GroupLessonRepository groupLessonRepository;
    private final FileRepository fileRepository;
    private final LessonFileService lessonFileService;
    private final LessonsIdsAndListOfFilesWithoutContentProvider lessonsIdsAndListOfFilesWithoutContentProvider;
    private final GroupLessonsQueryProvider groupLessonsQueryProvider;

    @Transactional
    @Override
    public GroupLessonResponse createGroupLesson(GroupLesson lesson) {
        GroupLessonEntity lessonEntity = GroupLessonToGroupLessonEntity.getInstance().apply(lesson);
        GroupLessonEntity savedLessonEntity = groupLessonRepository.save(lessonEntity);
        List<LessonFileAccessEntity> savedLessonFileAccessEntities = lessonFileService.attachFilesToLesson(savedLessonEntity.getId(), lesson.getFilesIds());
        Set<String> filesIds = savedLessonFileAccessEntities.stream().map(LessonFileAccessEntity::getFileId).collect(Collectors.toSet());
        List<FileWithoutContent> filesWithoutContent = filesIds.isEmpty() ? Collections.emptyList() : fileRepository.findAllByIdIn(filesIds);
        return GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse.getInstance().apply(lessonEntity, filesWithoutContent);
    }

    @Override
    public List<GroupLessonResponse> readGroupLessons(AccountInfo accountInfo, LessonRequestParams requestParams) {
        BooleanExpression queryBooleanExpression = groupLessonsQueryProvider.apply(accountInfo, requestParams);
        Iterable<GroupLessonEntity> lessons = groupLessonRepository.findAll(queryBooleanExpression);
        List<String> lessonsIds = StreamSupport.stream(lessons.spliterator(), false)
                .map(GroupLessonEntity::getId)
                .collect(Collectors.toList());
        Map<String, List<FileWithoutContent>> lessonFilesWithoutContent = lessonsIdsAndListOfFilesWithoutContentProvider.apply(lessonsIds);
        return StreamSupport.stream(lessons.spliterator(), false)
                .map(lesson -> GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse.getInstance().apply(lesson, lessonFilesWithoutContent.getOrDefault(lesson.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }
}
