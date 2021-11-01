package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer.GroupLessonToGroupLessonEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupLessonServiceImpl implements GroupLessonService {
    private final GroupLessonRepository groupLessonRepository;
    private final FileRepository fileRepository;
    private LessonFileService lessonFileService;

    @Override
    public GroupLessonResponse createGroupLesson(GroupLesson lesson) {
        GroupLessonEntity lessonEntity = GroupLessonToGroupLessonEntity.getInstance().apply(lesson);
        GroupLessonEntity savedLessonEntity = groupLessonRepository.save(lessonEntity);
        List<LessonFileAccessEntity> savedLessonFileAccessEntities = lessonFileService.attachFilesToLesson(savedLessonEntity.getId(), lesson.getFilesIds());
        Set<String> filesIds = savedLessonFileAccessEntities.stream().map(LessonFileAccessEntity::getFileId).collect(Collectors.toSet());
        List<FileWithoutContent> filesWithoutContent = filesIds.isEmpty() ? Collections.emptyList() : fileRepository.findAllByIdIn(filesIds);
        return GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse.getInstance().apply(lessonEntity, filesWithoutContent);
    }
}
