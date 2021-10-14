package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.LessonIdAndFilesIdsToIndividualLessonFileAccessList;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileAndUserIdToLessonFileEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileEntityToLessonFile;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileEntityToLessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileWithoutContentToLessonFileResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonFileServiceImpl implements LessonFileService {
    private final FileRepository fileRepository;
    private final LessonFileAccessRepository lessonFileAccessRepository;

    @Transactional
    @Override
    public LessonFileResponse createLessonFile(LessonFile file, String userId) {
        FileEntity entity = LessonFileAndUserIdToLessonFileEntity.getInstance().apply(file, userId);
        FileEntity savedEntity = fileRepository.save(entity);
        return LessonFileEntityToLessonFileResponse.getInstance().apply(savedEntity);
    }

    @Override
    public LessonFile readLessonFile(String fileId) {
        FileEntity entity = fileRepository.findById(fileId).get();
        return LessonFileEntityToLessonFile.getInstance().apply(entity);
    }

    @Override
    public List<LessonFileResponse> readFilesInformation(String fileOwnerId) {
        List<FileWithoutContent> filesWithoutContent = fileRepository.findAllByCreatedBy(fileOwnerId);
        return filesWithoutContent.stream().map(fileWithoutContent -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(fileWithoutContent)).collect(Collectors.toList());
    }

    @Override
    public List<LessonFileAccessEntity> attachFilesToLesson(String lessonId, List<String> filesIds) {
        if (CollectionUtils.isNotEmpty(filesIds)) {
            List<LessonFileAccessEntity> lessonFileAccessEntities = LessonIdAndFilesIdsToIndividualLessonFileAccessList.getInstance().apply(lessonId, filesIds);
            return lessonFileAccessRepository.saveAll(lessonFileAccessEntities);
        }
        return Collections.emptyList();
    }
}
